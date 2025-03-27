# Vaginalia

Pasos: 

1. Crear el proyecto desde PayaraStarted seleccionando CoreProfile

2. Dar permisos chmod 775 a mvnw y mvnw.cmd



5. 

 [https://github.com/avbravo/vaginalia.git](https://github.com/avbravo/vaginalia.git)


3. Execute the following command:

```
./mvn clean package payara-micro:dev
```

4. Once the runtime starts, you can access the project at http://localhost:8080/


## Building a Docker Image
To build a Docker image for this application follow these steps:

Open a terminal and navigate to the project's root directory. Make sure you have Docker installed and running on your system.
Execute the following Maven command to build the Docker image:

```
mvn docker:build
```

This command will build a Docker image for your application.

Once the image is built, you can run a Docker container from the image using the following command:

```
docker run -p 8080:8080 vaginalia:${project.version}
```
Replace vaginalia:${project.version} with the actual image name and tag.

That's it! You have successfully built and run the application in a Docker container.




# Package

mvn clean package

# Crear Uber Jar

#Crear  el Uberjar
java -jar /home/avbravo/software/payara/payara-micro-6.2025.3.jar --deploy /home/avbravo/NetBeansProjects/u/alcala/vaginalia/target/vaginalia-0.1-SNAPSHOT.war --outputUberJar /home/avbravo/Descargas/vaginalia.jar 


# Ejecutar

java -jar /home/avbravo/Descargas/vaginalia.jar --port 8080