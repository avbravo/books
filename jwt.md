# JWT


## Con PayaraMicro

Los proyecto se crearon mediante [https://start.payara.fish/](https://start.payara.fish/)
Pasos: 

1. Clonar proyecto 


[https://github.com/avbravo/jwt-payaramicro](https://github.com/avbravo/jwt-payaramicro)

2. Genere las llaves publicas y privadas

Llave privada

```shell
openssl req -newkey rsa:2048 -new -nodes -keyout privatekey.pem -out csr.pem

```

LLave publica

```shell
openssl rsa -in privatekey.pem -pubout > publickey.pem
```


3. Copie publickey.pem en src/main/resources del proyecto **serverjwt**

4. Copie privatekey.pem  en src/main/resources del proyecto **serverclient**

5. Ingrese al proyecto serverjwt y ejecute

```shell
mvn clean verify payara-micro:dev
```


6. Ingrese al proyecto serverclient y ejecute

 ```shell
mvn clean verify payara-micro:dev
```

7. Se muestra en el navegador [http://localhost:8080](http://localhost:8080])

Puede ingresar a las diversas opciones y puede ver los resultados


8. Observe en la pagina

 ![](jwt1.png)
 
De clic en **Call Secured endpoint with JWT in Authorization Header**

```

 <h3>JWT Auth</h3>
<a href="api/secured/test" target="_blank" >Call Secured endpoint with JWT in Authorization Header</a> <br/>

```

9. A modo de ejemplo colocamos la opción que el sistema muestre el JWT generado en la salida de serverjwt

![](jwt_token_generado.png)


* Copie el token que se genera para realizar las autentificaciones

  
   
10. Estudie del proyecto serverclient la clase TestSecureController que genera el JWT.

```java
package fish.payara.hello.secure;


import io.vertx.ext.auth.JWTOptions;
import io.vertx.ext.auth.PubSecKeyOptions;
import io.vertx.ext.auth.jwt.JWTAuth;
import io.vertx.ext.auth.jwt.JWTAuthOptions;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.WebTarget;
import jakarta.ws.rs.core.Response;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.UUID;
import org.eclipse.microprofile.config.inject.ConfigProperty;

@Path("/secured")
@ApplicationScoped
public class TestSecureController {

    private String key;
    @Inject @ConfigProperty(name="serviceb.url") String serviceB;

    @PostConstruct
    public void init() {
        key = readPemFile();
    }

    @GET
    @Path("/test")
    public String testSecureCall() {
        
   if (key == null) {
            throw new WebApplicationException("Unable to read privateKey.pem", 500);
        }
        
        String jwt = generateJWT(key);
        
        System.out.println("\tJWT Generado es "+jwt);
        WebTarget target = ClientBuilder.newClient().target(serviceB);
        Response response = target.request().header("authorization", "Bearer " + jwt).buildGet().invoke();
        return String.format("Claim value within JWT of 'custom-value' : %s", response.readEntity(String.class));
    }

    private static String generateJWT(String key) {
        JWTAuth provider = JWTAuth.create(null, new JWTAuthOptions()
                .addPubSecKey(new PubSecKeyOptions()
                        .setAlgorithm("RS256")
                        .setSecretKey(key)
                ));

        MPJWTToken token = new MPJWTToken();
        token.setAud("targetService");
        token.setIss("https://server.example.com");  // Must match the expected issues configuration values
        token.setJti(UUID.randomUUID().toString());
        token.setSub("Jessie");  // Sub is required for WildFly Swarm
        token.setUpn("Jessie");
        token.setPreferredUsername("Jessie");
        token.setIat(System.currentTimeMillis());
        token.setExp(System.currentTimeMillis() + 30000); // 30 Seconds expiration!
        token.addAdditionalClaims("custom-value", "Jessie specific value");
        token.setGroups(Arrays.asList("user", "protected"));

        return provider.generateToken(new io.vertx.core.json.JsonObject().mergeIn(token.toJSONString()), new JWTOptions().setAlgorithm("RS256"));
    }

    // NOTE:   Expected format is PKCS#8 (BEGIN PRIVATE KEY) NOT PKCS#1 (BEGIN RSA PRIVATE KEY)
    // See gencerts.sh
    private static String readPemFile() {
        StringBuilder sb = new StringBuilder(8192);
        try (BufferedReader is = new BufferedReader(
                new InputStreamReader(
                        TestSecureController.class.getResourceAsStream("/privateKey.pem"), StandardCharsets.US_ASCII))) {
            String line;
            while ((line = is.readLine()) != null) {
                if (!line.startsWith("-")) {
                    sb.append(line);
                    sb.append('\n');
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return sb.toString();
    }
}

```

## Otra ventana de navegador

Si intentamos verificar el endpoint protegido desde otra ventana del navegador

```
http://localhost:9011/api/protected
```

No muestra el mensaje de error de acceso

![](jwt_secure.png)




---

## JWT.IO

Para ver el JWT dirigase a [jwt.io](jwt.io)


## Curl

Puede probarlo mediante curl con el formato

```
curl -H "Authorization: Bearer {token_jwt}" http://localhost:9011/api/protected
```
Reemplace {token_jwt} por el token generado

* Tenga presente que el token tiene un tiempo de validez que se establece en        **token.setExp(System.currentTimeMillis() + 30000); // 30 Seconds expiration!**


```
curl -H "Authorization: Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiJ9.eyJpc3MiOiJodHRwczovL3NlcnZlci5leGFtcGxlLmNvbSIsImF1ZCI6InRhcmdldFNlcnZpY2UiLCJqdGkiOiI5NGE1YmZmNS0xOGU1LTQ4ZDItOGVlYy00NDUzZTMwMWQ4NTkiLCJleHAiOjE3MjA1NTkwNTUsImlhdCI6MTcyMDU1ODIyNSwic3ViIjoiSmVzc2llIiwidXBuIjoiSmVzc2llIiwicHJlZmVycmVkX3VzZXJuYW1lIjoiSmVzc2llIiwiY3VzdG9tLXZhbHVlIjoiSmVzc2llIHNwZWNpZmljIHZhbHVlIiwiZ3JvdXBzIjpbInVzZXIiLCJwcm90ZWN0ZWQiXX0.Oh6fnsjBbrC7ChWV1N-KG_7tvbhxQ_DIOYWKjAX_kife_wv68TpWyd3dg-EbtQYvgXDHsHkmjUt-r1N2lQMEqJAjO1faDAmq-y4CS3Gcv81p-0W1kJV79sjAzKCkxw0JwMXnXWkf9QEH97m7P9tp47L2lkvEQW64PaUZm1ekMIJZra_3RqFB3dY0aYGjhORbcpUGEISP9WJN5VM7MmKYM2VDTgInyFABwQYuF2FbhCitpvNy1cDP_YKGwGffApngeSHGNnFlCOXX2xCNCfgFrUWTrcLnUoJCw7CuCcXjHf1fxiV9WbKtm1NcI2T0h5GrF6_FtuxXfvNX8sEW3g2YDg" http://localhost:9011/api/protected
```


## Postman

Si desea probarlo con postman

* En autorization indique Bearen Token
* En Token pegue el token
* 

![](jwt_postman.png)

---


## Quarkus

[Authentication and Authorization Using JWT on Quarkus](https://ard333.medium.com/authentication-and-authorization-using-jwt-on-quarkus-aca1f844996a)

```
Create Public and Private Key
For unix-like OS you can run this command on terminal, for private key

openssl req -newkey rsa:2048 -new -nodes -keyout privatekey.pem -out csr.pem
for public key

openssl rsa -in privatekey.pem -pubout > publickey.pem
then, copy privatekey.pem and publickey.pem to resources folder (src/main/resources).

3. Config Project
Add some config to application.properties.

mp.jwt.verify.publickey.location=publickey.pem
mp.jwt.verify.issuer=https://ard333.com
quarkus.smallrye-jwt.enabled=true
# for jwt expiration duration
com.ard333.quarkusjwt.jwt.duration=3600
```

* [Quarkus Mastery: Unbeatable JWT & RBAC Security!](https://medium.com/deutsche-telekom-gurgaon/quarkus-mastery-unbeatable-jwt-rbac-security-e3ab880403a3)

* [MP  —  JWT Authentication](https://helidon.io/docs/v4/mp/jwt)

* [JWT Token authentication with Quarkus](https://www.linkedin.com/pulse/jwt-token-authentication-quarkus-ahmed-abd-el-aziz/)

<details><summary>JWT con Java</summary>



<p>

* [JSON Web Tokens](https://auth0.com/docs/secure/tokens/json-web-tokens)

* [Guía de Implementación: JWT para la Autenticación en Java]([https://docs.payara.fish/enterprise/docs/documentation/microprofile/jwt.html](https://medium.com/somos-pragma/gu%C3%ADa-de-implementaci%C3%B3n-jwt-para-la-autenticaci%C3%B3n-en-java-db47b04eda54))


</p>

</details>


<details><summary>Payara</summary>

<p>

* [Payara Micro Eclipse MicroProfile JWT Authentication API](https://docs.payara.fish/enterprise/docs/documentation/microprofile/jwt.html)

* [ejemplos](https://github.com/lreimer/jakartaee-jwt-security)

</p>

</details>

<details><summary>Videos</summary>

<p>

* [Comunicación entre microservicios usando Kafka/ Servicios HTTP](https://www.youtube.com/watch?v=c3Wa2hdTjK4)

</p>

</details>


<details>
  <summary>Helidon</summary>

<p>

* [JWT Authentication](https://helidon.io/docs/v4/mp/jwt)
  
* [JWT Token authentication with Quarkus](https://www.linkedin.com/pulse/jwt-token-authentication-quarkus-ahmed-abd-el-aziz/)
  
</p>

</details>
