
# ReplicaSet

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



