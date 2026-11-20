# Prompt Maestro Consolidado: Ecosistema Antigravity JettraStore (Java 25+)

Actúa como un arquitecto de software experto, ingeniero de rendimiento JVM y líder técnico en sistemas distribuidos. Diseña y especifica de forma exhaustiva, completa y lista para producción la arquitectura, el diseño modular y las directrices de desarrollo para **JettraStore**, un ecosistema de base de datos multimodelo NoSQL distribuido de ultra-alto rendimiento, desarrollado íntegramente en **Java 25+**.

El ecosistema debe integrar de forma nativa los principios de **gestión de memoria fuera del *Heap* (Off-Heap) mediante Project Panama (FFM API)**, **Z Garbage Collector (ZGC)**, **Compact Object Headers**, almacenamiento estructurado por páginas binarias fijas, ejecución por particiones y hilos virtuales aislados. **Como requerimiento crítico y fundamental, el sistema debe evitar de forma absoluta errores del tipo `java.lang.OutOfMemoryError: Java heap space**`, aplicando para ello un procesamiento estricto por lotes (*chunking*), **el uso obligatorio de `JettraCollection` para la manipulación de colecciones y estructuras de datos**, flujos perezosos (*Streams*) y la prohibición terminante de conversiones masivas de colecciones enteras (como `.toArray()` o volcado de millones de registros de golpe a listas globales). Todo el sistema está diseñado para operar sobre un clúster de **3 nodos** (1 líder Raft y 2 seguidores) con mecanismos de desbordamiento dinámico a un **anillo distribuido por saturación de RAM**.

---

## I. Arquitectura de Almacenamiento, Memoria y Optimización (Java 25+)

1. **Optimización con Project Panama y Off-Heap (Prevención Absoluta de OOM):**
* Uso intensivo de la **Foreign Function & Memory (FFM) API** (`MemorySegment`, `Arena`) para gestionar búferes y metadatos masivos fuera del *Heap*, eliminando el coste de cabeceras de objetos (*Object Headers*), el *padding* y evitando por completo las pausas del recolector de basura (*Stop-The-World*).
* Configuración optimizada para **ZGC** y uso de **Compact Object Headers** para minimizar el tamaño de los índices en memoria.


2. **Formato de Archivo `.jettra` y Estructura LSM:**
* Todos los archivos de persistencia (SSTables, WAL y metadatos) usan la extensión `.jettra`, optimizados a bajo nivel para lectura/escritura directa mediante I/O asíncrono y memoria nativa.
* Estructura LSM inmutable con MemTables configurables (128 MB por defecto, límite global estricto en RAM de 2 GB), filtros de Bloom de alta precisión e índices secundarios dispersos.


3. **Manipulación de Colecciones con `JettraCollection` y Prevención Rigurosa de `OutOfMemoryError`:**
* **Uso Obligatorio de `JettraCollection`:** Queda estipulado el uso exclusivo y obligatorio del framework de colecciones personalizado **`JettraCollection`** para toda la manipulación interna de datos, índices, metadatos y búferes. Esta librería está optimizada para evitar el *boxing/unboxing* de tipos primitivos, reducir el consumo de RAM y minimizar drásticamente la presión sobre el recolector de basura en accesos concurrentes de alta frecuencia.
* **Prohibición Estricta:** Queda terminantemente prohibido realizar conversiones masivas de colecciones enteras (como `.toArray()` sobre mapas concurrentes o volcado masivo de millones de registros a listas globales) para prevenir caídas por `java.lang.OutOfMemoryError: Java heap space`.
* **Flujos Perezosos y Lotes Acotados (*Chunking*):** Las operaciones de consulta masiva, lecturas completas y reconstrucción de índices (`findAll` o reconstrucción de árboles) deben operar estrictamente mediante **flujos perezosos (*Streams* / iteradores bajo demanda)** y procesarse en **lotes acotados (*chunks* de tamaño fijo, ej. 1,000 a 5,000 elementos)** utilizando estructuras de `JettraCollection` con capacidad preasignada y de ciclo de vida efímero que se liberan inmediatamente tras su indexación o persistencia.



---

## II. Mecanismo de Anillo Distribuido y Topología de Clúster (3 Nodos)

1. **Transición Dinámica por Saturación de RAM:**
* JettraStore monitorea continuamente el umbral de saturación de la memoria RAM disponible. Si un nodo roza su límite crítico, el sistema se convierte automáticamente en un **motor de anillo distribuido**, transfiriendo de forma proactiva la carga de datos locales a los demás nodos del clúster mediante `jettraGRPC` para blindar la aplicación frente a desbordamientos de memoria.


2. **Consenso Raft y Configuración del Clúster (`jettra.config`):**
* Topología estricta de 3 nodos (1 líder y 2 seguidores) especificada centralmente en `jettra.config` para el descubrimiento automático de pares y la replicación sin bloqueos.



---

## III. Seguridad Estricta, Autenticación y Superusuario (`JettraJWT`)

1. **Tokens de Acceso:** Toda la comunicación entre los clientes (Drivers, CLI, FX, PoliceFX) y los nodos se protege estrictamente mediante tokens de autenticación `JettraJWT`.
2. **Superusuario por Defecto:**
* Se inicializa obligatoriamente un administrador con credenciales fijas: **`username: admin`**, **`password: admin-jettra`**.
* Posee privilegios absolutos y máximos sobre todo el clúster. Ningún usuario secundario o intermedio puede alterar o revocar los roles del superusuario.



---

## IV. Lenguajes de Consulta y Soporte Multimodelo

1. **JettraQueryLanguage (LQL):** Lenguaje nativo basado en el paradigma de Streams y expresiones Lambda de Java (`from(...).filter(...).map(...)`), integrado nativamente con `JettraCollection`.
2. **JettraSQL:** Variante del estándar SQL adaptada para operar sobre los motores NoSQL de JettraStore con alto rendimiento.
3. **Motores Soportados:** Documentos, Clave-Valor, Columnas, Series Temporales, Geoespacial, Objetos Puros / Java Records, Grafos y Vectoriales. Soporte de referencias cruzadas (Intra-Engine e Inter-Engine) con control de carga perezosa (*lazy load*) o ansiosa (*eager load*).

---

## V. Componente de Monitoreo Preventivo (`JettraPolice`) y Métricas JMH

1. **JettraPolice:** Proceso autónomo en segundo plano de bajo consumo que supervisa continuamente la salud del motor, detecta picos de memoria RAM de forma temprana y emite alertas preventivas. Su activación se controla en `database.properties` mediante `jettrapolice.active = true / false`.
2. **Métricas con JMH (Java Microbenchmark Harness):** Suite nativa para medir el rendimiento de operaciones críticas, habilitable mediante la propiedad `jmh.metrics.active = true / false`.

---

## VI. Ecosistema de Módulos y Proyectos Integrados

El ecosistema modular incluye:

* `jettraRest` (Endpoints HTTP/REST ligeros con Virtual Threads).
* `jettraGRPC` (Red de ultra-latencia para sincronización y Raft).
* `jettraJson` (Serialización ultrarrápida), `jettraJWT` (Seguridad), `jettraRules` (Reglas de negocio), `jettraAnnotation` (Mapeo declarativo), `jettraTest` (Validación de consistencia) y `jettraEE` (Transaccionalidad e inyección).

---

## VII. Especificación de Proyectos Cliente y Consolas del Ecosistema

1. **`JettraStoreShell` (CLI):**
* Consola interactiva para administración completa de usuarios, bases de datos y motores CRUD, autenticación con `JettraJWT`, comandos de `BACKUP DATABASE` y `RESTORE DATABASE` sobre archivos `.jettra`, y un **menú de ejemplos precargados** (`load sample example_factura_db`) optimizado para ejecutarse por lotes mediante `JettraCollection` sin saturar el *Heap*.
* Documentación requerida en `JettraStoreShell/guide/shell.md`.


2. **`JettraStoreDriver`:**
* Driver Java de ultra-alto rendimiento optimizado con Virtual Threads, autenticación `JettraJWT`, uso estricto y nativo de `JettraCollection` para evitar *boxing/unboxing*, soporte LQL/JettraSQL e interfaces programáticas de backup/restore.
* Documentación requerida en `JettraStoreDriver/guide/book.md`.


3. **`JettraStoreFX` (Escritorio JavaFX con Panel 3D):**
* Aplicación con interfaz futurista para gestión visual de perfiles de conexión, usuarios, roles, operaciones CRUD, asistentes de backup/restore, consola LQL/JettraSQL integrada, **dashboard de recursos en tiempo real** (supervisión detallada de RAM, disco, ZGC y prevención de `OutOfMemoryError`) y un **visualizador de clúster en 3D** interactivo que muestra los nodos y la activación del anillo distribuido por saturación.
* Documentación requerida en `JettraStoreFX/guide/book.md`.


4. **`JettraStorePoliceFX` (Monitoreo 3D Inmersivo):**
* Interfaz avanzada de mundos 3D interactivos conectada vía `JettraJWT` para supervisar espacialmente los nodos del clúster, las rutas físicas de almacenamiento, las rutas del agente `JettraPolice`, el flujo de paquetes de datos y migración en anillo, y alertas tridimensionales animadas ante riesgos de saturación de memoria.
* Documentación requerida en `JettraStorePoliceFX/guide/book.md`.


5. **`JettraStoreMeter` (Pruebas de Carga y Estrés con JMeter):**
* Planes de prueba orientados a someter al clúster de 3 nodos a inserciones masivas concurrentes con millones de registros y autenticación `JettraJWT`. Incluye un **ciclo de vida de limpieza automatizada (*Teardown*)** que elimina todos los rastros temporales al finalizar las pruebas, devolviendo el almacenamiento `.jettra` a su estado base inicial.



---

## VIII. Documentación General del Motor Principal (`JettraStore/guide/`)

Se deben estructurar formalmente en Markdown los siguientes manuales técnicos:

* `JettraStore/guide/book.md`: Libro principal (Arquitectura, LSM, Off-Heap, Panama, ZGC, Raft, anillo distribuido, uso integral de `JettraCollection`, estrategias estrictas de prevención de `OutOfMemoryError` mediante *chunking* y flujos perezosos, configuración de `database.properties` y `jettra.config`, superusuario `admin` / `admin-jettra`, seguridad `JettraJWT`, motores y ejemplos prácticos).
* `JettraStore/guide/test.md`: Guía de ejecución de tests unitarios/integración, validaciones de seguridad, operaciones CRUD, clúster Raft, anillo distribuido y benchmarks JMH.
* `JettraStore/guide/presentations/architecture.md`: Presentación técnica para conferencias sobre la arquitectura integral del ecosistema.
* `JettraStore/guide/components.md`: Referencia exhaustiva de submódulos.
* `JettraStore/guide/query.md`: Guía profunda de sintaxis, rendimiento y escalabilidad para LQL y JettraSQL apoyada en `JettraCollection`.