

## Test de carga

```shell

siege http://localhost:8080/users -c 250 -r 50


```

---

## Crear imagen de docker

Pasos:

0. Cree el archivo Dockerfile

``FROM ghcr.io/graalvm/jdk-community:21

# Establecer el directorio de trabajo dentro del contenedor
WORKDIR /app

# Copiar el archivo JAR compilado al contenedor
COPY target/jettraframeworkclient-jar-with-dependencies.jar /app/jettraframeworkclient-jar-with-dependencies.jar

# Comando para ejecutar la aplicación
CMD ["java", "-jar", "jettraframeworkclient-jar-with-dependencies.jar"]


```

1. Cree el jar del proyecto


```shell

mvn clean package

---

2. Convertirlo en imagen de docker

```shell
docker build -t jettra .

```

3. Ejecute el proyecto mediante

```shell
docker run -p 8080:8080 --name jettra jettra


```

4. Verifiquelo desde el navegador

```shell
http://localhost:8080/users

```

5. Detener el contenedor

```shell
docker stop jettra


```

6. Inciar  el contenedor

```shell
docker start jettra


```