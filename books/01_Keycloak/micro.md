Aquí tienes un ejemplo de una aplicación Jakarta EE con Payara Micro que utiliza Eclipse MicroProfile y valida JWT con Keycloak:

1. **pom.xml**:

```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>

    <groupId>com.example</groupId>
    <artifactId>jwt-auth-example</artifactId>
    <version>1.0-SNAPSHOT</version>
    <packaging>war</packaging>

    <properties>
        <maven.compiler.source>11</maven.compiler.source>
        <maven.compiler.target>11</maven.compiler.target>
        <jakarta.platform.version>9.1.0</jakarta.platform.version>
        <microprofile.version>5.0</microprofile.version>
        <payara.micro.version>5.2022.5</payara.micro.version>
    </properties>

    <dependencies>
        <!-- Jakarta EE Web Profile -->
        <dependency>
            <groupId>jakarta.platform</groupId>
            <artifactId>jakarta.jakartaee-web-profile-api</artifactId>
            <version>${jakarta.platform.version}</version>
            <scope>provided</scope>
        </dependency>

        <!-- MicroProfile -->
        <dependency>
            <groupId>org.eclipse.microprofile</groupId>
            <artifactId>microprofile</artifactId>
            <version>${microprofile.version}</version>
            <type>pom</type>
            <scope>provided</scope>
        </dependency>
        
        <dependency>
            <groupId>org.eclipse.microprofile.jwt</groupId>
            <artifactId>microprofile-jwt-auth-api</artifactId>
            <version>2.1</version>
        </dependency>

        <!-- Payara Micro -->
        <dependency>
            <groupId>fish.payara.extras</groupId>
            <artifactId>payara-micro</artifactId>
            <version>${payara.micro.version}</version>
            <scope>provided</scope>
        </dependency>
    </dependencies>

    <build>
        <finalName>${project.artifactId}</finalName>
    </build>
</project>
```

2. **Configuración JWT (src/main/resources/META-INF/microprofile-config.properties)**:

```properties
mp.jwt.verify.publickey=YOUR_KEYCLOAK_PUBLIC_KEY
mp.jwt.verify.issuer=YOUR_KEYCLOAK_ISSUER_URL
```

3. **Clase principal de la aplicación**:

```java
import jakarta.ws.rs.ApplicationPath;
import jakarta.ws.rs.core.Application;
import org.eclipse.microprofile.auth.LoginConfig;

@LoginConfig(authMethod = "MP-JWT")
@ApplicationPath("/api")
public class JwtApplication extends Application {
}
```

4. **Endpoint protegido (src/main/java/com/example/ProtectedResource.java)**:

```java
import jakarta.annotation.security.RolesAllowed;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import org.eclipse.microprofile.jwt.JsonWebToken;

@Path("/protected")
public class ProtectedResource {

    @Inject
    private JsonWebToken jwt;

    @GET
    @RolesAllowed("user")
    public String protectedEndpoint() {
        return "Hello " + jwt.getName() + "! Your roles: " + jwt.getGroups();
    }
}
```

5. **Configuración de seguridad (src/main/java/com/example/JwtAuthConfig.java)**:

```java
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.security.enterprise.authentication.mechanism.http.OpenIdAuthenticationMechanismDefinition;
import jakarta.security.enterprise.identitystore.openid.OpenIdContext;

@ApplicationScoped
@OpenIdAuthenticationMechanismDefinition(
    providerURI = "${mp.jwt.verify.issuer}",
    clientId = "your-client-id",
    clientSecret = "your-client-secret"
)
public class JwtAuthConfig {
    
    @Inject
    private OpenIdContext context;

}
```

Para ejecutar la aplicación con Payara Micro:

```bash
mvn clean package
java -jar payara-micro.jar --deploy target/jwt-auth-example.war
```

**Configuración necesaria en Keycloak**:

1. Crear un realm y un client en Keycloak
2. Configurar roles y usuarios
3. Obtener la public key del realm (Realm Settings -> Keys -> RS256 Public Key)
4. Configurar el issuer URL (normalmente http://localhost:8080/auth/realms/your-realm)

**Para probar el endpoint**:

1. Obtener un token JWT de Keycloak
2. Hacer una petición con el token en el header Authorization:

```bash
curl -H "Authorization: Bearer YOUR_JWT_TOKEN" http://localhost:8080/api/protected
```

Recuerda reemplazar los valores de configuración (public key, issuer URL, client id, etc.) con los de tu instalación de Keycloak.

Este ejemplo asume que:
- Keycloak está corriendo en localhost:8080
- El client en Keycloak está configurado con access type "confidential"
- Los roles están correctamente asignados a los usuarios
- El protocolo OpenID Connect está habilitado

Para un entorno de producción, deberías:
- Configurar HTTPS
- Validar adicionalmente las claims del token
- Manejar adecuadamente los errores de autenticación
- Implementar refresh tokens si es necesario