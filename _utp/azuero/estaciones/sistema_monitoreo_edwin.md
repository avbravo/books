Sistema Monitoreo Edwin

## Repositorio

[https://github.com/ColdDEV507/estacionesserver2.0](https://github.com/ColdDEV507/estacionesserver2.0}


[https://github.com/ColdDEV507](https://github.com/ColdDEV507)

consulta

```
localhost:9000/estacionesserver/api/medicion/betweendate?fechainicial=01-01-2024&fechafinal=02-01-2024&idestacion=1&anio=2024&mes=0

```


# Restoredb

```shell
 docker cp /home/avbravo/Descargas/lecturas_2024db.gz 4108046ae6e7:/home/avbravo/lecturas_2024db.gz

docker exec -it mongodb-lastet bash

mongorestore --gzip --archive=lecturas_2024db.gz --username avbravo --password denver16


mongosh --username avbravo --password denver16 
```


## Postman

![](postman_post.png)

Para hacer la consulta POST: localhost:9000/estacionesserver/api/medicion y mando los siguientes datos:


### (Se guarda en la colección de Enero)
```


{"dir_Viento": 32.0, "fechahora": "2024-02-01T00:00:00Z[UTC]", "humedad": 2.0, "idestacion": 1, "pm25": 5.9, 

"temperatura": 26.6, "vel_Viento": 39.0, "co": 2.3,    "o3": 1.3,    "so2": 9.1,    "no2": 1.5,    "pm10": 13.1, "lluvia": 10.7}


{"dir_Viento": 32.0, "fechahora": "2024-02-01T01:00:00Z[UTC]", "humedad": 0.0, "idestacion": 1, "pm25": 5.9, 

"temperatura": 26.6, "vel_Viento": 39.0, "co": 2.3,    "o3": 1.3,    "so2": 9.1,    "no2": 1.5,    "pm10": 13.1, "lluvia": 0.7}


{"dir_Viento": 32.0, "fechahora": "2024-02-01T02:00:00Z[UTC]", "humedad": 2.0, "idestacion": 1, "pm25": 5.9, 

"temperatura": 26.6, "vel_Viento": 39.0, "co": 2.3,    "o3": 1.3,    "so2": 9.1,    "no2": 1.5,    "pm10": 13.1, "lluvia": 10.7}


{"dir_Viento": 32.0, "fechahora": "2024-02-01T03:00:00Z[UTC]", "humedad": 2.0, "idestacion": 1, "pm25": 5.9, 

"temperatura": 26.6, "vel_Viento": 39.0, "co": 2.3,    "o3": 1.3,    "so2": 9.1,    "no2": 1.5,    "pm10": 13.1, "lluvia": 10.7} 


{"dir_Viento": 32.0, "fechahora": "2024-02-01T04:00:00Z[UTC]", "humedad": 2.0, "idestacion": 1, "pm25": 5.9, 

"temperatura": 26.6, "vel_Viento": 39.0, "co": 2.3,    "o3": 1.3,    "so2": 9.1,    "no2": 1.5,    "pm10": 13.1, "lluvia": 10.7} 

```
### (Se guarda en la colección de Febrero)

```
{"dir_Viento": 32.0, "fechahora": "2024-02-01T05:00:00Z[UTC]", "humedad": 2.0, "idestacion": 1, "pm25": 5.9, 

"temperatura": 26.6, "vel_Viento": 39.0, "co": 2.3,    "o3": 1.3,    "so2": 9.1,    "no2": 1.5,    "pm10": 13.1, "lluvia": 10.7} 

```

## Este si lo convierte a Febrero

```
{"dir_Viento": 32.0, "fechahora": "2024-02-01T10:00:00Z", "humedad": 2.0, "idestacion": 1, "pm25": 5.9, 

"temperatura": 26.6, "vel_Viento": 39.0, "co": 2.3,    "o3": 1.3,    "so2": 9.1,    "no2": 1.5,    "pm10": 13.1, "lluvia": 10.7}

```
## Consultas
```

http://localhost:9000/medicion/betweendate?fechainicial=01-01-2024&fechafinal=05-01-2024&idestacion=1&anio=2024&mes=0

http://localhost:9000/estacionesserver/api/medicion/lookup?filter={"idmedicion": {"$ne": ""}}&sort={"idmedicion": -1}&page=0&size=0&idestacion=1&anio=2024&mes=2
```
