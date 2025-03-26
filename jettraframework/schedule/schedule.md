# Schedule 

Para crear una **API Java desde cero** que funcione como un **cron**, donde puedas programar tareas indicando la hora, minutos y segundos específicos para su ejecución, podemos usar la **API HTTP de Java** junto con el `ScheduledExecutorService` para manejar las tareas programadas. A continuación, te muestro cómo implementarlo paso a paso.

---

### 1. Configuración del Proyecto

No necesitas dependencias adicionales, ya que usaremos solo la **API HTTP de Java** (introducida en Java 11) y el `ScheduledExecutorService` de Java.

---

### 2. Implementación del Sistema de Programación de Tareas

#### Clase `CronScheduler`
Esta clase gestiona las tareas programadas utilizando `ScheduledExecutorService`. Permite programar tareas basadas en una hora específica (hora, minutos y segundos).

```java
import java.time.*;
import java.util.concurrent.*;
import java.util.HashMap;
import java.util.Map;

public class CronScheduler {

    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(5);
    private final Map<String, ScheduledFuture<?>> tasks = new HashMap<>();

    // Programar una tarea para una hora específica
    public void scheduleTaskAt(String taskId, Runnable task, int hour, int minute, int second) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime scheduledTime = LocalDateTime.of(now.getYear(), now.getMonth(), now.getDayOfMonth(), hour, minute, second);

        // Si la hora ya pasó hoy, programar para mañana
        if (scheduledTime.isBefore(now)) {
            scheduledTime = scheduledTime.plusDays(1);
        }

        Duration duration = Duration.between(now, scheduledTime);
        long delayInSeconds = duration.getSeconds();

        ScheduledFuture<?> future = scheduler.schedule(task, delayInSeconds, TimeUnit.SECONDS);
        tasks.put(taskId, future);
        System.out.println("Task scheduled: " + taskId + " at " + scheduledTime);
    }

    // Cancelar una tarea
    public boolean cancelTask(String taskId) {
        ScheduledFuture<?> future = tasks.get(taskId);
        if (future != null && !future.isDone()) {
            boolean cancelled = future.cancel(true);
            tasks.remove(taskId);
            System.out.println("Task cancelled: " + taskId);
            return cancelled;
        }
        System.out.println("Task not found or already completed: " + taskId);
        return false;
    }

    // Listar todas las tareas activas
    public Map<String, ScheduledFuture<?>> getTasks() {
        return new HashMap<>(tasks);
    }

    // Apagar el scheduler
    public void shutdown() {
        scheduler.shutdown();
        System.out.println("Scheduler shutdown.");
    }
}
```

---

### 3. Crear el Servidor HTTP

Usaremos la **API HTTP de Java** para crear un servidor que exponga endpoints para programar, cancelar y listar tareas.

#### Clase `HttpServerExample`

```java
import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.util.Map;

public class HttpServerExample {

    private static final CronScheduler cronScheduler = new CronScheduler();

    public static void main(String[] args) throws IOException {
        // Crear el servidor HTTP
        HttpServer server = HttpServer.create(new InetSocketAddress(8080), 0);

        // Endpoint para programar una tarea
        server.createContext("/schedule", new ScheduleHandler());

        // Endpoint para cancelar una tarea
        server.createContext("/cancel", new CancelHandler());

        // Endpoint para listar tareas activas
        server.createContext("/tasks", new TasksHandler());

        // Iniciar el servidor
        server.setExecutor(null); // Usa el executor por defecto
        server.start();
        System.out.println("HTTP Server is running on port 8080");
    }

    // Manejador para programar una tarea
    static class ScheduleHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            if ("POST".equals(exchange.getRequestMethod())) {
                String body = new String(exchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
                String[] params = body.split(",");
                if (params.length == 4) {
                    String taskId = params[0];
                    int hour = Integer.parseInt(params[1]);
                    int minute = Integer.parseInt(params[2]);
                    int second = Integer.parseInt(params[3]);

                    cronScheduler.scheduleTaskAt(taskId, () -> System.out.println("Executing task: " + taskId), hour, minute, second);
                    sendResponse(exchange, "Task scheduled: " + taskId);
                } else {
                    sendResponse(exchange, "Invalid parameters. Usage: taskId,hour,minute,second", 400);
                }
            } else {
                sendResponse(exchange, "Only POST method is supported.", 405);
            }
        }
    }

    // Manejador para cancelar una tarea
    static class CancelHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            if ("POST".equals(exchange.getRequestMethod())) {
                String body = new String(exchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
                String taskId = body.trim();

                boolean cancelled = cronScheduler.cancelTask(taskId);
                if (cancelled) {
                    sendResponse(exchange, "Task cancelled: " + taskId);
                } else {
                    sendResponse(exchange, "Failed to cancel task: " + taskId, 404);
                }
            } else {
                sendResponse(exchange, "Only POST method is supported.", 405);
            }
        }
    }

    // Manejador para listar tareas activas
    static class TasksHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            if ("GET".equals(exchange.getRequestMethod())) {
                Map<String, ?> tasks = cronScheduler.getTasks();
                StringBuilder response = new StringBuilder("Active tasks:\n");
                for (String taskId : tasks.keySet()) {
                    response.append(taskId).append("\n");
                }
                sendResponse(exchange, response.toString());
            } else {
                sendResponse(exchange, "Only GET method is supported.", 405);
            }
        }
    }

    // Método auxiliar para enviar respuestas HTTP
    private static void sendResponse(HttpExchange exchange, String response) throws IOException {
        sendResponse(exchange, response, 200);
    }

    private static void sendResponse(HttpExchange exchange, String response, int statusCode) throws IOException {
        exchange.sendResponseHeaders(statusCode, response.getBytes().length);
        OutputStream os = exchange.getResponseBody();
        os.write(response.getBytes());
        os.close();
    }
}
```

---

### 4. Ejecución del Servidor

1. **Iniciar el Servidor**:
   ```bash
   java HttpServerExample
   ```

2. **Probar los Endpoints**:
   - **Programar una Tarea**:
     ```bash
     curl -X POST http://localhost:8080/schedule -d "task1,14,30,0"
     ```
     Esto programa una tarea llamada `task1` para ejecutarse a las 14:30:00.

   - **Cancelar una Tarea**:
     ```bash
     curl -X POST http://localhost:8080/cancel -d "task1"
     ```

   - **Listar Tareas Activas**:
     ```bash
     curl http://localhost:8080/tasks
     ```

---

### 5. Explicación del Código

1. **CronScheduler**:
   - Calcula la diferencia de tiempo entre la hora actual y la hora programada.
   - Programa la tarea usando `ScheduledExecutorService`.

2. **Endpoints HTTP**:
   - `/schedule`: Programa una nueva tarea para una hora específica.
   - `/cancel`: Cancela una tarea existente.
   - `/tasks`: Lista las tareas activas.

3. **API HTTP de Java**:
   - Se utiliza para crear un servidor HTTP ligero que maneja las solicitudes.

---

### 6. Consideraciones

1. **Persistencia**:
   - Las tareas no son persistentes. Si el servidor se detiene, las tareas se pierden. Puedes agregar persistencia usando una base de datos.

2. **Zona Horaria**:
   - El sistema usa la zona horaria local. Si necesitas soportar múltiples zonas horarias, considera usar `ZonedDateTime`.

3. **Seguridad**:
   - Agrega autenticación y autorización para proteger los endpoints.

4. **Pruebas**:
   - Usa herramientas como **Postman** o **curl** para probar los endpoints.

---

Con este ejemplo, has creado una API estilo **cron** desde cero usando la **API HTTP de Java** y `ScheduledExecutorService`. Esta solución es simple pero funcional para casos de uso básicos.
