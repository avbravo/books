# balanceo carga 

Implementar un **Load Balancer** en una aplicación Java que utiliza la API HTTP puede ser útil para distribuir las solicitudes entre varios servidores backend. Esto mejora la disponibilidad, escalabilidad y rendimiento del sistema.

A continuación, te muestro cómo implementar un **Load Balancer** básico utilizando la API HTTP de Java (introducida en Java 11 con `HttpClient`) y una estrategia simple de balanceo de carga (por ejemplo, Round Robin).

---

### 1. Configuración del Proyecto
Primero, asegúrate de tener Java 11 o superior instalado, ya que utilizaremos la clase `HttpClient` de la API HTTP estándar.

Si usas Maven, no necesitas dependencias adicionales, ya que la API HTTP es parte del JDK desde Java 11.

---

### 2. Estrategia de Balanceo de Carga: Round Robin
El **Round Robin** es una estrategia simple en la que las solicitudes se distribuyen secuencialmente entre los servidores disponibles.

#### Código del Load Balancer

```java
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;

public class LoadBalancer {

    private final List<String> servers; // Lista de servidores backend
    private int currentIndex = 0;       // Índice para el algoritmo Round Robin

    public LoadBalancer(List<String> servers) {
        this.servers = new ArrayList<>(servers);
    }

    // Método para obtener el siguiente servidor en la lista
    private synchronized String getNextServer() {
        String server = servers.get(currentIndex);
        currentIndex = (currentIndex + 1) % servers.size(); // Avanza al siguiente servidor
        return server;
    }

    // Método para enviar una solicitud HTTP a un servidor balanceado
    public String sendRequest(String path) throws Exception {
        String server = getNextServer();
        URI uri = URI.create(server + path);

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(uri)
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        return "Response from " + server + ": " + response.body();
    }

    public static void main(String[] args) throws Exception {
        // Lista de servidores backend
        List<String> servers = List.of(
                "http://localhost:8081",
                "http://localhost:8082",
                "http://localhost:8083"
        );

        LoadBalancer loadBalancer = new LoadBalancer(servers);

        // Simular varias solicitudes
        for (int i = 0; i < 10; i++) {
            System.out.println(loadBalancer.sendRequest("/api/resource"));
        }
    }
}
```

---

### 3. Explicación del Código

1. **Lista de Servidores (`servers`)**:
   - Contiene las URLs de los servidores backend.
   - Puedes agregar tantos servidores como desees.

2. **Algoritmo Round Robin**:
   - El método `getNextServer()` selecciona el siguiente servidor en la lista de manera cíclica.
   - Usa un índice (`currentIndex`) para rastrear el servidor actual.

3. **Envío de Solicitudes HTTP**:
   - El método `sendRequest()` construye una solicitud HTTP GET hacia el servidor seleccionado.
   - Utiliza la API HTTP de Java (`HttpClient`, `HttpRequest`, `HttpResponse`) para realizar la solicitud.

4. **Simulación de Solicitudes**:
   - En el método `main`, se realizan 10 solicitudes consecutivas.
   - Cada solicitud se envía a un servidor diferente según el algoritmo Round Robin.

---

### 4. Ejemplo de Servidores Backend
Para probar el balanceador de carga, puedes crear servidores simples usando cualquier framework (por ejemplo, Spring Boot). Aquí hay un ejemplo básico con **Spring Boot**:

#### Servidor 1 (Puerto 8081):
```java
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
public class Server1Application {

    public static void main(String[] args) {
        SpringApplication.run(Server1Application.class, args);
    }

    @GetMapping("/api/resource")
    public String getResource() {
        return "Response from Server 1";
    }
}
```

#### Servidor 2 (Puerto 8082):
```java
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
public class Server2Application {

    public static void main(String[] args) {
        SpringApplication.run(Server2Application.class, args);
    }

    @GetMapping("/api/resource")
    public String getResource() {
        return "Response from Server 2";
    }
}
```

#### Servidor 3 (Puerto 8083):
```java
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
public class Server3Application {

    public static void main(String[] args) {
        SpringApplication.run(Server3Application.class, args);
    }

    @GetMapping("/api/resource")
    public String getResource() {
        return "Response from Server 3";
    }
}
```

---

### 5. Resultado Esperado
Cuando ejecutes el programa `LoadBalancer`, verás una salida similar a esta:

```
Response from http://localhost:8081: Response from Server 1
Response from http://localhost:8082: Response from Server 2
Response from http://localhost:8083: Response from Server 3
Response from http://localhost:8081: Response from Server 1
Response from http://localhost:8082: Response from Server 2
Response from http://localhost:8083: Response from Server 3
...
```

Las solicitudes se distribuyen uniformemente entre los servidores backend.

---

### 6. Mejoras Posibles
1. **Detección de Fallos**:
   - Implementa un mecanismo para detectar servidores inactivos y excluirlos temporalmente del balanceo.

2. **Estrategias de Balanceo**:
   - Agrega otras estrategias, como **Weighted Round Robin**, **Least Connections**, o **IP Hash**.

3. **Persistencia de Sesión**:
   - Si necesitas mantener sesiones, implementa un mecanismo de "sticky sessions".

4. **Monitoreo**:
   - Añade métricas para monitorear el rendimiento y la carga de cada servidor.

---

Este es un ejemplo básico pero funcional de un **Load Balancer** en Java utilizando la API HTTP. Puedes expandirlo según tus necesidades específicas.

---

Crear un servidor de proxy inverso para balanceo de carga desde cero en Java es un proyecto interesante y desafiante. Este tipo de servidor actúa como intermediario entre los clientes y las instancias de los microservicios, distribuyendo las solicitudes entrantes entre varias instancias del backend.

A continuación, te guiaré paso a paso para implementar un **proxy inverso básico con balanceo de carga** en Java desde cero.

---

### **1. Arquitectura del Proxy Inverso**
El proxy inverso tendrá las siguientes características:
- Recibirá solicitudes HTTP de los clientes.
- Distribuirá las solicitudes a diferentes instancias del backend utilizando un algoritmo de balanceo de carga (por ejemplo, Round Robin).
- Actuará como intermediario entre el cliente y el backend, reenviando las respuestas del backend al cliente.

---

### **2. Implementación Paso a Paso**

#### **Paso 1: Configuración del Proyecto**
Usaremos `HttpServer` de Java para crear un servidor HTTP simple. Asegúrate de tener Java instalado en tu sistema.

#### **Paso 2: Crear el Balanceador de Carga**
Implementaremos un balanceador de carga simple basado en el algoritmo **Round Robin**.

```java
import java.util.ArrayList;
import java.util.List;

public class LoadBalancer {
    private List<String> backendServers;
    private int currentIndex;

    public LoadBalancer(List<String> servers) {
        this.backendServers = new ArrayList<>(servers);
        this.currentIndex = 0;
    }

    // Algoritmo Round Robin
    public synchronized String getNextServer() {
        if (backendServers.isEmpty()) {
            throw new IllegalStateException("No hay servidores disponibles.");
        }
        String server = backendServers.get(currentIndex);
        currentIndex = (currentIndex + 1) % backendServers.size();
        return server;
    }
}
```

---

#### **Paso 3: Crear el Servidor Proxy**
El servidor proxy escuchará las solicitudes entrantes, seleccionará un backend usando el balanceador de carga y reenviará la solicitud al backend correspondiente.

```java
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Arrays;
import java.util.List;

public class ReverseProxyServer {
    private static final int PORT = 8080; // Puerto del proxy
    private static final List<String> BACKEND_SERVERS = Arrays.asList(
        "http://localhost:8081",
        "http://localhost:8082",
        "http://localhost:8083"
    );

    public static void main(String[] args) throws IOException {
        // Crear el balanceador de carga
        LoadBalancer loadBalancer = new LoadBalancer(BACKEND_SERVERS);

        // Crear el servidor HTTP
        HttpServer server = HttpServer.create(new java.net.InetSocketAddress(PORT), 0);
        server.createContext("/", new ProxyHandler(loadBalancer));
        server.setExecutor(null); // Usa el executor por defecto
        server.start();

        System.out.println("Proxy inverso iniciado en http://localhost:" + PORT);
    }

    static class ProxyHandler implements HttpHandler {
        private final LoadBalancer loadBalancer;

        public ProxyHandler(LoadBalancer loadBalancer) {
            this.loadBalancer = loadBalancer;
        }

        @Override
        public void handle(HttpExchange exchange) throws IOException {
            // Obtener el backend seleccionado
            String backendUrl = loadBalancer.getNextServer();
            String path = exchange.getRequestURI().getPath();

            try {
                // Construir la URL del backend
                URL url = new URL(backendUrl + path);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod(exchange.getRequestMethod());

                // Copiar encabezados del cliente al backend
                exchange.getRequestHeaders().forEach((key, value) -> {
                    connection.setRequestProperty(key, String.join(",", value));
                });

                // Enviar la solicitud al backend
                connection.setDoOutput(true);
                if ("POST".equalsIgnoreCase(exchange.getRequestMethod())) {
                    try (OutputStream os = connection.getOutputStream()) {
                        exchange.getRequestBody().transferTo(os);
                    }
                }

                // Leer la respuesta del backend
                int responseCode = connection.getResponseCode();
                exchange.sendResponseHeaders(responseCode, 0);

                // Copiar la respuesta del backend al cliente
                try (var inputStream = connection.getInputStream()) {
                    inputStream.transferTo(exchange.getResponseBody());
                }
                exchange.close();
            } catch (Exception e) {
                e.printStackTrace();
                exchange.sendResponseHeaders(500, -1); // Error interno del servidor
                exchange.close();
            }
        }
    }
}
```

---

### **3. Explicación del Código**

1. **Balanceador de Carga**:
   - La clase `LoadBalancer` implementa un algoritmo **Round Robin** para seleccionar una instancia de backend.
   - Puedes extender esta clase para agregar más algoritmos, como Weighted Round Robin o Random Selection.

2. **Servidor Proxy**:
   - Usamos `HttpServer` de Java para crear un servidor HTTP que escucha en el puerto 8080.
   - El `ProxyHandler` maneja las solicitudes entrantes:
     - Selecciona un backend usando el balanceador de carga.
     - Reenvía la solicitud al backend.
     - Envía la respuesta del backend al cliente.

3. **Reenvío de Solicitudes**:
   - El proxy copia los encabezados y el cuerpo de la solicitud del cliente al backend.
   - También copia la respuesta del backend al cliente.

---

### **4. Ejecución del Proxy**

1. **Iniciar los Backends**:
   Asegúrate de tener tres servidores backend ejecutándose en los puertos 8081, 8082 y 8083. Puedes usar cualquier framework o herramienta para crear estos servidores. Por ejemplo, puedes usar Python para simularlos:

   ```python
   from http.server import BaseHTTPRequestHandler, HTTPServer

   class Handler(BaseHTTPRequestHandler):
       def do_GET(self):
           self.send_response(200)
           self.end_headers()
           self.wfile.write(b"Respuesta desde el backend")

   HTTPServer(("localhost", 8081), Handler).serve_forever()
   ```

   Repite este código para los puertos 8082 y 8083.

2. **Iniciar el Proxy**:
   Ejecuta la clase `ReverseProxyServer`. Verás un mensaje indicando que el proxy está escuchando en `http://localhost:8080`.

3. **Probar el Proxy**:
   Usa un navegador o una herramienta como `curl` para enviar solicitudes al proxy:

   ```bash
   curl http://localhost:8080/
   ```

   Observa cómo las solicitudes se distribuyen entre los backends.

---

### **5. Mejoras Posibles**

1. **Detección de Instancias Activas**:
   - Implementa un mecanismo para detectar dinámicamente las instancias activas (por ejemplo, usando un servicio de registro como Eureka).

2. **Manejo de Errores**:
   - Agrega reintento si un backend no responde.
   - Implementa un circuit breaker para evitar sobrecargar un backend fallido.

3. **Soporte para HTTPS**:
   - Configura SSL/TLS para cifrar las comunicaciones entre el cliente y el proxy.

4. **Monitoreo**:
   - Registra métricas como el número de solicitudes procesadas, latencia, etc.

---

### **Conclusión**
Este ejemplo muestra cómo crear un servidor de proxy inverso con balanceo de carga en Java desde cero. Aunque es básico, puede ser extendido para manejar casos de uso más complejos. Si necesitas una solución más robusta, considera usar herramientas como NGINX, HAProxy o frameworks como Spring Cloud Gateway.