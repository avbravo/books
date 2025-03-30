# Helidon


Para utilizar PrimeFaces con Helidon en un proyecto Java, necesitarás seguir algunos pasos específicos, ya que Helidon es una plataforma de microservicios y no sigue el mismo modelo de desarrollo que un proyecto JSF (JavaServer Faces) tradicional. Aquí te dejo una guía paso a paso:

1. **Agregar Dependencias**: Primero, necesitas agregar las dependencias de PrimeFaces y JSF a tu proyecto. Si estás utilizando Maven, puedes hacerlo agregando las siguientes dependencias en tu archivo `pom.xml`. Si no estás utilizando Maven, tendrás que descargar manualmente los archivos JAR y agregarlos a tu classpath.

   ```xml
   <dependencies>
       <!-- PrimeFaces -->
       <dependency>
           <groupId>org.primefaces</groupId>
           <artifactId>primefaces</artifactId>
           <version>10.0.0</version> <!-- Asegúrate de usar la última versión -->
       </dependency>
       <!-- JSF -->
       <dependency>
           <groupId>jakarta.platform</groupId>
           <artifactId>jakarta.jakartaee-api</artifactId>
           <version>8.0.0</version> <!-- Asegúrate de usar la última versión -->
           <scope>provided</scope>
       </dependency>
   </dependencies>
   ```

2. **Configuración de JSF**: Para que JSF funcione correctamente, necesitas configurar el `web.xml` de tu aplicación. Esto incluye definir el servlet de JSF y cualquier otra configuración necesaria para tu aplicación. Aquí tienes un ejemplo básico de cómo podría verse:

   ```xml
   <web-app xmlns="http://xmlns.jcp.org/xml/ns/javaee"
            xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
            xsi:schemaLocation="http://xmlns.jcp.org/xml/ns/javaee http://xmlns.jcp.org/xml/ns/javaee/web-app_4_0.xsd"
            version="4.0">
       <servlet>
           <servlet-name>Faces Servlet</servlet-name>
           <servlet-class>javax.faces.webapp.FacesServlet</servlet-class>
           <load-on-startup>1</load-on-startup>
       </servlet>
       <servlet-mapping>
           <servlet-name>Faces Servlet</servlet-name>
           <url-pattern>*.xhtml</url-pattern>
       </servlet-mapping>
   </web-app>
   ```

3. **Despliegue de la Aplicación**: Una vez que hayas configurado tus dependencias y configurado JSF, puedes proceder a desplegar tu aplicación. Si estás utilizando Helidon, asegúrate de seguir las instrucciones de despliegue específicas para Helidon, que pueden variar dependiendo del entorno de ejecución (por ejemplo, Kubernetes, OpenShift, etc.).

4. **Uso de Componentes de PrimeFaces**: Ahora que tu aplicación está configurada y desplegada, puedes comenzar a utilizar los componentes de PrimeFaces en tus páginas XHTML. Asegúrate de declarar el espacio de nombres de PrimeFaces en tus páginas XHTML:

   ```xml
   <?xml version="1.0" encoding="UTF-8"?>
   <!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
   <html xmlns="http://www.w3.org/1999/xhtml"
         xmlns:h="http://xmlns.jcp.org/jsf/html"
         xmlns:p="http://primefaces.org/ui">
       <h:head>
           <title>PrimeFaces con Helidon</title>
       </h:head>
       <h:body>
           <!-- Aquí puedes comenzar a usar componentes de PrimeFaces -->
           <p:button value="Haz Click" />
       </h:body>
   </html>
   ```

Recuerda que este es un ejemplo básico. La configuración exacta puede variar dependiendo de las necesidades específicas de tu proyecto y la versión de las herramientas que estés utilizando.

Citations:
[1] https://stackoverflow.com/questions/24801372/primefaces-in-eclipse-java-ee-ide-for-web-developers
[2] https://helidon.io/docs/v4
[3] https://forum.primefaces.org/viewtopic.php?t=25973
[4] https://www.youtube.com/watch?v=Jz89sHm7NGs
[5] http://static1.1.sqspcdn.com/static/f/923743/17640188/1334265614870/1.%2BParte%2BI%2B-%2BNetBeans.pdf%3Ftoken%3DB0vLshWLCqjAvdFVuQIpOMgEWtA%253D