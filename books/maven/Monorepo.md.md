# Monorepo (Recomendado si consolidarás el código
Para estructurar un proyecto Maven multimódulo donde los submódulos provienen de otros repositorios o proyectos independientes y subirlos a GitHub, existen dos enfoques principales según cómo desees gestionar el código:

Monorepo (Copiar/Mover el código directamente): Un solo repositorio en GitHub que contiene el POM padre y las carpetas de todos los módulos. Es el más simple de mantener si vas a evolucionar todos los proyectos juntos.

Git Submodules (Mantener repositorios independientes): El proyecto padre tiene su propio repositorio y enlaza a los repositorios de cada submódulo mediante punteros a commits específicos.

Opción 1: Enfoque Monorepo (Recomendado si consolidarás el código)
1. Estructura de directorios
Crea una carpeta raíz y coloca los proyectos dentro

```
mi-proyecto-padre/
├── pom.xml               <-- POM agregador (packaging pom)
├── modulo-auth/          <-- Proyecto 1 (tiene su propio pom.xml)
│   ├── pom.xml
│   └── src/
└── modulo-servicios/     <-- Proyecto 2 (tiene su propio pom.xml)
    ├── pom.xml
    └── src/

```

Configurar el pom.xml padre
En la raíz (mi-proyecto-padre/pom.xml), define el empaquetado como pom y lista los módulos:


```
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 
                             http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>

    <groupId>com.tuempresa</groupId>
    <artifactId>mi-proyecto-padre</artifactId>
    <version>1.0.0-SNAPSHOT</version>
    <packaging>pom</packaging>

    <modules>
        <module>modulo-auth</module>
        <module>modulo-servicios</module>
    </modules>

    <properties>
        <maven.compiler.release>21</maven.compiler.release>
        <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
    </properties>
</project>
```

# Crear un proyecto desde maven

```
mvn archetype:generate   -DgroupId=io.jettra   -DartifactId=jettraexamples   -Dversion=1.0.0-SNAPSHOT   -DarchetypeArtifactId=maven-archetype-quickstart   -DinteractiveMode=false


```

remover el directorio src

```
cd jettraexamples/

rm -rf src

```

Abra el proyecto desde NetBeans IDE

Cambie el packing de jar a pom en la seccion <packaging>
```
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
  xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/maven-v4_0_0.xsd">
  <modelVersion>4.0.0</modelVersion>
  <groupId>io.jettra</groupId>
  <artifactId>jettraexamples</artifactId>
  <packaging>pom</packaging>
  <version>1.0.0-SNAPSHOT</version>
  <name>jettraexamples</name>


```

![](resource/00.png)


De esa manera se agregan los modulos

crear un repositorio en github del proyecto principal


# TRABAJARLO COMO SBOM

Para convertir jettraexamples en un BOM (Bill of Materials) agregador y publicarlo en JitPack, debes transformar la sección <dependencies> en <dependencyManagement> y agregar el plugin maven-publish o las propiedades de compilación requeridas por JitPack.

Aquí tienes el archivo pom.xml ajustado y los pasos exactos para publicarlo y consumirlo.

1. Archivo pom.xml configurado como BOM
Reemplaza tu archivo por el siguiente:

```
<project xmlns="http://maven.apache.org/POM/4.0.0" 
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 
                             http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>

    <groupId>io.jettra</groupId>
    <artifactId>jettraexamples</artifactId>
    <version>1.0.0-SNAPSHOT</version>
    <packaging>pom</packaging>
    <name>jettraexamples</name>

    <properties>
        <maven.compiler.release>21</maven.compiler.release>
        <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
        <!-- Permite a JitPack o a proyectos hijos reutilizar la versión exacta -->
        <jettra.version>${project.version}</jettra.version>
    </properties>

    <!-- Módulos que compila y empaqueta el reactor -->
    <modules>
        <module>modulo-auth</module>
        <module>modulo-servicios</module>
        <module>JettraFluxBackEnd</module>
        <module>JettraFluxExample</module>
        <module>JettraFluxServerExample</module>
        <module>JettraWebBackEnd</module>
        <module>JettraWebExample</module>
        <module>JettraEEExample</module>
        <module>JettraEEFluxExample</module>
    </modules>

    <!-- Definición del BOM: centraliza versiones sin forzar la descarga de todas -->
    <dependencyManagement>
        <dependencies>
            <!-- Módulos propios del ecosistema Jettra -->
            <dependency>
                <groupId>${project.groupId}</groupId>
                <artifactId>modulo-auth</artifactId>
                <version>${project.version}</version>
            </dependency>
            <dependency>
                <groupId>${project.groupId}</groupId>
                <artifactId>modulo-servicios</artifactId>
                <version>${project.version}</version>
            </dependency>
            <dependency>
                <groupId>${project.groupId}</groupId>
                <artifactId>JettraFluxBackEnd</artifactId>
                <version>${project.version}</version>
            </dependency>
            <dependency>
                <groupId>${project.groupId}</groupId>
                <artifactId>JettraFluxExample</artifactId>
                <version>${project.version}</version>
            </dependency>
            <dependency>
                <groupId>${project.groupId}</groupId>
                <artifactId>JettraFluxServerExample</artifactId>
                <version>${project.version}</version>
            </dependency>
            <dependency>
                <groupId>${project.groupId}</groupId>
                <artifactId>JettraWebBackEnd</artifactId>
                <version>${project.version}</version>
            </dependency>
            <dependency>
                <groupId>${project.groupId}</groupId>
                <artifactId>JettraWebExample</artifactId>
                <version>${project.version}</version>
            </dependency>
            <dependency>
                <groupId>${project.groupId}</groupId>
                <artifactId>JettraEEExample</artifactId>
                <version>${project.version}</version>
            </dependency>
            <dependency>
                <groupId>${project.groupId}</groupId>
                <artifactId>JettraEEFluxExample</artifactId>
                <version>${project.version}</version>
            </dependency>

            <!-- Librerías de terceros compartidas (ej. JUnit 5) -->
            <dependency>
                <groupId>org.junit.jupiter</groupId>
                <artifactId>junit-jupiter</artifactId>
                <version>5.10.2</version>
                <scope>test</scope>
            </dependency>
        </dependencies>
    </dependencyManagement>

    <build>
        <plugins>
            <!-- Necesario para que JitPack instale y publique el código compilado de los módulos -->
            <plugin>
                <groupId>org.apache.maven.plugins</groupId>
                <artifactId>maven-install-plugin</artifactId>
                <version>3.1.1</version>
            </plugin>
        </plugins>
    </build>
</project>

```

Subir el proyecto a Github y luego a jitpack

En el proyecto de ejemplo 

```
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 
                             http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>

    <groupId>com.miempresa</groupId>
    <artifactId>miejemplo</artifactId>
    <version>1.0.0-SNAPSHOT</version>
    <packaging>jar</packaging>
    <name>miejemplo</name>

    <properties>
        <maven.compiler.release>21</maven.compiler.release>
        <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
    </properties>

    <!-- 1. Repositorio de JitPack para resolver las librerías -->
    <repositories>
        <repository>
            <id>jitpack.io</id>
            <name>JitPack Repository</name>
            <url>https://jitpack.io</url>
        </repository>
    </repositories>

    <!-- 2. Importación del BOM de jettraexamples -->
    <dependencyManagement>
        <dependencies>
            <dependency>
                <groupId>com.github.TU_USUARIO_GITHUB</groupId>
                <artifactId>jettraexamples</artifactId>
                <!-- Usa el release/tag (ej: v1.0.0) o la rama (main-SNAPSHOT) -->
                <version>v1.0.0</version>
                <type>pom</type>
                <scope>import</scope>
            </dependency>
        </dependencies>
    </dependencyManagement>

    <!-- 3. Submódulos que este proyecto va a utilizar -->
    <dependencies>
        <!-- Módulo JettraFluxBackEnd (la versión la resuelve el BOM) -->
        <dependency>
            <groupId>com.github.TU_USUARIO_GITHUB.jettraexamples</groupId>
            <artifactId>JettraFluxBackEnd</artifactId>
        </dependency>

        <!-- Módulo modulo-auth (la versión la resuelve el BOM) -->
        <dependency>
            <groupId>com.github.TU_USUARIO_GITHUB.jettraexamples</groupId>
            <artifactId>modulo-auth</artifactId>
        </dependency>

        <!-- Módulo JettraEEExample (ejemplo adicional si lo necesitas) -->
        <dependency>
            <groupId>com.github.TU_USUARIO_GITHUB.jettraexamples</groupId>
            <artifactId>JettraEEExample</artifactId>
        </dependency>

        <!-- Pruebas unitarias: resuelta desde el BOM sin poner versión -->
        <dependency>
            <groupId>org.junit.jupiter</groupId>
            <artifactId>junit-jupiter</artifactId>
            <scope>test</scope>
        </dependency>
    </dependencies>

    <build>
        <plugins>
            <plugin>
                <groupId>org.apache.maven.plugins</groupId>
                <artifactId>maven-compiler-plugin</artifactId>
                <version>3.13.0</version>
            </plugin>
        </plugins>
    </build>
</project>


```


