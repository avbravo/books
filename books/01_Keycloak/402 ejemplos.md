Añade un ejemplo con código Java dónde se muestra cómo conectarse al keycloack desde payara micro y helidon me e incluye un ejemplo usando una aplicación creada con Jakarta faces y otra con vaadin donde se validen los roles 

A continuación, te proporciono un ejemplo detallado de cómo integrar Keycloak con **Payara Micro**, **Helidon ME**, y aplicaciones web creadas con **Jakarta Faces** y **Vaadin**, validando roles en cada caso.

---

### **1. Configuración de Keycloak**
Antes de empezar, asegúrate de tener configurado Keycloak:
- Crea un realm (por ejemplo, `myrealm`).
- Registra dos clientes: uno para Payara Micro (`payara-client`) y otro para Helidon ME (`helidon-client`).
- Define roles como `admin` y `user`.
- Asegúrate de que los tokens JWT incluyan roles en las claims.

---

### **2. Ejemplo con Payara Micro y Jakarta Faces**

#### **a. Dependencias Maven**
```xml
<dependency>
    <groupId>jakarta.platform</groupId>
    <artifactId>jakarta.jakartaee-api</artifactId>
    <version>9.1.0</version>
    <scope>provided</scope>
</dependency>
<dependency>
    <groupId>org.keycloak</groupId>
    <artifactId>keycloak-adapter-core</artifactId>
    <version>21.0.0</version>
</dependency>
```

#### **b. Configuración de Keycloak en `web.xml`**
```xml
<login-config>
    <auth-method>KEYCLOAK</auth-method>
    <realm-name>myrealm</realm-name>
</login-config>

<security-constraint>
    <web-resource-collection>
        <web-resource-name>Protected Resources</web-resource-name>
        <url-pattern>/faces/*</url-pattern>
    </web-resource-collection>
    <auth-constraint>
        <role-name>user</role-name>
        <role-name>admin</role-name>
    </auth-constraint>
</security-constraint>

<security-role>
    <role-name>user</role-name>
</security-role>
<security-role>
    <role-name>admin</role-name>
</security-role>
```

#### **c. Archivo `keycloak.json`**
Coloca este archivo en `WEB-INF/keycloak.json`:
```json
{
  "realm": "myrealm",
  "auth-server-url": "http://localhost:8080/auth/",
  "ssl-required": "external",
  "resource": "payara-client",
  "public-client": true,
  "confidential-port": 0
}
```

#### **d. Validación de roles en un Managed Bean**
```java
import jakarta.annotation.security.RolesAllowed;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Named;

@Named
@RequestScoped
public class UserBean {

    @RolesAllowed("admin")
    public String getAdminMessage() {
        return "Welcome, Admin!";
    }

    @RolesAllowed("user")
    public String getUserMessage() {
        return "Welcome, User!";
    }
}
```

#### **e. Vista con Jakarta Faces (`index.xhtml`)**
```xml
<!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/xhtml"
      xmlns:h="http://xmlns.jcp.org/jsf/html">
<h:head>
    <title>Keycloak Example</title>
</h:head>
<h:body>
    <h:form>
        <h:outputText value="#{userBean.adminMessage}" rendered="#{request.isUserInRole('admin')}" />
        <h:outputText value="#{userBean.userMessage}" rendered="#{request.isUserInRole('user')}" />
    </h:form>
</h:body>
</html>
```

---

### **3. Ejemplo con Helidon ME**

#### **a. Dependencias Maven**
```xml
<dependency>
    <groupId>io.helidon.security.providers</groupId>
    <artifactId>helidon-security-providers-oidc</artifactId>
    <version>3.0.0</version>
</dependency>
```

#### **b. Configuración de Keycloak en `application.yaml`**
```yaml
security:
  providers:
    - oidc:
        client-id: helidon-client
        client-secret: your-client-secret
        identity-uri: http://localhost:8080/auth/realms/myrealm
        redirect-uri: http://localhost:8080/callback
```

#### **c. Validación de roles en un Endpoint**
```java
import io.helidon.security.annotations.Authenticated;
import io.helidon.security.annotations.RolesAllowed;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

@Path("/api")
public class MyResource {

    @GET
    @Path("/admin")
    @Produces(MediaType.TEXT_PLAIN)
    @Authenticated
    @RolesAllowed("admin")
    public String adminEndpoint() {
        return "Admin access granted!";
    }

    @GET
    @Path("/user")
    @Produces(MediaType.TEXT_PLAIN)
    @Authenticated
    @RolesAllowed("user")
    public String userEndpoint() {
        return "User access granted!";
    }
}
```

---

### **4. Ejemplo con Vaadin**

#### **a. Dependencias Maven**
```xml
<dependency>
    <groupId>com.vaadin</groupId>
    <artifactId>vaadin</artifactId>
    <version>24.0.0</version>
</dependency>
<dependency>
    <groupId>org.keycloak</groupId>
    <artifactId>keycloak-spring-boot-starter</artifactId>
    <version>21.0.0</version>
</dependency>
```

#### **b. Configuración de Keycloak en `application.properties`**
```properties
keycloak.realm=myrealm
keycloak.auth-server-url=http://localhost:8080/auth
keycloak.resource=vaadin-client
keycloak.public-client=true
```

#### **c. Validación de roles en una Vista de Vaadin**
```java
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import org.springframework.security.core.context.SecurityContextHolder;

@Route("")
public class MainView extends VerticalLayout {

    public MainView() {
        H1 header = new H1("Keycloak Example");

        Button adminButton = new Button("Admin Access", event -> {
            if (SecurityContextHolder.getContext().getAuthentication().getAuthorities().stream()
                    .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("ROLE_ADMIN"))) {
                UI.getCurrent().getPage().executeJs("alert('Admin access granted!')");
            } else {
                UI.getCurrent().getPage().executeJs("alert('Access denied!')");
            }
        });

        Button userButton = new Button("User Access", event -> {
            if (SecurityContextHolder.getContext().getAuthentication().getAuthorities().stream()
                    .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("ROLE_USER"))) {
                UI.getCurrent().getPage().executeJs("alert('User access granted!')");
            } else {
                UI.getCurrent().getPage().executeJs("alert('Access denied!')");
            }
        });

        add(header, adminButton, userButton);
    }
}
```

---

### **5. Resumen**
- **Payara Micro**: Usa `keycloak.json` y anotaciones como `@RolesAllowed` para validar roles.
- **Helidon ME**: Configura OIDC en `application.yaml` y usa anotaciones como `@RolesAllowed`.
- **Jakarta Faces**: Utiliza `#{request.isUserInRole('role')}` para verificar roles en la vista.
- **Vaadin**: Usa Spring Security para validar roles en la lógica de la aplicación.

Estos ejemplos muestran cómo integrar Keycloak con diferentes frameworks y tecnologías, asegurando una autenticación y autorización robusta. Si necesitas más detalles o tienes preguntas adicionales, no dudes en preguntar. 😊