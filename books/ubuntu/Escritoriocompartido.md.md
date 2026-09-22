# Escritorio Compartido

Se cuenta con dos computadoras con Ubuntu-

En la computadora cliente

```shell
sudo apt update
sudo apt install openssh-server -y
sudo systemctl enable --now ssh
```

Verifica el ip
```shell
ip a

```

En la maquina cliente

```shell
ssh usuario@IP_DE_LA_PC_2
```


---

# Usando Escritorio Remoto basado en RDP (Remote Desktop),

1.Habilitar el Escritorio Remoto:Configuración en PC 2.

En la computadora que quieres controlar (PC 2), ve a la Configuración del sistema:

**Entra en la sección Sistema o Compartir (según la versión de Ubuntu).**

Activa la opción de Escritorio Remoto (Remote Desktop).

Asegúrate de activar también **Control Remoto (Remote Control)*** para permitir que la computadora 1 envíe clics y movimientos de teclado.

Anota el Nombre de usuario y la Contraseña que aparecen en esa misma ventana (los necesitarás para conectarte).

Verificación: El interruptor principal de Escritorio Remoto debe quedar en color verde/activado.


Verifica el Ip

```shell
ip a
```

## En la computadora 1

En tu computadora principal (PC 1), 

abre la aplicación predeterminada Conexión de Escritorio Remoto 

(o puedes instalar un cliente compatible con RDP como Remmina ejecutando sudo apt install remmina).

Selecciona el protocolo RDP.

Introduce la dirección IP de la PC 2.

Inicia la conexión e introduce el usuario y la contraseña que anotaste en el Paso 1.

Verificación: Verás aparecer la pantalla de Escritorio de la PC 2 en una ventana dentro de tu computadora 1.


