¡Claro! Vamos a mejorar el código e incluir una nueva clase llamada `Pais` (País), además de implementar operaciones CRUD completas para esta entidad mediante un `HttpHandler`. Esto incluirá las operaciones de **Crear**, **Leer**, **Actualizar** y **Eliminar** (`POST`, `GET`, `PUT`, `DELETE`) para la entidad `Pais`.

---

### **1. Definir la Clase `Pais`**
Esta será nuestra nueva entidad.

```java
public class Pais {
    private String id;
    private String nombre;
    private String capital;

    // Getters y Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getCapital() { return capital; }
    public void setCapital(String capital) { this.capital = capital; }

    @Override
    public String toString() {
        return "Pais{id='" + id + "', nombre='" + nombre + "', capital='" + capital + "'}";
    }
}
```

---

### **2. Implementar el Repositorio para `Pais`**
Este repositorio manejará las operaciones CRUD para la entidad `Pais` en MongoDB.

```java
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.bson.types.ObjectId;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

public class PaisRepository {
    private final MongoCollection<Document> collection;

    @Inject
    public PaisRepository(MongoClient mongoClient) {
        MongoDatabase database = mongoClient.getDatabase("testdb");
        this.collection = database.getCollection("paises");
    }

    // Crear un país
    public void save(Pais pais) {
        Document doc = new Document("nombre", pais.getNombre())
                .append("capital", pais.getCapital());
        collection.insertOne(doc);
        pais.setId(doc.getObjectId("_id").toString());
    }

    // Leer todos los países
    public List<Pais> findAll() {
        List<Pais> paises = new ArrayList<>();
        for (Document doc : collection.find()) {
            Pais pais = new Pais();
            pais.setId(doc.getObjectId("_id").toString());
            pais.setNombre(doc.getString("nombre"));
            pais.setCapital(doc.getString("capital"));
            paises.add(pais);
        }
        return paises;
    }

    // Leer un país por ID
    public Pais findById(String id) {
        Document doc = collection.find(new Document("_id", new ObjectId(id))).first();
        if (doc != null) {
            Pais pais = new Pais();
            pais.setId(doc.getObjectId("_id").toString());
            pais.setNombre(doc.getString("nombre"));
            pais.setCapital(doc.getString("capital"));
            return pais;
        }
        return null;
    }

    // Actualizar un país
    public void update(String id, Pais pais) {
        Document query = new Document("_id", new ObjectId(id));
        Document update = new Document("$set", new Document("nombre", pais.getNombre())
                .append("capital", pais.getCapital()));
        collection.updateOne(query, update);
    }

    // Eliminar un país
    public void delete(String id) {
        Document query = new Document("_id", new ObjectId(id));
        collection.deleteOne(query);
    }
}
```

---

### **3. Implementar el Manejador HTTP para CRUD**
Este manejador manejará las solicitudes HTTP para realizar operaciones CRUD sobre la entidad `Pais`.

```java
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class PaisHttpHandler implements HttpHandler {
    private final PaisRepository paisRepository;

    @Inject
    public PaisHttpHandler(PaisRepository paisRepository) {
        this.paisRepository = paisRepository;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        try {
            String method = exchange.getRequestMethod();
            String path = exchange.getRequestURI().getPath();

            if (method.equals("GET") && path.equals("/paises")) {
                handleGetAll(exchange);
            } else if (method.equals("GET") && path.matches("/paises/\\w+")) {
                handleGetById(exchange, path.split("/")[2]);
            } else if (method.equals("POST") && path.equals("/paises")) {
                handleCreate(exchange);
            } else if (method.equals("PUT") && path.matches("/paises/\\w+")) {
                handleUpdate(exchange, path.split("/")[2]);
            } else if (method.equals("DELETE") && path.matches("/paises/\\w+")) {
                handleDelete(exchange, path.split("/")[2]);
            } else {
                sendResponse(exchange, 405, "Método no permitido");
            }
        } catch (Exception e) {
            sendResponse(exchange, 500, "Error interno del servidor: " + e.getMessage());
        }
    }

    private void handleGetAll(HttpExchange exchange) throws IOException {
        List<Pais> paises = paisRepository.findAll();
        sendResponse(exchange, 200, paises.toString());
    }

    private void handleGetById(HttpExchange exchange, String id) throws IOException {
        Pais pais = paisRepository.findById(id);
        if (pais != null) {
            sendResponse(exchange, 200, pais.toString());
        } else {
            sendResponse(exchange, 404, "País no encontrado");
        }
    }

    private void handleCreate(HttpExchange exchange) throws IOException {
        String body = new String(exchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
        String[] parts = body.split(",");
        if (parts.length == 2) {
            Pais pais = new Pais();
            pais.setNombre(parts[0].trim());
            pais.setCapital(parts[1].trim());
            paisRepository.save(pais);
            sendResponse(exchange, 201, "País creado con ID: " + pais.getId());
        } else {
            sendResponse(exchange, 400, "Datos inválidos");
        }
    }

    private void handleUpdate(HttpExchange exchange, String id) throws IOException {
        String body = new String(exchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
        String[] parts = body.split(",");
        if (parts.length == 2) {
            Pais pais = new Pais();
            pais.setNombre(parts[0].trim());
            pais.setCapital(parts[1].trim());
            paisRepository.update(id, pais);
            sendResponse(exchange, 200, "País actualizado");
        } else {
            sendResponse(exchange, 400, "Datos inválidos");
        }
    }

    private void handleDelete(HttpExchange exchange, String id) throws IOException {
        paisRepository.delete(id);
        sendResponse(exchange, 200, "País eliminado");
    }

    private void sendResponse(HttpExchange exchange, int statusCode, String response) throws IOException {
        exchange.sendResponseHeaders(statusCode, response.getBytes(StandardCharsets.UTF_8).length);
        OutputStream os = exchange.getResponseBody();
        os.write(response.getBytes(StandardCharsets.UTF_8));
        os.close();
    }
}
```

---

### **4. Registrar el Manejador y Ejecutar el Servidor**
Actualizamos el método `main` para registrar el nuevo repositorio y manejador.

```java
import com.sun.net.httpserver.HttpServer;

import java.net.InetSocketAddress;

public class Main {
    public static void main(String[] args) throws Exception {
        DependencyInjector injector = new DependencyInjector();

        // Registrar dependencias
        MongoProducer producer = new MongoProducer();
        injector.register(MongoClient.class, producer.createMongoClient());
        injector.register(PaisRepository.class, new PaisRepository(injector.resolve(MongoClient.class)));

        // Crear e inyectar el manejador HTTP
        PaisHttpHandler handler = new PaisHttpHandler(null);
        injector.injectDependencies(handler);

        // Iniciar el servidor HTTP
        HttpServer server = HttpServer.create(new InetSocketAddress(8080), 0);
        server.createContext("/paises", handler);
        server.setExecutor(null); // Usar el ejecutor predeterminado
        server.start();
        System.out.println("Servidor iniciado en el puerto 8080");
    }
}
```

---

### **Cómo Probar**
1. **Crear un País**: Envía una solicitud `POST` a `/paises` con el cuerpo `nombre,capital` (ejemplo: `Mexico,Ciudad de Mexico`).
2. **Obtener Todos los Países**: Envía una solicitud `GET` a `/paises`.
3. **Obtener un País por ID**: Envía una solicitud `GET` a `/paises/{id}`.
4. **Actualizar un País**: Envía una solicitud `PUT` a `/paises/{id}` con el cuerpo `nombre,capital`.
5. **Eliminar un País**: Envía una solicitud `DELETE` a `/paises/{id}`.

---

Este código proporciona una solución completa para gestionar entidades `Pais` mediante un servidor HTTP ligero en Java SE, utilizando MongoDB como base de datos.