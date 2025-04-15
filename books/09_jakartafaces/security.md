A continuación, te proporcionaré un ejemplo claro y estructurado de cómo integrar **Jakarta Security** con **Jakarta Faces (JSF)** en una aplicación que se ejecuta en **Payara Micro**. Este ejemplo incluirá la configuración necesaria para autenticar usuarios y proteger recursos utilizando anotaciones como `@RolesAllowed`.

---

### 1. **Configuración del Proyecto**

Primero, asegúrate de tener las dependencias necesarias en tu archivo `pom.xml` si usas Maven:

```xml
<dependencies>
    <!-- Jakarta Faces (JSF) -->
    <dependency>
        <groupId>jakarta.faces</groupId>
        <artifactId>jakarta.faces-api</artifactId>
        <version>4.0.0</version>
        <scope>provided</scope>
    </dependency>

    <!-- Jakarta Security -->
    <dependency>
        <groupId>jakarta.security.enterprise</groupId>
        <artifactId>jakarta.security.enterprise-api</artifactId>
        <version>3.0.0</version>
        <scope>provided</scope>
    </dependency>

    <!-- Payara Micro Provided Dependencies -->
    <dependency>
        <groupId>fish.payara.api</groupId>
        <artifactId>payara-micro</artifactId>
        <version>6.2023.5</version>
        <scope>provided</scope>
    </dependency>
</dependencies>
```

---

### 2. **Creación de la Interfaz de Usuario con Jakarta Faces**

Crea una página JSF (`login.xhtml`) para el formulario de inicio de sesión:

```xml
<!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/xhtml"
      xmlns:h="http://xmlns.jcp.org/jsf/html">
<h:head>
    <title>Login</title>
</h:head>
<h:body>
    <h:form>
        <h:outputLabel for="username" value="Username:" />
        <h:inputText id="username" value="#{authController.username}" required="true" />
        <br />

        <h:outputLabel for="password" value="Password:" />
        <h:inputSecret id="password" value="#{authController.password}" required="true" />
        <br />

        <h:commandButton value="Login" action="#{authController.login}" />
    </h:form>

    <h:messages />
</h:body>
</html>
```

---

### 3. **Controlador de Autenticación**

Crea un controlador (`AuthController.java`) para manejar la lógica de autenticación utilizando Jakarta Security:

```java
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Named;
import jakarta.security.enterprise.AuthenticationStatus;
import jakarta.security.enterprise.SecurityContext;
import jakarta.security.enterprise.authentication.mechanism.http.AuthenticationParameters;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import javax.inject.Inject;

@Named
@RequestScoped
public class AuthController {

    @Inject
    private SecurityContext securityContext;

    @Inject
    private HttpServletRequest request;

    @Inject
    private HttpServletResponse response;

    private String username;
    private String password;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String login() {
        AuthenticationStatus status = securityContext.authenticate(
            request,
            response,
            AuthenticationParameters.withParams()
                .credential(new UsernamePasswordCredential(username, password))
        );

        if (status == AuthenticationStatus.SUCCESS) {
            return "/protected/home.xhtml?faces-redirect=true";
        } else {
            return "/login.xhtml?faces-redirect=true&error=true";
        }
    }
}
```

---

### 4. **Protección de Recursos con Jakarta Security**

Para proteger ciertas páginas o endpoints, puedes usar la anotación `@RolesAllowed` en un bean administrado o en un método específico:

```java
import jakarta.annotation.security.RolesAllowed;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Named;

@Named
@RequestScoped
public class ProtectedBean {

    @RolesAllowed("USER")
    public String getProtectedMessage() {
        return "Bienvenido a la página protegida.";
    }
}
```

---

### 5. **Configuración de Usuarios y Roles**

En Payara Micro, puedes configurar usuarios y roles en el archivo `microprofile-config.properties`:

```properties
# Configuración de usuarios y roles
javax.security.auth.login.config=classpath:login.conf
```

Y en el archivo `login.conf`, define los usuarios y roles:

```
SampleRealm {
    org.apache.geronimo.security.realm.providers.PropertiesFileLoginModule required
    usersURI="users.properties"
    groupsURI="groups.properties";
};
```

En `users.properties`:
```
user1=password123
user2=password456
```

En `groups.properties`:
```
user1=USER
user2=ADMIN
```

---

### 6. **Despliegue en Payara Micro**

Compila tu proyecto y despliega la aplicación en Payara Micro:

```bash
java -jar payara-micro.jar --deploy target/myapp.war
```

---

### 7. **Pruebas**

1. Accede a la página de inicio de sesión (`/login.xhtml`).
2. Ingresa las credenciales de un usuario configurado.
3. Si la autenticación es exitosa, serás redirigido a la página protegida (`/protected/home.xhtml`).

---

### Notas Finales

- Asegúrate de que los archivos de configuración (`login.conf`, `users.properties`, `groups.properties`) estén correctamente ubicados en el classpath.
- Puedes personalizar el mecanismo de autenticación implementando una clase que extienda `HttpAuthenticationMechanism`.

Este ejemplo demuestra cómo integrar Jakarta Security con Jakarta Faces en Payara Micro, permitiendo autenticar usuarios y proteger recursos de manera eficiente.