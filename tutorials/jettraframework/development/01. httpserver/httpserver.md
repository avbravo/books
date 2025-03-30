
## HTTP Server
Si no deseas usar **Spring**, **Jakarta EE** ni frameworks como **SparkJava**, puedes crear un servicio RESTful desde cero utilizando solo las bibliotecas estándar de Java, como `HttpServer` de la API `com.sun.net.httpserver`. Este enfoque es completamente manual y te permite entender cómo funcionan los servidores HTTP bajo el capó.

A continuación, te muestro cómo implementar un servicio RESTful básico usando solo las herramientas proporcionadas por el JDK.

---

### Paso 1: Configuración del Proyecto
No necesitas configurar dependencias adicionales. Este ejemplo utiliza solo el JDK estándar, por lo que puedes crear un proyecto simple en cualquier IDE o editor de texto.

---

### Paso 2: Crear el Modelo de Datos
Define una clase simple para representar los datos, por ejemplo, un modelo de `Usuario`.

```java
package com.example.restapi.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {
    private String id;
    private String name;
    private String email;
}
```

---

### Paso 3: Crear el Servidor HTTP
Usa la clase `HttpServer` de la API `com.sun.net.httpserver` para manejar solicitudes HTTP.

```java
package com.example.restapi;

import com.example.restapi.model.User;
import com.fasterxml.jackson.databind.ObjectMapper;

import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class RestApiApplication {

    private static final List<User> users = new ArrayList<>();
    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static void main(String[] args) throws IOException {
        // Crear un servidor HTTP en el puerto 8080
        HttpServer server = HttpServer.create(new InetSocketAddress(8080), 0);

        // Definir rutas
        server.createContext("/users", new UserHandler());
        server.createContext("/users/", new UserHandler());

        // Iniciar el servidor
        server.setExecutor(null); // Usa el executor por defecto
        server.start();
        System.out.println("Servidor iniciado en http://localhost:8080");
    }

    static class UserHandler implements HttpHandler {

        @Override
        public void handle(HttpExchange exchange) throws IOException {
            String method = exchange.getRequestMethod();
            String path = exchange.getRequestURI().getPath();

            try {
                if (method.equalsIgnoreCase("GET") && path.equals("/users")) {
                    handleGetAllUsers(exchange);
                } else if (method.equalsIgnoreCase("GET") && path.startsWith("/users/")) {
                    handleGetUserById(exchange);
                } else if (method.equalsIgnoreCase("POST") && path.equals("/users")) {
                    handleCreateUser(exchange);
                } else if (method.equalsIgnoreCase("DELETE") && path.startsWith("/users/")) {
                    handleDeleteUser(exchange);
                } else {
                    sendResponse(exchange, 404, "Endpoint no encontrado");
                }
            } catch (Exception e) {
                sendResponse(exchange, 500, "Error interno del servidor: " + e.getMessage());
            }
        }

        private void handleGetAllUsers(HttpExchange exchange) throws IOException {
            String jsonResponse = objectMapper.writeValueAsString(users);
            sendResponse(exchange, 200, jsonResponse);
        }

        private void handleGetUserById(HttpExchange exchange) throws IOException {
            String path = exchange.getRequestURI().getPath();
            String id = path.substring(path.lastIndexOf("/") + 1);

            Optional<User> user = users.stream()
                    .filter(u -> u.getId().equals(id))
                    .findFirst();

            if (user.isPresent()) {
                String jsonResponse = objectMapper.writeValueAsString(user.get());
                sendResponse(exchange, 200, jsonResponse);
            } else {
                sendResponse(exchange, 404, "Usuario no encontrado");
            }
        }

        private void handleCreateUser(HttpExchange exchange) throws IOException {
            String body = new String(exchange.getRequestBody().readAllBytes());
            User user = objectMapper.readValue(body, User.class);
            users.add(user);

            sendResponse(exchange, 201, objectMapper.writeValueAsString(user));
        }

        private void handleDeleteUser(HttpExchange exchange) throws IOException {
            String path = exchange.getRequestURI().getPath();
            String id = path.substring(path.lastIndexOf("/") + 1);

            boolean removed = users.removeIf(user -> user.getId().equals(id));

            if (removed) {
                sendResponse(exchange, 204, "");
            } else {
                sendResponse(exchange, 404, "Usuario no encontrado");
            }
        }

        private void sendResponse(HttpExchange exchange, int statusCode, String response) throws IOException {
            exchange.getResponseHeaders().set("Content-Type", "application/json");
            exchange.sendResponseHeaders(statusCode, response.getBytes().length);
            OutputStream os = exchange.getResponseBody();
            os.write(response.getBytes());
            os.close();
        }
    }
}
```

---

### Paso 4: Ejecutar la Aplicación
Simplemente ejecuta la clase principal (`RestApiApplication`) como una aplicación Java estándar. El servidor HTTP se iniciará en el puerto `8080`.

---

### Paso 5: Probar el API
Una vez que la aplicación esté en ejecución, puedes probar los endpoints RESTful utilizando herramientas como **Postman** o **cURL**.

#### Ejemplo de Solicitudes:
1. **Obtener Todos los Usuarios**:  
   `GET http://localhost:8080/users`

2. **Obtener Usuario por ID**:  
   `GET http://localhost:8080/users/{id}`

3. **Crear Usuario**:  
   `POST http://localhost:8080/users`  
   Body:
   ```json
   {
       "id": "1",
       "name": "John Doe",
       "email": "john.doe@example.com"
   }
   ```

4. **Eliminar Usuario**:  
   `DELETE http://localhost:8080/users/{id}`

---

### Resumen
Este ejemplo muestra cómo crear un servicio RESTful desde cero utilizando solo las herramientas estándar del JDK, específicamente la clase `HttpServer` de la API `com.sun.net.httpserver`. Aunque este enfoque es más manual y requiere escribir más código, es útil para comprender cómo funcionan los servidores HTTP y cómo procesar solicitudes y respuestas.

Este método es adecuado para proyectos pequeños o prototipos rápidos donde no deseas depender de frameworks externos. Sin embargo, para aplicaciones más grandes o complejas, es recomendable usar frameworks como Spring o Jakarta EE para simplificar el desarrollo y mejorar la productividad.

---

# tema
HttpServer

crear rest services endpoint usandoj ava api HttpServer y virtual trheads usando un crud 



Aquí tienes un ejemplo de un servicio REST CRUD usando Java HttpServer y virtual threads (Java 21+):

```java
import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicLong;

import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

class User {
    private Long id;
    private String name;
    private String email;
    
    // Getters y setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
}

public class RestServer {
    private static final ConcurrentHashMap<Long, User> users = new ConcurrentHashMap<>();
    private static final AtomicLong idCounter = new AtomicLong(1);
    private static final int PORT = 8080;

    public static void main(String[] args) throws IOException {
        HttpServer server = HttpServer.create(new InetSocketAddress(PORT), 0);
        
        // Configurar executor con virtual threads
        server.setExecutor(Executors.newVirtualThreadPerTaskExecutor());
        
        // Registrar endpoints
        server.createContext("/users", new UsersHandler());
        server.createContext("/users/", new UserHandler());
        
        server.start();
        System.out.println("Server started on port " + PORT);
    }

    static class UsersHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            try {
                switch (exchange.getRequestMethod()) {
                    case "GET" -> handleGetAll(exchange);
                    case "POST" -> handlePost(exchange);
                    default -> sendResponse(exchange, 405, "Method Not Allowed");
                }
            } finally {
                exchange.close();
            }
        }

        private void handleGetAll(HttpExchange exchange) throws IOException {
            String response = JsonUtil.toJson(users.values());
            sendResponse(exchange, 200, response);
        }

        private void handlePost(HttpExchange exchange) throws IOException {
            User user = JsonUtil.fromJson(exchange.getRequestBody(), User.class);
            user.setId(idCounter.getAndIncrement());
            users.put(user.getId(), user);
            sendResponse(exchange, 201, JsonUtil.toJson(user));
        }
    }

    static class UserHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            try {
                Long id = extractId(exchange.getRequestURI().getPath());
                if (id == null) {
                    sendResponse(exchange, 400, "Invalid ID");
                    return;
                }

                switch (exchange.getRequestMethod()) {
                    case "GET" -> handleGet(exchange, id);
                    case "PUT" -> handlePut(exchange, id);
                    case "DELETE" -> handleDelete(exchange, id);
                    default -> sendResponse(exchange, 405, "Method Not Allowed");
                }
            } finally {
                exchange.close();
            }
        }

        private void handleGet(HttpExchange exchange, Long id) throws IOException {
            User user = users.get(id);
            if (user == null) {
                sendResponse(exchange, 404, "User not found");
            } else {
                sendResponse(exchange, 200, JsonUtil.toJson(user));
            }
        }

        private void handlePut(HttpExchange exchange, Long id) throws IOException {
            User existing = users.get(id);
            if (existing == null) {
                sendResponse(exchange, 404, "User not found");
                return;
            }

            User updates = JsonUtil.fromJson(exchange.getRequestBody(), User.class);
            existing.setName(updates.getName());
            existing.setEmail(updates.getEmail());
            sendResponse(exchange, 200, JsonUtil.toJson(existing));
        }

        private void handleDelete(HttpExchange exchange, Long id) throws IOException {
            if (users.remove(id) != null) {
                sendResponse(exchange, 204, "");
            } else {
                sendResponse(exchange, 404, "User not found");
            }
        }

        private Long extractId(String path) {
            try {
                String[] parts = path.split("/");
                return Long.parseLong(parts[2]);
            } catch (Exception e) {
                return null;
            }
        }
    }

    static class JsonUtil {
        private static final ObjectMapper mapper = new ObjectMapper();

        public static String toJson(Object object) throws IOException {
            return mapper.writeValueAsString(object);
        }

        public static <T> T fromJson(InputStream is, Class<T> type) throws IOException {
            return mapper.readValue(is, type);
        }
    }

    private static void sendResponse(HttpExchange exchange, int statusCode, String response) throws IOException {
        if (statusCode == 204) {
            exchange.sendResponseHeaders(statusCode, -1);
            return;
        }
        
        byte[] bytes = response.getBytes(StandardCharsets.UTF_8);
        exchange.getResponseHeaders().set("Content-Type", "application/json");
        exchange.sendResponseHeaders(statusCode, bytes.length);
        try (OutputStream os = exchange.getResponseBody()) {
            os.write(bytes);
        }
    }
}
```

Para usar este ejemplo necesitarás:

1. Java 21+ (para virtual threads)
2. Agregar la dependencia de Jackson para JSON en tu pom.xml:

```xml
<dependency>
    <groupId>com.fasterxml.jackson.core</groupId>
    <artifactId>jackson-databind</artifactId>
    <version>2.15.0</version>
</dependency>
```

Endpoints disponibles:

- `GET /users` - Obtener todos los usuarios
- `POST /users` - Crear nuevo usuario
- `GET /users/{id}` - Obtener usuario por ID
- `PUT /users/{id}` - Actualizar usuario
- `DELETE /users/{id}` - Eliminar usuario

Ejemplo de uso con curl:

```bash
# Crear usuario
curl -X POST -H "Content-Type: application/json" -d '{"name":"John Doe","email":"john@example.com"}' http://localhost:8080/users

# Obtener todos los usuarios
curl http://localhost:8080/users

# Obtener usuario específico
curl http://localhost:8080/users/1

# Actualizar usuario
curl -X PUT -H "Content-Type: application/json" -d '{"name":"John Updated","email":"john.new@example.com"}' http://localhost:8080/users/1

# Eliminar usuario
curl -X DELETE http://localhost:8080/users/1
```

Características principales:

1. Usa el HttpServer nativo de Java
2. Implementa virtual threads para mejor escalabilidad
3. CRUD completo para recursos de usuario
4. Manejo básico de JSON con Jackson
5. Almacenamiento en memoria usando ConcurrentHashMap
6. Manejo de códigos de estado HTTP apropiados
7. Validación básica de IDs

Este es un ejemplo básico que puedes extender con:
- Validación más robusta
- Manejo de errores mejorado
- Paginación para GET /users
- Autenticación/autorización
- Conexión a base de datos real
- Logging
- Pruebas unitarias


___

A continuación, te muestro un ejemplo de un proyecto Java que utiliza el API HTTP (`java.net.http`) y `com.sun.net.httpserver.HttpServer` para crear endpoints con `HttpHandler` que implementan operaciones CRUD (Create, Read, Update, Delete). Este proyecto es un servidor HTTP básico que maneja solicitudes GET, POST, PUT y DELETE.

---

### Estructura del Proyecto
```
crud-http-server/
├── src/
│   ├── Main.java
│   ├── User.java
│   └── UserHandler.java
└── README.md
```

---

### Código Fuente

#### 1. `Main.java`
Este archivo contiene la configuración del servidor HTTP y la asignación de los endpoints.

```java
import com.sun.net.httpserver.HttpServer;
import java.io.IOException;
import java.net.InetSocketAddress;

public class Main {
    public static void main(String[] args) throws IOException {
        // Crear una instancia del servidor HTTP
        HttpServer server = HttpServer.create(new InetSocketAddress(8080), 0);

        // Asignar el manejador para las rutas CRUD
        server.createContext("/users", new UserHandler());
        server.createContext("/users/", new UserHandler()); // Para manejar IDs específicos

        // Iniciar el servidor
        server.setExecutor(null); // Usa el executor por defecto
        server.start();
        System.out.println("Servidor HTTP iniciado en http://localhost:8080");
    }
}
```

---

#### 2. `User.java`
Esta clase representa un modelo simple de usuario.

```java
public class User {
    private int id;
    private String name;
    private String email;

    // Constructor
    public User(int id, String name, String email) {
        this.id = id;
        this.name = name;
        this.email = email;
    }

    // Getters y Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String toString() {
        return "User{id=" + id + ", name='" + name + "', email='" + email + "'}";
    }
}
```

---

#### 3. `UserHandler.java`
Este archivo contiene la lógica para manejar las solicitudes HTTP y realizar operaciones CRUD.

```java
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class UserHandler implements HttpHandler {
    // Simulación de una base de datos en memoria
    private static List<User> users = new ArrayList<>();
    private static int nextId = 1;

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String method = exchange.getRequestMethod();
        String path = exchange.getRequestURI().getPath();

        // Manejar las operaciones CRUD
        try {
            if (method.equalsIgnoreCase("GET")) {
                handleGet(exchange, path);
            } else if (method.equalsIgnoreCase("POST")) {
                handlePost(exchange);
            } else if (method.equalsIgnoreCase("PUT")) {
                handlePut(exchange, path);
            } else if (method.equalsIgnoreCase("DELETE")) {
                handleDelete(exchange, path);
            } else {
                sendResponse(exchange, 405, "Método no permitido");
            }
        } catch (Exception e) {
            sendResponse(exchange, 500, "Error interno del servidor: " + e.getMessage());
        }
    }

    // Manejar GET (Leer usuarios)
    private void handleGet(HttpExchange exchange, String path) throws IOException {
        if (path.equals("/users")) {
            // Obtener todos los usuarios
            String response = users.toString();
            sendResponse(exchange, 200, response);
        } else {
            // Obtener un usuario específico por ID
            Optional<Integer> userId = extractIdFromPath(path);
            if (userId.isPresent()) {
                User user = findUserById(userId.get());
                if (user != null) {
                    sendResponse(exchange, 200, user.toString());
                } else {
                    sendResponse(exchange, 404, "Usuario no encontrado");
                }
            } else {
                sendResponse(exchange, 400, "ID inválido");
            }
        }
    }

    // Manejar POST (Crear usuario)
    private void handlePost(HttpExchange exchange) throws IOException {
        String body = new String(exchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
        String[] parts = body.split("&");
        String name = "", email = "";

        for (String part : parts) {
            String[] keyValue = part.split("=");
            if (keyValue.length == 2) {
                if (keyValue[0].equals("name")) {
                    name = keyValue[1];
                } else if (keyValue[0].equals("email")) {
                    email = keyValue[1];
                }
            }
        }

        if (!name.isEmpty() && !email.isEmpty()) {
            User newUser = new User(nextId++, name, email);
            users.add(newUser);
            sendResponse(exchange, 201, "Usuario creado: " + newUser);
        } else {
            sendResponse(exchange, 400, "Datos incompletos");
        }
    }

    // Manejar PUT (Actualizar usuario)
    private void handlePut(HttpExchange exchange, String path) throws IOException {
        Optional<Integer> userId = extractIdFromPath(path);
        if (userId.isPresent()) {
            User user = findUserById(userId.get());
            if (user != null) {
                String body = new String(exchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
                String[] parts = body.split("&");
                String name = "", email = "";

                for (String part : parts) {
                    String[] keyValue = part.split("=");
                    if (keyValue.length == 2) {
                        if (keyValue[0].equals("name")) {
                            name = keyValue[1];
                        } else if (keyValue[0].equals("email")) {
                            email = keyValue[1];
                        }
                    }
                }

                if (!name.isEmpty()) {
                    user.setName(name);
                }
                if (!email.isEmpty()) {
                    user.setEmail(email);
                }

                sendResponse(exchange, 200, "Usuario actualizado: " + user);
            } else {
                sendResponse(exchange, 404, "Usuario no encontrado");
            }
        } else {
            sendResponse(exchange, 400, "ID inválido");
        }
    }

    // Manejar DELETE (Eliminar usuario)
    private void handleDelete(HttpExchange exchange, String path) throws IOException {
        Optional<Integer> userId = extractIdFromPath(path);
        if (userId.isPresent()) {
            User user = findUserById(userId.get());
            if (user != null) {
                users.remove(user);
                sendResponse(exchange, 200, "Usuario eliminado: " + user);
            } else {
                sendResponse(exchange, 404, "Usuario no encontrado");
            }
        } else {
            sendResponse(exchange, 400, "ID inválido");
        }
    }

    // Enviar respuesta al cliente
    private void sendResponse(HttpExchange exchange, int statusCode, String response) throws IOException {
        exchange.sendResponseHeaders(statusCode, response.getBytes().length);
        OutputStream os = exchange.getResponseBody();
        os.write(response.getBytes());
        os.close();
    }

    // Extraer ID de la ruta
    private Optional<Integer> extractIdFromPath(String path) {
        try {
            String[] parts = path.split("/");
            if (parts.length == 3) {
                return Optional.of(Integer.parseInt(parts[2]));
            }
        } catch (NumberFormatException e) {
            // Ignorar errores de formato
        }
        return Optional.empty();
    }

    // Buscar usuario por ID
    private User findUserById(int id) {
        return users.stream().filter(user -> user.getId() == id).findFirst().orElse(null);
    }
}
```

---

### Ejecución del Proyecto

1. Compila el proyecto:
   ```bash
   javac src/*.java -d out
   ```

2. Ejecuta el servidor:
   ```bash
   java -cp out Main
   ```

3. Prueba los endpoints usando herramientas como `curl` o Postman:

   - **Crear usuario** (POST):
     ```bash
     curl -X POST -d "name=John&email=john@example.com" http://localhost:8080/users
     ```

   - **Obtener todos los usuarios** (GET):
     ```bash
     curl http://localhost:8080/users
     ```

   - **Obtener un usuario por ID** (GET):
     ```bash
     curl http://localhost:8080/users/1
     ```

   - **Actualizar usuario** (PUT):
     ```bash
     curl -X PUT -d "name=Jane&email=jane@example.com" http://localhost:8080/users/1
     ```

   - **Eliminar usuario** (DELETE):
     ```bash
     curl -X DELETE http://localhost:8080/users/1
     ```

---

### Notas Finales
- Este ejemplo utiliza `HttpServer` de Java, que es adecuado para proyectos simples o prototipos.
- Para aplicaciones más robustas, considera usar frameworks como Spring Boot o Jakarta EE.
- Asegúrate de manejar correctamente las excepciones y validar los datos en un entorno de producción.

¡Espero que este ejemplo te sea útil!


___

# ejemplo con procedimientos de anotaciones 


El uso de **Java Annotation Processing** para generar endpoints HTTP es un enfoque avanzado que permite automatizar la creación de código basado en anotaciones personalizadas. Este método es comúnmente utilizado en frameworks como Spring o JAX-RS, donde las anotaciones como `@GET`, `@POST`, etc., se utilizan para definir rutas y métodos HTTP.

A continuación, te muestro cómo implementar un sistema básico de procesamiento de anotaciones para generar endpoints CRUD automáticamente. Este ejemplo utiliza el API de Java `javax.annotation.processing` para procesar anotaciones personalizadas y generar código dinámicamente.

---

### Estructura del Proyecto
```
crud-annotation-processing/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   ├── Main.java
│   │   │   ├── User.java
│   │   │   ├── UserHandler.java
│   │   │   ├── annotations/
│   │   │   │   ├── Endpoint.java
│   │   │   │   ├── GET.java
│   │   │   │   ├── POST.java
│   │   │   │   ├── PUT.java
│   │   │   │   └── DELETE.java
│   │   │   └── processor/
│   │   │       └── EndpointProcessor.java
│   │   └── resources/
│   └── generated/
└── README.md
```

---

### Código Fuente

#### 1. Anotaciones Personalizadas
Definimos anotaciones para representar los métodos HTTP (`GET`, `POST`, `PUT`, `DELETE`) y una anotación principal para marcar las clases que contienen endpoints.

##### `annotations/Endpoint.java`
```java
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.SOURCE)
@Target(ElementType.TYPE)
public @interface Endpoint {
    String path();
}
```

##### `annotations/GET.java`
```java
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.SOURCE)
@Target(ElementType.METHOD)
public @interface GET {
    String path() default "";
}
```

##### `annotations/POST.java`
```java
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.SOURCE)
@Target(ElementType.METHOD)
public @interface POST {
    String path() default "";
}
```

##### `annotations/PUT.java`
```java
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.SOURCE)
@Target(ElementType.METHOD)
public @interface PUT {
    String path() default "";
}
```

##### `annotations/DELETE.java`
```java
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.SOURCE)
@Target(ElementType.METHOD)
public @interface DELETE {
    String path() default "";
}
```

---

#### 2. Procesador de Anotaciones
El procesador de anotaciones analiza las clases anotadas con `@Endpoint` y genera código para manejar los métodos HTTP.

##### `processor/EndpointProcessor.java`
```java
import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.tools.JavaFileObject;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Set;

@SupportedAnnotationTypes("annotations.Endpoint")
@SupportedSourceVersion(SourceVersion.RELEASE_8)
public class EndpointProcessor extends AbstractProcessor {

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        for (Element element : roundEnv.getElementsAnnotatedWith(Endpoint.class)) {
            if (element instanceof TypeElement) {
                TypeElement typeElement = (TypeElement) element;
                Endpoint endpointAnnotation = typeElement.getAnnotation(Endpoint.class);
                String className = typeElement.getSimpleName().toString();
                String handlerClassName = className + "Handler";

                try {
                    generateHandlerClass(handlerClassName, endpointAnnotation.path(), typeElement);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return true;
    }

    private void generateHandlerClass(String handlerClassName, String basePath, TypeElement typeElement) throws IOException {
        StringBuilder handlerCode = new StringBuilder();
        handlerCode.append("import com.sun.net.httpserver.HttpExchange;\n");
        handlerCode.append("import com.sun.net.httpserver.HttpHandler;\n");
        handlerCode.append("import java.io.IOException;\n");
        handlerCode.append("import java.io.OutputStream;\n\n");

        handlerCode.append("public class ").append(handlerClassName).append(" implements HttpHandler {\n");
        handlerCode.append("    @Override\n");
        handlerCode.append("    public void handle(HttpExchange exchange) throws IOException {\n");
        handlerCode.append("        String method = exchange.getRequestMethod();\n");
        handlerCode.append("        String path = exchange.getRequestURI().getPath();\n\n");

        for (Element enclosedElement : typeElement.getEnclosedElements()) {
            if (enclosedElement instanceof ExecutableElement) {
                ExecutableElement methodElement = (ExecutableElement) enclosedElement;

                if (methodElement.getAnnotation(GET.class) != null) {
                    GET getAnnotation = methodElement.getAnnotation(GET.class);
                    String fullPath = basePath + getAnnotation.path();
                    handlerCode.append("        if (method.equalsIgnoreCase(\"GET\") && path.equals(\"").append(fullPath).append("\")) {\n");
                    handlerCode.append("            // Lógica para GET\n");
                    handlerCode.append("            String response = \"Respuesta GET\";\n");
                    handlerCode.append("            exchange.sendResponseHeaders(200, response.getBytes().length);\n");
                    handlerCode.append("            OutputStream os = exchange.getResponseBody();\n");
                    handlerCode.append("            os.write(response.getBytes());\n");
                    handlerCode.append("            os.close();\n");
                    handlerCode.append("            return;\n");
                    handlerCode.append("        }\n");
                }

                // Similarmente, puedes agregar lógica para POST, PUT, DELETE...
            }
        }

        handlerCode.append("        exchange.sendResponseHeaders(404, -1); // Not Found\n");
        handlerCode.append("    }\n");
        handlerCode.append("}\n");

        // Escribir el archivo generado
        JavaFileObject file = processingEnv.getFiler().createSourceFile(handlerClassName);
        try (PrintWriter writer = new PrintWriter(file.openWriter())) {
            writer.print(handlerCode.toString());
        }
    }
}
```

---

#### 3. Clase Principal (`Main.java`)
Aquí configuramos el servidor HTTP y registramos los handlers generados.

```java
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;

public class Main {
    public static void main(String[] args) throws IOException {
        HttpServer server = HttpServer.create(new InetSocketAddress(8080), 0);

        // Registrar el handler generado automáticamente
        server.createContext("/users", new UserHandler());

        server.setExecutor(null);
        server.start();
        System.out.println("Servidor HTTP iniciado en http://localhost:8080");
    }
}
```

---

#### 4. Ejemplo de Uso de Anotaciones
##### `UserHandler.java`
```java
import annotations.Endpoint;
import annotations.GET;

@Endpoint(path = "/users")
public class UserHandler {
    @GET(path = "")
    public void getAllUsers() {
        // Lógica para obtener todos los usuarios
    }

    @GET(path = "/{id}")
    public void getUserById() {
        // Lógica para obtener un usuario por ID
    }
}
```

---

### Compilación y Ejecución

1. Configura el proyecto para incluir el procesador de anotaciones en tu `build.gradle` o `pom.xml`.
2. Compila el proyecto:
   ```bash
   javac -processor processor.EndpointProcessor src/main/java/*.java -d out
   ```
3. Ejecuta el servidor:
   ```bash
   java -cp out Main
   ```

---

### Resultado
El procesador de anotaciones generará automáticamente un archivo `UserHandler.java` que implementa `HttpHandler` y maneja las solicitudes HTTP basadas en las anotaciones definidas.

Este enfoque es modular y permite extender fácilmente el sistema para soportar más anotaciones y funcionalidades.


___

# genera HTML 



Para agregar un generador de código HTML usando **Java Annotation Processing**, podemos extender el sistema anterior para que, además de generar handlers HTTP, también genere archivos HTML estáticos basados en las anotaciones. Estos archivos HTML podrían representar vistas simples para interactuar con los endpoints CRUD.

A continuación, te muestro cómo modificar el proyecto para incluir esta funcionalidad.

---

### Estructura del Proyecto Actualizada
```
crud-annotation-processing/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   ├── Main.java
│   │   │   ├── User.java
│   │   │   ├── UserHandler.java
│   │   │   ├── annotations/
│   │   │   │   ├── Endpoint.java
│   │   │   │   ├── GET.java
│   │   │   │   ├── POST.java
│   │   │   │   ├── PUT.java
│   │   │   │   └── DELETE.java
│   │   │   └── processor/
│   │   │       └── EndpointProcessor.java
│   │   └── resources/
│   └── generated/
└── README.md
```

---

### Código Fuente

#### 1. Modificación del Procesador de Anotaciones
El procesador ahora generará tanto el handler HTTP como un archivo HTML estático para cada endpoint.

##### `processor/EndpointProcessor.java`
```java
import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.tools.JavaFileObject;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Set;

@SupportedAnnotationTypes("annotations.Endpoint")
@SupportedSourceVersion(SourceVersion.RELEASE_8)
public class EndpointProcessor extends AbstractProcessor {

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        for (Element element : roundEnv.getElementsAnnotatedWith(Endpoint.class)) {
            if (element instanceof TypeElement) {
                TypeElement typeElement = (TypeElement) element;
                Endpoint endpointAnnotation = typeElement.getAnnotation(Endpoint.class);
                String className = typeElement.getSimpleName().toString();
                String handlerClassName = className + "Handler";

                try {
                    generateHandlerClass(handlerClassName, endpointAnnotation.path(), typeElement);
                    generateHtmlFiles(endpointAnnotation.path(), typeElement);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return true;
    }

    private void generateHandlerClass(String handlerClassName, String basePath, TypeElement typeElement) throws IOException {
        StringBuilder handlerCode = new StringBuilder();
        handlerCode.append("import com.sun.net.httpserver.HttpExchange;\n");
        handlerCode.append("import com.sun.net.httpserver.HttpHandler;\n");
        handlerCode.append("import java.io.IOException;\n");
        handlerCode.append("import java.io.OutputStream;\n\n");

        handlerCode.append("public class ").append(handlerClassName).append(" implements HttpHandler {\n");
        handlerCode.append("    @Override\n");
        handlerCode.append("    public void handle(HttpExchange exchange) throws IOException {\n");
        handlerCode.append("        String method = exchange.getRequestMethod();\n");
        handlerCode.append("        String path = exchange.getRequestURI().getPath();\n\n");

        for (Element enclosedElement : typeElement.getEnclosedElements()) {
            if (enclosedElement instanceof ExecutableElement) {
                ExecutableElement methodElement = (ExecutableElement) enclosedElement;

                if (methodElement.getAnnotation(GET.class) != null) {
                    GET getAnnotation = methodElement.getAnnotation(GET.class);
                    String fullPath = basePath + getAnnotation.path();
                    handlerCode.append("        if (method.equalsIgnoreCase(\"GET\") && path.equals(\"").append(fullPath).append("\")) {\n");
                    handlerCode.append("            // Lógica para GET\n");
                    handlerCode.append("            String response = \"Respuesta GET\";\n");
                    handlerCode.append("            exchange.sendResponseHeaders(200, response.getBytes().length);\n");
                    handlerCode.append("            OutputStream os = exchange.getResponseBody();\n");
                    handlerCode.append("            os.write(response.getBytes());\n");
                    handlerCode.append("            os.close();\n");
                    handlerCode.append("            return;\n");
                    handlerCode.append("        }\n");
                }
            }
        }

        handlerCode.append("        exchange.sendResponseHeaders(404, -1); // Not Found\n");
        handlerCode.append("    }\n");
        handlerCode.append("}\n");

        JavaFileObject file = processingEnv.getFiler().createSourceFile(handlerClassName);
        try (PrintWriter writer = new PrintWriter(file.openWriter())) {
            writer.print(handlerCode.toString());
        }
    }

    private void generateHtmlFiles(String basePath, TypeElement typeElement) throws IOException {
        for (Element enclosedElement : typeElement.getEnclosedElements()) {
            if (enclosedElement instanceof ExecutableElement) {
                ExecutableElement methodElement = (ExecutableElement) enclosedElement;

                if (methodElement.getAnnotation(GET.class) != null) {
                    GET getAnnotation = methodElement.getAnnotation(GET.class);
                    String fullPath = basePath + getAnnotation.path();
                    String htmlContent = generateHtmlForGet(fullPath);

                    // Guardar el archivo HTML en la carpeta resources
                    Files.write(Paths.get("src/main/resources" + fullPath + ".html"), htmlContent.getBytes());
                }
            }
        }
    }

    private String generateHtmlForGet(String path) {
        return "<!DOCTYPE html>\n" +
               "<html lang=\"en\">\n" +
               "<head>\n" +
               "    <meta charset=\"UTF-8\">\n" +
               "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n" +
               "    <title>" + path + "</title>\n" +
               "</head>\n" +
               "<body>\n" +
               "    <h1>Bienvenido a la ruta: " + path + "</h1>\n" +
               "    <p>Esta es una vista generada automáticamente para el endpoint GET.</p>\n" +
               "</body>\n" +
               "</html>";
    }
}
```

---

#### 2. Ejemplo de Uso de Anotaciones
##### `UserHandler.java`
```java
import annotations.Endpoint;
import annotations.GET;

@Endpoint(path = "/users")
public class UserHandler {
    @GET(path = "")
    public void getAllUsers() {
        // Lógica para obtener todos los usuarios
    }

    @GET(path = "/{id}")
    public void getUserById() {
        // Lógica para obtener un usuario por ID
    }
}
```

---

#### 3. Servidor HTTP (`Main.java`)
Modificamos el servidor para servir archivos HTML estáticos.

```java
import com.sun.net.httpserver.HttpServer;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;

public class Main {
    public static void main(String[] args) throws IOException {
        HttpServer server = HttpServer.create(new InetSocketAddress(8080), 0);

        // Registrar el handler generado automáticamente
        server.createContext("/users", new UserHandler());

        // Servir archivos HTML estáticos
        server.createContext("/", exchange -> {
            String path = exchange.getRequestURI().getPath();
            File file = new File("src/main/resources" + path);

            if (file.exists() && file.isFile()) {
                exchange.sendResponseHeaders(200, file.length());
                try (OutputStream os = exchange.getResponseBody();
                     FileInputStream fis = new FileInputStream(file)) {
                    byte[] buffer = new byte[1024];
                    int bytesRead;
                    while ((bytesRead = fis.read(buffer)) != -1) {
                        os.write(buffer, 0, bytesRead);
                    }
                }
            } else {
                exchange.sendResponseHeaders(404, -1); // Not Found
            }
        });

        server.setExecutor(null);
        server.start();
        System.out.println("Servidor HTTP iniciado en http://localhost:8080");
    }
}
```

---

### Resultado Final

1. **Generación Automática**:
   - El procesador genera un archivo HTML para cada endpoint `@GET`.
   - Los archivos HTML se guardan en `src/main/resources`.

2. **Servidor HTTP**:
   - El servidor sirve los archivos HTML generados cuando se accede a las rutas correspondientes.

3. **Ejemplo de Acceso**:
   - Accede a `http://localhost:8080/users` para ver la vista HTML generada automáticamente.

Este enfoque permite automatizar la creación de vistas HTML junto con los endpoints HTTP, lo que puede ser útil para prototipos rápidos o proyectos pequeños.

___

# subir archivos 


Para añadir la funcionalidad de manejo de archivos en los endpoints utilizando **Java Annotation Processing** y el **API HTTP**, podemos extender el sistema para permitir operaciones como subir (`POST`) y descargar (`GET`) archivos. A continuación, te muestro cómo implementar esta funcionalidad.

---

### Estructura del Proyecto Actualizada
```
crud-annotation-processing/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   ├── Main.java
│   │   │   ├── User.java
│   │   │   ├── UserHandler.java
│   │   │   ├── annotations/
│   │   │   │   ├── Endpoint.java
│   │   │   │   ├── GET.java
│   │   │   │   ├── POST.java
│   │   │   │   ├── PUT.java
│   │   │   │   └── DELETE.java
│   │   │   └── processor/
│   │   │       └── EndpointProcessor.java
│   │   └── resources/
│   │       └── uploads/  (Carpeta para almacenar archivos)
│   └── generated/
└── README.md
```

---

### Código Fuente

#### 1. Anotaciones Personalizadas
Agregamos una nueva anotación `@FileOperation` para manejar operaciones relacionadas con archivos.

##### `annotations/FileOperation.java`
```java
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.SOURCE)
@Target(ElementType.METHOD)
public @interface FileOperation {
    String path();
    OperationType type();

    enum OperationType {
        UPLOAD,
        DOWNLOAD
    }
}
```

---

#### 2. Modificación del Procesador de Anotaciones
El procesador ahora generará código para manejar operaciones de archivos (`UPLOAD` y `DOWNLOAD`).

##### `processor/EndpointProcessor.java`
```java
import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.tools.JavaFileObject;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Set;

@SupportedAnnotationTypes("annotations.Endpoint")
@SupportedSourceVersion(SourceVersion.RELEASE_8)
public class EndpointProcessor extends AbstractProcessor {

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        for (Element element : roundEnv.getElementsAnnotatedWith(Endpoint.class)) {
            if (element instanceof TypeElement) {
                TypeElement typeElement = (TypeElement) element;
                Endpoint endpointAnnotation = typeElement.getAnnotation(Endpoint.class);
                String className = typeElement.getSimpleName().toString();
                String handlerClassName = className + "Handler";

                try {
                    generateHandlerClass(handlerClassName, endpointAnnotation.path(), typeElement);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return true;
    }

    private void generateHandlerClass(String handlerClassName, String basePath, TypeElement typeElement) throws IOException {
        StringBuilder handlerCode = new StringBuilder();
        handlerCode.append("import com.sun.net.httpserver.HttpExchange;\n");
        handlerCode.append("import com.sun.net.httpserver.HttpHandler;\n");
        handlerCode.append("import java.io.*;\n");
        handlerCode.append("import java.nio.file.*;\n\n");

        handlerCode.append("public class ").append(handlerClassName).append(" implements HttpHandler {\n");
        handlerCode.append("    @Override\n");
        handlerCode.append("    public void handle(HttpExchange exchange) throws IOException {\n");
        handlerCode.append("        String method = exchange.getRequestMethod();\n");
        handlerCode.append("        String path = exchange.getRequestURI().getPath();\n\n");

        for (Element enclosedElement : typeElement.getEnclosedElements()) {
            if (enclosedElement instanceof ExecutableElement) {
                ExecutableElement methodElement = (ExecutableElement) enclosedElement;

                if (methodElement.getAnnotation(FileOperation.class) != null) {
                    FileOperation fileOperation = methodElement.getAnnotation(FileOperation.class);
                    String fullPath = basePath + fileOperation.path();
                    String operationType = fileOperation.type().name();

                    if (operationType.equals("UPLOAD")) {
                        handlerCode.append("        if (method.equalsIgnoreCase(\"POST\") && path.equals(\"").append(fullPath).append("\")) {\n");
                        handlerCode.append("            // Manejar subida de archivos\n");
                        handlerCode.append("            Path uploadDir = Paths.get(\"src/main/resources/uploads/\");\n");
                        handlerCode.append("            Files.createDirectories(uploadDir);\n");
                        handlerCode.append("            Path filePath = uploadDir.resolve(exchange.getRequestHeaders().getFirst(\"filename\"));\n");
                        handlerCode.append("            Files.copy(exchange.getRequestBody(), filePath, StandardCopyOption.REPLACE_EXISTING);\n");
                        handlerCode.append("            exchange.sendResponseHeaders(200, \"Archivo subido correctamente\".getBytes().length);\n");
                        handlerCode.append("            OutputStream os = exchange.getResponseBody();\n");
                        handlerCode.append("            os.write(\"Archivo subido correctamente\".getBytes());\n");
                        handlerCode.append("            os.close();\n");
                        handlerCode.append("            return;\n");
                        handlerCode.append("        }\n");
                    } else if (operationType.equals("DOWNLOAD")) {
                        handlerCode.append("        if (method.equalsIgnoreCase(\"GET\") && path.equals(\"").append(fullPath).append("\")) {\n");
                        handlerCode.append("            // Manejar descarga de archivos\n");
                        handlerCode.append("            Path uploadDir = Paths.get(\"src/main/resources/uploads/\");\n");
                        handlerCode.append("            Path filePath = uploadDir.resolve(exchange.getRequestHeaders().getFirst(\"filename\"));\n");
                        handlerCode.append("            if (Files.exists(filePath)) {\n");
                        handlerCode.append("                exchange.sendResponseHeaders(200, Files.size(filePath));\n");
                        handlerCode.append("                OutputStream os = exchange.getResponseBody();\n");
                        handlerCode.append("                Files.copy(filePath, os);\n");
                        handlerCode.append("                os.close();\n");
                        handlerCode.append("            } else {\n");
                        handlerCode.append("                exchange.sendResponseHeaders(404, -1); // Not Found\n");
                        handlerCode.append("            }\n");
                        handlerCode.append("            return;\n");
                        handlerCode.append("        }\n");
                    }
                }
            }
        }

        handlerCode.append("        exchange.sendResponseHeaders(404, -1); // Not Found\n");
        handlerCode.append("    }\n");
        handlerCode.append("}\n");

        JavaFileObject file = processingEnv.getFiler().createSourceFile(handlerClassName);
        try (PrintWriter writer = new PrintWriter(file.openWriter())) {
            writer.print(handlerCode.toString());
        }
    }
}
```

---

#### 3. Ejemplo de Uso de Anotaciones
##### `UserHandler.java`
```java
import annotations.Endpoint;
import annotations.FileOperation;

@Endpoint(path = "/files")
public class UserHandler {
    @FileOperation(path = "/upload", type = FileOperation.OperationType.UPLOAD)
    public void uploadFile() {
        // Lógica para subir archivos
    }

    @FileOperation(path = "/download", type = FileOperation.OperationType.DOWNLOAD)
    public void downloadFile() {
        // Lógica para descargar archivos
    }
}
```

---

#### 4. Servidor HTTP (`Main.java`)
No es necesario modificar el servidor HTTP, ya que el procesador genera automáticamente el código necesario.

---

### Resultado Final

1. **Subir Archivos**:
   - Usa una solicitud `POST` al endpoint `/files/upload`.
   - Incluye el nombre del archivo en el encabezado `filename`.

   ```bash
   curl -X POST -H "filename: example.txt" --data-binary @example.txt http://localhost:8080/files/upload
   ```

2. **Descargar Archivos**:
   - Usa una solicitud `GET` al endpoint `/files/download`.
   - Incluye el nombre del archivo en el encabezado `filename`.

   ```bash
   curl -X GET -H "filename: example.txt" http://localhost:8080/files/download --output example.txt
   ```

3. **Almacenamiento de Archivos**:
   - Los archivos se almacenan en la carpeta `src/main/resources/uploads/`.

---

### Notas Finales
- Este enfoque utiliza el API HTTP de Java (`com.sun.net.httpserver`) para manejar solicitudes de archivos.
- La carpeta `uploads` debe existir o ser creada automáticamente por el servidor.
- Puedes extender este sistema para manejar validaciones, errores y seguridad adicional según sea necesario.

¡Espero que este ejemplo sea útil para tu proyecto!

___


# con manejo de sesión 


Para agregar el manejo de sesiones de usuario al ejemplo anterior, podemos implementar un sistema básico de autenticación y gestión de sesiones utilizando **cookies**. Las cookies permiten almacenar información del lado del cliente (como un ID de sesión) que puede ser utilizada para identificar a los usuarios en solicitudes posteriores.

A continuación, te muestro cómo extender el proyecto para incluir el manejo de sesiones de usuario.

---

### Estructura del Proyecto Actualizada
```
crud-annotation-processing/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   ├── Main.java
│   │   │   ├── User.java
│   │   │   ├── UserHandler.java
│   │   │   ├── SessionManager.java
│   │   │   ├── annotations/
│   │   │   │   ├── Endpoint.java
│   │   │   │   ├── GET.java
│   │   │   │   ├── POST.java
│   │   │   │   ├── PUT.java
│   │   │   │   └── DELETE.java
│   │   │   └── processor/
│   │   │       └── EndpointProcessor.java
│   │   └── resources/
│   │       └── uploads/  (Carpeta para almacenar archivos)
│   └── generated/
└── README.md
```

---

### Código Fuente

#### 1. Manejador de Sesiones (`SessionManager.java`)
Creamos una clase `SessionManager` para gestionar las sesiones de usuario. Esta clase utiliza un mapa para almacenar las sesiones activas.

```java
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class SessionManager {
    private static final Map<String, String> sessions = new HashMap<>();

    // Crear una nueva sesión para un usuario
    public static String createSession(String username) {
        String sessionId = UUID.randomUUID().toString();
        sessions.put(sessionId, username);
        return sessionId;
    }

    // Obtener el nombre de usuario asociado a una sesión
    public static String getUsername(String sessionId) {
        return sessions.getOrDefault(sessionId, null);
    }

    // Eliminar una sesión
    public static void removeSession(String sessionId) {
        sessions.remove(sessionId);
    }
}
```

---

#### 2. Modificación del Procesador de Anotaciones
El procesador generará código para manejar la autenticación y las sesiones en los endpoints.

##### `processor/EndpointProcessor.java`
```java
import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.tools.JavaFileObject;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Set;

@SupportedAnnotationTypes("annotations.Endpoint")
@SupportedSourceVersion(SourceVersion.RELEASE_8)
public class EndpointProcessor extends AbstractProcessor {

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        for (Element element : roundEnv.getElementsAnnotatedWith(Endpoint.class)) {
            if (element instanceof TypeElement) {
                TypeElement typeElement = (TypeElement) element;
                Endpoint endpointAnnotation = typeElement.getAnnotation(Endpoint.class);
                String className = typeElement.getSimpleName().toString();
                String handlerClassName = className + "Handler";

                try {
                    generateHandlerClass(handlerClassName, endpointAnnotation.path(), typeElement);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return true;
    }

    private void generateHandlerClass(String handlerClassName, String basePath, TypeElement typeElement) throws IOException {
        StringBuilder handlerCode = new StringBuilder();
        handlerCode.append("import com.sun.net.httpserver.HttpExchange;\n");
        handlerCode.append("import com.sun.net.httpserver.HttpHandler;\n");
        handlerCode.append("import java.io.*;\n");
        handlerCode.append("import java.nio.file.*;\n\n");

        handlerCode.append("public class ").append(handlerClassName).append(" implements HttpHandler {\n");
        handlerCode.append("    @Override\n");
        handlerCode.append("    public void handle(HttpExchange exchange) throws IOException {\n");
        handlerCode.append("        String method = exchange.getRequestMethod();\n");
        handlerCode.append("        String path = exchange.getRequestURI().getPath();\n");
        handlerCode.append("        String sessionId = exchange.getRequestHeaders().getFirst(\"Cookie\") != null ?\n");
        handlerCode.append("                           exchange.getRequestHeaders().getFirst(\"Cookie\").split(\"=\")[1] : null;\n");
        handlerCode.append("        String username = SessionManager.getUsername(sessionId);\n\n");

        handlerCode.append("        if (path.equals(\"/login\")) {\n");
        handlerCode.append("            if (method.equalsIgnoreCase(\"POST\")) {\n");
        handlerCode.append("                String body = new String(exchange.getRequestBody().readAllBytes());\n");
        handlerCode.append("                String[] parts = body.split(\"&\");\n");
        handlerCode.append("                String usernameParam = \"\", password = \"\";\n");
        handlerCode.append("                for (String part : parts) {\n");
        handlerCode.append("                    String[] keyValue = part.split(\"=\");\n");
        handlerCode.append("                    if (keyValue[0].equals(\"username\")) usernameParam = keyValue[1];\n");
        handlerCode.append("                    if (keyValue[0].equals(\"password\")) password = keyValue[1];\n");
        handlerCode.append("                }\n");
        handlerCode.append("                if (authenticate(usernameParam, password)) {\n");
        handlerCode.append("                    String newSessionId = SessionManager.createSession(usernameParam);\n");
        handlerCode.append("                    exchange.getResponseHeaders().add(\"Set-Cookie\", \"sessionId=\" + newSessionId);\n");
        handlerCode.append("                    exchange.sendResponseHeaders(200, \"Login exitoso\".getBytes().length);\n");
        handlerCode.append("                    OutputStream os = exchange.getResponseBody();\n");
        handlerCode.append("                    os.write(\"Login exitoso\".getBytes());\n");
        handlerCode.append("                    os.close();\n");
        handlerCode.append("                } else {\n");
        handlerCode.append("                    exchange.sendResponseHeaders(401, -1); // Unauthorized\n");
        handlerCode.append("                }\n");
        handlerCode.append("                return;\n");
        handlerCode.append("            }\n");
        handlerCode.append("        }\n\n");

        handlerCode.append("        if (username == null) {\n");
        handlerCode.append("            exchange.sendResponseHeaders(401, -1); // Unauthorized\n");
        handlerCode.append("            return;\n");
        handlerCode.append("        }\n\n");

        for (Element enclosedElement : typeElement.getEnclosedElements()) {
            if (enclosedElement instanceof ExecutableElement) {
                ExecutableElement methodElement = (ExecutableElement) enclosedElement;

                if (methodElement.getAnnotation(GET.class) != null) {
                    GET getAnnotation = methodElement.getAnnotation(GET.class);
                    String fullPath = basePath + getAnnotation.path();
                    handlerCode.append("        if (method.equalsIgnoreCase(\"GET\") && path.equals(\"").append(fullPath).append("\")) {\n");
                    handlerCode.append("            String response = \"Respuesta GET para usuario: \" + username;\n");
                    handlerCode.append("            exchange.sendResponseHeaders(200, response.getBytes().length);\n");
                    handlerCode.append("            OutputStream os = exchange.getResponseBody();\n");
                    handlerCode.append("            os.write(response.getBytes());\n");
                    handlerCode.append("            os.close();\n");
                    handlerCode.append("            return;\n");
                    handlerCode.append("        }\n");
                }
            }
        }

        handlerCode.append("        exchange.sendResponseHeaders(404, -1); // Not Found\n");
        handlerCode.append("    }\n\n");

        handlerCode.append("    private boolean authenticate(String username, String password) {\n");
        handlerCode.append("        // Lógica de autenticación simple\n");
        handlerCode.append("        return \"admin\".equals(username) && \"password\".equals(password);\n");
        handlerCode.append("    }\n");
        handlerCode.append("}\n");

        JavaFileObject file = processingEnv.getFiler().createSourceFile(handlerClassName);
        try (PrintWriter writer = new PrintWriter(file.openWriter())) {
            writer.print(handlerCode.toString());
        }
    }
}
```

---

#### 3. Ejemplo de Uso de Anotaciones
##### `UserHandler.java`
```java
import annotations.Endpoint;
import annotations.GET;

@Endpoint(path = "/users")
public class UserHandler {
    @GET(path = "")
    public void getAllUsers() {
        // Lógica para obtener todos los usuarios
    }

    @GET(path = "/{id}")
    public void getUserById() {
        // Lógica para obtener un usuario por ID
    }
}
```

---

#### 4. Servidor HTTP (`Main.java`)
No es necesario modificar el servidor HTTP, ya que el procesador genera automáticamente el código necesario.

---

### Resultado Final

1. **Iniciar Sesión**:
   - Usa una solicitud `POST` al endpoint `/login` con los datos de usuario.
   ```bash
   curl -X POST -d "username=admin&password=password" http://localhost:8080/login
   ```

2. **Acceder a Endpoints Protegidos**:
   - Incluye la cookie `sessionId` en las solicitudes posteriores.
   ```bash
   curl -H "Cookie: sessionId=<session-id>" http://localhost:8080/users
   ```

3. **Cerrar Sesión**:
   - Implementa un endpoint `/logout` para eliminar la sesión.

---

### Notas Finales
- Este sistema utiliza cookies para manejar sesiones, pero puedes extenderlo para usar tokens JWT u otros mecanismos de autenticación.
- La lógica de autenticación es básica y debe reemplazarse con una implementación segura en producción.
- Asegúrate de proteger las rutas sensibles validando siempre la sesión del usuario.

¡Espero que este ejemplo sea útil para tu proyecto!

___

