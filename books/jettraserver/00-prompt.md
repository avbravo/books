Plan maestro estructurado y detallado para el desarrollo integral de **`JettraServer`** en **Google Antigravity IDE**, incorporando la arquitectura de alta eficiencia, la integración nativa con **`JettraFlux`**, el soporte para especificaciones **Jakarta EE** ligeras, y el nuevo selector de **tres modos de almacenamiento operativos (`JVM_RAM`, `DISK_MEMORY` y `OFF_HEAP`)**.

---

# Plan Maestro de Desarrollo: `JettraServer`

## 1. Objetivos del Proyecto

### 1.1 Objetivo General

Diseñar, estructurar y documentar el servidor modular de alto rendimiento **`JettraServer`** compatible con especificaciones Jakarta EE, optimizado para Java 25 bajo el ecosistema de Antigravity IDE, garantizando un consumo mínimo de recursos, ejecución rápida mediante E/S asíncrona, integración nativa con el motor de componentes UI **`JettraFlux`** y un subsistema de persistencia de triple modo flexible (`JVM_RAM`, `DISK_MEMORY` y `OFF_HEAP`).

### 1.2 Objetivos Específicos

1. **Establecer la estructura multi-módulo Maven:** Configurar los módulos base (`jettra-server-core`, `jettra-server-jakarta` y `jettra-server-flux`) utilizando Liberica JDK 25 con perfiles de optimización JVM (`Generational ZGC`, `Compact Object Headers`).
2. **Implementar el Motor NIO y Concurrencia:** Desarrollar un núcleo de red basado en hilos virtuales (`Virtual Threads`) y canales no bloqueantes con rendimiento idéntico a `JettraEE` y arranque en frío inferior a 100ms.
3. **Integrar el Selector de Tres Modos de Almacenamiento:** Proveer una estrategia de persistencia intercambiable en tiempo de arranque:
* **`JVM_RAM`**: Almacenamiento estándar en memoria Heap/Stack de la JVM.
* **`DISK_MEMORY`**: Motor de disco directo Off-Heap estructurado bajo el modelo LSM (`JettraMemory`).
* **`OFF_HEAP`**: Motor de páginas ranuradas nativas de alta velocidad (*Slotted-Page*).


4. **Soporte Jakarta EE y `JettraFlux`:** Proveer un contenedor ligero de inyección de dependencias, interceptores JAX-RS y el pipeline de renderizado de componentes tipados en Java sin HTML/JS manual.
5. **Generar Documentación Técnica Oficial:** Crear las guías especializadas para usuarios (`/guide/user-book.md`) y desarrolladores de núcleo (`/guide/core-book.md`).
6. **Asegurar la Calidad con `JettraTest`:** Configurar suites de pruebas estrictas eliminando cualquier dependencia de JUnit.

---

## 2. Fases de Desarrollo del Plan

### Fase 1: Arquitectura Multi-Módulo y Configuración de Compilación

* **Definición de Módulos:** Creación del proyecto raíz en Antigravity IDE y separación de responsabilidades:
* `jettra-server-core`: Gestión de red NIO, ciclo de vida del servidor y capa de abstracción de almacenamiento.
* `jettra-server-jakarta`: SPI para CDI y servicios REST ligeros.
* `jettra-server-flux`: Adaptador de renderizado para interfaces web tipadas en Java.


* **Configuración del Entorno:** Activación de flags de previsualización de Java 25 y gestión de dependencias mediante Maven y JitPack.

### Fase 2: Implementación del Subsistema de Tres Modos de Almacenamiento

* **Diseño del SPI de Persistencia:** Interfaz común para operaciones de lectura/escritura y transaccionalidad independiente del motor subyacente.
* **Desarrollo del Modo `JVM_RAM`:** Implementación de estructuras de datos eficientes en memoria Heap/Stack para entornos de desarrollo y pruebas rápidas.
* **Desarrollo del Modo `DISK_MEMORY` (`JettraMemory`):** Integración del motor de disco directo estructurado en árboles LSM para persistencia duradera de gran volumen sin saturar el recolector de basura.
* **Desarrollo del Modo `OFF_HEAP` (`Slotted-Page`):** Gestión directa de memoria nativa fuera del montón mediante la API FFM de Project Panama, organizando los datos en páginas ranuradas de tamaño fijo/variable con cero copias y latencia ultra baja.

### Fase 3: Núcleo de Red NIO y Soporte Jakarta EE

* **Bucle de Aceptación y Despacho:** Conexión de sockets mediante E/S asíncrona delegada a hilos virtuales para maximizar el throughput concurrente.
* **Contenedor CDI y JAX-RS:** Implementación del escaneo de metadatos por anotaciones para inyección de dependencias y mapeo de endpoints REST de manera ligera.

### Fase 4: Integración con `JettraFlux` y Aseguramiento de Calidad

* **Pipeline de UI:** Conexión directa del flujo HTTP con el renderizador de componentes visuales en Java y soporte para WebSockets reactivos.
* **Suite de Pruebas `JettraTest`:** Creación de escenarios de prueba de estrés, validación de concurrencia y verificación de integridad para los tres modos de almacenamiento.

---

## 3. Métodos de Validación

* **Validación de Rendimiento y Memoria (Benchmarks):**
* Medición del tiempo de arranque en frío (*Cold Start*), exigiendo tiempos menores a 100ms.
* Pruebas de presión de memoria mediante herramientas de monitoreo de la JVM para comprobar la ausencia de fragmentación y pausas de GC inferiores a 1ms bajo el perfil ZGC generacional.


* **Validación de Modos de Almacenamiento:**
* Pruebas unitarias y de integración cruzadas ejecutando la suite de `JettraTest` bajo cada perfil (`JVM_RAM`, `DISK_MEMORY`, `OFF_HEAP`) para garantizar consistencia en las operaciones CRUD y recuperación ante fallos.


* **Validación de Funcionalidad Web y Jakarta EE:**
* Pruebas de integración HTTP automatizadas simulando peticiones REST concurrentes y renderizado de componentes a través de `JettraFlux`.



---

## 4. Estructura y Contenido de la Documentación Oficial

Se generarán dos documentos fundamentales dentro del repositorio del proyecto:

### 4.1 Guía de Usuario: `/guide/user-book.md`

Este manual está dirigido a arquitectos y desarrolladores de aplicaciones que despliegan servicios sobre `JettraServer`. Su contenido incluye:

* **Introducción y Requisitos:** Guía rápida de instalación usando Liberica JDK 25 y SDKMAN.
* **Configuración del Servidor:** Parámetros de arranque en línea de comandos y variables de entorno.
* **Guía de Selección del Modo de Almacenamiento:** Criterios para elegir entre `JVM_RAM`, `DISK_MEMORY` y `OFF_HEAP` según los requerimientos de latencia, durabilidad y recursos de hardware.
* **Desarrollo de Endpoints y UI:** Ejemplos de uso de anotaciones Jakarta EE y despliegue de vistas utilizando `JettraFlux`.

### 4.2 Guía de Desarrollo del Núcleo: `/guide/core-book.md`

Este manual está dirigido a los ingenieros y mantenedores del núcleo del servidor (`jettra-server-core`). Su contenido incluye:

* **Arquitectura Interna del Motor NIO:** Documentación del flujo de eventos, manejo de canales y asignación de hilos virtuales.
* **Internos del Subsistema de Persistencia:** Diseño técnico del motor de páginas ranuradas (`Slotted-Page`) en Off-Heap, el árbol LSM de `DISK_MEMORY` y la gestión de memoria nativa mediante Project Panama FFM API.
* **Guía de Pruebas con `JettraTest`:** Convenciones, escritura de assertions y ejecución de pipelines de pruebas sin JUnit.
* **Normas de Contribución y Estilo de Código:** Directrices para mantener la compatibilidad con Java 25 y el bajo consumo de recursos en el core.