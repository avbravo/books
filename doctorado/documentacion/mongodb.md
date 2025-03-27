# Agregar la aplicacion

# IP & USer para MongoDB en Servidor de Alcala


ip: 192.168.71.75:27017
 
user:gises_us
 
password: XcvTyH879*



# Usuario de base de datos

```
docker run --name mongodb-lastet -d -p 27017:27017 -e MONGODB_INITDB_ROOT_USERNAME=Patern8 -e MONGODB_INITDB_ROOT_PASSWORD=M19a$Rtea1sj123 -v mongodb_data_container:/data/db -d mongodb/mongodb-community-server:latest 

```




```json
{
  
  "actionHistory" : [{
      "fecha" : ISODate("2023-03-13T02:15:44.973Z"),
      "iduser" : NumberLong(1),
      "evento" : "crear",
      "clase" : "ProyectoFaces",
      "metodo" : "save"
    }],
  "active" : true,
  "applicative" : "Detección de Cancer",
  "applicativeprivilege" : [{
      "event" : "proyecto.autoridad",
      "create" : true,
      "read" : true,
      "update" : true,
      "delete" : true,
      "idrole" : NumberLong(1),
      "active" : true
    }, {
      "event" : "proyecto.colaborador",
      "create" : false,
      "read" : true,
      "update" : false,
      "delete" : false,
      "idrole" : NumberLong(7),
      "active" : true
    }],
  "applicativerole" : [{
      "idrole" : NumberLong(1),
      "active" : true,
      "path" : "scrum"
    }, {
      "idrole" : NumberLong(5),
      "active" : true,
      "path" : "scrum"
    }, {
      "idrole" : NumberLong(7),
      "active" : true,
      "path" : "scrum"
    }, {
      "idrole" : NumberLong(8),
      "active" : true,
      "path" : "scrum"
    }, {
      "idrole" : NumberLong(9),
      "active" : true,
      "path" : "scrum"
    }, {
      "idrole" : NumberLong(11),
      "active" : true,
      "path" : "scrum"
    }, {
      "idrole" : NumberLong(16),
      "active" : true,
      "path" : "scrum"
    }, {
      "idrole" : NumberLong(17),
      "active" : true,
      "path" : "scrum"
    }, {
      "idrole" : NumberLong(18),
      "active" : true,
      "path" : "scrum"
    }, {
      "idrole" : NumberLong(19),
      "active" : true,
      "path" : "scrum"
    }],
  "description" : "Deteccion de Cancer",
  "emailconfiguration" : [{
      "email" : "utp.azuero@utp.ac.pa",
      "mailSmtpAuth" : "true",
      "mailSmtpHost" : "smtp.office365.com",
      "mailSmtpPort" : "587",
      "mailSmtpStarttlsEnable" : "true",
      "password" : "80mOQZVruzxKq70wiPEu5Q==",
      "active" : true
    }],
  "idapplicative" : NumberLong(7),
  "image" : "",
  "path" : "/cancerdetector",
  "shortname" : "CANCER"
}


```



## Agregar Roles a Usuarios

```


```



## Cancer Detector

### Backup

```
echo '      mongodump [cancerdetector]' 
mongodump --archive=cancerdetector.gz --gzip --db=cancerdetector
```

### Restore
```
echo '      mongorestore [cancerdetector]' 
mongorestore --gzip --archive=cancerdetector.gz
```

###  Descargar desde docker

```
  docker cp d17ef4de2dba:/home/avbravo/cancerdetector.gz  /home/avbravo/Descargas/cancerdetector.gz
```


## Version 4.4.15



Se instalo sin user ni password
## Version 4.4.15

```
docker run --name mongodb-4.4.15 -d -p 27017:27017 -v mongodb_data_container:/data/db -d mongo:4.4.15

```
Ver la imagen
```
docker ps -a
```

Iniciar

```
docker start mongodb-4.4.15 
```


Ingresar

```
docker exec -it mongodb-4.4.15 bash
```


Autentificarse

```
mongo -u "Patern8" -p "M19a$Rtea1sj123"
```


subir archivos

```
docker cp cancerdetector.gz mongodb-4.4.15:/home

```

Seguir los pasos

```
docker exec -it mongodb-4.4.15 bash


mongorestore --gzip --archive=cancerdetector.gz
```

Verificar la restauracion

```
mongo

show dbs

use cancerdector

show collections


```
---
## Java

Los archivos estan almacenados en /home/utecpa/ProyectoUAH/jar


```
/usr/local/graalvm-community-openjdk-21.0.2+13.1/bin/java -jar -Xmx512m cancerdetectorserver.jar --noHazelcast --logo --port 9002 >>log.txt
```


```
/usr/local/graalvm-community-openjdk-21.0.2+13.1/bin/java -jar -Xmx512m cancerdetector.jar --noHazelcast --logo --port 8080 >>log.txt
```

---

## MAntenerlos en Ejecucion

presionar 

Ctrl + z en el terminal

Ejecuta los siguientes comandos:

disown -h %1 

Luego

bg 1 


### Eliminar los procesos

Pasos

Entrar al server

Ver el proceso store.jar

ps -x

Matar el proceso store.jar

kill #proceso 






---
# Version 4.4.15

```
```
docker run --name mongodb-4.4.15 -d -p 27017:27017 -e MONGO_INITDB_ROOT_USERNAME=Patern8 -e MONGO_INITDB_ROOT_PASSWORD=M19a$Rtea1sj123 -v mongodb_data_container:/data/db -d mongo:4.4.15
```
