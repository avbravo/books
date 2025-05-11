
# Cambiar el nombre a la base de datos

Pasos:

1. Cree un backup de la base de datos

```shell

mongodump --archive=accreditation.gz --gzip --db=accreditation

```

2. Restaure la base de datos usando
* --nsInclude para indicar las colecciones a transferir
* --nsFrom: Para indicar desde cual base de  datos
* --nsTo: Nombre de la nueva base de datos


```shell

mongorestore --gzip --archive=accreditation.gz --nsInclude='*.*' --nsFrom='accreditation.*' --nsTo='salesdb.*'


```

3. Contamos con la nueva base de datos **salesdb**



