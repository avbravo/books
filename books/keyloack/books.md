# Helidon MP OIDC Security Provider

* [Helidon MP OIDC Security Provider](https://helidon.io/docs/v3/mp/guides/security-oidc)



En el capitulo nos basaremos en la guia propuesta por Helidon que consiste en implementar seguridad de Open ID Connect (OIDC). 

```
OIDC es un mecanismo seguro para que una aplicación contacte con un servicio de identidad. 

Está basado en OAuth 2.0 y proporciona protocolos completos de autenticación y autorización.
```


## KeyLoak

[keycloak](https://www.keycloak.org/)

```
Gestión de identidad y acceso de código abierto
Autentique sus aplicaciones y proteja sus servicios con el mínimo esfuerzo.
Olvídese del almacenamiento y la autenticación de usuarios.

Keycloak proporciona federación de usuarios, autenticación sólida, gestión de usuarios, autorización detallada y mucho más.
```



## Instalar Keycloak

Mediante docker. Especifique el puerto, en el ejemplo se usa el usuario admin, y password admin, y la versión de keyloak = 26.1.4

```
docker run -p 8080:8080 -e KC_BOOTSTRAP_ADMIN_USERNAME=admin -e KC_BOOTSTRAP_ADMIN_PASSWORD=admin quay.io/keycloak/keycloak:26.1.4 start-dev

```


### Modos de ejecución

* Desarrollador



```shell
   start-dev
```

* Building an optimized server runtime:
```shell
      build <OPTIONS>
```

  Start the server in production mode:
```shell
     start <OPTIONS>
```


### Consola de Administracion

Ingrese a [http://localhost:8080/admin](http://localhost:8080/admin)

![](resources/00_consola.png)

