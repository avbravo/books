
# Para implementar el framework en Quarkus

En el archivo pom.xml elimine

```xml

       <dependency>
            <groupId>org.mongodb</groupId>
            <artifactId>mongodb-driver-sync</artifactId>
            <version>4.0.6</version>
            <version>4.6.0</version>
        </dependency>

       <dependency>
            <groupId>org.glassfish.jersey.core</groupId>
            <artifactId>jersey-common</artifactId>
            <version>3.1.0-M3</version>
            <type>jar</type>
        </dependency>
```

agregue

```xml
            <dependency>
            <groupId>io.quarkus</groupId>
            <artifactId>quarkus-mongodb-client</artifactId>
        </dependency>




```

