# servidoralcala

Acabo de crear una cuenta en el servidor del grupo HCIS para que podáis instalar la base de datos de la aplicación de microbiología que estuvimos comentando en la reunión de jueves día 18. 

El nombre de la cuenta es 'utecpa' y la clave de acceso es 'vargas'.

 ```

Podéis acceder a través de ssh a la dirección 192.168.71.78 estando conectados previamente a la UAH a través de VPN.

```


# VPN

![](Captura desde 2024-07-10 20-59-19.png)

---

## Credenciales

Pasos:

1. [https://uah.atlassian.net/wiki/spaces/CAU/pages/3703171/Linux](https://uah.atlassian.net/wiki/spaces/CAU/pages/3703171/Linux)


```
Por medio de la presente, adjunto las credenciales tanto del VPN de la UAH como al servidor con el propósito que pueda integrar y testear sus aplicaciones. En el previo correo, se encuentras las instrucciones para su caso es la versión servidor.

UAH VPN (https://uah.atlassian.net/wiki/spaces/CAU/pages/3703378/Windows)
User: miguel.vargasl 
PWD: Sofia21261971


SSH servidor (192.168.71.75)
user: utecpa
PWD: vargas


ssh utecpa@192.168.71.75


```

## Otros Clientes

[Conecta con tu VPN de Fortinet con OPENFORTIVPN](https://www.youtube.com/watch?v=PRMt2PHElzg)

# OpenFortiVPN

Clientes

## Openfortivpn

[Conecta con tu VPN de Fortinet con OPENFORTIVPN](https://www.youtube.com/watch?v=PRMt2PHElzg)

Instalación

```
sudo apt install openfortivpn

```

Edite el archivo

```
sudo gnome-text-editor /etc/openfortivpn/config


````



Configurar credenciales

```
# config file for openfortivpn, see man openfortivpn(1)
host =conecta.uah.es
port =10443
username =aristides.villarreal
password =UCox$jav84$mAgzu
```


## Ejecutar

```
 sudo openfortivpn
```

o mediante

```
sudo openfortivpn conecta.uah.es:10443 -u aristides.villarreal 

```



 
