# Native Image

Crear una imagen nativa de Java utilizando GraalVM es un proceso que permite compilar aplicaciones Java en ejecutables nativos, lo que mejora el rendimiento de arranque y reduce el uso de memoria. A continuación, se describe paso a paso cómo crear una imagen nativa de una aplicación Java con GraalVM.

---

### **Requisitos previos**
1. **Instalar GraalVM**:
   - Descarga GraalVM desde su [sitio oficial](https://www.graalvm.org/).
   - Configura `JAVA_HOME` para apuntar a la instalación de GraalVM.
   - Asegúrate de que el comando `java` y `native-image` estén disponibles en tu PATH.

2. **Instalar el plugin `native-image`**:
   - Ejecuta el siguiente comando para instalar el plugin `native-image`:
     ```bash
     gu install native-image
     ```

3. **Configurar Maven o Gradle** (si usas un sistema de construcción):
   - Si usas Maven, asegúrate de tener el plugin `native-maven-plugin`.
   - Si usas Gradle, configura el plugin `org.graalvm.buildtools.native`.

4. **Compilador C**:
   - GraalVM utiliza un compilador C durante la creación de imágenes nativas. Asegúrate de tener instalado GCC (Linux), Xcode Command Line Tools (macOS) o Visual Studio Build Tools (Windows).

---

### **Paso 1: Crear una aplicación Java simple**
Crea un archivo `HelloWorld.java` con el siguiente contenido:

```java
public class HelloWorld {
    public static void main(String[] args) {
        System.out.println("¡Hola, mundo!");
    }
}
```

Compila el archivo usando GraalVM:

```bash
javac HelloWorld.java
```

---

### **Paso 2: Generar la imagen nativa**
Usa el comando `native-image` para generar la imagen nativa:

```bash
native-image HelloWorld
```

Esto generará un ejecutable nativo llamado `helloworld` (en Linux/macOS) o `helloworld.exe` (en Windows).

---

### **Paso 3: Ejecutar la imagen nativa**
Ejecuta el archivo generado:

```bash
./helloworld
```

Deberías ver la salida:

```
¡Hola, mundo!
```

---

### **Opción alternativa: Usar Maven**
Si prefieres usar Maven, sigue estos pasos:

1. **Crea un proyecto Maven**:
   - Inicializa un proyecto Maven con el siguiente `pom.xml`:

     ```xml
     <project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
         <modelVersion>4.0.0</modelVersion>

         <groupId>com.example</groupId>
         <artifactId>graalvm-native-example</artifactId>
         <version>1.0-SNAPSHOT</version>

         <properties>
             <maven.compiler.source>17</maven.compiler.source>
             <maven.compiler.target>17</maven.compiler.target>
         </properties>

         <build>
             <plugins>
                 <plugin>
                     <groupId>org.graalvm.buildtools</groupId>
                     <artifactId>native-maven-plugin</artifactId>
                     <version>0.9.28</version>
                     <executions>
                         <execution>
                             <goals>
                                 <goal>compile</goal>
                             </goals>
                         </execution>
                     </executions>
                 </plugin>
             </plugins>
         </build>
     </project>
     ```

2. **Compila y genera la imagen nativa**:
   - Ejecuta el siguiente comando para generar la imagen nativa:
     ```bash
     mvn package -Pnative
     ```

3. **Ejecuta el archivo nativo**:
   - El ejecutable se generará en `target/graalvm-native-example`.

---

### **Consideraciones adicionales**
1. **Reflexión y frameworks**:
   - Si tu aplicación usa reflexión, serialización u otros mecanismos dinámicos, necesitarás configurar archivos de metadatos (`reflect-config.json`, `resource-config.json`, etc.) para que GraalVM los tenga en cuenta.

2. **Frameworks populares**:
   - Frameworks como Spring Boot tienen soporte específico para GraalVM. Consulta la documentación oficial del framework para obtener instrucciones detalladas.

3. **Depuración**:
   - Usa la opción `--verbose` con `native-image` para obtener más detalles sobre el proceso de compilación.

---

Con estos pasos, deberías poder crear una imagen nativa de Java utilizando GraalVM. Si tienes alguna pregunta adicional o encuentras problemas, no dudes en preguntar. ¡Buena suerte!

[Maven plugin for GraalVM Native Image building](https://graalvm.github.io/native-build-tools/latest/maven-plugin.html)
[Getting Started with Maven Plugin for GraalVM Native Image](https://graalvm.github.io/native-build-tools/latest/maven-plugin-quickstart.html)



---

#### 1. Configuración del Proyecto para GraalVM
Asegúrate de que el proyecto sea compatible con la compilación nativa. Esto implica:
- Evitar reflexión no registrada.
- Registrar clases dinámicas si es necesario.

En este caso, dado que estamos utilizando `com.sun.net.httpserver.HttpServer`, que es compatible con GraalVM, no se requieren cambios adicionales.

---

#### 2. Dockerfile
El archivo `Dockerfile` utiliza una imagen base de GraalVM para compilar el proyecto en modo nativo y luego genera una imagen ligera con la aplicación.

```dockerfile
# Etapa 1: Compilación del proyecto con GraalVM
FROM ghcr.io/graalvm/native-image:ol8-java17 AS build

# Instalar dependencias necesarias
RUN gu install native-image

# Copiar el código fuente al contenedor
WORKDIR /app
COPY . .

# Compilar el proyecto Java
RUN javac -d out src/main/java/*.java

# Generar la imagen nativa
RUN native-image --no-fallback -H:Name=crud-http-server -cp out

# Etapa 2: Crear una imagen ligera con la aplicación nativa
FROM scratch

# Copiar la aplicación nativa desde la etapa anterior
COPY --from=build /app/crud-http-server /crud-http-server

# Exponer el puerto 8080
EXPOSE 8080

# Comando para ejecutar la aplicación
ENTRYPOINT ["/crud-http-server"]
```

---

#### 3. Explicación del Dockerfile
1. **Etapa 1: Compilación con GraalVM**
   - Usamos la imagen oficial de GraalVM (`ghcr.io/graalvm/native-image:ol8-java17`) para compilar el proyecto.
   - Instalamos la herramienta `native-image` con `gu install native-image`.
   - Compilamos el código Java con `javac` y generamos la imagen nativa con `native-image`.

2. **Etapa 2: Imagen Ligera**
   - Usamos la imagen base `scratch`, que es una imagen vacía, para crear una imagen Docker minimalista.
   - Copiamos el binario nativo generado (`crud-http-server`) a la nueva imagen.
   - Exponemos el puerto `8080` y configuramos el punto de entrada para ejecutar la aplicación.

---

#### 4. Generar la Imagen Docker
Sigue estos pasos para construir y ejecutar la imagen Docker:

1. **Construir la Imagen**:
   ```bash
   docker build -t crud-http-server-native .
   ```

2. **Ejecutar el Contenedor**:
   ```bash
   docker run -p 8080:8080 crud-http-server-native
   ```

3. **Probar la Aplicación**:
   Accede a los endpoints de la aplicación en `http://localhost:8080`.

---

### Resultado Final

1. **Imagen Nativa**:
   - La aplicación se compila en modo nativo utilizando GraalVM, lo que reduce significativamente el tiempo de inicio y el uso de memoria.
   - La imagen final es extremadamente ligera porque utiliza la imagen base `scratch`.

2. **Beneficios**:
   - Mejora el rendimiento y reduce el tamaño de la imagen Docker.
   - Ideal para entornos de producción donde la eficiencia es crítica.

---

### Notas Finales
- **Dependencias Dinámicas**: Si tu proyecto utiliza reflexión o clases dinámicas, deberás registrarlas explícitamente en un archivo de configuración para GraalVM. Puedes usar herramientas como `native-image-agent` para generar automáticamente esta configuración.
- **Compatibilidad**: Asegúrate de que todas las bibliotecas utilizadas sean compatibles con GraalVM.
- **Optimización**: Puedes ajustar las opciones de `native-image` para mejorar aún más el rendimiento y reducir el tamaño de la imagen.

¡Espero que este ejemplo te ayude a integrar GraalVM y Docker en tu proyecto!


# graalvm 

Para incluir un archivo `Dockerfile` que utilice **GraalVM** para generar una imagen nativa del proyecto, seguiremos estos pasos:

1. **Configurar el proyecto para GraalVM**: Asegurarse de que el proyecto sea compatible con la compilación nativa.
2. **Crear el Dockerfile**: Utilizar una imagen base de GraalVM para compilar el proyecto y generar una imagen nativa.
3. **Generar la imagen Docker**: Crear una imagen Docker optimizada con la aplicación compilada en modo nativo.

---

### Estructura del Proyecto Actualizada
```
crud-annotation-processing/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   ├── Main.java
│   │   │   ├── User.java
│   │   │   ├── UserHandler.java
│   │   │   ├── SessionManager.java
│   │   │   ├── annotations/
│   │   │   │   ├── Endpoint.java
│   │   │   │   ├── GET.java
│   │   │   │   ├── POST.java
│   │   │   │   ├── PUT.java
│   │   │   │   └── DELETE.java
│   │   │   └── processor/
│   │   │       └── EndpointProcessor.java
│   │   └── resources/
│   │       └── uploads/  (Carpeta para almacenar archivos)
│   └── generated/
├── Dockerfile
└── README.md
```


