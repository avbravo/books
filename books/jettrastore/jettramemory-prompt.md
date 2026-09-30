# Prompt para Antigravity IDE: Creación del proyecto JettraMemory

Actúa como Arquitecto de Software Senior y Desarrollador Core de Sistemas de Alto Rendimiento en Java. Tu tarea es generar la estructura completa, la configuración y el código base inicial para una librería reutilizable de alto rendimiento llamada **JettraMemory**.

## 1. Descripción General del Proyecto
`JettraMemory` es una librería de almacenamiento de objetos de alta velocidad y ultrabajo consumo diseñada para integrarse de forma nativa, limpia y transparente en **todos los componentes del ecosistema Jettra**. Opera **directamente en el disco duro**, evitando por completo el uso del Heap de la JVM para prevenir las pausas del Recolector de Basura (GC). El sistema gestiona su propio **mecanismo de recolección de basura y compactación personalizado** para reciclar el espacio en disco de manera autónoma.

El proyecto debe estar desarrollado utilizando **Java 25 (o superior)**, aprovechando las características de optimización más avanzadas de la plataforma (como Foreign Function & Memory API / Project Panama, concurrencia avanzada, etc.). Operará de forma nativa en un **entorno Jettra distribuido de tres nodos**, ofreciendo APIs modulares orientadas a una integración fluida con **JettraStore**, **JettraEE** y **JettraCollections**, asegurando plena compatibilidad binaria con los objetos generados por JettraStore.

---

## 2. Requisitos Técnicos y Arquitectura Core

### A. Librería Modular y de Fácil Integración (Plug-and-Play)
*   Diseñada como una librería desacoplada (tipo SDK / Starter) que cualquier componente Jettra pueda inicializar con una mínima configuración (patrón builder o inyección de dependencias).
*   Interfaces claras y contratos universales para que la persistencia y recuperación de datos sean idénticas sin importar qué módulo Jettra la invoque.

### B. Almacenamiento Off-Heap y Directo a Disco (Java 25+ Optimizado)
*   Utilizar APIs de alto rendimiento (`FileChannel`, `MappedByteBuffer` o la Foreign Function & Memory API) para la gestión segura de archivos de índice y datos.
*   Manipulación de representaciones binarias compactas que eviten la deserialización masiva en la memoria RAM del Heap.
*   Estructura basada en:
    1.  **Index File (.idx):** Mapea claves con ubicaciones físicas (offset/longitud).
    2.  **Data File (.jettra):** Almacena de manera secuencial los registros binarios totalmente compatibles con los objetos generados por **JettraStore**.

### C. Recolector de Basura Personalizado (Custom GC & Compaction)
*   Componente autónomo (`JettraGarbageCollector`) independiente del GC de la JVM.
*   **Mecanismo de Tombstones:** Marcado lógico de registros eliminados en el índice.
*   **Proceso de Compactación:** Hilo de fondo o tarea explícita para desfragmentar el archivo `.jettra` y liberar el espacio físico en disco sin interrumpir operaciones concurrentes.

### D. Entorno Distribuido de Tres Nodos y Replicación
*   Clúster resiliente configurado para operar en un **entorno distribuido de tres nodos**, garantizando sincronización y consistencia de datos entre ellos.

### E. Integración Nativa con el Ecosistema Jettra
*   **JettraCollections:** Vistas y adaptadores de datos para manipular registros en disco como estructuras de colección nativas.
*   **JettraStore y JettraEE:** Repositorios clave-valor optimizados, compatibilidad total con contenedores Jakarta EE / Helidon e interoperabilidad nativa de formatos de objetos.

---

## 3. Estructura de Paquetes Esperada
Genera la siguiente estructura de clases y paquetes base:

1. `com.jettra.memory.engine`: Motor principal de lectura/escritura en disco (`DiskStorageEngine`, `IndexManager`).
2. `com.jettra.memory.gc`: Lógica del recolector personalizado (`JettraGarbageCollector`, `CompactionTask`).
3. `com.jettra.memory.cluster`: Coordinación y sincronización para el clúster distribuido de tres nodos (`NodeCoordinator`, `ClusterReplicationManager`).
4. `com.jettra.memory.collections`: Adaptadores de API para **JettraCollections**.
5. `com.jettra.memory.serializer`: Utilidades de serialización binaria ultrarrápida compatibles con **JettraStore**.
6. `com.jettra.memory.integration`: Conectores universales para **JettraStore**, **JettraEE** y el resto de componentes Jettra.
7. `com.jettra.memory.api`: Clases de servicio de alto nivel y configuración inicial de la librería.

---

## 4. Entregables Esperados por el IDE
*   Archivo de configuración (`pom.xml` o `build.gradle`) estructurado como librería reutilizable para **Java 25+**.
*   Clase de entrada principal (`JettraMemoryBootstrap` o `JettraMemoryEngine`) con métodos core: `put(String key, byte[] data)`, `get(String key)`, `delete(String key)`, y `compact()`.
*   Implementación funcional del `JettraGarbageCollector` para el barrido de espacios muertos en el archivo `.jettra`.
*   Módulo base de coordinación para el clúster distribuido de tres nodos.
*   Ejemplo de prueba funcional utilizando el framework **JettraTest** demostrando la fácil integración de la librería, operaciones distribuidas con objetos de **JettraStore**, integración con **JettraCollections** y cero impacto en el Heap de la JVM.