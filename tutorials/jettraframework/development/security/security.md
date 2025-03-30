To bootstrap a Java SE application with Jakarta EE security, you need to integrate Jakarta EE security features into a standalone Java SE environment. Jakarta EE (formerly Java EE) is typically used in enterprise environments with application servers, but you can leverage some of its security features in a Java SE application by using appropriate libraries and configurations.

Here’s a step-by-step guide to bootstrap a Java SE application with Jakarta EE security:

---

### 1. **Understand Jakarta EE Security**
Jakarta EE security provides features like:
- **Authentication**: Verifying user identity.
- **Authorization**: Granting access to resources based on roles.
- **Data Integrity and Confidentiality**: Ensuring secure communication.
- **Annotations and APIs**: Such as `@RolesAllowed`, `@DenyAll`, `@PermitAll`, etc.

In Java SE, you won’t have an application server to handle these features, so you’ll need to implement them manually or use lightweight libraries.

---

### 2. **Add Dependencies**
To use Jakarta EE security features in a Java SE application, include the necessary libraries in your project. If you’re using Maven, add the following dependencies to your `pom.xml`:

```xml
<dependencies>
    <!-- Jakarta Security API -->
    <dependency>
        <groupId>jakarta.security.enterprise</groupId>
        <artifactId>jakarta.security.enterprise-api</artifactId>
        <version>3.0.0</version>
    </dependency>

    <!-- Jakarta Authentication API -->
    <dependency>
        <groupId>jakarta.authentication</groupId>
        <artifactId>jakarta.authentication-api</artifactId>
        <version>2.0.0</version>
    </dependency>

    <!-- Jakarta Authorization API -->
    <dependency>
        <groupId>jakarta.authorization</groupId>
        <artifactId>jakarta.authorization-api</artifactId>
        <version>2.0.0</version>
    </dependency>

    <!-- Optional: Use a lightweight security implementation like Soteria -->
    <dependency>
        <groupId>org.glassfish.soteria</groupId>
        <artifactId>javax.security.enterprise</artifactId>
        <version>1.1.0</version>
    </dependency>
</dependencies>
```

---

### 3. **Configure Security**
In a Java SE environment, you’ll need to manually configure security contexts, authentication, and authorization. Here’s an example of how to set up basic security:

#### **Authentication**
Create a custom `IdentityStore` to validate user credentials (e.g., username and password).

```java
import jakarta.security.enterprise.identitystore.DatabaseIdentityStoreDefinition;
import jakarta.security.enterprise.identitystore.IdentityStore;
import jakarta.security.enterprise.identitystore.Pbkdf2PasswordHash;
import jakarta.security.enterprise.credential.UsernamePasswordCredential;
import jakarta.security.enterprise.identitystore.CredentialValidationResult;

@DatabaseIdentityStoreDefinition(
    dataSourceLookup = "java:comp/DefaultDataSource",
    callerQuery = "SELECT password FROM users WHERE username = ?",
    groupsQuery = "SELECT role FROM user_roles WHERE username = ?"
)
public class CustomIdentityStore implements IdentityStore {

    @Inject
    private Pbkdf2PasswordHash passwordHash;

    public CredentialValidationResult validate(UsernamePasswordCredential credential) {
        // Validate username and password (e.g., against a database)
        String username = credential.getCaller();
        String password = credential.getPasswordAsString();

        // Example: Check if the user exists and the password matches
        if (isValidUser(username, password)) {
            return new CredentialValidationResult(username, getRoles(username));
        }
        return CredentialValidationResult.INVALID_RESULT;
    }

    private boolean isValidUser(String username, String password) {
        // Implement your logic to validate the user
        return true; // Replace with actual validation
    }

    private Set<String> getRoles(String username) {
        // Implement your logic to fetch roles
        return new HashSet<>(Arrays.asList("USER")); // Replace with actual roles
    }
}
```

#### **Authorization**
Use annotations like `@RolesAllowed` to enforce role-based access control.

```java
import jakarta.annotation.security.RolesAllowed;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;

@Path("/secure")
public class SecureResource {

    @GET
    @RolesAllowed("USER")
    public String secureEndpoint() {
        return "This is a secure endpoint!";
    }
}
```

---

### 4. **Initialize Security Context**
In Java SE, you need to manually initialize the security context. Use a library like **Soteria** (Jakarta Security implementation) to simplify this process.

```java
import jakarta.security.enterprise.SecurityContext;
import jakarta.security.enterprise.authentication.mechanism.http.BasicAuthenticationMechanismDefinition;
import jakarta.security.enterprise.identitystore.DatabaseIdentityStoreDefinition;

@BasicAuthenticationMechanismDefinition(
    realmName = "my-realm"
)
@DatabaseIdentityStoreDefinition(
    dataSourceLookup = "java:comp/DefaultDataSource",
    callerQuery = "SELECT password FROM users WHERE username = ?",
    groupsQuery = "SELECT role FROM user_roles WHERE username = ?"
)
public class SecurityConfig {

    @Inject
    private SecurityContext securityContext;

    public void initSecurity() {
        // Initialize security context
    }
}
```

---

### 5. **Run the Application**
Compile and run your Java SE application. Ensure that the security context is properly initialized and that authentication and authorization are enforced.

---

### 6. **Optional: Use a Lightweight Framework**
If you find it cumbersome to manually configure Jakarta EE security in Java SE, consider using a lightweight framework like **Apache Shiro** or **Spring Security**. These frameworks provide similar security features and are easier to integrate into standalone applications.

---

### Example Project Structure
```
src
├── main
│   ├── java
│   │   ├── com
│   │   │   └── example
│   │   │       ├── SecurityConfig.java
│   │   │       ├── CustomIdentityStore.java
│   │   │       └── SecureResource.java
│   ├── resources
│   │   └── application.properties
│   └── webapp
│       └── WEB-INF
│           └── beans.xml
pom.xml
```

---

### Key Points
- Jakarta EE security is designed for enterprise environments, but you can adapt it for Java SE.
- Use libraries like **Soteria** to simplify security implementation.
- Manually configure authentication and authorization in the absence of an application server.
- Consider lightweight alternatives like **Spring Security** or **Apache Shiro** for simpler setups.

Let me know if you need further clarification or additional examples!