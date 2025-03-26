# cache 

Para agregar soporte de **caché** al ejemplo del **Load Balancer**, podemos implementar un mecanismo que almacene las respuestas de las solicitudes HTTP en una caché. Esto permite reducir la carga en los servidores backend y mejorar el rendimiento, ya que las solicitudes repetidas pueden ser atendidas directamente desde la caché.

A continuación, te muestro cómo modificar el ejemplo anterior para incluir soporte de caché.

---

### 1. Implementación de la Caché
Usaremos una caché simple basada en un `HashMap` para almacenar las respuestas. También agregaremos un tiempo de expiración (TTL - Time To Live) para evitar que los datos almacenados en caché se vuelvan obsoletos.

#### Clase de Caché

```java
import java.util.HashMap;
import java.util.Map;

public class Cache {

    private final Map<String, CacheEntry> cache = new HashMap<>();
    private final long ttl; // Tiempo de vida de las entradas en milisegundos

    public Cache(long ttl) {
        this.ttl = ttl;
    }

    // Almacenar una respuesta en la caché
    public void put(String key, String value) {
        cache.put(key, new CacheEntry(value, System.currentTimeMillis()));
    }

    // Obtener una respuesta de la caché
    public String get(String key) {
        CacheEntry entry = cache.get(key);
        if (entry == null) {
            return null; // No está en caché
        }
        if (System.currentTimeMillis() - entry.timestamp > ttl) {
            cache.remove(key); // Eliminar si ha expirado
            return null;
        }
        return entry.value;
    }

    // Clase interna para almacenar entradas con su timestamp
    private static class CacheEntry {
        String value;
        long timestamp;

        CacheEntry(String value, long timestamp) {
            this.value = value;
            this.timestamp = timestamp;
        }
    }
}
```

---

### 2. Modificación del Load Balancer
Ahora, modificamos el `LoadBalancer` para utilizar la caché. Antes de enviar una solicitud al servidor backend, verificamos si la respuesta ya está en caché.

#### Código del Load Balancer con Caché

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
    private final Cache cache;          // Instancia de la caché

    public LoadBalancer(List<String> servers, long cacheTTL) {
        this.servers = new ArrayList<>(servers);
        this.cache = new Cache(cacheTTL); // Inicializar la caché con un TTL
    }

    // Método para obtener el siguiente servidor en la lista
    private synchronized String getNextServer() {
        String server = servers.get(currentIndex);
        currentIndex = (currentIndex + 1) % servers.size(); // Avanza al siguiente servidor
        return server;
    }

    // Método para enviar una solicitud HTTP a un servidor balanceado
    public String sendRequest(String path) throws Exception {
        String cacheKey = path; // Usar la ruta como clave de caché
        String cachedResponse = cache.get(cacheKey);

        if (cachedResponse != null) {
            return "Cached Response: " + cachedResponse; // Devolver la respuesta de la caché
        }

        String server = getNextServer();
        URI uri = URI.create(server + path);

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(uri)
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        // Almacenar la respuesta en caché
        cache.put(cacheKey, response.body());

        return "Response from " + server + ": " + response.body();
    }

    public static void main(String[] args) throws Exception {
        // Lista de servidores backend
        List<String> servers = List.of(
                "http://localhost:8081",
                "http://localhost:8082",
                "http://localhost:8083"
        );

        // Crear el Load Balancer con un TTL de 10 segundos (10000 ms)
        LoadBalancer loadBalancer = new LoadBalancer(servers, 10000);

        // Simular varias solicitudes
        for (int i = 0; i < 15; i++) {
            System.out.println(loadBalancer.sendRequest("/api/resource"));
            Thread.sleep(1000); // Esperar 1 segundo entre solicitudes
        }
    }
}
```

---

### 3. Explicación de los Cambios

1. **Caché**:
   - Se agrega una instancia de la clase `Cache` al `LoadBalancer`.
   - La caché utiliza la ruta (`path`) como clave para almacenar y recuperar respuestas.

2. **Verificación de Caché**:
   - Antes de enviar una solicitud al servidor backend, se verifica si la respuesta ya está en caché.
   - Si la respuesta está en caché y no ha expirado, se devuelve directamente.

3. **Almacenamiento en Caché**:
   - Después de recibir una respuesta del servidor backend, se almacena en caché para futuras solicitudes.

4. **Tiempo de Vida (TTL)**:
   - Las entradas en caché tienen un tiempo de vida definido (por ejemplo, 10 segundos).
   - Si una entrada ha expirado, se elimina de la caché y se realiza una nueva solicitud al servidor backend.

---

### 4. Resultado Esperado
Cuando ejecutes el programa `LoadBalancer`, verás una salida similar a esta:

```
Response from http://localhost:8081: Response from Server 1
Cached Response: Response from Server 1
Cached Response: Response from Server 1
Response from http://localhost:8082: Response from Server 2
Cached Response: Response from Server 2
Cached Response: Response from Server 2
Response from http://localhost:8083: Response from Server 3
Cached Response: Response from Server 3
Cached Response: Response from Server 3
...
```

Las primeras solicitudes se envían al servidor backend, pero las siguientes se atienden desde la caché hasta que expiren.

---

### 5. Mejoras Posibles
1. **Caché Distribuida**:
   - Usa una solución de caché distribuida como **Redis** o **Memcached** para escalar horizontalmente.

2. **Políticas de Evicción**:
   - Implementa políticas de evicción más avanzadas, como LRU (Least Recently Used) o LFU (Least Frequently Used).

3. **Invalidación de Caché**:
   - Agrega un mecanismo para invalidar manualmente las entradas en caché cuando los datos cambien en los servidores backend.

4. **Métricas de Rendimiento**:
   - Monitorea el uso de la caché y el impacto en el rendimiento.

---

Este ejemplo demuestra cómo agregar soporte de caché a un **Load Balancer** en Java utilizando la API HTTP. Puedes adaptarlo según tus necesidades específicas.