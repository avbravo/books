Explícame como funciona keycloack para autentificación y autorización en aplicaciones Java que usen eclipse micropofile y Jakartaee por ejemplo helidon me y payara micro y dame un ejemplo de como poder agregar nuevos usuarios a keycloack usando el API rest client de eclipse micropofile y explícame cómo se da la sesión y se reconecta automáticamente 

---

Keycloak es un sistema de gestión de identidad y acceso (IAM) de código abierto que se utiliza para manejar la autenticación y autorización en aplicaciones. Es especialmente útil en entornos de microservicios, donde múltiples servicios necesitan compartir información sobre usuarios y permisos de manera segura. A continuación, te explico cómo funciona Keycloak con aplicaciones Java basadas en Eclipse MicroProfile, Jakarta EE, Helidon ME y Payara Micro.

---

### **1. Funcionamiento básico de Keycloak**

Keycloak actúa como un proveedor de autenticación y autorización centralizado utilizando protocolos estándar como OAuth 2.0 y OpenID Connect (OIDC). Aquí están los pasos clave:

#### **a. Configuración inicial**
- **Realm**: Un "realm" en Keycloak es un espacio aislado donde se gestionan los usuarios, roles y configuraciones de seguridad para una aplicación o grupo de aplicaciones.
- **Client**: Cada aplicación debe registrarse en Keycloak como un "client". Se configuran detalles como el tipo de cliente (confidencial o público), las URLs de redirección permitidas, etc.
- **Usuarios y Roles**: Se crean usuarios y se les asignan roles que definen qué recursos pueden acceder.

#### **b. Flujo de autenticación**
1. El usuario intenta acceder a una aplicación protegida.
2. La aplicación redirige al usuario al servidor de Keycloak para iniciar sesión.
3. Keycloak autentica al usuario y genera un token JWT (JSON Web Token) que contiene información sobre el usuario y sus roles.
4. El token JWT se envía de vuelta a la aplicación, que lo valida utilizando la clave pública del realm de Keycloak.
5. La aplicación utiliza el token para tomar decisiones de autorización.

#### **c. Autorización**
- Los roles y permisos definidos en Keycloak se utilizan para controlar el acceso a recursos específicos en la aplicación.
- Las aplicaciones pueden validar tokens JWT para verificar si un usuario tiene los roles necesarios para acceder a ciertos endpoints.

---

### **2. Integración con Eclipse MicroProfile, Jakarta EE, Helidon ME y Payara Micro**

Estos frameworks admiten la integración con Keycloak mediante el uso de especificaciones como **MicroProfile JWT Authentication** y **Jakarta Security**.

#### **a. Eclipse MicroProfile**
- Usa la especificación **MicroProfile JWT Auth** para validar tokens JWT emitidos por Keycloak.
- Los tokens se validan automáticamente contra la clave pública del realm de Keycloak.
- Las anotaciones como `@RolesAllowed` se utilizan para restringir el acceso a métodos o endpoints basados en roles.

#### **b. Jakarta EE**
- Utiliza la especificación **Jakarta Security** para manejar la autenticación y autorización.
- Se puede configurar un filtro de servlet para validar tokens JWT.

#### **c. Helidon ME**
- Helidon proporciona soporte nativo para OIDC y JWT. Puedes configurar Helidon para que utilice Keycloak como proveedor de identidad.

#### **d. Payara Micro**
- Payara Micro permite configurar la autenticación y autorización mediante el uso de archivos `microprofile-config.properties` y anotaciones como `@LoginConfig`.

---

### **3. Ejemplo: Agregar nuevos usuarios a Keycloak usando MicroProfile REST Client**

Para agregar nuevos usuarios a Keycloak, puedes usar su API REST. Aquí tienes un ejemplo de cómo hacerlo con MicroProfile REST Client:

#### **a. Configuración del cliente REST**
1. **Dependencias**: Asegúrate de incluir las dependencias necesarias en tu proyecto Maven:
   ```xml
   <dependency>
       <groupId>org.eclipse.microprofile.rest.client</groupId>
       <artifactId>microprofile-rest-client-api</artifactId>
       <version>3.0</version>
   </dependency>
   ```

2. **Definición del cliente REST**:
   ```java
   import javax.ws.rs.*;
   import javax.ws.rs.core.MediaType;
   import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

   @RegisterRestClient(configKey = "keycloak.api")
   public interface KeycloakAdminClient {

       @POST
       @Path("/admin/realms/{realm}/users")
       @Consumes(MediaType.APPLICATION_JSON)
       void createUser(@PathParam("realm") String realm, UserRepresentation user);

       @POST
       @Path("/admin/realms/{realm}/users/{userId}/reset-password")
       @Consumes(MediaType.APPLICATION_JSON)
       void resetPassword(@PathParam("realm") String realm, @PathParam("userId") String userId, CredentialRepresentation password);
   }
   ```

3. **Clases de representación**:
   ```java
   public class UserRepresentation {
       private String username;
       private String email;
       private boolean enabled;
       private CredentialRepresentation credentials;

       // Getters and setters
   }

   public class CredentialRepresentation {
       private String type = "password";
       private String value;
       private boolean temporary;

       // Getters and setters
   }
   ```

4. **Configuración en `microprofile-config.properties`**:
   ```properties
   keycloak.api/mp-rest/url=http://localhost:8080/auth
   keycloak.api/mp-rest/scope=javax.inject.Singleton
   ```

#### **b. Uso del cliente REST**
```java
import jakarta.inject.Inject;
import org.eclipse.microprofile.rest.client.inject.RestClient;

public class UserService {

    @Inject
    @RestClient
    KeycloakAdminClient keycloakAdminClient;

    public void createUser(String realm, String username, String email, String password) {
        UserRepresentation user = new UserRepresentation();
        user.setUsername(username);
        user.setEmail(email);
        user.setEnabled(true);

        CredentialRepresentation credential = new CredentialRepresentation();
        credential.setValue(password);
        credential.setTemporary(false);

        user.setCredentials(new CredentialRepresentation[] { credential });

        keycloakAdminClient.createUser(realm, user);

        // Opcional: Cambiar contraseña
        // keycloakAdminClient.resetPassword(realm, userId, credential);
    }
}
```

---

### **4. Sesión y reconexión automática**

#### **a. Sesión**
- Cuando un usuario inicia sesión, Keycloak genera un token de acceso (JWT) y un token de actualización.
- El token de acceso tiene una vida útil limitada (por ejemplo, 5 minutos).
- El token de actualización se utiliza para obtener un nuevo token de acceso sin requerir que el usuario inicie sesión nuevamente.

#### **b. Reconexión automática**
- Las aplicaciones pueden implementar un mecanismo para detectar cuándo el token de acceso ha expirado.
- Al detectar la expiración, se envía el token de actualización a Keycloak para obtener un nuevo token de acceso.
- Esto se puede hacer automáticamente utilizando interceptores HTTP o librerías específicas que manejen la renovación de tokens.

---

### **Conclusión**

Keycloak es una herramienta poderosa para manejar la autenticación y autorización en aplicaciones Java modernas. Con MicroProfile REST Client, puedes interactuar fácilmente con la API de Keycloak para realizar operaciones como agregar usuarios. Además, el manejo de sesiones y la reconexión automática garantizan una experiencia fluida para los usuarios.

Si necesitas más detalles o ejemplos específicos, no dudes en preguntar. 😊


