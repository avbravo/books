
# MongoDB

```shell

sudo mkdir -p /data/db

sudo chmod 777 /data/db

sudo mkdir -p /var/log/mongodb

```


Instalar MongoDB Con credenciales
```shell
docker pull mongodb/mongodb-community-server:latest


docker run --name mongodb -d -p 27017:27017 -e MONGO_INITDB_ROOT_USERNAME=avbravo  -e MONGO_INITDB_ROOT_PASSWORD=denver16 mongodb/mongodb-community-server:latest  -v mongodb_data_container:/data/db 

docker run --name mongodb-credenciales -d -p 27017:27017 -e MONGODB_INITDB_ROOT_USERNAME=avbravo  -e MONGODB_INITDB_ROOT_PASSWORD=denver16 mongodb/mongodb-community-server:8.0-ubi8  -v mongodb_data_container:/data/db 


docker ps -a
docker exec -it mongodb bash
Autenticarse con usuario y password
mongosh --username myusername --password mypassword
show dbs

docker stop mongodb
docker rm mongodb

```


---


# Historia
[MongoDB Evolved – Version History](https://www.mongodb.com/resources/products/mongodb-version-history)

# Instalación MongoDB


[mongodb/mongodb-community-serve](https://hub.docker.com/r/mongodb/mongodb-community-server/tags)


## Instalar

[Install MongoDB Community with Docker](https://www.mongodb.com/docs/manual/tutorial/install-mongodb-community-with-docker/)


```shell

sudo mkdir -p /data/db

sudo chmod 777 /data/db

sudo mkdir -p /var/log/mongodb

```

## Sugerencias
<p>
Si genera errores al instalar verifique que no existe el volumen que desea instalar mongodb_data_container.
 
</p>

---

## Bitnami

[https://hub.docker.com/r/bitnami/mongodb](https://hub.docker.com/r/bitnami/mongodb)

```
docker pull bitnami/mongodb:latest
```

```
docker pull bitnami/mongodb:latest -e MONGODB_INITDB_ROOT_USERNAME=gises_us -e MONGODB_INITDB_ROOT_PASSWORD=XcvTyH879* -v mongodb_data_container:/data/db -d mongodb/mongodb-
```

---


## Lasted Version


```shell
docker pull mongodb/mongodb-community-server:latest

```

## Ultima versión sin credenciales de acceso

```shell
docker run -d --name mongodb-latest -p 27017:27017 -v mongodb_data_container:/data/db -d mongodb/mongodb-community-server:latest 

```

## Ultima versión con credenciales de acceso

```shell
docker run --name mongodb-latest -p 27017:27017 -e MONGODB_INITDB_ROOT_USERNAME=user -e MONGODB_INITDB_ROOT_PASSWORD=pass -v mongodb_data_container:/data/db -d mongodb/mongodb-community-server:latest 
```
## Ultima versión con credenciales de acceso avbravo

```shell
docker run --name mongodb-latest  -p 27017:27017 -e MONGODB_INITDB_ROOT_USERNAME=avbravo -e MONGODB_INITDB_ROOT_PASSWORD=denver16 -v mongodb_data_container:/data/db -d mongodb/mongodb-community-server:latest  
```

---
### version  4.4

```shell
docker run -d -p 27017:27017 -it -v mongodb_data_container:/data/db --name mongodb4.4 mongo:4.4
```

---
###  versión MongoDB 5.0

```shell
docker run --name mongodb5.0 -p 27017:27017 -d mongodb/mongodb-community-server:5.0-ubuntu2004 -v mongodb_data_container:/data/db
```

---
## Con user y password

```shell
docker run --name mongodb5.0 -p 27017:27017 -d mongodb/mongodb-community-server:5.0-ubuntu2004 -v mongodb_data_container:/data/db -e MONGODB_INITDB_ROOT_USERNAME=myuser -e MONGODB_INITDB_ROOT_PASSWORD=mypassword
```

---


## Verificar la imagen el contenedor

```shell
docker container ls
```


Luego para iniciar el contenedor utilice si no se ha ejecutado

```
docker start mongodb-lastet
```


# Ingresar a la imagen

```shell
 docker exec -it mongodb-lastet bash
```

## Ejecutar mongosh

## Sin password

```shell
mongosh
```

## Con credenciales

```shell

mongosh --username user --password pass

```

mongosh --username admin --password denver16

## Ver las bases de datos

```shell
show dbs
```


## Con ReplicaSet
```shell
docker run -p 27017:27017 -d mongodb/mongodb-community-server:latest --name mongodb --replSet myReplicaSet
```

---

## Imagen con permisos restringidos

* Cambiar el password del contenedor

```shell
docker exec -itu 0 {contenedor} passwd

```

* Ingresar al contenerdo

```shell

docker exec -it {contenedor} bash
```
* Ingresar como super usuario

```shell
su
```
Se solicita la contraseña

* Crear directorio de trabajo

```
cd home

mkdir avbravo
```
---
## Renombrar una colección

```
db.medicion_2_Noviembre.renameCollection("medicion_2_noviembre") 
```


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
---



<details>
<summary>Cluster</summary>

<p>


[Deploying a MongoDB Cluster with Docker](https://www.mongodb.com/resources/products/compatibilities/deploying-a-mongodb-cluster-with-docker)


Create a Docker Network

```
docker network create mongoCluster
```

Start MongoDB Instances

```
docker run -d --rm -p 27017:27017 --name mongo1 --network mongoCluster mongo:5 mongod --replSet myReplicaSet --bind_ip localhost,mongo1
```

Inicie los otros contenedores

```
docker run -d --rm -p 27018:27017 --name mongo2 --network mongoCluster mongo:5 mongod --replSet myReplicaSet --bind_ip localhost,mongo2
 
docker run -d --rm -p 27019:27017 --name mongo3 --network mongoCluster mongo:5 mongod --replSet myReplicaSet --bind_ip localhost,mongo3
```

## Indique el ReplicaSet

```
docker exec -it mongo1 mongosh --eval "rs.initiate({
 _id: \"myReplicaSet\",
 members: [
   {_id: 0, host: \"mongo1\"},
   {_id: 1, host: \"mongo2\"},
   {_id: 2, host: \"mongo3\"}
 ]
})"

```
devuelve el mensaje

```
{ ok: 1 }
```

## Test and Verify the Replica Set

```
docker exec -it mongo1 mongosh --eval "rs.status()"
```

En este ejemplo puede conectarse a las diversos contenedores e imagenes puerto 27017, 20018, 27019 y observada que los replica set se actualizan de manera automatica.




[Deploy a MongoDB Cluster with Docker](https://dev.to/mattdark/deploy-a-mongodb-cluster-with-docker-1fal)

</p>

</details>



<details>
<summary>Sharded</summary>

<p>

[A Comprehensive Guide To Understanding MongoDB Sharding](https://kinsta.com/blog/mongodb-sharding/)

[Demystifying MongoDB Sharding](https://medium.com/@parvjn616/demystifying-mongodb-sharding-255983d376e1)  

[Demystifying Sharding in MongoDB (MongoDB World 2022)](https://www.youtube.com/watch?v=EvzPncoCr_M)
  
[M103: Basic Cluster Administration](https://learn.mongodb.com/courses/m103-basic-cluster-administration)
  
[Setting Up a MongoDB Sharded Cluster: A Step-by-Step Guide](https://medium.com/@parvjn616/setting-up-a-mongodb-sharded-cluster-a-step-by-step-guide-20cbba33c0e8)

[MongoDB Replication](https://www.mongodb.com/resources/products/capabilities/replication)
</p>

</details>


# Indices

```java
db.analisis.createIndex({ "idanalisis": 1 }) 



MongoClient mongoClient = new MongoClient();
MongoDatabase database = mongoClient.getDatabase("yourDatabaseName");
MongoCollection<Document> collection = database.getCollection("yourCollectionName");

// Create an ascending index on the "name" field
collection.createIndex(Indexes.ascending("name"));

```

[MongoDB Index](https://www.mongodb.com/docs/drivers/java/sync/current/fundamentals/builders/indexes/)


[Indexes]([https://www.mongodb.com/docs/drivers/java/sync/v4.3/fundamentals/indexes/#:~:text=The%20MongoDB%20Java%20Driver%20provides,different%20MongoDB%20Index%20key%20types.)



Java drivers 


<details>
<summary>Performance</summary>

<p>

[Identifying MongoDB Performance Pitfalls](https://dev.to/romanright/identifying-mongodb-performance-pitfalls-5302)

[12 Patterns for Extreme MongoDB Performance and Scalability ](https://www.youtube.com/watch?v=2ZRbJnaIqAI)

</p>

</details>


<details>
<summary>MongoDB 8.0</summary>
<p>

</p>
</details>


[How Stripe’s document databases supported 99.999% uptime with zero-downtime data migrations](https://stripe.com/blog/how-stripes-document-databases-supported-99.999-uptime-with-zero-downtime-data-migrations?user_id=65bbc84362f442011db351e0&sn_type=TWITTER&cpost_id=66674ab0c14cb81089f87725&post_id=13752706375&asset_id=ADVOCACY_205_66673497aad9714ce1a899c6)

# Bases datos vectoriales 

[¿Qué son las bases de datos vectoriales](https://www.mongodb.com/es/resources/basics/vector-databases)


---
<details>
<summary>Using the XFS filesystem </summary>

<p>
 Using the XFS filesystem is strongly recommended with the WiredTiger storage engine. See

Configure MongoDB para producción

[http://dochub.mongodb.org/core/prodnotes-filesystem](http://dochub.mongodb.org/core/prodnotes-filesystem)

</p>

</details>
 

---
<details>
<summary>Error</summary>

<p>

 Error

[nertworkweb/mongodb-no-avx](https://hub.docker.com/r/nertworkweb/mongodb-no-avx/tags)

</p>

</details>



---

<details>
<summary>GridFS almacenamiento de archivos</summary>

<p>

 GridFS almacenamiento de archivos

 [GridFS](https://www.mongodb.com/docs/drivers/java/sync/current/fundamentals/gridfs/)

```java
String filePath = "/path/to/project.zip";
try (InputStream streamToUploadFrom = new FileInputStream(filePath) ) {
    // Defines options that specify configuration information for files uploaded to the bucket
    GridFSUploadOptions options = new GridFSUploadOptions()
            .chunkSizeBytes(1048576)
            .metadata(new Document("type", "zip archive"));
    // Uploads a file from an input stream to the GridFS bucket
    ObjectId fileId = gridFSBucket.uploadFromStream("myProject.zip", streamToUploadFrom, options);
    // Prints the "_id" value of the uploaded file
    System.out.println("The file id of the uploaded file is: " + fileId.toHexString());
}
```


* [How to Store Huge Media Files in Mongo Database](https://www.youtube.com/watch?v=Mq1CjEyFd88)

</p>

</details>

## MongoDB 8.0 Docker noble

Verifique el sitio:
[https://hub.docker.com/_/mongo](https://hub.docker.com/_/mongo)

Agregue


```shell
 docker pull mongo:noble
```



Ejecutar

```shell
docker run -d --name mongodb8.0-noble -p 27017:27017 -v mongodb_data_container:/data/db -d mongo:noble 
```

---

# Instalar MongoDB Community Edition en Ubuntu

[Install MongoDB Community Edition on Ubuntu](https://www.mongodb.com/docs/manual/tutorial/install-mongodb-on-ubuntu/)

nstall MongoDB Community Edition
Follow these steps to install MongoDB Community Edition using the apt package manager.

1
Import the public key.
From a terminal, install gnupg and curl if they are not already available:

sudo apt-get install gnupg curl

To import the MongoDB public GPG key, run the following command:

curl -fsSL https://www.mongodb.org/static/pgp/server-8.0.asc | \
   sudo gpg -o /usr/share/keyrings/mongodb-server-8.0.gpg \
   --dearmor

2
Create the list file.
Create the list file /etc/apt/sources.list.d/mongodb-org-8.0.list for your version of Ubuntu.


Ubuntu 24.04 (Noble)

Ubuntu 22.04 (Jammy)

Ubuntu 20.04 (Focal)
Create the list file for Ubuntu 24.04 (Noble):

echo "deb [ arch=amd64,arm64 signed-by=/usr/share/keyrings/mongodb-server-8.0.gpg ] https://repo.mongodb.org/apt/ubuntu noble/mongodb-org/8.0 multiverse" | sudo tee /etc/apt/sources.list.d/mongodb-org-8.0.list

3
Reload the package database.
Issue the following command to reload the local package database:

sudo apt-get update

4
Install MongoDB Community Server.
You can install either the latest stable version of MongoDB or a specific version of MongoDB.


Latest Release

Specific Release
To install the latest stable version, issue the following

sudo apt-get install -y mongodb-org

For help with troubleshooting errors encountered while installing MongoDB on Ubuntu, see our troubleshooting guide.

Run MongoDB Community Edition
ulimit Considerations
Most Unix-like operating systems limit the system resources that a process may use. These limits may negatively impact MongoDB operation, and should be adjusted. See UNIX ulimit Settings for Self-Managed Deployments for the recommended settings for your platform.

Note
If the ulimit value for number of open files is under 64000, MongoDB generates a startup warning.

Directories
If you installed through the package manager, the data directory /var/lib/mongodb and the log directory /var/log/mongodb are created during the installation.

By default, MongoDB runs using the mongodb user account. If you change the user that runs the MongoDB process, you must also modify the permission to the data and log directories to give this user access to these directories.

Configuration File
The official MongoDB package includes a configuration file (/etc/mongod.conf). These settings (such as the data directory and log directory specifications) take effect upon startup. That is, if you change the configuration file while the MongoDB instance is running, you must restart the instance for the changes to take effect.

Procedure
Follow these steps to run MongoDB Community Edition on your system. These instructions assume that you are using the official mongodb-org package -- not the unofficial mongodb package provided by Ubuntu -- and are using the default settings.

Init System

To run and manage your mongod process, you will be using your operating system's built-in init system. Recent versions of Linux tend to use systemd (which uses the systemctl command), while older versions of Linux tend to use System V init (which uses the service command).

If you are unsure which init system your platform uses, run the following command:

ps --no-headers -o comm 1

Then select the appropriate tab below based on the result:

systemd - select the systemd (systemctl) tab below.

init - select the System V Init (service) tab below.



systemd (systemctl)

System V Init (service)
1
Start MongoDB.
You can start the mongod process by issuing the following command:

sudo systemctl start mongod

If you receive an error similar to the following when starting mongod:

Failed to start mongod.service: Unit mongod.service not found.

Run the following command first:

sudo systemctl daemon-reload

Then run the start command above again.

2
Verify that MongoDB has started successfully.
sudo systemctl status mongod

You can optionally ensure that MongoDB will start following a system reboot by issuing the following command:

sudo systemctl enable mongod

3
Stop MongoDB.
As needed, you can stop the mongod process by issuing the following command:

sudo systemctl stop mongod

4
Restart MongoDB.
You can restart the mongod process by issuing the following command:

sudo systemctl restart mongod

You can follow the state of the process for errors or important messages by watching the output in the /var/log/mongodb/mongod.log file.

5
Begin using MongoDB.
Start a mongosh session on the same host machine as the mongod. You can run mongosh without any command-line options to connect to a mongod that is running on your localhost with default port 27017.

mongosh

For more information on connecting using mongosh, such as to connect to a mongod instance running on a different host and/or port, see the mongosh documentation.

To help you start using MongoDB, MongoDB provides Getting Started Guides in various driver editions. For the driver documentation, see Start Developing with MongoDB.

Uninstall MongoDB Community Edition
To completely remove MongoDB from a system, you must remove the MongoDB applications themselves, the configuration files, and any directories containing data and logs. The following section guides you through the necessary steps.

Warning
This process will completely remove MongoDB, its configuration, and all databases. This process is not reversible, so ensure that all of your configuration and data is backed up before proceeding.

1
Stop MongoDB.
Stop the mongod process by issuing the following command:

sudo service mongod stop

2
Remove Packages.
Remove any MongoDB packages that you had previously installed.

sudo apt-get purge mongodb-org*

3
Remove Data Directories.
Remove MongoDB databases and log files.

sudo rm -r /var/log/mongodb
sudo rm -r /var/lib/mongodb

Additional Information
Localhost Binding by Default
By default, MongoDB launches with bindIp set to 127.0.0.1, which binds to the localhost network interface. This means that the mongod can only accept connections from clients that are running on the same machine. Remote clients will not be able to connect to the mongod, and the mongod will not be able to initialize a replica set unless this value is set to a valid network interface.

This value can be configured either:

in the MongoDB configuration file with bindIp, or

via the command-line argument --bind_ip



---
# Guia Docker

[https://www.mongodb.com/resources/products/compatibilities/docker](https://www.mongodb.com/resources/products/compatibilities/docker)
