# Conexion SSH

sudo apt update
sudo apt install openssh-server
sudo systemctl enable --now ssh


verificar la direccion ip

ip a




Conectarse

ssh desarrollador@192.168.60.246


# Copiar archivos


Copiar archivos del equipo local al servidor remoto
Usa el comando scp indicando primero la ruta del archivo local y luego el destino con el formato usuario@ip:ruta_remota.

scp /ruta/local/archivo.txt Itsias2016@192.168.60.246:/home/Itsias2016/


Si quieres copiar una carpeta completa, agrega la bandera -r (recursivo):


scp -r /ruta/local/mi_carpeta Itsias2016@192.168.60.246:/home/Itsias2016/



. Copiar archivos del servidor remoto a tu equipo local
Invierte el orden: coloca primero la ruta remota y al final la ruta de tu computador local (puedes usar . para indicar la carpeta actual donde estás parado).

scp Itsias2016@192.168.60.246:/home/Itsias2016/archivo.txt ./


Alternativa recomendada para carpetas grandes: rsync
Si necesitas transferir directorios grandes o quieres reanudar transferencias interrumpidas, rsync con compresión (-z) y barra de progreso (-P) es mucho más eficiente:


rsync -avzP /ruta/local/carpeta/ Itsias2016@192.168.60.246:/home/Itsias2016/carpeta_destino/



De remoto a local:

rsync -avzP Itsias2016@192.168.60.246:/home/Itsias2016/archivo.txt ./
