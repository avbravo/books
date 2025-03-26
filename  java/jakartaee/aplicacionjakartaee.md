

crea una aplicacion jakarta ee con primefaces usando diseño responsivo con un dashboard estilo tailwindcss

A continuación, te proporcionaré una guía paso a paso para crear una aplicación Jakarta EE con PrimeFaces que utilice un diseño responsivo y un dashboard inspirado en el estilo de TailwindCSS. Esta aplicación estará diseñada para ser moderna, funcional y adaptable a diferentes dispositivos.

---

### **1. Configuración del Proyecto**

#### **1.1. Herramientas necesarias**
- **IDE**: Eclipse, IntelliJ IDEA o NetBeans.
- **Servidor de aplicaciones**: Apache TomEE, WildFly o Payara Server.
- **Maven**: Para la gestión de dependencias.
- **PrimeFaces**: Framework de componentes UI.
- **Bootstrap 5 / TailwindCSS**: Para el diseño responsivo.

#### **1.2. Crear un proyecto Maven**
Crea un proyecto Maven estándar con la siguiente estructura:

```
my-jakarta-app/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com.example.dashboard
│   │   │       └── DashboardController.java
│   │   ├── resources/
│   │   │   └── META-INF/
│   │   │       └── beans.xml
│   │   └── webapp/
│   │       ├── WEB-INF/
│   │       │   └── web.xml
│   │       ├── index.xhtml
│   │       └── css/
│   │           └── styles.css
├── pom.xml
```

---

### **2. Configuración del `pom.xml`**

Añade las dependencias necesarias para Jakarta EE, PrimeFaces y otros componentes:

```xml
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>

    <groupId>com.example</groupId>
    <artifactId>jakarta-dashboard</artifactId>
    <version>1.0-SNAPSHOT</version>
    <packaging>war</packaging>

    <properties>
        <jakarta.version>9.1.0</jakarta.version>
        <primefaces.version>12.0.0</primefaces.version>
    </properties>

    <dependencies>
        <!-- Jakarta EE API -->
        <dependency>
            <groupId>jakarta.platform</groupId>
            <artifactId>jakarta.jakartaee-api</artifactId>
            <version>${jakarta.version}</version>
            <scope>provided</scope>
        </dependency>

        <!-- PrimeFaces -->
        <dependency>
            <groupId>org.primefaces</groupId>
            <artifactId>primefaces</artifactId>
            <version>${primefaces.version}</version>
        </dependency>

        <!-- Servlet API (opcional si no está incluido en tu servidor) -->
        <dependency>
            <groupId>jakarta.servlet</groupId>
            <artifactId>jakarta.servlet-api</artifactId>
            <version>6.0.0</version>
            <scope>provided</scope>
        </dependency>
    </dependencies>

    <build>
        <finalName>jakarta-dashboard</finalName>
    </build>
</project>
```

---

### **3. Configuración del `web.xml`**

Configura el archivo `web.xml` para habilitar Jakarta Faces y PrimeFaces:

```xml
<web-app xmlns="https://jakarta.ee/xml/ns/jakartaee"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="https://jakarta.ee/xml/ns/jakartaee https://jakarta.ee/xml/ns/jakartaee/web-app_5_0.xsd"
         version="5.0">

    <servlet>
        <servlet-name>Faces Servlet</servlet-name>
        <servlet-class>jakarta.faces.webapp.FacesServlet</servlet-class>
        <load-on-startup>1</load-on-startup>
    </servlet>

    <servlet-mapping>
        <servlet-name>Faces Servlet</servlet-name>
        <url-pattern>*.xhtml</url-pattern>
    </servlet-mapping>

    <context-param>
        <param-name>primefaces.THEME</param-name>
        <param-value>saga</param-value>
    </context-param>

    <welcome-file-list>
        <welcome-file>index.xhtml</welcome-file>
    </welcome-file-list>
</web-app>
```

---

### **4. Crear el Controlador del Dashboard**

Crea un controlador básico para manejar los datos del dashboard:

```java
package com.example.dashboard;

import jakarta.enterprise.context.SessionScoped;
import jakarta.inject.Named;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Named
@SessionScoped
public class DashboardController implements Serializable {

    private List<String> tasks;

    public DashboardController() {
        tasks = new ArrayList<>();
        tasks.add("Tarea 1");
        tasks.add("Tarea 2");
        tasks.add("Tarea 3");
    }

    public List<String> getTasks() {
        return tasks;
    }

    public void addTask(String task) {
        tasks.add(task);
    }
}
```

---

### **5. Diseño del Dashboard con PrimeFaces**

Crea el archivo `index.xhtml` para el diseño del dashboard:

```xml
<!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/xhtml"
      xmlns:h="http://xmlns.jakarta.ee/jsf/html"
      xmlns:p="http://primefaces.org/ui">

<h:head>
    <title>Dashboard</title>
    <h:outputStylesheet library="css" name="styles.css"/>
</h:head>

<h:body>
    <div class="container mx-auto p-4">
        <h1 class="text-2xl font-bold text-center">Dashboard</h1>

        <!-- Panel de estadísticas -->
        <div class="grid grid-cols-1 md:grid-cols-3 gap-4 mt-4">
            <p:panel header="Usuarios Activos" styleClass="bg-blue-500 text-white p-4 rounded">
                <h:outputText value="1,234" styleClass="text-3xl font-bold"/>
            </p:panel>
            <p:panel header="Ventas Totales" styleClass="bg-green-500 text-white p-4 rounded">
                <h:outputText value="$50,000" styleClass="text-3xl font-bold"/>
            </p:panel>
            <p:panel header="Tareas Pendientes" styleClass="bg-yellow-500 text-white p-4 rounded">
                <h:outputText value="15" styleClass="text-3xl font-bold"/>
            </p:panel>
        </div>

        <!-- Lista de tareas -->
        <div class="mt-8">
            <p:dataList value="#{dashboardController.tasks}" var="task" styleClass="space-y-2">
                <h:outputText value="#{task}" styleClass="text-lg"/>
            </p:dataList>
        </div>
    </div>
</h:body>
</html>
```

---

### **6. Estilos Personalizados (TailwindCSS)**

Para usar estilos similares a TailwindCSS, puedes añadir clases CSS personalizadas en el archivo `styles.css`:

```css
/* Espaciado */
.mt-4 { margin-top: 1rem; }
.mt-8 { margin-top: 2rem; }

/* Grid */
.grid { display: grid; }
.grid-cols-1 { grid-template-columns: repeat(1, minmax(0, 1fr)); }
.md\:grid-cols-3 { grid-template-columns: repeat(3, minmax(0, 1fr)); }

/* Espaciado entre elementos */
.gap-4 { gap: 1rem; }

/* Texto */
.text-2xl { font-size: 1.5rem; }
.text-3xl { font-size: 2rem; }
.font-bold { font-weight: bold; }
.text-center { text-align: center; }

/* Colores */
.bg-blue-500 { background-color: #3b82f6; }
.bg-green-500 { background-color: #22c55e; }
.bg-yellow-500 { background-color: #facc15; }
.text-white { color: white; }

/* Bordes */
.rounded { border-radius: 0.5rem; }

/* Padding */
.p-4 { padding: 1rem; }
```

---

### **7. Ejecutar la Aplicación**

1. Despliega el proyecto en tu servidor Jakarta EE (por ejemplo, Apache TomEE).
2. Accede a la aplicación desde tu navegador en `http://localhost:8080/jakarta-dashboard/index.xhtml`.

---

### **Resultado Final**

La aplicación mostrará un dashboard con:
- Un diseño responsivo basado en grillas.
- Componentes de PrimeFaces integrados.
- Estilos inspirados en TailwindCSS.

Si necesitas más detalles o ajustes, no dudes en preguntar. ¡Buena suerte con tu proyecto! 🚀
