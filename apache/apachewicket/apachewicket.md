## Apache Wicket
Crear un proyecto desde

[https://wicket.apache.org/start/quickstart.html](https://wicket.apache.org/start/quickstart.html)

Ejecutarlo mediante

```shell

 mvn jetty:run

```

Ingresar a
```
http://localhost:8080/
```

Entendido. Si no deseas usar Jetty como servidor HTTP, podemos utilizar un enfoque alternativo para ejecutar una aplicación Java con Apache Wicket utilizando únicamente el **servidor HTTP integrado de Java** (a través del paquete `com.sun.net.httpserver.HttpServer`), que está disponible desde Java 6 en adelante.

A continuación, te muestro cómo configurar y ejecutar una aplicación Java con Apache Wicket sin depender de Jetty ni otros servidores externos.

---

### 1. Configuración del Proyecto
Usaremos Maven para gestionar las dependencias. Aquí está el archivo `pom.xml`:

```xml
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>

    <groupId>com.example</groupId>
    <artifactId>wicket-http-server</artifactId>
    <version>1.0-SNAPSHOT</version>

    <properties>
        <maven.compiler.source>17</maven.compiler.source>
        <maven.compiler.target>17</maven.compiler.target>
        <wicket.version>9.13.0</wicket.version> <!-- Versión de Apache Wicket -->
    </properties>

    <dependencies>
        <!-- Apache Wicket Core -->
        <dependency>
            <groupId>org.apache.wicket</groupId>
            <artifactId>wicket-core</artifactId>
            <version>${wicket.version}</version>
        </dependency>
    </dependencies>

    <build>
        <plugins>
            <plugin>
                <groupId>org.apache.maven.plugins</groupId>
                <artifactId>maven-compiler-plugin</artifactId>
                <version>3.11.0</version>
                <configuration>
                    <source>${maven.compiler.source}</source>
                    <target>${maven.compiler.target}</target>
                </configuration>
            </plugin>
        </plugins>
    </build>
</project>
```

Este archivo define solo la dependencia de Apache Wicket, sin incluir ningún servidor HTTP externo.

---

### 2. Crear la Aplicación Wicket
Crea una clase principal que extienda `WebApplication` de Apache Wicket.

#### Archivo Java (`MyWicketApplication.java`):
```java
package com.example;

import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.protocol.http.WebApplication;

public class MyWicketApplication extends WebApplication {
    @Override
    public Class<? extends WebPage> getHomePage() {
        return HomePage.class; // Página inicial
    }
}
```

---

### 3. Crear la Página Inicial
Crea una página simple llamada `HomePage`.

#### Archivo Java (`HomePage.java`):
```java
package com.example;

import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;

public class HomePage extends WebPage {
    public HomePage() {
        add(new Label("message", "¡Bienvenido a Apache Wicket!"));
    }
}
```

#### Archivo HTML (`HomePage.html`):
Crea un archivo HTML en `src/main/resources/com/example/HomePage.html`:

```html
<!DOCTYPE html>
<html xmlns:wicket="http://wicket.apache.org">
<head>
    <meta charset="UTF-8">
    <title>Página de Inicio</title>
</head>
<body>
    <h1 wicket:id="message">Mensaje de bienvenida</h1>
</body>
</html>
```

---

### 4. Implementar el Servidor HTTP Integrado
Usaremos el servidor HTTP integrado de Java (`com.sun.net.httpserver.HttpServer`) para manejar las solicitudes HTTP y enrutarlas a Apache Wicket.

#### Archivo Java (`Main.java`):
```java
package com.example;

import com.sun.net.httpserver.HttpServer;
import org.apache.wicket.protocol.http.WicketFilter;
import org.apache.wicket.protocol.http.WicketServlet;

import javax.servlet.Servlet;
import javax.servlet.ServletException;
import java.io.IOException;
import java.net.InetSocketAddress;

public class Main {
    public static void main(String[] args) throws IOException {
        // Crear el servidor HTTP en el puerto 8080
        HttpServer server = HttpServer.create(new InetSocketAddress(8080), 0);

        // Crear el servlet de Wicket
        Servlet wicketServlet = new WicketServlet() {
            @Override
            protected void init() throws ServletException {
                super.init();
                getFilterConfig().getServletContext().setAttribute(
                        WicketFilter.APP_ROOT_KEY,
                        MyWicketApplication.class.getName()
                );
            }
        };

        // Registrar el servlet en el contexto "/"
        server.createContext("/", exchange -> {
            try {
                wicketServlet.service(exchange.getRequest(), exchange.getResponse());
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        // Iniciar el servidor
        server.setExecutor(null); // Usa el executor por defecto
        server.start();

        System.out.println("Servidor HTTP iniciado en http://localhost:8080");
    }
}
```

---

### 5. Ejecutar la Aplicación
1. Compila el proyecto usando Maven:
   ```bash
   mvn clean package
   ```

2. Ejecuta la aplicación:
   ```bash
   java -cp target/wicket-http-server-1.0-SNAPSHOT.jar com.example.Main
   ```

3. Abre un navegador y visita `http://localhost:8080`. Deberías ver la página de inicio con el mensaje "¡Bienvenido a Apache Wicket!".

---

### Notas Finales
- Este enfoque utiliza el servidor HTTP integrado de Java (`com.sun.net.httpserver.HttpServer`), que es adecuado para pruebas y desarrollo rápido.
- Para entornos de producción, se recomienda usar un servidor HTTP más robusto como Tomcat, Undertow o cualquier otro servidor compatible con servlets.
- Si necesitas más funcionalidades (como soporte para sesiones o filtros avanzados), considera usar un servidor HTTP completo en lugar del servidor integrado.

Si tienes más preguntas o necesitas ayuda adicional, no dudes en preguntar. 😊