# JettraServer

Utiliza JakartaEE, Microprofile, con VirtualTrhead desde JavaSE consume menos recursos.


[https://github.com/avbravo/jettraserver](https://github.com/avbravo/jettraserver)


mvn clean package -Pexec

java -jar target/jettraserver.jar


curl -X GET -i http://localhost:8080/employees


curl -X GET -i http://localhost:8080/hello

curl -X GET -i http://localhost:8080/employees/1

curl -X POST -i http://localhost:8080/employees -H 'Content-Type: application/json' -d '{"firstname":"Davor","lastname":"Suker", "jobTitle": "Electrician"}'


curl -X PUT -i http://localhost:8080/employees/2 -H 'Content-Type: application/json' -d '{"firstname": "Shawn", "lastname":"Michaels", "jobTitle": "Admin"}'


curl -X DELETE -i http://localhost:8080/employees/3 -H "Accept: application/json"

## Ejemplo

[CRUD REST API With Jakarta Core Profile Running on Java SE](https://dzone.com/articles/CRUD-REST-API-with-Jakarta-Core-Profile-running-on-Java-SE)

[Head Crashing Informatics](https://headcrashing.wordpress.com/tag/java-se-bootstrap-api/)

[Coding Microservice From Scratch (Part 16) | JAX-RS Done Right! | Head Crashing Informatics 83](https://headcrashing.wordpress.com/2023/11/19/coding-microservice-from-scratch-part-16-jax-rs-done-right-head-crashing-informatics-83/)

[Run your Jakarta Application without Runtime](https://www.atbash.be/2023/01/05/run-your-jakarta-application-without-runtime/)

[Run on Java SE](https://github.com/atbashEE/core-profile-demo/tree/main/se-run)

[Getting started with Jakarta RESTful Services](https://www.mastertheboss.com/jboss-frameworks/resteasy/getting-started-with-jakarta-restful-services/)

[Lee el archivo microprofile](https://github.com/atbashEE/core-profile-demo/tree/main/atbash-run)

[Bootstrapping a CDI Container in Java SE](https://jakarta.ee/learn/docs/jakartaee-tutorial/current/cdi/cdi-bootstrap-se8/cdi-bootstrap-se8.html)

uso

[https://github.com/atbashEE/core-profile-demo/blob/main/atbash-run/src/main/java/be/atbash/runtime/demo/rest/JakartaApplication.java](https://github.com/atbashEE/core-profile-demo/blob/main/atbash-run/src/main/java/be/atbash/runtime/demo/rest/JakartaApplication.java)

```java



public class JakartaApplication {

    public static void main(String[] args) {

        // This uses scanning of the package to discover the JAX-RS resources.
        JakartaSERunnerBuilder.newBuilder(DemoApplication.class)
                .withPort(8888)
                .addConfig("special", "fromCode")  // This is exposed as MP Config data
                .run();

        /*
        // Define the JAX-RS resources.
        JakartaSERunnerBuilder.newBuilder(HelloResource.class, PersonResource.class)
                .withPort(8888)
                .run();
        */

```



```java


EnableWebFluxSecurity
public class EmployeeWebSecurityConfig {

    // ...

    @Bean
    public SecurityWebFilterChain springSecurityFilterChain(
      ServerHttpSecurity http) {
        http.csrf().disable()
          .authorizeExchange()
          .pathMatchers(HttpMethod.POST, "/employees/update").hasRole("ADMIN")
          .pathMatchers("/**").permitAll()
          .and()
          .httpBasic();
        return http.build();
    }
}

```

`ç