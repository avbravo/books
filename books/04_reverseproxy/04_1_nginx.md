### **Instalar y Configurar Nginx con Docker**

Usar **Docker** para instalar y configurar **Nginx** es una excelente opción porque simplifica el proceso de instalación, garantiza que el entorno sea consistente y facilita la gestión del servidor. A continuación, te explico cómo hacerlo paso a paso.

---

### **Paso 1: Instalar Docker**
Si no tienes Docker instalado, sigue estos pasos:

#### En Ubuntu/Debian:
```bash
sudo apt update
sudo apt install -y docker.io
sudo systemctl start docker
sudo systemctl enable docker
```

#### En CentOS/RHEL:
```bash
sudo yum install -y docker
sudo systemctl start docker
sudo systemctl enable docker
```

Verifica que Docker esté instalado correctamente:
```bash
docker --version
```

---

### **Paso 2: Crear un Archivo de Configuración para Nginx**

Antes de ejecutar el contenedor de Nginx, necesitas crear un archivo de configuración personalizado. Este archivo será montado en el contenedor de Docker.

1. Crea un directorio para los archivos de configuración:
   ```bash
   mkdir -p ~/nginx-config
   cd ~/nginx-config
   ```

2. Crea un archivo `default.conf` con la configuración básica de Nginx:
   ```bash
   nano default.conf
   ```

3. Agrega la siguiente configuración (ajústala según tus necesidades):
   ```nginx
   server {
       listen 80;
       server_name localhost;

       location / {
           proxy_pass http://backend:8080;  # Dirección del backend (Payara Micro)
           proxy_set_header Host $host;
           proxy_set_header X-Real-IP $remote_addr;
           proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
           proxy_set_header X-Forwarded-Proto $scheme;
       }
   }
   ```

   **Nota:** Si tu backend está en otro contenedor Docker, puedes usar el nombre del servicio (`backend`) en lugar de `localhost`.

---

### **Paso 3: Ejecutar el Contenedor de Nginx**

Usa el siguiente comando para iniciar un contenedor de Nginx con tu archivo de configuración:

```bash
docker run --name nginx-proxy \
  -p 80:80 \
  -v ~/nginx-config/default.conf:/etc/nginx/conf.d/default.conf \
  -d nginx
```

#### Explicación del comando:
- `--name nginx-proxy`: Asigna un nombre al contenedor.
- `-p 80:80`: Mapea el puerto 80 del host al puerto 80 del contenedor.
- `-v ~/nginx-config/default.conf:/etc/nginx/conf.d/default.conf`: Monta tu archivo de configuración personalizado dentro del contenedor.
- `-d nginx`: Ejecuta el contenedor en segundo plano usando la imagen oficial de Nginx.

---

### **Paso 4: Verificar el Funcionamiento**

1. Abre un navegador y visita `http://localhost`. Deberías ver la página predeterminada de Nginx o el contenido de tu aplicación backend si está correctamente configurada.

2. Si necesitas verificar los logs de Nginx, usa:
   ```bash
   docker logs nginx-proxy
   ```

---

### **Paso 5: Conectar Nginx con Payara Micro en Docker**

Si estás ejecutando **Payara Micro** en otro contenedor Docker, puedes conectar ambos servicios usando una red Docker.

1. Crea una red Docker:
   ```bash
   docker network create app-network
   ```

2. Inicia el contenedor de Payara Micro en la misma red:
   ```bash
   docker run --name payara-backend \
     --network app-network \
     -p 8080:8080 \
     -d payara/micro
   ```

3. Modifica el archivo `default.conf` de Nginx para apuntar al nombre del servicio de Payara (`payara-backend`):
   ```nginx
   proxy_pass http://payara-backend:8080;
   ```

4. Reinicia el contenedor de Nginx para aplicar los cambios:
   ```bash
   docker restart nginx-proxy
   ```

---

### **Paso 6: Habilitar HTTPS con Let's Encrypt (Opcional)**

Para habilitar HTTPS, puedes usar **Certbot** dentro de un contenedor Docker:

1. Detén el contenedor de Nginx:
   ```bash
   docker stop nginx-proxy
   ```

2. Usa Certbot para obtener un certificado SSL:
   ```bash
   docker run -it --rm \
     -v ~/nginx-config:/etc/nginx/conf.d \
     -v ~/certs:/etc/letsencrypt \
     certbot/certbot certonly --standalone -d tu-dominio.com
   ```

3. Actualiza el archivo `default.conf` para usar SSL:
   ```nginx
   server {
       listen 80;
       server_name tu-dominio.com;
       return 301 https://$host$request_uri;
   }

   server {
       listen 443 ssl;
       server_name tu-dominio.com;

       ssl_certificate /etc/letsencrypt/live/tu-dominio.com/fullchain.pem;
       ssl_certificate_key /etc/letsencrypt/live/tu-dominio.com/privkey.pem;

       location / {
           proxy_pass http://payara-backend:8080;
           proxy_set_header Host $host;
           proxy_set_header X-Real-IP $remote_addr;
           proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
           proxy_set_header X-Forwarded-Proto $scheme;
       }
   }
   ```

4. Reinicia el contenedor de Nginx:
   ```bash
   docker restart nginx-proxy
   ```

---

### **Conclusión**

Con esta configuración, has implementado **Nginx como reverse proxy** usando Docker. Esto te permite gestionar fácilmente tu aplicación Eclipse MicroProfile con Payara Micro, mejorar la seguridad y el rendimiento, y escalar tu infraestructura de manera eficiente.

Si tienes más preguntas o necesitas ayuda adicional, no dudes en preguntar. ¡Buena suerte!