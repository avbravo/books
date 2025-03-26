# Capítulo 17

En este capítulo trata sobre 


Crear proyecto con starter

https://start.jakarta.ee/


#Maven

[https://stackoverflow.com/questions/6819888/how-to-run-all-tests-in-a-particular-package-with-maven](https://stackoverflow.com/questions/6819888/how-to-run-all-tests-in-a-particular-package-with-maven)
saltarse los test

```
mvn package -DskipTests

```


## Surfire Test Report

En el capitulo 16 se muestra un ejemplo de como implementar los test.


[https://maven.apache.org/surefire/maven-surefire-report-plugin/usage.html](https://maven.apache.org/surefire/maven-surefire-report-plugin/usage.html)


mvn site 

mvn surefire-report:report  


[https://maven.apache.org/surefire/maven-surefire-report-plugin/examples/report-custom-location.html](https://maven.apache.org/surefire/maven-surefire-report-plugin/examples/report-custom-location.html)


pasos 
1. capitulo 16 se uso

agregue al archivo pom.xml

```
 <reporting>
        <plugins>
            <plugin>
                <groupId>org.apache.maven.plugins</groupId>
                <artifactId>maven-surefire-report-plugin</artifactId>
                <version>3.0.0-M7</version>
                <configuration>
                  
                    <showSuccess>true</showSuccess>
                    <outputDirectory>target/surefire-reports</outputDirectory>
                    <linkXRef>false</linkXRef>
                </configuration>    
      
            </plugin>
      
            <plugin>
                <groupId>org.apache.maven.plugins</groupId>
                <artifactId>maven-site-plugin</artifactId>
                <version>4.0.0-M3</version>
                <configuration>
                    <outputDirectory>${basedir}/target/surefire-reports</outputDirectory>
                </configuration>
            </plugin>

        </plugins>
    </reporting>

```


Ejecute desde consola

```
mvn site

```
y

```
mvn surefire-report:report  
```

puede observar que se genera en la carpeta target un carpeta site que contiene el archivo html del reporte

y la carpeta sufire-reports que contiene informe de los test



## Testing

[How to Test your Applications in Payara Server](https://blog.payara.fish/how-to-test-your-applications-in-payara-server?utm_content=227856298&utm_medium=social&utm_source=twitter&hss_channel=tw-2599580401)


[Piranha Cloud ofrece ejemplos](https://piranha.cloud/core-profile/guides/)


[How to Test your Applications in Payara Server](https://blog.payara.fish/how-to-test-your-applications-in-payara-server?utm_content=227542447&utm_medium=social&utm_source=twitter&hss_channel=tw-2599580401)

[Testing Jakarta EE 8 Applications](https://medium.com/swlh/testing-jakarta-ee-8-applications-9ca250da20e3)


[https://github.com/primefaces/primefaces-test/blob/master/pom.xml](https://github.com/primefaces/primefaces-test/blob/master/pom.xml)

JUnit
Arquillian

Microshell Testing

https://blog.payara.fish/integration-testing-using-microprofile-testing-and-payara-micro


[Testing Helidon with TestNG](https://medium.com/helidon/testing-helidon-with-testng-c94b9f2d8db5)

## ArchUnit
https://www.infoq.com/news/2022/10/archunit/

https://www.archunit.org/userguide/html/000_Index.html

## JHM



## Testing

En este capitulo mencionaremos como implementar Testing en nuestras aplicaciones
[https://medium.com/swlh/testing-jakarta-ee-8-applications-9ca250da20e3](https://medium.com/swlh/testing-jakarta-ee-8-applications-9ca250da20e3)

[Testing Jakarta EE 9 Applications with Arquillian and Payara 6](https://itnext.io/testing-jakarta-ee-9-applications-with-arquillian-and-payara-6-52fd153d8d9)
[Integration Testing with Payara Micro](https://www.devwithimagination.com/2019/09/03/integration-testing-with-payara-micro/)
[MicroProfile generated Application](https://github.com/dhutchison/microprofile-experiments)

[PayaraMicro Arquillian Demo](https://github.com/ospringauf/PayaraMicroArquillianDemo)

[Victor Orozco](https://www.youtube.com/watch?v=wR_52fnHFDs)

[Payara](https://docs.payara.fish/community/docs/5.201/documentation/ecosystem/arquillian-containers/payara-micro.html)

[Ejemplo](https://itnext.io/testing-jakarta-ee-9-applications-with-arquillian-and-payara-6-52fd153d8d9)

[jakartaee9-starter-boilerplate](https://github.com/hantsy/jakartaee9-starter-boilerplate)

[jpa ](https://www.apuntesdejava.com/2018/01/pruebas-unitarias-jpa-y-servicios-rest.html)
[Rest Diego Silva](https://www.apuntesdejava.com/2018/02/pruebas-unitarias-jpa-y-servicios-rest.html)

