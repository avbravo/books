# SQL Server 


## Credenciales Base de datos
<p>

* Instale NetBeans
* Agregue un nuevo driver desde Services
* Establezca la configuración
```
SQL Server.

Servidor: 192.168.91.63

Puerto: 1433

Usuario: hodavillareal

Contraseña: Master*Divo*01

base de datos: PRACTI


jdbc:sqlserver://192.168.91.63:1433;databaseName=PRACTI;encrypt=false

schema: dbo

name="DefaultConnection" providerName="System.Data.SqlClient" connectionString="Data Source=192.168.91.63;Initial Catalog=PRACTI;Integrated Security=False;User ID=hoddalvarenga;Password=*****;MultipleActiveResultSets=True
```

Seleccionar dbo como esquema



</p>


## Conexion jdbc

[Connection modules for Microsoft SQL Database](https://learn.microsoft.com/en-us/sql/connect/sql-connection-libraries?view=sql-server-ver16)

## SQL Server en Ubuntu

[Install SQL Server 2022 on Ubuntu in 5 minutes](https://www.dbblogger.com/post/install-sql-server-2022-on-ubuntu-in-5-minutes)


## SQL Server Client in Ubuntu

[Install SQL Client on Ubuntu](https://snapcraft.io/install/sql-client/ubuntu)

Instalar

```shell
sudo snap install sql-client --beta
```

Ejecutar el cliente
```
sql-client
```
