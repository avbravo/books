# Architecture Ring
como uso la Replicación (High Availability / Failover)
Arquitectura tipo anillo


ScyllaDB (Anillo descentralizado multiprimario):• No tiene nodos primarios ni secundarios; todos los nodos son iguales (peer topology / multi-primary).
• Organiza el clúster en un anillo virtual (vNodes) y utiliza el protocolo Gossip para la comunicación entre nodos.
• Al añadir nodos, la re-partición es instantánea

raft vs protocolo Gossip


ScyllaDB vs MongoDB

[https://hackernoon.com/lang/es/mongodb-vs-scylladb-architecture-comparison](https://hackernoon.com/lang/es/mongodb-vs-scylladb-architecture-comparison)


Combinar **Gossip** y **Raft** es una de las prácticas arquitectónicas más potentes y utilizadas en el diseño de sistemas distribuidos modernos a gran escala (por ejemplo, en bases de datos distribuidas y gestores de orquestación como **HashiCorp Consul** o **CockroachDB**).

Mientras que **Raft** sufre de problemas de escalabilidad cuando intentas meter a cientos o miles de nodos en un solo grupo de consenso, **Gossip** es excelente para escalar pero incapaz de garantizar consistencia estricta. Unirlos crea un sistema híbrido donde cada protocolo hace lo que mejor sabe hacer.

---

### ¿Cómo se dividen el trabajo? (Patrón Arquitectónico)

En una arquitectura combinada, el sistema se divide típicamente en dos capas o planos:

1. **La Capa Periférica (Gossip):** Maneja el **descubrimiento de nodos (*membership discovery*)**, la detección rápida de fallos (*health checks* de red) y la difusión ligera de metadatos o topología general a lo largo de miles de nodos.
2. **La Capa Central/Crítica (Raft):** Maneja el **estado compartido estricto, transacciones y metadatos críticos** dentro de subgrupos pequeños y altamente confiables (generalmente clústeres de 3 o 5 nodos por cada shard o partición).

---

### Los 3 Patrones Principales de Combinación

#### 1. El Modelo de Descubrimiento y Consistencia (Ej: HashiCorp Consul)

* **El Problema:** Raft necesita saber exactamente quiénes son los miembros del clúster para calcular los quórumes ($N/2 + 1$). Si un nodo nuevo entra o uno viejo muere, actualizar la configuración de Raft requiere un paso de consenso estricto.
* **La Solución con Gossip:**
* Se ejecuta una capa de **Gossip (como Serf/memberlist)** en todo el clúster. Cuando un nuevo nodo se enciende, entra a la red haciendo "gossip" y los demás se enteran de inmediato de su IP y puerto.
* Una vez que el clúster detecta vía Gossip que hay suficientes nodos estables, esa capa de membresía alimenta o notifica a la **capa de Raft** para que integre formalmente al nodo al grupo de consenso o cree nuevos grupos de réplica.



#### 2. Multi-Raft con Enrutamiento por Gossip (Ej: CockroachDB)

* **El Problema:** En una base de datos distribuida masiva, tener un solo grupo de Raft para toda la base de datos es imposible. Se usa **Multi-Raft** (miles de pequeños grupos de Raft, donde cada grupo gobierna un *range* o partición de datos). Pero, ¿cómo sabe un cliente a qué grupo de Raft debe enviar su consulta?
* **La Solución con Gossip:**
* Cada nodo ejecuta un demonio de **Gossip** que difunde constantemente el mapa de rangos de la base de datos (qué nodo tiene qué pedazo de datos).
* Cuando un nodo recibe una escritura, consulta su tabla de enrutamiento local (mantenida al día por Gossip) para saber qué grupo de Raft específico es el líder de esa llave, y redirige la petición a ese líder de Raft.



#### 3. Detección de Fallos para Reconfiguración Dinámica de Raft

* **El Problema:** Los timeouts internos de Raft para detectar la muerte de un líder o seguidor a veces son lentos o generan falsos positivos si hay congestión de red transitoria. Además, cambiar la membresía de Raft de forma dinámica es complejo.
* **La Solución con Gossip:**
* **Gossip** actúa como un sistema de alerta temprana descentralizado. Si un nodo deja de responder al Gossip, la red lo marca rápidamente como "sospechoso" o "muerto".
* Esa señal de Gossip es recogida por un orquestador que inicia de forma segura un cambio de configuración en Raft (*joint consensus*) para retirar al nodo caído y promover a un nuevo seguidor, evitando los retrasos de los timeouts de Raft puro.



---

### Resumen de Beneficios de la Combinación

| Métrica / Reto | Solo Raft | Solo Gossip | **Combinados (Híbrido)** |
| --- | --- | --- | --- |
| **Escalabilidad de Nodos** | Baja (pesado para >9-11 nodos por grupo). | Extrema (miles de nodos sin problema). | **Alta**, porque Gossip absorbe el ruido de la red masiva y Raft solo protege los datos críticos. |
| **Consistencia de Datos** | Estricta (Linearizable). | Débil (Eventual). | **Fuerte** donde se necesita (vía Raft) y flexible en la periferia (vía Gossip). |
| **Detección de Caídas** | Basada en timeouts de latidos del líder. | Basada en sondeos aleatorios descentralizados. | **Rápida y robusta**, combinando alertas de red con quórumes de estado. |

Combinar ambos te permite construir motores de almacenamiento distribuido o sistemas de alta disponibilidad capaces de crecer horizontalmente a nivel de infraestructura física, manteniendo la seguridad absoluta de que los datos y metadatos críticos jamás sufrirán de *split-brain* o corromperán su orden de escritura.ç