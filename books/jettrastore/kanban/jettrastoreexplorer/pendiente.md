Aquí tienes los tres prompts mejorados y estructurados profesionalmente para la IDE (como Google Antigravity IDE) o para tu asistente de desarrollo con Java 25 y JettraFlux.

Cada uno incluye el contexto técnico necesario, la ubicación exacta del componente y los requisitos de implementación para asegurar que la inteligencia artificial genere el código correcto a la primera.

---

### Prompt 1: Corrección del cierre de nodos en la página de Clúster

> "En el módulo `JettraStoreExplorer`, específicamente en la vista o página de gestión del clúster (`ClusterPage`), al hacer clic en el botón **'Apagar Nodo'** (`shutdownNode`), la acción no está deteniendo el nodo objetivo en el backend.
> Por favor:
> 1. Revisa la ruta o endpoint REST / evento WebSocket asociado al comando de apagado en el controlador o servicio del clúster.
> 2. Asegúrate de que el comando envíe la señal de interrupción adecuada al proceso del nodo o al gestor de ciclo de vida (`LifecycleManager`) utilizando Java 25.
> 3. Implementa un mecanismo de actualización de estado en tiempo real para que, una vez confirmado el apagado, la interfaz refleje inmediatamente que el nodo pasó a estado `OFFLINE`."
> 
> 

---

### Prompt 2: Modernización del diálogo de restauración con JettraFlux

> "En `JettraStoreExplorer`, dentro de la página `/explorer`, al abrir el diálogo **'Historial de Versiones del Registro'** y presionar el botón **'Restaurar esta Versión'**, actualmente se muestra un diálogo de confirmación nativo de JavaScript (`window.confirm`).
> Por favor:
> 1. Reemplaza el diálogo de confirmación tradicional por un componente modal nativo construido íntegramente con abstracciones de **JettraFlux** (componentes puramente en Java).
> 2. Asegúrate de que el modal mantenga el estilo visual coherente con el panel, ofreciendo botones claros de confirmación y cancelación.
> 3. Conecta el evento de aceptación del modal de JettraFlux para que ejecute la lógica de rollback/restauración de la versión seleccionada del registro."
> 
> 

---

### Prompt 3: Consola interactiva y ayuda en la página de consultas (`/query`)

> "En `JettraStore`, específicamente en la página `/query`, la consola de comandos interactiva no permite ejecutar comandos directos emulando el comportamiento de `JettraStoreShell`.
> Por favor:
> 1. Actualiza el componente de consola en la ruta `/query` para que procese y ejecute comandos de forma idéntica a `JettraStoreShell` (soporte para parsing de comandos, manejo de errores y salida formateada en la interfaz).
> 2. Añade un botón flotante o de cabecera etiquetado como **'Ayuda'** (`Help`).
> 3. Al hacer clic en dicho botón, despliega un panel colapsable o modal creado con JettraFlux que muestre la lista completa de comandos disponibles junto con ejemplos prácticos de uso."
> 
> 

---