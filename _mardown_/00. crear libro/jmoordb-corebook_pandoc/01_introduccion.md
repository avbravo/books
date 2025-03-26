\newpage

\begin{flushright}
\section{Capítulo 1}
\end{flushright}

## Introducción  
  Jmoordb-core es un framework Java para bases de datos NoSQL, que utiliza las
APIs de Jakarta EE, Microprofile.
 El desarrollo y evolución de Java en el lado del servidor sin lugar a dudas 
ocupa los primeros lugares como una solución escalable. Desde los primeros años,
el lenguaje y plataforma se han ido consolidando a nivel empresarial. Por estos
motivos consideramos que es una tecnología probada y con un largo camino de éxitos.    
 Haremos un recorrido breve por Jakarta EE, algunos conceptos de 
Java, e ideas sobre como crear un framework para bases de datos NoSQL.

    
## Jakarta EE

[Jakarta EE ](https://jakarta.ee/), son un conjunto de especificaciones Java,
para el entorno empresarial que han surgido de la evolución de lo que 
antiguamente se conoció como Java EE.
Estas especificaciones definen un conjunto de tecnologías que facilitan el
desarrollo de aplicaciones de alto desempeño utilizando las mejores prácticas de
desarrollo.
Recomendamos la lectura de las especificaciones al lector.

|Especificación    | Especificación | Especificación  |
|-----------       | -----------    |----------- |
|                  |                |            |
|[Jakarta EE Platform](https://jakarta.ee/specifications/platform/10/)  | [Jakarta EE Web Profile](https://jakarta.ee/specifications/webprofile/10/) | [Jakarta EE Core Profile](https://jakarta.ee/specifications/coreprofile/10/)|
|[Jakarta Activation](https://jakarta.ee/specifications/activation/2.1/) |[Jakarta Annotations](https://jakarta.ee/specifications/annotations/2.1/) |[Jakarta Authentication](https://jakarta.ee/specifications/authentication/3.0/)|
|[Jakarta Authorization](https://jakarta.ee/specifications/authorization/2.1/) | [Jakarta Batch](https://jakarta.ee/specifications/batch/2.1/) | [Jakarta Bean Validation](https://jakarta.ee/specifications/bean-validation/3.0/) |
|[Jakarta Concurrency](https://jakarta.ee/specifications/concurrency/3.0/) | [Jakarta Config](https://jakarta.ee/specifications/config/) |[Jakarta Connectors](https://jakarta.ee/specifications/connectors/2.1/)|
|[Jakarta Contexts and Dependency Injection](https://jakarta.ee/specifications/cdi/4.0/) | [Jakarta Data](https://jakarta.ee/specifications/data/) |[Jakarta Debugging Support for Other Languages](https://jakarta.ee/specifications/debugging/2.0/)|
|[Jakarta Dependency Injection](https://jakarta.ee/specifications/dependency-injection/2.0/) | [Jakarta Deployment](https://jakarta.ee/specifications/deployment/1.7/) |[Jakarta Enterprise Beans](https://jakarta.ee/specifications/enterprise-beans/4.0/)|
|[Jakarta Enterprise Web Services](https://jakarta.ee/specifications/enterprise-ws/2.0/) | [Jakarta Expression Language](https://jakarta.ee/specifications/expression-language/5.0/) |[Jakarta Faces](https://jakarta.ee/specifications/faces/4.0/)|
|[Jakarta Interceptors](https://jakarta.ee/specifications/interceptors/2.1/) | [Jakarta JSON Binding](https://jakarta.ee/specifications/jsonb/3.0/) |[Jakarta JSON Processing](https://jakarta.ee/specifications/jsonp/2.1/)|
|[Jakarta Mail](https://jakarta.ee/specifications/mail/2.1/) | [Jakarta Managed Beans](https://jakarta.ee/specifications/managedbeans/2.0/) |[Jakarta Management](https://jakarta.ee/specifications/management/1.1/)|
|[Jakarta Messaging](https://jakarta.ee/specifications/messaging/3.1/) | [Jakarta MVC](https://jakarta.ee/specifications/mvc/2.1/) |[Jakarta NoSQL](https://jakarta.ee/specifications/nosql/1.0/)|
|[Jakarta Persistence](https://jakarta.ee/specifications/persistence/3.1/) | [Jakarta RESTful Web Services](https://jakarta.ee/specifications/restful-ws/4.0/) |[Jakarta RPC](https://jakarta.ee/specifications/rpc/)|
|[Jakarta Security](https://jakarta.ee/specifications/security/3.0/) | [Jakarta Server Pages](https://jakarta.ee/specifications/pages/3.1/) |[Jakarta Servlet](https://jakarta.ee/specifications/servlet/6.0/)|
|[Jakarta SOAP with Attachments](https://jakarta.ee/specifications/soap-attachments/3.0/) | [Jakarta Standard Tag Library](https://jakarta.ee/specifications/tags/3.0/) |[Jakarta Transactions](https://jakarta.ee/specifications/transactions/2.0/)|
|[Jakarta Web Services Metadata](https://jakarta.ee/specifications/web-services-metadata/3.0/) | [Jakarta WebSocket](https://jakarta.ee/specifications/websocket/2.1/) |[Jakarta XML Binding](https://jakarta.ee/specifications/xml-binding/4.0/)|
|[Jakarta XML Registries](https://jakarta.ee/specifications/xml-registries/1.0/) | [Jakarta XML RPC](https://jakarta.ee/specifications/xml-rpc/1.1/) |[Jakarta XML Web Services Specification](https://jakarta.ee/specifications/xml-web-services/4.0/)|


 Si desea conocer un poco más de Jakarta EE , junto a Geovany Mendoza y Otavio
Santana escribimos el libro [Building Modern Web Applications With Jakarta EE, NoSQL Databases and Microservices: Create Web Applications Jakarta EE with Microservice](https://www.amazon.co.uk/Building-Applications-Jakarta-Databases-Microservices/dp/9389423341)


## Microprofile
[Microprofile](http://microprofile.io). Es un conjunto de especificaciones Java
para la creación de microservicios.
Es recomendable que lea las especificaciones a lo largo de este libro mostraremos 
ejemplos de uso de las especificaciones.

|Especificación    | Especificación | Especificación  |
|-----------       | -----------    |-----------      |
|                  |                |                 |
| [ OpenTracing ](https://github.com/eclipse/microprofile-opentracing/releases/tag/3.0)  | [OpenAPI](https://github.com/eclipse/microprofile-open-api/releases/tag/3.0) | [Rest Client ](https://github.com/eclipse/microprofile-rest-client/releases/tag/3.0)|
| [Config ](https://github.com/eclipse/microprofile-config/releases/tag/3.0)  | [Fault Tolerance](https://github.com/eclipse/microprofile-fault-tolerance/releases/tag/4.0) | [Metrics](https://github.com/eclipse/microprofile-metrics/releases/tag/4.0)|
| [JWT Auth ](https://github.com/eclipse/microprofile-jwt-auth/releases/tag/2.0)  | [CDI](https://jakarta.ee/specifications/cdi/3.0/jakarta-cdi-spec-3.0.html) | [JSON-P](https://jakarta.ee/specifications/jsonp/2.0/apidocs/)|
| [JAX-RS ](https://jakarta.ee/specifications/restful-ws/3.0/jakarta-restful-ws-spec-3.0.html)  | [JSON-B](https://jakarta.ee/specifications/jsonb/2.0/jakarta-jsonb-spec-2.0.html) | [Jakarta Annotations](https://jakarta.ee/specifications/annotations/2.0/annotations-spec-2.0.html)|



## NoSQL
  Las bases de datos NoSQL se diseñaron para ofrecer escalabilidad horizontal, 
alto desempeño, manejo de datos no estructurados, soportar enormes volúmenes de 
datos.
Existen varias categorías de bases de datos NoSQL entre ellas:  
* Orientadas a documentos  
* Orientadas a columnas  
* Clave Valor  
* Grafos  


Estos 4 grupos abarcan una gran variedad de bases de datos, que podemos utilizar
en el desarrollo de proyectos de todo tipo.

## MongoDB
[MongoDB](https://www.mongodb.com/) una base de datos NoSQL orientada a 
documentos y grafos. En MongoDB no existen tablas, en
su lugar tenemos colecciones, y los documentos almacenan información que 
correspondería en una analogía a filas y columnas de una base  de datos relaciones.


Ventajas:  
* Orientada a documentos  
* Libre de esquema  
* Escalamiento horizontal  
* Muy eficiente con grandes volúmenes de datos.  


Consideraciones   
* No utilizan valores autoincrementables, para su implementación queda a 
consideración del desarrollador.   
* Emplea referencias, no se maneja el concepto de relaciones.  
* Tiene su propia sintaxis.  

En MongoDB los documentos son compuestos de pares clave:valor
\small
```json
{
   name: "Aristides",
   country: "Panama"
}
```
\normalsize


### MongoDB Query API 
 Es él [API](https://www.mongodb.com/docs/manual/query-api/) de MongoDB para 
interactuar con los datos almacenados en las colecciones.
Nos permite realizar operaciones C.R.U.D. y operaciones avanzadas con su sintaxis
propia, por ejemplo para consultar todos los documentos de una colección 
utilizamos:
\small
```shell
    db.collection.find();
```
\normalsize

Para filtrar por uno o más campos específicos

\small
```shell
    db.collection.find("name":"Aristides");
```
\normalsize
### MongoDB Java Driver
 Para comunicarnos con la base de datos MongoDB utilizamos él 
[driver](https://www.mongodb.com/docs/drivers/java-drivers/) oficial de Java, 
Podemos observar el ejemplo [oficial](https://www.mongodb.com/docs/drivers/java/sync/current/fundamentals/connection/connect/) 
para conectarse a una base de datos
 
\small  

```java
import org.bson.BsonDocument;
import org.bson.BsonInt64;
import org.bson.Document;
import org.bson.conversions.Bson;
import com.mongodb.MongoClientSettings;
import com.mongodb.MongoException;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;
public class MongoClientConnectionExample {
public static void main(String[] args) {

String uri =
"mongodb://user:pass@sample.host:27017/?maxPoolSize=20&w=majority";
try (MongoClient mongoClient = MongoClients.create(uri)) {
  MongoDatabase database = mongoClient.getDatabase("admin");
  try {
   Bson command = new BsonDocument("ping", new BsonInt64(1));
   Document commandResult = database.runCommand(command);
   System.out.println("Connected successfully to server.");
  } catch (MongoException me) {
    System.err.println("An error : " + me);
  }
  }
 }
}
```
\normalsize
**Nota:** 
Cuando utilices jmoordb-core, todo el código para gestionar la conexión será 
generado por el framework.



## Interfaces
  Una interface en java es un conjunto de métodos vacíos(hasta 
la llegada de Java 8 que permitía definir código en los métodos 
mediante el uso de la palabra reservada **default**) ,cuyo un comportamiento
o funcionalidad debe ser implementada.
Por ejemplo
\small
```java
   public interface Vehiculo {
    void inicar();
    void detener();

}
```
\normalsize

El comportamiento de los métodos iniciar() y detener() debe ser 
definido en las clases que la implementan.
Por ejemplo  
\small
```java
   public clase Sedan implements Vehiculo {  
    @Override
    void inicar(){
         System.our.println("iniciar");
    }
    void detener(){
         System.our.println("detener");
    }
}
```
\normalsize

 Las interfaces son importantes para el desarrollo de aplicaciones, ya que nos
permite que utilicemos sus métodos directamente sin preocuparnos de la clase que
la implemente, esto es muy útil para la generación de código en tiempo de 
compilación y su inserción en las diversas clases que la requieran.


## Genéricos 
Fue introducido en Java 5, y permite un tipo seguro de manejo de objetos de 
diversos tipos. En el sitio Oficial de Oracle puedes encontrar mayor información 
[Generics](https://docs.oracle.com/javase/tutorial/extra/generics/intro.html)
Ventajas: 
* Elimina la duplicidad de código, ya que podemos definir interfaces y métodos 
que pueden recibir cualquier tipo de datos y procesarlos.
* La gran mayoría de los frameworks actuales utilizan genéricos
\small
```java
   public interface Crud <E> {
    void save(E e);

}
```
\normalsize


Un ejemplo de la versión antigua de jmoordb utilizaba genéricos para procesar
en tiempo de ejecución las operaciones de conversión de entidades a documentos
y viceversa. Además de generar código para el driver Java de MongoDB, por ejemplo:
\small  
```java
     /**
     *
     * @param t
     * @param verifyID
     * @return
     */
public Boolean save(T t, Boolean... verifyID) {
try {
    Boolean verificate = true;
    if (verifyID.length != 0) {
        verificate = verifyID[0];

    }
    if (verificate) {

        T t_ = (T) findInternal(findDocPrimaryKey(t));

        if (t_ == null) {
            //
        } else {
         return false;
        }
    }

 getMongoDatabase().getCollection(collection).insertOne(toDocument(t));

    return true;

} catch (Exception e) {          
 //...
}
return false;
}
```
\normalsize

## Reflexion
 Es una API que es definido en el artículo en Oracle [Using Java Reflection](https://www.oracle.com/technical-resources/articles/java/javareflection.html).
Su definición indica que mediante esta API se permite analizar en tiempo de
 ejecución información sobre las clases e interactuar con ellas. De esta manera
un framework, puede conocer en tiempo de ejecución los métodos y atributos de 
una clase y realizar invocaciones a los métodos para asignar valores a los 
atributos, entre otras operaciones.
 Son enormes las ventajas que nos ofrece, pero también son uno de los principales
causantes del consumo de recursos y performance de las aplicaciones Java. La gran
mayoría de frameworks utilizan una combinación de genéricos y reflexión.
Por ejemplo, en la antigua versión de jmoordb usamos esta combinación para 
obtener en tiempo de ejecución información sobre las entidades y asignar valores 
de los documentos encontrados en las diversas acciones sobre las colecciones.
En el ejemplo, obtenemos mediante reflexión los métodos declarados de la clase
principalmente de la llave o llaves primaria, e invocamos el método get para 
obtener el valor y asignarlo a un documento que será usado como filtro de una
consulta a la colección.
\small  
```java
public Optional<T> findById(T t2) {
Document doc = new Document();
try {
    Object t = entityClass.newInstance();
    for (PrimaryKey p : primaryKeyList) {
        String name = "get" + util.letterToUpper(p.getName());
        Method method;
        try {
          method = entityClass.getDeclaredMethod(name);
          doc.put(p.getName(), method.invoke(t2));
          return find(doc);
         } catch (Exception e) { }              
    }
} catch (Exception e) {          
  ///
}
return Optional.empty();
}
```
\normalsize

 El método find es invocado y realizaba la consulta a la colección mediante el 
filtro creado por la llave primaria y el valor de retorno era convertido 
mediante genéricos al tipo de datos de la entidad.
Segmento de código que muestra la implementación del método find()
\small  
```java

@Override
public Optional<T> find(Document document) {
 try {
  MongoDatabase db = mongoClient().getDatabase(database);
  FindIterable<Document> iterable = db.getCollection(collection).
                                    find(document);
  tlocal = (T) iterableSimple(iterable);
  if (tlocal == null) {
     return Optional.empty();
  }
  return Optional.of(tlocal);
 } catch (Exception e) {          

 }
return Optional.empty();

}
```
\normalsize

Jmoordb-core prescinde del uso de genéricos y de reflexión, ya que el código se
genere en tiempo de complicación optimizada para cada entidad. No necesitamos
ejecutar procesos adicionales en tiempo de ejecución, lo que permite una mayor
eficiencia en el consumo de los recursos y nos permite obtener un código más 
nativo y manejable sobre el que tenemos mayor control.

## Java Annotation
La definición oficial de una anotación Java la puedes encontrar en 
[Java Tutorials Lesson Annotations](https://docs.oracle.com/javase/tutorial/java/annotations/)

 Las anotaciones nos ofrecen una serie de metadatos sobre el programa en sí mismo, 
pero no forman parte del mismo. Ellas no causan algún efecto en el programa
donde son usadas. Brindan información útil al compilador sobre el programa y
permiten al desarrollador definir información sin necesidad de utilizar archivos
de configuraciones adicionales para declararlos. Estas anotaciones también pueden 
ser leídas en tiempo de ejecución.

Por ejemplo [Jakarta Bean Validation](https://jakarta.ee/specifications/bean-validation/3.0/),
,ofrece anotaciones que permiten definir validaciones sobre atributos en tiempo
de ejecución.
\small  
```java
public class Usuario {

    @NotNull(message = "Nombre no debe ser null")
    private String nombre;
    
    @Email(message = "Email debe ser válido")
    private String email;
}
```
\normalsize

En jmoordb-core proporciona una serie de anotaciones que permiten declarar
entidades y sus tipos, al igual que anotaciones para las interfaces repositorios.
La anotación Entity, se usará para definir entidades y atributos que decoran
la anotación con valores predeterminados.   
Segmento de código que muestra la anotación Entity.
\small 
```java
import com.jmoordb.core.annotation.enumerations.JakartaSource;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Entity {
    String collection() default "";
    String database() default "{mongodb,database}";
    JakartaSource jakartaSource() default JakartaSource.JAKARTA;
}

```
\normalsize
de esta manera podemos definir las clases Java con esta anotación para que sean
consideradas como Entidades en Jmoordb-core.
\small  
```java
@Entity()
public class Oceano {
    @Id
    private String idoceano;
    @Column
    private String oceano;
}

@Entity(collection="tiposdatos")
public class Tiposdatos {

    @Id(autogeneratedActive = AutogeneratedActive.ON)
    private Long idtiposdatos;

    @Column
    private Double saldo;
    @Column
    private Integer edad;
    @Column
    private Date fecha;
}

```
\normalsize

¿Por qué es importante conocer sobre anotaciones, reflexión y genéricos?.
Es relevante ya que su uso es fundamental para el desarrollo de esta nueva 
versión de jmoord-core. Cuyo objetivo primario es el de mejorar el performance 
de las aplicaciones y generar código más nativo en tiempo de compilación, 
eliminando el uso de genéricos y reflexión. 
Otavio Santana mostró la viabilidad de utilización en 
[Jakarta NoSQL](https://jakarta.ee/specifications/nosql/1.0/), (especificación
Java de referencia para bases de datos NoSQL.)
  Por lo tanto, el camino a seguir más recomendable era evitar el empleo de
reflexión que se había utilizado en el antiguo framework Jmoordb. El proceso fue
interesante. Crear desde cero el nuevo framework, ampliar la utilización de 
anotaciones e implementar el procesamiento de las anotaciones en tiempo de 
compilación. La siguiente sección muestra que es Java Annotation Processing, 
y como se implementó para producir jmoordb-core.


## Java Annotation Processing
 La definición oficial de [Annotation Proccesor](https://docs.oracle.com/javase/8/docs/api/javax/annotation/processing/Processor.html)
Un procesador de anotaciones, procesa las anotaciones en tiempo de complicación
o ejecución ofreciendo funcionalidades como generación de código, verificación de 
errores. Estas se procesan en rondas (round), en las cuales se pueden procesar
las anotaciones utilizadas en las clases o interfaces Java.


 Describiremos como procesar la anotación **@Repository**  y la forma en que 
generamos el código en tiempo de compilación. Usaremos para las descripciones
algunos paquetes, entre ellos la interface javax.annotation.processing.Processor, 
y extenderemos javax.annotation.processing.AbstractProcessor que contiene los
métodos que nos servirán para procesar las anotaciones.


Recuerde que esta sección no describe a fondo Java Annotation Processing, solo
describe de manera general como se implementó dentro de jmoordb-core.
En el archivo javax.annotation.processing.Processor ubicado en la carpeta 
META-INF.services
<figure>
    ![javax.annotation.processing.Processor](imagenes/capitulo01/figure_javaxannotationprocessing.png)
    <figcaption></figcaption>
</figure>
Indique las clases Java que procesaran las anotaciones, por ejemplo estas deben
ser expresadas con los paquetes donde están ubicadas.
\small  
```java
com.jmoordb.core.processor.RepositoryProcessor
com.jmoordb.core.processor.EntityProcessor
com.jmoordb.core.processor.AutoImplementProcessor 
com.jmoordb.core.processor.AutosecuenceRepositoryProcessor
com.jmoordb.core.processor.JsonObjectProcessor

```
\normalsize
Se puede observar que tenemos algunas clases, la que nos interesa evaluar en este
momento es RepositoryProcessor, que aún no hemos desarrollado. Antes de proseguir
debemos recordar que deseamos procesar anotaciones, y, por lo tanto, declaramos 
algunas de estas anotaciones:
\small
```java
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.SOURCE)
public @interface Repository {

   Class<?> entity();

    JakartaSource jakartaSource() default JakartaSource.JAKARTA;

    String collection() default "";

    /**
     * database_name --> Es un nombre de base de datos que indique el
     * desarrollador {mongodb.database} --> Es el parametro en
     * Microorofile-config.properties
     *
     * @return
     */
    String database() default "{mongodb.database}";

}


@Target(ElementType.METHOD)
@Retention(RetentionPolicy.SOURCE)
public @interface Query {
  String  where() default "";
}


@Target(ElementType.METHOD)
@Retention(RetentionPolicy.SOURCE)
public @interface Find {
}
```
\normalsize
Omitiré varias anotaciones del framework para mostrar ejemplos de implementación.

Proceda a crear la clase RepositoryProcessor, indique donde está ubicada la 
anotación que desea procesar en @SupportedAnnotationTypes(), usted puede especificar
la versión mínima de Java que usara su procesador en @SupportedSourceVersion. La 
clase debe extender AbstractProcessor.
Con esto hemos generado la base para procesar la anotación @Repositorty.
\small
  
```java
package com.jmoordb.core.processor
//...
@SupportedAnnotationTypes(
        {"com.jmoordb.core.annotation.repository.Repository"})
@SupportedSourceVersion(SourceVersion.RELEASE_11)
public class RepositoryProcessor extends AbstractProcessor {
}
```
\normalsize
Esto es todo, por supuesto que no, pero es el inicio para manejar el procesamiento
de las anotaciones. Ahora definir el método process() que procesa la anotación, 
contiene dos parámetros Set<? extends TypeElement> contiene la anotación encontrada
y javax.annotation.processing.RoundEnvironment que es el objeto que nos permite
inspeccionar la anotación. Su valor de retorno es de tipo Boolean, true especifica
que no se llamará a otro procesador al finalizar el procesamiento de la anotación
si devuelve false otro procesador de anotaciones puede ser notificado de la
anotación.
\small
  
```java
@Override
public boolean process(Set<? extends TypeElement> annotations,
                                                  RoundEnvironment roundEnv) { }
```
\normalsize
La anotación se puede aplicar a muchas interfaces y estas pueden contener 
métodos donde se apliquen otras anotaciones.
\small
  
```java
@Repository(entity = Oceano.class, jakartaSource = JakartaSource.JAKARTA,
        database = "{mongodb.database}", collection = "oceano")
public interface OceanoRepository extends CrudRepository<Oceano, String>{ 
 @Find()
 public Set<Oceano> findByOceano(String oceano);
 
 @Query(where = "idoceano .eq. @idoceano")
 public List<Oceano> queryByIdOceanoSorted(String idoceano, Sorted sorted);
}

@Repository(entity = Tiposdatos.class)
public interface TiposdatosRepository extends CrudRepository<Tiposdatos,Long>{
      
}

```
\normalsize

Necesitamos procesar todos las interfaces que utilicen la anotación @Repository,
para ello mediante el método roundEnv.getElementsAnnotatedWith(), obtenemos un
Set<> de estos elementos que utilizan dicha anotación.
\small  
```java
Set<? extends Element> elements = 
                   roundEnv.getElementsAnnotatedWith(Repository.class);

List<String> uniqueIdCheckList = new ArrayList<>();

for (Element element : elements) {
      Repository repository = element.getAnnotation(Repository.class);
}
```
\normalsize


En el framework se definieron unas clases Supplier para analizar las anotaciones,
además de clases utilitarias que almacén la información de los métodos procesados,
de los datos, de la anotación.

\small  
```java
RepositoryData repositoryData = repositoryDataSupplier.get(
                                 RepositoryData::new, element);

RepositoryAnalizer repositoryAnalizer = RepositoryAnalizer.get(
     lement, messager, database, typeEntity, repositoryMethodList
       );
```
\normalsize
Podemos obtener información sobre los parámetros y valores de retorno de los
métodos que poseen una determinada anotación

\small  
```java
List<? extends VariableElement> parameters = 
                                 executableElement.getParameters();
if (parameters.size() <= 0) {

} else {
    /**
     * Se cargan los parámetros del método.
     */
    for (int i = 0; i < parameters.size(); i++) {
        VariableElement param = parameters.get(i);
        ParamTypeElement paramTypeElement = new ParamTypeElement.Builder()
             .type(param.asType().toString())
             .name(param.getSimpleName().toString())
             .build();

        paramTypeElementList.add(paramTypeElement);
    }
```
\normalsize
Es posible analizar otras anotaciones usadas dentro de la interface 
\small
  
```java
Query query = executableElement.getAnnotation(Query.class);
if (query != null) {

    if (!QueryAnalizer.analizer(query, element, executableElement, 
               typeEntity, repositoryMethod)) {
        messager.printMessage(Diagnostic.Kind.ERROR, 
                QueryAnalizer.getMessage(), element
                              );
    }
    repositoryMethod.setAnnotationType(AnnotationType.QUERY);

}
```
\normalsize
Al finalizar este proceso de análisis y obtención de información, se ejecuta la 
generación de código.  
\small  
```java
 generateJavaFile(repositoryData.getPackageOfRepository() + "." + 
   repositoryData.getInterfaceName() + "Impl", 
   repositorySourceBuilder.end());
```
\normalsize

En este método se genera el archivo
\small
```java
private void generateJavaFile(String qfn, String end) throws IOException {
 try {
   JavaFileObject sourceFile = processingEnv.getFiler().createSourceFile(qfn);
   Writer writer = sourceFile.openWriter();
   writer.write(end);
   writer.close();
  } catch (Exception e) {
   MessagesUtil.error(MessagesUtil.nameOfClassAndMethod() + " error() " 
                      + e.getLocalizedMessage());
   }
}
```
\normalsize
Tenga presente que esta sección solo es introductoria al procesamiento de 
anotaciones en Java y mostramos pequeños segmentos de como ha sido implementado 
en el framework. Entre los beneficios obtenidos está:
* Código más sencillo. 
* Reducimos el tamaño del framework comparado con jmoordb.
* Mejor control sobre el código generado
* Al generar código en tiempo de compilación se logra mejorar el performance
de la aplicación, ya que no utilizamos reflexión.
* Es vital el estudio del API Java para MongoDB, porque la generación del código
debe ser optimizada para su implementación.


En el siguiente capítulo iniciaremos explorando jmoordb-core para el desarrollo
de aplicaciones con bases de datos NoSQL.

## Resumen
En este capítulo se hizo una introducción a Jakarta EE, Microprofile, se explicó
brevemente interfaces, anotaciones, reflexión y Java Annotation Processing, con la
finalidad de que el lector se familiarice con los conceptos que se contemplan en
este libro.
En el siguiente capítulo, se hará una introducción a Jmoordb-core y los requisitos
necesarios para trabajar con el framework.