# Archivo de Propiedades

1. Cree el directorio com/properties en src/main/resources

![](001_properties.png)


2. Añada los archivos de propiedades


* Añada una propiedad al archivo messages.properties

```
label.pendiente=Pendiente

```

3. Edite el archivo pom.xml y agregue

```xml

           <version.jmoordbutilfaces>3.4</version.jmoordbutilfaces>


```

* Añada la dependencia

```xml
    <dependency>
            <groupId>com.github.avbravo</groupId>
            <artifactId>jmoordbutilfaces</artifactId>
            <version>${version.jmoordbutilfaces}</version>
        </dependency>


```


* Añada el repositorio

```java


  <repositories>
        <repository>
            <id>jitpack.io</id>
            <url>https://jitpack.io</url>
        </repository>
    </repositories>

``


3. Edite la clase MainView.java

Agregue

```java

   @Inject
    JmoordbResourcesFiles rf;

``