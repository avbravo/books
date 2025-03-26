# jmh


Para medir el rendimiento de una aplicación que utiliza la **API HTTP de Java** (introducida en Java 11), puedes usar **JMH (Java Microbenchmark Harness)**. JMH es una herramienta diseñada específicamente para realizar benchmarks precisos y confiables en aplicaciones Java.

A continuación, te muestro un ejemplo completo de cómo medir el rendimiento de una solicitud HTTP utilizando la API HTTP de Java con JMH.

---

### 1. Configuración del Proyecto
Si usas Maven, agrega las siguientes dependencias al archivo `pom.xml`:

```xml
<dependencies>
    <!-- JMH Core -->
    <dependency>
        <groupId>org.openjdk.jmh</groupId>
        <artifactId>jmh-core</artifactId>
        <version>1.36</version>
    </dependency>

    <!-- JMH Annotation Processor -->
    <dependency>
        <groupId>org.openjdk.jmh</groupId>
        <artifactId>jmh-generator-annprocess</artifactId>
        <version>1.36</version>
        <scope>provided</scope>
    </dependency>
</dependencies>

<build>
    <plugins>
        <!-- Plugin para generar el ejecutable JMH -->
        <plugin>
            <groupId>org.apache.maven.plugins</groupId>
            <artifactId>maven-shade-plugin</artifactId>
            <version>3.4.1</version>
            <executions>
                <execution>
                    <phase>package</phase>
                    <goals>
                        <goal>shade</goal>
                    </goals>
                    <configuration>
                        <finalName>http-benchmark</finalName>
                        <transformers>
                            <transformer implementation="org.apache.maven.plugins.shade.resource.ManifestResourceTransformer">
                                <mainClass>org.openjdk.jmh.Main</mainClass>
                            </transformer>
                        </transformers>
                    </configuration>
                </execution>
            </executions>
        </plugin>
    </plugins>
</build>
```

---

### 2. Código del Benchmark

#### Clase de Benchmark

```java
import org.openjdk.jmh.annotations.*;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.concurrent.TimeUnit;

@BenchmarkMode(Mode.AverageTime) // Medir el tiempo promedio de ejecución
@OutputTimeUnit(TimeUnit.MILLISECONDS) // Resultados en milisegundos
@State(Scope.Thread) // Estado por hilo
@Fork(value = 1) // Número de procesos JVM separados para ejecutar el benchmark
@Warmup(iterations = 2) // Iteraciones de calentamiento
@Measurement(iterations = 5) // Iteraciones de medición
public class HttpBenchmark {

    private HttpClient client;
    private HttpRequest request;

    @Setup(Level.Trial)
    public void setup() {
        // Inicializar el cliente HTTP y la solicitud
        client = HttpClient.newHttpClient();
        request = HttpRequest.newBuilder()
                .uri(URI.create("https://jsonplaceholder.typicode.com/posts/1"))
                .GET()
                .build();
    }

    @Benchmark
    public void measureHttpRequest() throws Exception {
        // Realizar la solicitud HTTP
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        // Verificar que la respuesta sea exitosa
        if (response.statusCode() != 200) {
            throw new RuntimeException("HTTP request failed with status code: " + response.statusCode());
        }
    }
}
```

---

### 3. Explicación del Código

1. **Anotaciones JMH**:
   - `@BenchmarkMode(Mode.AverageTime)`: Mide el tiempo promedio de ejecución de cada iteración.
   - `@OutputTimeUnit(TimeUnit.MILLISECONDS)`: Los resultados se muestran en milisegundos.
   - `@State(Scope.Thread)`: Define el alcance del estado compartido entre las iteraciones.
   - `@Fork(value = 1)`: Ejecuta el benchmark en un proceso JVM separado.
   - `@Warmup(iterations = 2)`: Realiza 2 iteraciones de calentamiento antes de medir.
   - `@Measurement(iterations = 5)`: Realiza 5 iteraciones de medición.

2. **Método `setup()`**:
   - Se ejecuta antes de iniciar el benchmark.
   - Inicializa el cliente HTTP (`HttpClient`) y la solicitud (`HttpRequest`).

3. **Método `measureHttpRequest()`**:
   - Este es el método que se medirá.
   - Realiza una solicitud HTTP GET a un endpoint público (`https://jsonplaceholder.typicode.com/posts/1`).
   - Verifica que la respuesta tenga un código de estado `200`.

---

### 4. Ejecución del Benchmark

1. **Compilar el Proyecto**:
   Ejecuta el siguiente comando para compilar el proyecto y generar el ejecutable JMH:
   ```bash
   mvn clean package
   ```

2. **Ejecutar el Benchmark**:
   Ejecuta el siguiente comando para iniciar el benchmark:
   ```bash
   java -jar target/http-benchmark.jar
   ```

---

### 5. Resultado Esperado
El resultado mostrará métricas como el tiempo promedio de ejecución, desviación estándar y otros detalles. Por ejemplo:

```
Benchmark                      Mode  Cnt   Score   Error  Units
HttpBenchmark.measureHttpRequest  avgt    5   85.123 ± 2.345   ms/op
```

Esto indica que, en promedio, la solicitud HTTP tardó aproximadamente **85.123 milisegundos** en completarse.

---

### 6. Mejoras Posibles

1. **Pruebas con Diferentes Endpoints**:
   - Cambia el endpoint de la solicitud HTTP para medir el rendimiento con diferentes servidores o cargas de trabajo.

2. **Medición de Métricas Adicionales**:
   - Usa otras anotaciones de JMH, como `@Threads`, para medir el rendimiento con múltiples hilos.

3. **Comparación de Implementaciones**:
   - Compara el rendimiento de la API HTTP de Java con otras bibliotecas HTTP, como **Apache HttpClient** o **OkHttp**.

4. **Análisis de Rendimiento en Redes Lentas**:
   - Simula condiciones de red lentas usando herramientas como **tc (Traffic Control)** en Linux.

---

Este ejemplo demuestra cómo medir el rendimiento de una solicitud HTTP utilizando la API HTTP de Java y JMH. Puedes adaptarlo según tus necesidades específicas para analizar y optimizar el rendimiento de tu aplicación.