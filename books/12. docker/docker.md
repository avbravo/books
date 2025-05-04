# Docker

## Docker from Source

```
sudo apt update -y
sudo apt install docker.io -y
curl -L "https://github.com/docker/compose/releases/download/v2.23.3/docker-compose-$(uname -s)-$(uname -m)" -o /usr/local/bin/docker-compose

chmod +x /usr/local/bin/docker-compose

docker-compose --version
 
```
# Docker en Ubuntu 24.04
[Instalación de Docker en Ubuntu 24.04 LTS](https://cursosdedesarrollo.com/2024/04/instalacion-de-docker-en-ubuntu-24-04-lts/)

---

Instalacion snap


1. Para llevar a cabo la instalación, por favor ejecute los siguientes comandos:

```shell

 sudo snap install docker



```
 
3. Para autorizar a los usuarios a ejecutar comandos Docker, por favor ingrese el siguiente comando:
 
```shell
    sudo groupadd docker

    sudo usermod -aG docker ${USER}
```

4. Asignar los permisos correspondientes al grupo Docker:
 
```shell
    su - ${USER}
```
 
5. Compruebe que el usuario pertenece al grupo Docker:"
 
```shell
    id -nG
```

---
# status

 systemctl status docker



---
## Volumenes

[Docker + Mongo + Volúmenes](https://www.lemoncode.tv/curso/docker-y-mongodb/leccion/docker-volumenes-mongodb)

* Ver volumenes
  
```
docker volume ls
```

* Crear un volumen
  
```shell

docker volume create mongodb_data_container

```

* Eliminar un volumen

```
docker volume rm  nombrevolumen

```

* Borrar todos los volumenes

```
docker volume prune
```

---

## Cambiar password de un contenedor

* Ejecutar el siguiente comando, se activa el prompt para ingresar el nuevo password
* 
```
docker exec -itu 0 {contenedor} passwd

```

entrar al contenedor y ejecutar

```shell
sudo su
```



# Eliminar imagens

```
docker images

docker rmi <nombreimagne>

```


# Eliminar contenedores

```
docker ps -a

docker rm <nombrecontenedor>


```
----
# Hello word

docker run hello-world




# NginX
---
[How To Run Nginx in a Docker Container on Ubuntu 22.04](https://www.digitalocean.com/community/tutorials/how-to-run-nginx-in-a-docker-container-on-ubuntu-22-04)


---
 # Crear una imagen de Docker de una aplicación Java 

 Para crear un programa en Java que genere una imagen de Docker, primero necesitas tener un proyecto Java básico. Luego, puedes crear un `Dockerfile` que describa cómo construir la imagen de Docker para tu aplicación Java. A continuación, te muestro un ejemplo paso a paso:

### Paso 1: Crear un proyecto Java simple

Supongamos que tienes un proyecto Java simple con la siguiente estructura:

```
mi-proyecto-java/
│
├── src/
│   └── Main.java
│
└── Dockerfile
```

El archivo `Main.java` podría ser algo tan simple como:

```java
public class Main {
    public static void main(String[] args) {
        System.out.println("Hola, Docker!");
    }
}
```

### Paso 2: Crear el Dockerfile

El `Dockerfile` es un archivo de texto que contiene las instrucciones para construir la imagen de Docker. Aquí tienes un ejemplo de un `Dockerfile` para una aplicación Java:

```dockerfile
# Usar una imagen base con JDK preinstalado
FROM openjdk:11-jre-slim

# Establecer el directorio de trabajo dentro del contenedor
WORKDIR /app

# Copiar el archivo JAR compilado al contenedor
COPY target/my-app.jar /app/my-app.jar

# Comando para ejecutar la aplicación
CMD ["java", "-jar", "my-app.jar"]
```

### Paso 3: Compilar el proyecto Java

Antes de construir la imagen de Docker, necesitas compilar tu proyecto Java y generar un archivo JAR. Puedes hacerlo con Maven, Gradle o manualmente con `javac` y `jar`.

Si estás usando Maven, puedes compilar y empaquetar tu proyecto con:

```bash
mvn clean package
```

Esto generará un archivo JAR en el directorio `target/`.

### Paso 4: Construir la imagen de Docker

Una vez que tengas el archivo JAR, puedes construir la imagen de Docker. Navega al directorio donde se encuentra tu `Dockerfile` y ejecuta:

```bash
docker build -t mi-aplicacion-java .
```

Esto construirá una imagen de Docker llamada `mi-aplicacion-java`.

### Paso 5: Ejecutar el contenedor

Finalmente, puedes ejecutar un contenedor a partir de la imagen que acabas de construir:

```bash
docker run mi-aplicacion-java
```

Esto debería imprimir "Hola, Docker!" en la consola.

### Resumen

1. **Crea un proyecto Java simple**.
2. **Escribe un `Dockerfile`** para definir cómo construir la imagen.
3. **Compila tu proyecto Java** y genera un archivo JAR.
4. **Construye la imagen de Docker** usando el `Dockerfile`.
5. **Ejecuta el contenedor** para ver tu aplicación en acción.

Este es un ejemplo básico, pero puedes personalizar el `Dockerfile` según las necesidades de tu aplicación, como agregar dependencias, configurar variables de entorno, exponer puertos, etc.


---
# Desintalar Docker


[How to Uninstall Docker on Ubuntu? Step-by-Step Guide](https://www.cherryservers.com/blog/how-to-uninstall-docker)

* Detener contenedores

docker stop -t 30 [container name or ID]

* Eliminar contenedores

docker rm [container name or ID]


* Remover las imagenes no usadas

docker system prune --volumes


* Detener docker

  sudo systemctl stop docker

* Liberar espacio

sudo apt-get purge docker.io -y

* Remover la instalacion

sudo apt-get autoremove --purge docker-io -y

* Remover directorio

rm -rf /etc/docker

* remover daemon

sudo rm -rf /var/lib/docker

* Testing estatus

    sudo systemctl status docker
