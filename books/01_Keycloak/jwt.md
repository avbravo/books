,ñz 
¡Claro! Vamos a mejorar y ampliar el ejemplo para Helidon MP, haciendo que sea más robusto y detallado. Añadiremos configuraciones adicionales, mejores prácticas y un enfoque modular para facilitar la escalabilidad y mantenimiento.

---

### **1. Mejoras en la Configuración de Keycloak**

Asegúrate de que Keycloak esté configurado correctamente:

- **Realm**: `myrealm`
- **Clientes**:
  - `helidon-client` (para Helidon MP)
    - Tipo: `confidential`
    - Redirect URIs: `http://localhost:7001/*`
    - Scopes: `openid`, `profile`, `email`
  - Roles: `admin`, `user`

Obtén las credenciales del cliente (`Client ID` y `Client Secret`) desde Keycloak.

---

### **2. Estructura del Proyecto Helidon MP**

Organiza tu proyecto de manera modular. Aquí hay una estructura sugerida:

```
helidon-keycloak/
├── src/main/java/com/example/
│   ├── Main.java
│   ├── config/
│   │   └── SecurityConfig.java
│   ├── resource/
│   │   └── SecureResource.java
│   └── model/
│       └── UserContext.java
├── src/main/resources/
│   └── application.yaml
└── pom.xml
```

---

### **3. Dependencias en `pom.xml`**

Asegúrate de incluir las dependencias necesarias para autenticación y autorización con Keycloak:

```xml
<dependencies>
    <!-- Helidon MicroProfile -->
    <dependency>
        <groupId>io.helidon.microprofile.bundles</groupId>
        <artifactId>helidon-microprofile</artifactId>
        <version>4.0.0</version>
    </dependency>

    <!-- OIDC Provider -->
    <dependency>
        <groupId>io.helidon.security.providers</groupId>
        <artifactId>helidon-security-providers-oidc</artifactId>
    </dependency>

    <!-- JSON Web Token Support -->
    <dependency>
        <groupId>io.helidon.security</groupId>
        <artifactId>helidon-security-integration-jwt</artifactId>
    </dependency>
</dependencies>
```

---

### **4. Configuración en `application.yaml`**

La configuración YAML debe ser clara y modular. A continuación, se muestra un ejemplo mejorado:

```yaml
server:
  port: 7001

security:
  providers:
    - oidc:
        audience: "helidon-client"
        identity-uri: "http://localhost:8080/realms/myrealm"
        client-id: "helidon-client"
        client-secret: "your-helidon-client-secret"
        redirect-uri: "http://localhost:7001/callback"
        scopes: ["openid", "profile", "email"]
        token-endpoint-auth-method: "client_secret_post"
        logout-enabled: true
        logout-uri: "http://localhost:8080/auth/realms/myrealm/protocol/openid-connect/logout"
```

Explicación de los campos clave:
- `identity-uri`: URL base del Realm de Keycloak.
- `client-id` y `client-secret`: Credenciales del cliente.
- `redirect-uri`: URI de redirección después de la autenticación.
- `scopes`: Permisos solicitados al usuario.
- `logout-enabled`: Habilita el cierre de sesión.
- `logout-uri`: URI para cerrar sesión en Keycloak.

---

### **5. Clase Principal (`Main.java`)**

La clase principal inicializa la aplicación Helidon MP:

```java
package com.example;

import io.helidon.microprofile.server.Server;

public class Main {
    public static void main(String[] args) {
        Server server = Server.create();
        server.start();
        System.out.println("Helidon MP server started at http://localhost:7001");
    }
}
```

---

### **6. Configuración de Seguridad (`SecurityConfig.java`)**

Crea una clase separada para manejar la configuración de seguridad. Esto mejora la modularidad y claridad:

```java
package com.example.config;

import io.helidon.security.Security;
import io.helidon.security.providers.oidc.OidcProvider;

public class SecurityConfig {

    public static Security createSecurity() {
        return Security.builder()
                .addProvider(OidcProvider.create())
                .build();
    }
}
```

---

### **7. Recursos Protegidos (`SecureResource.java`)**

Define endpoints protegidos usando anotaciones como `@RolesAllowed`. Además, puedes extraer información del token JWT:

```java
package com.example.resource;

import jakarta.annotation.security.RolesAllowed;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.SecurityContext;

@Path("/api")
public class SecureResource {

    @GET
    @Path("/admin")
    @RolesAllowed("admin")
    public String adminEndpoint(@Context SecurityContext securityContext) {
        String username = securityContext.getUserPrincipal().getName();
        return "Admin access granted to: " + username;
    }

    @GET
    @Path("/user")
    @RolesAllowed({"user", "admin"})
    public String userEndpoint(@Context SecurityContext securityContext) {
        String username = securityContext.getUserPrincipal().getName();
        return "User access granted to: " + username;
    }
}
```

---

### **8. Modelo de Contexto (`UserContext.java`)**

Puedes crear un modelo para representar el contexto del usuario, extrayendo información del token JWT:

```java
package com.example.model;

import jakarta.json.JsonObject;

public class UserContext {
    private final String username;
    private final String email;
    private final String[] roles;

    public UserContext(JsonObject jwtClaims) {
        this.username = jwtClaims.getString("preferred_username");
        this.email = jwtClaims.getString("email");
        this.roles = jwtClaims.getJsonArray("roles").stream()
                .map(role -> role.toString())
                .toArray(String[]::new);
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public String[] getRoles() {
        return roles;
    }
}
```

---

### **9. Pruebas y Validación**

1. **Iniciar Keycloak**:
   ```bash
   docker run -p 8080:8080 -e KEYCLOAK_ADMIN=admin -e KEYCLOAK_ADMIN_PASSWORD=admin quay.io/keycloak/keycloak:latest start-dev
   ```

2. **Ejecutar la aplicación Helidon MP**:
   ```bash
   mvn clean package
   java -jar target/helidon-keycloak.jar
   ```

3. **Acceder a los endpoints**:
   - Accede a `http://localhost:7001/api/admin` o `http://localhost:7001/api/user`.
   - Serás redirigido a Keycloak para autenticarte.

4. **Verificar los roles**:
   - Los roles se validan automáticamente gracias a `@RolesAllowed`.

---

### **10. Mejoras Adicionales**

- **Logging**: Agrega logs para depurar problemas de autenticación.
- **Custom Claims**: Si necesitas claims personalizados en el token JWT, configúralos en Keycloak.
- **Health Checks**: Implementa endpoints de salud para monitorear la aplicación.
- **Swagger/OpenAPI**: Documenta tus endpoints protegidos usando Swagger.

Con estas mejoras, tu aplicación Helidon MP estará bien estructurada, segura y lista para producción. 😊

¿Necesitas más detalles o tienes preguntas específicas? ¡Estoy aquí para ayudarte!