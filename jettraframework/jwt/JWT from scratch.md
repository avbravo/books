
# JWT from scratch

Creating a JSON Web Token (JWT) from scratch in Java SE without relying on Jakarta EE or any external libraries involves manually implementing the JWT structure and encoding/decoding logic. A JWT consists of three parts: the header, the payload, and the signature, all encoded in Base64 URL-safe format.

Here’s a step-by-step guide to creating a JWT manually:

### 1. Structure of JWT
A JWT is composed of three parts:
- **Header**: Contains metadata about the token (e.g., algorithm used for signing).
- **Payload**: Contains claims (data) such as user information, expiration time, etc.
- **Signature**: Ensures the integrity of the token by signing the header and payload with a secret key.

The final JWT is structured as:
```
<base64-encoded-header>.<base64-encoded-payload>.<signature>
```

### 2. Implementation in Java SE

Below is a complete implementation of JWT creation in Java SE:

```java
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.Base64;

public class JwtCreator {

    // Method to create a JWT
    public static String createJWT(String payload, String secretKey) {
        // Step 1: Create Header
        String header = "{\"alg\":\"HS256\",\"typ\":\"JWT\"}";

        // Step 2: Encode Header and Payload using Base64 URL-safe encoding
        String encodedHeader = base64UrlEncode(header);
        String encodedPayload = base64UrlEncode(payload);

        // Step 3: Create Signature
        String dataToSign = encodedHeader + "." + encodedPayload;
        String signature = hmacSha256(dataToSign, secretKey);

        // Step 4: Combine all parts to form the JWT
        return encodedHeader + "." + encodedPayload + "." + signature;
    }

    // Helper method to perform Base64 URL-safe encoding
    private static String base64UrlEncode(String input) {
        return Base64.getUrlEncoder()
                     .withoutPadding()
                     .encodeToString(input.getBytes(StandardCharsets.UTF_8));
    }

    // Helper method to generate HMAC-SHA256 signature
    private static String hmacSha256(String data, String secretKey) {
        try {
            // Initialize the HMAC-SHA256 algorithm
            javax.crypto.Mac mac = javax.crypto.Mac.getInstance("HmacSHA256");
            javax.crypto.spec.SecretKeySpec secretKeySpec =
                    new javax.crypto.spec.SecretKeySpec(secretKey.getBytes(StandardCharsets.UTF_8), "HmacSHA256");
            mac.init(secretKeySpec);

            // Compute the hash
            byte[] hmacData = mac.doFinal(data.getBytes(StandardCharsets.UTF_8));

            // Encode the hash using Base64 URL-safe encoding
            return Base64.getUrlEncoder()
                         .withoutPadding()
                         .encodeToString(hmacData);
        } catch (Exception e) {
            throw new RuntimeException("Error generating HMAC-SHA256 signature", e);
        }
    }

    // Main method to test the JWT creation
    public static void main(String[] args) {
        // Example payload (JSON string)
        String payload = "{\"sub\":\"1234567890\",\"name\":\"John Doe\",\"iat\":1516239022}";

        // Secret key for signing the JWT
        String secretKey = "my-secret-key";

        // Create the JWT
        String jwt = createJWT(payload, secretKey);

        // Output the JWT
        System.out.println("Generated JWT: " + jwt);
    }
}
```

### 3. Explanation of the Code

#### Header
- The header specifies the algorithm (`HS256` for HMAC SHA-256) and the type of token (`JWT`).
- It is encoded using Base64 URL-safe encoding.

#### Payload
- The payload contains the claims (data) you want to include in the token.
- It is also encoded using Base64 URL-safe encoding.

#### Signature
- The signature is created by hashing the concatenated string of the encoded header and payload using HMAC-SHA256 with a secret key.
- The resulting hash is then Base64 URL-safe encoded.

#### Final JWT
- The final JWT is formed by concatenating the encoded header, encoded payload, and signature with periods (`.`).

### 4. Sample Output
When you run the program, it will output a JWT like this:
```
Generated JWT: eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiaWF0IjoxNTE2MjM5MDIyfQ.SflKxwRJSMeKKF2QT4fwpMeJf36POk6yJV_adQssw5c
```

### 5. Notes
- **Security**: Ensure that the secret key is kept secure and not exposed.
- **Validation**: This implementation only creates a JWT. To validate a JWT, you would need to decode it, verify the signature, and check the claims (e.g., expiration time).
- **Base64 URL-safe Encoding**: Standard Base64 encoding includes padding (`=`) and characters (`+`, `/`) that are not URL-safe. The `Base64.getUrlEncoder()` method ensures the output is URL-safe.

This implementation provides a basic understanding of how JWTs work and how they can be created manually in Java SE.
