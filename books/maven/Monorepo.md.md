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




