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

scp /home/avbravo/archivo.txt desarrollador@192.168.60.246:/home/desarrollador/


Si quieres copiar una carpeta completa, agrega la bandera -r (recursivo):


scp -r /home/avbravo/Descargas/jettra1 desarrollador@192.168.60.246:/home/desarrollador/jettra1

    scp -r ~/jettra-node desarrollador@192.168.60.246:/home/desarrollador/jettra-node

. Copiar archivos del servidor remoto a tu equipo local
Invierte el orden: coloca primero la ruta remota y al final la ruta de tu computador local (puedes usar . para indicar la carpeta actual donde estás parado).

scp desarrollador@192.168.60.246:/home/desarrollador/archivo.txt ./


Alternativa recomendada para carpetas grandes: rsync
Si necesitas transferir directorios grandes o quieres reanudar transferencias interrumpidas, rsync con compresión (-z) y barra de progreso (-P) es mucho más eficiente:


rsync -avzP /home/avbravo/carpeta/ desarrollador@192.168.60.246:/home/desarrollador/carpeta_destino/



De remoto a local:

rsync -avzP desarrollador@192.168.60.246:/home/desarrollador/archivo.txt ./




scp /home/avbravo/archivo.txt de@192.168.60.246:/home/desarrollador/