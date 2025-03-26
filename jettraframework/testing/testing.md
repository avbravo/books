# Testing


A continuación, te muestro un ejemplo completo de cómo implementar pruebas para una aplicación que utiliza la **API HTTP de Java** utilizando **JCuCumber**, **Mockito** y **Selenium**. Este ejemplo combina las herramientas para probar tanto el backend (API HTTP) como el frontend (interfaz web).

---

### 1. Configuración del Proyecto
Si usas Maven, agrega las siguientes dependencias al archivo `pom.xml`:

```xml
<dependencies>
    <!-- JUnit -->
    <dependency>
        <groupId>junit</groupId>
        <artifactId>junit</artifactId>
        <version>4.13.2</version>
        <scope>test</scope>
    </dependency>

    <!-- Cucumber -->
    <dependency>
        <groupId>io.cucumber</groupId>
        <artifactId>cucumber-java</artifactId>
        <version>7.13.0</version>
        <scope>test</scope>
    </dependency>
    <dependency>
        <groupId>io.cucumber</groupId>
        <artifactId>cucumber-junit</artifactId>
        <version>7.13.0</version>
        <scope>test</scope>
    </dependency>

    <!-- Mockito -->
    <dependency>
        <groupId>org.mockito</groupId>
        <artifactId>mockito-core</artifactId>
        <version>5.5.0</version>
        <scope>test</scope>
    </dependency>

    <!-- Selenium -->
    <dependency>
        <groupId>org.seleniumhq.selenium</groupId>
        <artifactId>selenium-java</artifactId>
        <version>4.12.0</version>
        <scope>test</scope>
    </dependency>

    <!-- WebDriver Manager (para gestionar drivers de Selenium) -->
    <dependency>
        <groupId>io.github.bonigarcia</groupId>
        <artifactId>webdrivermanager</artifactId>
        <version>5.5.3</version>
        <scope>test</scope>
    </dependency>
</dependencies>
```

---

### 2. Implementación de la API HTTP

#### Clase que Utiliza la API HTTP de Java
Creamos una clase simple que realiza una solicitud HTTP GET.

```java
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class HttpService {

    public String fetchData(String url) throws Exception {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() != 200) {
            throw new RuntimeException("HTTP request failed with status code: " + response.statusCode());
        }

        return response.body();
    }
}
```

---

### 3. Pruebas con JCucumber

#### Archivo de Características (Feature File)
Crea un archivo `.feature` en `src/test/resources` llamado `http_service.feature`.

```gherkin
Feature: Test HTTP Service

  Scenario: Fetch data from an external API
    Given the HTTP service is available
    When I send a GET request to "https://jsonplaceholder.typicode.com/posts/1"
    Then the response should contain "userId"
```

---

#### Paso Definiciones (Step Definitions)
Crea una clase para implementar los pasos del escenario.

```java
import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import io.cucumber.java.en.Then;
import static org.junit.Assert.*;

public class HttpServiceSteps {

    private HttpService httpService;
    private String response;

    @Given("the HTTP service is available")
    public void the_http_service_is_available() {
        httpService = new HttpService();
    }

    @When("I send a GET request to {string}")
    public void i_send_a_get_request_to(String url) throws Exception {
        response = httpService.fetchData(url);
    }

    @Then("the response should contain {string}")
    public void the_response_should_contain(String expectedContent) {
        assertTrue(response.contains(expectedContent));
    }
}
```

---

### 4. Pruebas con Mockito

#### Mockear la API HTTP
Usamos Mockito para simular la respuesta de la API HTTP sin realizar una solicitud real.

```java
import org.junit.Test;
import org.mockito.Mockito;

import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

public class HttpServiceTest {

    @Test
    public void testFetchDataWithMock() throws Exception {
        // Mockear HttpClient y HttpResponse
        HttpClient mockClient = mock(HttpClient.class);
        HttpResponse<String> mockResponse = mock(HttpResponse.class);

        // Simular la respuesta
        when(mockResponse.statusCode()).thenReturn(200);
        when(mockResponse.body()).thenReturn("{\"userId\": 1, \"id\": 1, \"title\": \"Sample Title\"}");
        when(mockClient.send(any(HttpRequest.class), eq(HttpResponse.BodyHandlers.ofString())))
                .thenReturn(mockResponse);

        // Crear HttpService con el cliente mockeado
        HttpService httpService = new HttpService() {
            @Override
            public HttpClient createHttpClient() {
                return mockClient;
            }
        };

        // Llamar al método bajo prueba
        String result = httpService.fetchData("https://jsonplaceholder.typicode.com/posts/1");

        // Verificar el resultado
        assertTrue(result.contains("userId"));

        // Verificar que el método send fue llamado
        verify(mockClient).send(any(HttpRequest.class), eq(HttpResponse.BodyHandlers.ofString()));
    }
}
```

---

### 5. Pruebas con Selenium

#### Prueba de Interfaz Web
Supongamos que tienes una interfaz web que muestra datos obtenidos de la API HTTP.

```java
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import io.github.bonigarcia.wdm.WebDriverManager;

import static org.junit.Assert.*;

public class WebInterfaceTest {

    private WebDriver driver;

    @Before
    public void setUp() {
        // Configurar el WebDriver
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
    }

    @Test
    public void testWebInterface() {
        // Abrir la página web
        driver.get("https://example.com");

        // Encontrar un elemento en la página
        WebElement element = driver.findElement(By.id("data"));
        assertNotNull(element);

        // Verificar que el contenido es correcto
        assertTrue(element.getText().contains("Sample Data"));
    }

    @After
    public void tearDown() {
        // Cerrar el navegador
        if (driver != null) {
            driver.quit();
        }
    }
}
```

---

### 6. Ejecución de las Pruebas

1. **Ejecutar Pruebas de Cucumber**:
   ```bash
   mvn test
   ```

2. **Ejecutar Pruebas Unitarias con Mockito**:
   Las pruebas unitarias se ejecutan automáticamente con `mvn test`.

3. **Ejecutar Pruebas de Selenium**:
   Asegúrate de tener un navegador compatible instalado (por ejemplo, Chrome). Las pruebas de Selenium también se ejecutan con `mvn test`.

---

### 7. Resultados Esperados
- **Cucumber**: El escenario debería pasar si la API devuelve datos válidos.
- **Mockito**: La prueba unitaria debería verificar que el servicio HTTP funciona correctamente con mocks.
- **Selenium**: La prueba debería verificar que la interfaz web muestra los datos esperados.

Este ejemplo combina **Cucumber**, **Mockito** y **Selenium** para probar tanto el backend como el frontend de una aplicación que utiliza la API HTTP de Java. Puedes adaptarlo según tus necesidades específicas.