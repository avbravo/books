# AÑADIR USUARIOS
    
##Añadir usuario
sudo adduser nuevo_usuario


##Dar permisos de administrador (opcional)

sudo usermod -aG sudo nuevo_usuario

##Probar el acceso con el nuevo usuario

su - nuevo_usuario

