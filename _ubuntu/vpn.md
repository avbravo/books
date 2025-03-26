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
