# onetlme token 

Implementar un **One-Time Token (OTT)** es una técnica común para garantizar la seguridad en aplicaciones web, especialmente en flujos de autenticación o autorización. Un token de un solo uso se genera para una transacción específica y se invalida después de ser utilizado, lo que ayuda a prevenir ataques como el reenvío de tokens o el uso no autorizado.

A continuación, te muestro cómo implementar un sistema de **One-Time Token** en Java desde cero, utilizando la **API HTTP de Java** para manejar las solicitudes.

---

### 1. Configuración del Proyecto

No necesitas dependencias adicionales, ya que usaremos solo la **API HTTP de Java** (introducida en Java 11) y algunas utilidades estándar de Java para generar y validar tokens.

---

### 2. Implementación del Sistema de One-Time Token

#### Clase `OneTimeTokenManager`
Esta clase gestiona la generación, validación e invalidación de tokens de un solo uso.

```java
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class OneTimeTokenManager {

    private final Map<String, String> tokenStore = new HashMap<>();

    // Generar un nuevo token
    public String generateToken(String userId) {
        String token = UUID.randomUUID().toString();
        tokenStore.put(token, userId);
        System.out.println("Generated token for user: " + userId);
        return token;
    }

    // Validar un token
    public boolean validateToken(String token) {
        if (tokenStore.containsKey(token)) {
            String userId = tokenStore.remove(token); // Invalidar el token después de usarlo
            System.out.println("Token validated and invalidated for user: " + userId);
            return true;
        }
        System.out.println("Invalid or expired token: " + token);
        return false;
    }

    // Listar todos los tokens activos (opcional)
    public Map<String, String> getActiveTokens() {
        return new HashMap<>(tokenStore);
    }
}
```

---

### 3. Crear el Servidor HTTP

Usaremos la **API HTTP de Java** para crear un servidor que exponga endpoints para generar, validar y listar tokens.

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

    private static final OneTimeTokenManager tokenManager = new OneTimeTokenManager();

    public static void main(String[] args) throws IOException {
        // Crear el servidor HTTP
        HttpServer server = HttpServer.create(new InetSocketAddress(8080), 0);

        // Endpoint para generar un token
        server.createContext("/generate", new GenerateHandler());

        // Endpoint para validar un token
        server.createContext("/validate", new ValidateHandler());

        // Endpoint para listar tokens activos
        server.createContext("/tokens", new TokensHandler());

        // Iniciar el servidor
        server.setExecutor(null); // Usa el executor por defecto
        server.start();
        System.out.println("HTTP Server is running on port 8080");
    }

    // Manejador para generar un token
    static class GenerateHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            if ("POST".equals(exchange.getRequestMethod())) {
                String body = new String(exchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
                String userId = body.trim();

                if (!userId.isEmpty()) {
                    String token = tokenManager.generateToken(userId);
                    sendResponse(exchange, "Token generated: " + token);
                } else {
                    sendResponse(exchange, "Invalid user ID", 400);
                }
            } else {
                sendResponse(exchange, "Only POST method is supported.", 405);
            }
        }
    }

    // Manejador para validar un token
    static class ValidateHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            if ("POST".equals(exchange.getRequestMethod())) {
                String body = new String(exchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
                String token = body.trim();

                if (!token.isEmpty()) {
                    boolean isValid = tokenManager.validateToken(token);
                    if (isValid) {
                        sendResponse(exchange, "Token is valid.");
                    } else {
                        sendResponse(exchange, "Token is invalid or expired.", 401);
                    }
                } else {
                    sendResponse(exchange, "Invalid token", 400);
                }
            } else {
                sendResponse(exchange, "Only POST method is supported.", 405);
            }
        }
    }

    // Manejador para listar tokens activos
    static class TokensHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            if ("GET".equals(exchange.getRequestMethod())) {
                Map<String, String> activeTokens = tokenManager.getActiveTokens();
                StringBuilder response = new StringBuilder("Active tokens:\n");
                for (Map.Entry<String, String> entry : activeTokens.entrySet()) {
                    response.append(entry.getKey()).append(" -> ").append(entry.getValue()).append("\n");
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
   - **Generar un Token**:
     ```bash
     curl -X POST http://localhost:8080/generate -d "user123"
     ```
     Esto genera un token para el usuario `user123`.

   - **Validar un Token**:
     ```bash
     curl -X POST http://localhost:8080/validate -d "generated-token-here"
     ```
     Si el token es válido, se invalidará automáticamente.

   - **Listar Tokens Activos**:
     ```bash
     curl http://localhost:8080/tokens
     ```

---

### 5. Explicación del Código

1. **OneTimeTokenManager**:
   - Genera tokens únicos usando `UUID.randomUUID()`.
   - Almacena los tokens en un `HashMap` junto con el ID del usuario asociado.
   - Invalida el token después de su primera validación.

2. **Endpoints HTTP**:
   - `/generate`: Genera un token para un usuario específico.
   - `/validate`: Valida un token y lo invalida si es correcto.
   - `/tokens`: Lista los tokens activos (opcional).

3. **Seguridad**:
   - Los tokens son únicos y se invalidan después de su primer uso.
   - Puedes agregar cifrado o firmas digitales si necesitas mayor seguridad.

---

### 6. Consideraciones

1. **Persistencia**:
   - Los tokens no son persistentes. Si el servidor se detiene, los tokens se pierden. Puedes almacenarlos en una base de datos para persistencia.

2. **Expiración**:
   - Actualmente, los tokens no tienen tiempo de expiración. Puedes agregar un timestamp al generar el token y verificar su validez antes de usarlo.

3. **Seguridad Adicional**:
   - Usa HTTPS para proteger las comunicaciones.
   - Agrega autenticación y autorización para proteger los endpoints.

4. **Escalabilidad**:
   - Para sistemas distribuidos, considera usar un almacén centralizado como Redis para gestionar los tokens.

---

Con este ejemplo, has implementado un sistema de **One-Time Token** desde cero usando la **API HTTP de Java**. Este sistema es simple pero funcional para casos de uso básicos.