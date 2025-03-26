
./mvnw quarkus:add-extension -Dextensions="container-image-docker"

Builds a Docker image running as a standard JVM application.

mvn clean package -Ddocker
docker run -i --rm -p 8080:8080 melloware/quarkus-faces:latest

Recuerde que Quarkus posee sus propias implementaciones de 