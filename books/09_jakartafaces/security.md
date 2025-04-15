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

---

Proteger páginas en una aplicación **Jakarta Faces (JSF)** utilizando **Jakarta Security** es un proceso que implica configurar roles y restringir el acceso a ciertas páginas o recursos. A continuación, te mostraré cómo proteger páginas específicas en una aplicación JSF utilizando anotaciones de seguridad y configuraciones adicionales.

---

### 1. **Configuración de Roles y Usuarios**

Primero, asegúrate de tener definidos los usuarios y roles en tu servidor de aplicaciones (en este caso, Payara Micro). Puedes hacerlo mediante archivos de configuración como `users.properties` y `groups.properties`.

#### Archivo `users.properties`:
```properties
user1=password123
admin1=adminpass
```

#### Archivo `groups.properties`:
```properties
user1=USER
admin1=ADMIN
```

Estos archivos definen dos usuarios: `user1` con el rol `USER` y `admin1` con el rol `ADMIN`.

---

### 2. **Protección de Páginas con Anotaciones**

Puedes usar anotaciones como `@RolesAllowed` para proteger beans administrados que controlan las páginas JSF. Por ejemplo:

#### Bean Administrado (`ProtectedBean.java`):
```java
import jakarta.annotation.security.RolesAllowed;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Named;

@Named
@RequestScoped
public class ProtectedBean {

    @RolesAllowed("USER")
    public String getUserMessage() {
        return "Bienvenido, usuario normal.";
    }

    @RolesAllowed("ADMIN")
    public String getAdminMessage() {
        return "Bienvenido, administrador.";
    }
}
```

En este ejemplo:
- El método `getUserMessage()` solo puede ser accedido por usuarios con el rol `USER`.
- El método `getAdminMessage()` solo puede ser accedido por usuarios con el rol `ADMIN`.

---

### 3. **Restringir Acceso a Páginas JSF**

Para restringir el acceso a páginas completas, puedes usar la configuración del archivo `web.xml`. Esto permite bloquear el acceso a ciertas URLs basadas en roles.

#### Configuración en `web.xml`:
```xml
<security-constraint>
    <web-resource-collection>
        <web-resource-name>Protected Pages</web-resource-name>
        <url-pattern>/protected/*</url-pattern>
    </web-resource-collection>
    <auth-constraint>
        <role-name>USER</role-name>
        <role-name>ADMIN</role-name>
    </auth-constraint>
</security-constraint>

<login-config>
    <auth-method>FORM</auth-method>
    <form-login-config>
        <form-login-page>/login.xhtml</form-login-page>
        <form-error-page>/login-error.xhtml</form-error-page>
    </form-login-config>
</login-config>

<security-role>
    <role-name>USER</role-name>
</security-role>
<security-role>
    <role-name>ADMIN</role-name>
</security-role>
```

En este ejemplo:
- Todas las páginas bajo `/protected/*` están protegidas.
- Solo los usuarios con los roles `USER` o `ADMIN` pueden acceder a estas páginas.
- Si un usuario no está autenticado, será redirigido a `/login.xhtml`.
- Si la autenticación falla, se mostrará `/login-error.xhtml`.

---

### 4. **Páginas JSF Protegidas**

Crea las páginas JSF que estarán protegidas. Por ejemplo:

#### `protected/home.xhtml`:
```xml
<!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/xhtml"
      xmlns:h="http://xmlns.jcp.org/jsf/html">
<h:head>
    <title>Página Protegida</title>
</h:head>
<h:body>
    <h1>Bienvenido a la página protegida</h1>
    <p>#{protectedBean.userMessage}</p>
</h:body>
</html>
```

#### `protected/admin.xhtml`:
```xml
<!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/xhtml"
      xmlns:h="http://xmlns.jcp.org/jsf/html">
<h:head>
    <title>Página de Administrador</title>
</h:head>
<h:body>
    <h1>Página exclusiva para administradores</h1>
    <p>#{protectedBean.adminMessage}</p>
</h:body>
</html>
```

---

### 5. **Control de Acceso en Beans**

Si necesitas verificar el rol del usuario directamente en un bean, puedes inyectar `SecurityContext`:

```java
import jakarta.security.enterprise.SecurityContext;
import jakarta.inject.Inject;

@Named
@RequestScoped
public class AccessControlBean {

    @Inject
    private SecurityContext securityContext;

    public boolean isUserInRole(String role) {
        return securityContext.isCallerInRole(role);
    }
}
```

Luego, puedes usar este bean en tus páginas JSF para mostrar contenido condicionalmente:

```xml
<h:panelGroup rendered="#{accessControlBean.isUserInRole('ADMIN')}">
    <p>Contenido exclusivo para administradores.</p>
</h:panelGroup>
```

---

### 6. **Autenticación con Jakarta Security**

Para manejar la autenticación, puedes usar un formulario de inicio de sesión como el siguiente:

#### `login.xhtml`:
```xml
<!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/xhtml"
      xmlns:h="http://xmlns.jcp.org/jsf/html">
<h:head>
    <title>Iniciar Sesión</title>
</h:head>
<h:body>
    <h:form>
        <h:outputLabel for="username" value="Usuario:" />
        <h:inputText id="username" value="#{authController.username}" required="true" />
        <br />

        <h:outputLabel for="password" value="Contraseña:" />
        <h:inputSecret id="password" value="#{authController.password}" required="true" />
        <br />

        <h:commandButton value="Iniciar Sesión" action="#{authController.login}" />
    </h:form>

    <h:messages />
</h:body>
</html>
```

El `AuthController` maneja la lógica de autenticación, como se mostró en la respuesta anterior.

---

### 7. **Pruebas**

1. Despliega la aplicación en Payara Micro.
2. Intenta acceder a `/protected/home.xhtml` sin iniciar sesión. Deberías ser redirigido a `/login.xhtml`.
3. Inicia sesión con un usuario válido:
   - Con `user1` (rol `USER`), deberías poder acceder a `/protected/home.xhtml`.
   - Con `admin1` (rol `ADMIN`), deberías poder acceder tanto a `/protected/home.xhtml` como a `/protected/admin.xhtml`.

---

### Resumen

- Usa `@RolesAllowed` para proteger métodos en beans administrados.
- Configura restricciones de acceso en `web.xml` para proteger páginas completas.
- Implementa un formulario de inicio de sesión con Jakarta Security para autenticar usuarios.
- Usa `SecurityContext` para verificar roles dinámicamente en beans.

Este enfoque garantiza que las páginas JSF estén protegidas y que solo los usuarios autorizados puedan acceder a ellas.