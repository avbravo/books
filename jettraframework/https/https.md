# https 

Configurar **HTTPS** en un proyecto Java que utiliza la **API HTTP** requiere generar certificados digitales y configurar el servidor para usarlos. A continuación, te explico cómo hacerlo paso a paso:

---

### 1. Generar Certificados Digitales en Ubuntu

Para habilitar HTTPS, necesitas un certificado digital. Puedes generar uno autofirmado (para desarrollo) o usar un certificado emitido por una Autoridad de Certificación (CA) como Let's Encrypt (para producción).

#### Opción 1: Generar un Certificado Autofirmado (Desarrollo)

1. **Instalar OpenSSL**:
   Si no tienes OpenSSL instalado, puedes instalarlo con:
   ```bash
   sudo apt update
   sudo apt install openssl
   ```

2. **Generar una Clave Privada**:
   Crea una clave privada RSA:
   ```bash
   openssl genpkey -algorithm RSA -out server.key
   ```

3. **Generar un CSR (Certificate Signing Request)**:
   El CSR contiene la información del certificado.
   ```bash
   openssl req -new -key server.key -out server.csr
   ```
   Durante este proceso, se te pedirá información como el nombre común (CN), que debe coincidir con el dominio o IP del servidor.

4. **Generar el Certificado Autofirmado**:
   Firma el CSR con tu clave privada para crear el certificado:
   ```bash
   openssl x509 -req -days 365 -in server.csr -signkey server.key -out server.crt
   ```
   Esto genera un certificado válido por 365 días.

5. **Verificar los Archivos Generados**:
   Deberías tener los siguientes archivos:
   - `server.key`: Clave privada.
   - `server.crt`: Certificado público.
   - `server.csr`: Solicitud de firma (opcional, no es necesario para HTTPS).

---

#### Opción 2: Obtener un Certificado de Let's Encrypt (Producción)

1. **Instalar Certbot**:
   Certbot es una herramienta para obtener certificados gratuitos de Let's Encrypt.
   ```bash
   sudo apt update
   sudo apt install certbot
   ```

2. **Generar el Certificado**:
   Ejecuta Certbot para obtener un certificado para tu dominio:
   ```bash
   sudo certbot certonly --standalone -d yourdomain.com
   ```
   Esto generará los archivos del certificado en `/etc/letsencrypt/live/yourdomain.com/`.

3. **Renovar Automáticamente el Certificado**:
   Configura un cron job para renovar automáticamente el certificado:
   ```bash
   sudo crontab -e
   ```
   Agrega esta línea para renovar diariamente:
   ```bash
   0 0 * * * /usr/bin/certbot renew --quiet
   ```

---

### 2. Configurar HTTPS en un Proyecto Java API HTTP

La API HTTP de Java permite configurar un servidor HTTPS usando los certificados generados.

#### Código del Servidor HTTPS

```java
import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.HttpsServer;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpsConfigurator;
import com.sun.net.httpserver.HttpsParameters;

import javax.net.ssl.*;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.security.KeyStore;

public class HttpsServerExample {

    public static void main(String[] args) throws Exception {
        // Puerto del servidor HTTPS
        int port = 8443;

        // Crear el servidor HTTPS
        HttpsServer server = HttpsServer.create(new InetSocketAddress(port), 0);

        // Configurar el contexto y el manejador
        server.createContext("/api", new MyHandler());

        // Configurar SSL/TLS
        char[] password = "password".toCharArray(); // Contraseña del keystore
        KeyStore ks = KeyStore.getInstance("PKCS12");
        try (FileInputStream fis = new FileInputStream("server.p12")) {
            ks.load(fis, password);
        }

        KeyManagerFactory kmf = KeyManagerFactory.getInstance("SunX509");
        kmf.init(ks, password);

        TrustManagerFactory tmf = TrustManagerFactory.getInstance("SunX509");
        tmf.init(ks);

        SSLContext sslContext = SSLContext.getInstance("TLS");
        sslContext.init(kmf.getKeyManagers(), tmf.getTrustManagers(), null);

        server.setHttpsConfigurator(new HttpsConfigurator(sslContext) {
            @Override
            public void configure(HttpsParameters params) {
                try {
                    SSLContext context = getSSLContext();
                    SSLEngine engine = context.createSSLEngine();
                    params.setNeedClientAuth(false);
                    params.setCipherSuites(engine.getEnabledCipherSuites());
                    params.setProtocols(engine.getEnabledProtocols());
                } catch (Exception e) {
                    throw new RuntimeException("Failed to configure HTTPS", e);
                }
            }
        });

        // Iniciar el servidor
        server.setExecutor(null); // Usa el executor por defecto
        server.start();
        System.out.println("HTTPS Server is running on port " + port);
    }

    // Manejador de solicitudes
    static class MyHandler implements HttpHandler {
        @Override
        public void handle(com.sun.net.httpserver.HttpExchange exchange) throws IOException {
            String response = "Hello, this is an HTTPS response!";
            exchange.sendResponseHeaders(200, response.getBytes().length);
            OutputStream os = exchange.getResponseBody();
            os.write(response.getBytes());
            os.close();
        }
    }
}
```

---

### 3. Convertir Certificados a PKCS12

La API HTTP de Java requiere un archivo en formato PKCS12. Si usaste OpenSSL para generar los certificados, conviértelos así:

```bash
openssl pkcs12 -export -in server.crt -inkey server.key -out server.p12 -name "server" -CAfile ca.crt -caname root
```

Durante el proceso, se te pedirá una contraseña para el archivo PKCS12. Usa esta contraseña en el código (`password`).

---

### 4. Probar el Servidor HTTPS

1. **Iniciar el Servidor**:
   Ejecuta el programa Java:
   ```bash
   java HttpsServerExample
   ```

2. **Acceder al Servidor**:
   Abre un navegador o usa `curl` para probar el servidor:
   ```bash
   curl -k https://localhost:8443/api
   ```
   La opción `-k` ignora errores de certificado autofirmado.

---

### 5. Consideraciones

1. **Certificados Autofirmados**:
   - Son útiles para desarrollo, pero los navegadores mostrarán advertencias.
   - Para producción, usa certificados de una CA confiable como Let's Encrypt.

2. **Firewall**:
   Asegúrate de que el puerto HTTPS (por ejemplo, 8443) esté abierto en el firewall.

3. **Seguridad**:
   - Usa contraseñas seguras para los certificados.
   - Mantén los certificados actualizados.

4. **Depuración**:
   Si encuentras problemas, verifica los logs del servidor y asegúrate de que los certificados sean válidos.

---

Con estos pasos, habrás configurado correctamente HTTPS en un proyecto Java que utiliza la API HTTP y generado los certificados necesarios en Ubuntu.