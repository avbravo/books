 \newpage

\begin{flushright}
\section{Capítulo 6}
\end{flushright}

## Java Drivers MongoDB
   Este capítulo muestra el código que el framework genera al momento de compilar 
el proyecto. Usted no tiene que programarlo, sin embargo es importante conocer 
como funciona el driver oficial de [Java para MongoDB](https://www.mongodb.com/docs/drivers/java-drivers/), 
lo que ayuda a entender jmoordb-core. Es importante tener presente que el driver
oficial nos ofrece dos formas de trabajar:
* [Java Driver](https://www.mongodb.com/docs/drivers/java/sync/current) para aplicaciones sincronas
* [Reactive Streams Driver](https://www.mongodb.com/docs/drivers/reactive-streams/) , para procesar 
Stream asíncronos.

Hemos visto en los capítulos anteriores necesitamos en primer lugar indicar la 
configuración del framework en el archivo microprofile-config.properties, 
luego definir las entidades y crear las interfaces repositorio para cada entidad.
Recuerde que puede tener entidades que estarán embebidas o otras referenciadas 
en diversas colecciones.
Luego proceda a crear la clase MongoDBManagerProducer.

 Para comunicarnos con MongoDB mediante Java necesitamos establecer una comunicación 
con la base de datos. Esta puede ser una base de datos local o en un servidor,
o en MongoDB Atlas, indistintamente del lugar donde se encuentre, se establece 
una comunicación básica.

## MongoDBManagerProducer  

  Como recordara en el archivo microprofile-config.properties establecimos la 
conexión a la base de datos.

\small
```
#mongodb.uri=mongodb+srv://conexion-mongodb-atlas/;
mongodb.uri=mongodb://localhost:27017
```
\normalsize

Usted debe crear la clase **MongoDBManagerProducer.java**, esta clase no la 
genera el framework, su función es producir un MongoClient, que se usara en las 
implementaciones de los repositorios.

A continuación se muestra la clase MongoDBManagerProducer

\small
```java
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import java.io.Serializable;
import javax.enterprise.inject.Disposes;
import javax.enterprise.inject.Produces;
import org.eclipse.microprofile.config.Config;
import org.eclipse.microprofile.config.inject.ConfigProperty;

@ApplicationScoped
public class MongoDBManagerProducer implements Serializable {

    @Inject
    private Config config;
    @Inject
    @ConfigProperty(name = "mongodb.uri")
    private String mongodburi;

    
    @Produces
    @ApplicationScoped
    public MongoClient mongoClient() {
           MongoClient mongoClient = MongoClients.create(mongodburi);
          return mongoClient;
    }

    public void close(@Disposes final MongoClient mongoClient) {
        mongoClient.close();
    }
}

```
\normalsize

Ahora pasemos a entender qué ocurre con las entidades y repositorios

## ¿Qué es un EntitySupplier

 Cuando analizamos los entitys podemos encontrar entidades muy básicas, 
con solo algunos atributos y otras más complejas, con documentos embebidos
y/o referenciados, con diferentes formas de operar sobre ellos. Cada entidad 
que utilice la anotación @Entity generará una clase con la siguiente sintaxis:
**EntitySupplier.java**, la cual se encargará de realizar la conversión de 
documentos a objetos Java y viceversa.
 Entendiendo que para documentos referenciados hay dos formas de manejarlos
una que corresponde  a una referencia tradicional en la cual mediante la llave
primaria se busca en la colección referenciada y el otro mecanismo, se efectúa
mediante el uso de documento embebido sobre la referencia en el cual busca en el
mismo documento y lo carga como si fuese un documento referenciado mejorando
de manera considerable el tiempo de respuestas y el performance de la aplicación,
si usa esta alternativa debe implementar consultas externas para verificar si los
datos han sido modificados.

  Una entidad básica  como mencionamos anteriormente tiene atributos simples
definidos que representan los elementos de un documento en MongoDB.
Considere la colección océano.

\small
```json
oceano{
      idoceano:"pacifico",
      oceano: "Oceano Pacifico"
}
```
\normalsize

Definimos una entidad Java que represente documentos de la colección océano

\small
```java
@Entity()
public class Oceano {
    @Id
    private String idoceano;
    @Column
    private String oceano;

    public Oceano() {
    }

   //setter/getter
}

```
\normalsize


  Cuando se compila el proyecto , jmoordb-core, genera de forma automática la clase
OceanoSupplier.java en el mismo paquete donde se encuentra la entidad definida.
El método get recibe dos argumentos, un Supplier del tipo de la entidad y un
objeto Document de MongoDB que contiene el documento a procesar.
Se verifican los elementos que componen la entidad Oceano.java y se asignan los 
valores invocando el método set de cada atributo ,realizando la conversión 
correspondiente al tipo de datos obtenido de Document. Al finalizar el proceso, 
todos los atributos de la entidad océano han sido asignados con los valores 
correspondientes.

\small
```java
import jakarta.enterprise.context.RequestScoped;
import java.io.Serializable;
import java.util.function.Supplier;
import com.jmoordb.core.util.MessagesUtil;
import org.bson.Document;

@RequestScoped
public class OceanoSupplier implements Serializable {
    public Oceano get(Supplier<? extends Oceano> s, Document document) {
        Oceano oceano = s.get();
        try {
            oceano.setIdoceano(document.getString("idoceano"));
            oceano.setOceano(document.getString("oceano"));

        } catch (Exception e) {
            MessagesUtil.error(MessagesUtil.nameOfClassAndMethod() + " " 
                             + e.getLocalizedMessage());
        }
        return oceano;
    }
}

```
\normalsize

### Atributos Java primitivos
  Algunos tipos de datos primitivos, tales como int, float, e incluso ObjectId de MongoDB
son soportados directamente por el framework.

\small
```java
public class Tiposdatos{
    @Column
    private ObjectId objectId;
    @Column
    private Float flotante;
    @Column
    private int intprimitivo;
}
```
\normalsize

Al compilar el proyecto, el Supplier contendría un código similar a este en el 
método get(), donde se realizaran las asignaciones.

\small
```java
   tiposdatos.setObjectId(document.getObjectId("objectId"));
   tiposdatos.setFlotante((Float)document.get("flotante"));
   tiposdatos.setIntprimitivo((int)document.get("intprimitivo"));
```
\normalsize


### Colecciones
    Las entidades pueden contener atributos que representan una colección de elementos
tales como List<>, Set<>, o Stream<>.
Por ejemplo

\small
```java
public class Tiposdatos{
    @Column
    private List<String> apodos;
    @Column
    private List<Double> ventas;
    @Column
    private Set<String> deportes;
    @Column
    private Stream<String> valores;
}
```
\normalsize

Al compilar el proyecto el Supplier contendría un código similar al siguiente
con las conversiones apropiadas a cada tipo de datos.

\small
```java
tiposdatos.setApodos(document.getList("apodos",java.lang.String.class));
tiposdatos.setVentas(document.getList("ventas",java.lang.Double.class));
tiposdatos.setDeportes(new java.util.HashSet<>(document.getList("deportes",
                                               java.lang.String.class)));
tiposdatos.setValores(document.getList("valores",java.lang.String.class).stream());
```
\normalsize

### Embebidos
   En el capítulo de entidades se indicó como definir entidades embebidas
y los diversos tipos que se soportan, Simple, List<>, Set<>, Stream<>. En el 
ejemplo siguiente podemos ver el soporte para estas entidades.

\small
```java
@Entity
public class Tiposdatos{
    @Embedded
    private Idioma idioma;

    @Embedded
    private List<Musica> musica;
    @Embedded
    private Set<Persona> persona;

    @Embedded
    private Stream<Corregimiento> corregimiento;
}
```
\normalsize

Para procesar documentos embebidos se utiliza [JSON-B](https://jakarta.ee/specifications/jsonb/2.0/jakarta-jsonb-spec-2.0.html=.
Los documentos embebidos los obtenemos de la misma manera que cualquier otro
atributo mediante el método **get("nombre-atributo");** dependiendo del tipo
de retorno se hacen las conversiones necesarias. Como se puede observar en el 
siguiente segmento de código.

\small
```java
// Embedded idioma
Document doc = (Document) document.get("idioma");
Jsonb jsonb = JsonbBuilder.create();
Idioma idioma = jsonb.fromJson(doc.toJson(), Idioma.class);
tiposdatos.setIdioma(idioma);
// Embedded List<musica>
List<Musica> musicaList = new ArrayList<>();
List<Document> musicaDoc = (List) document.get("musica");
for( Document docMusica : musicaDoc){
        Musica musica = jsonb.fromJson(docMusica.toJson(),Musica.class);
        musicaList.add(musica);
};
tiposdatos.setMusica(musicaList);
// Embedded Set<persona>
List<Persona> personaList = new ArrayList<>();
List<Document> personaDoc = (List) document.get("persona");
for( Document docPersona : personaDoc){
        Persona persona = jsonb.fromJson(docPersona.toJson(),Persona.class);
        personaList.add(persona);
};
tiposdatos.setPersona(new java.util.HashSet<>(personaList));
// Embedded Stream<corregimiento>
List<Corregimiento> corregimientoList = new ArrayList<>();
List<Document> corregimientoDoc = (List) document.get("corregimiento");
for( Document docCorregimiento : corregimientoDoc){
        Corregimiento corregimiento = jsonb.fromJson(docCorregimiento.toJson(),
                                                     Corregimiento.class);
        corregimientoList.add(corregimiento);
};
tiposdatos.setCorregimiento(corregimientoList.stream());
```
\normalsize


### Referenciados
  Las referencias nos permiten realizar consultas a otros colecciones. Que se han
establecido mediante la anotación @Referenced. Esta referencia se efectúa mediante
campos llaves. Un comportamiento similar a las relaciones en bases de datos relacionales.
Jmoordb-core permite ejecutar consultas en el mismo documento sin necesidad de 
efectuar una consulta a otra colección. Para esta operación usted debe definir 
el atributo **typeReferenced = TypeReferenced.EMBEDDED**, de manera predeterminada 
está asignado a **typeReferenced = TypeReferenced.REFERENCE**.
El siguiente segmento de código muestra las definiciones de documentos referenciados

\small
```java
@Entity
public class Tiposdatos{
@Referenced(from = "oceano", localField = "oceano.idoceano")
private Oceano oceano;

@Referenced(from = "idprofesion", localField = "profesion.idprofesion")
private Profesion profesion;

@Referenced(from = "planeta", localField = "planeta.idplaneta")
private List<Planeta> planeta;

@Referenced(from = "grupoprofesion", 
                   localField = "grupoprofesion.idgrupoprofesion" )
private Stream<Grupoprofesion> grupoprofesion;

@Referenced(from = "edificio", localField = "edificio.idedificio",
                    typeReferenced = TypeReferenced.REFERENCED)
private Set<Edificio> edificio;

@Referenced(from = "museo", localField = "museo.idmuseo",
            typeReferenced = TypeReferenced.EMBEDDED)
private List<Museo> museo;
}
```
\normalsize

List<Museo> museo, es una referencia de tipo **EMBEDDED**, esté referenciada como
embebida al especificar **typeReferenced = TypeReferenced.EMBEDDED**, solo se 
buscará en el mismo Tiposdatos y no en la colección Museo. En todos los otros casos
se consultará en las colecciones referenciadas.
En el método get se invoca el método findByPk() de cada repositorio para realizar 
búsquedas mediante la llave primaria.

Código generado en el proceso de compilación del proyecto:

\small
```java
@RequestScoped
public class TiposdatosSupplier  implements Serializable{

@Inject
OceanoRepository oceanoRepository ;
@Inject
ProfesionRepository profesionRepository ;
@Inject
PlanetaRepository planetaRepository ;
@Inject
GrupoprofesionRepository grupoprofesionRepository ;
@Inject
EdificioRepository edificioRepository ;
@Inject
com.jmoordbcore.processor.example.repository.MuseoRepository museoRepository ;

   // Referenced oceano
    String idOceano= document.getString("oceano.idoceano");
     Optional<Oceano> oceanoOptional = oceanoRepository.findByPk(idOceano);
    if(oceanoOptional.isPresent()){
            tiposdatos.setOceano(oceanoOptional.get());
    }
    // Referenced profesion
    Long idProfesion= document.getLong("profesion.idprofesion");
    Optional<Profesion> profesionOptional = profesionRepository.findByPk(
                                                                idProfesion);
    if(profesionOptional.isPresent()){
            tiposdatos.setProfesion(profesionOptional.get());
    }
    // Referenced List<planeta>
     List<String>PlanetaPKList = (List)document.get("planeta.idplaneta");
    List<Planeta> planetaList = new ArrayList<>();
    for(String index :PlanetaPKList){
             Optional<Planeta> planetaOptional = planetaRepository.findByPk(
                                                                    index);
            if(planetaOptional.isPresent()){
                    planetaList.add(planetaOptional.get());
            }
    }
    tiposdatos.setPlaneta(planetaList);
    // Referenced Stream<grupoprofesion>
     List<String>GrupoprofesionPKList = (List)document.get(
                                        "grupoprofesion.idgrupoprofesion");
    List<Grupoprofesion> grupoprofesionList = new ArrayList<>();
    for(String index :GrupoprofesionPKList){
             Optional<Grupoprofesion> grupoprofesionOptional = 
                                     grupoprofesionRepository.findByPk(index);
            if(grupoprofesionOptional.isPresent()){
                    grupoprofesionList.add(grupoprofesionOptional.get());
            }
    }
    tiposdatos.setGrupoprofesion(grupoprofesionList.stream());
    // Referenced Set<edificio>
     List<Long>EdificioPKList = (List)document.get("edificio.idedificio");
    List<Edificio> edificioList = new ArrayList<>();
    for(Long index :EdificioPKList){
             Optional<Edificio> edificioOptional = edificioRepository.findByPk(
                                                                         index);
            if(edificioOptional.isPresent()){
                    edificioList.add(edificioOptional.get());
            }
    }
    tiposdatos.setEdificio(new java.util.HashSet<>(edificioList));
    // Embedded List<museo>
    List<Museo> museoList = new ArrayList<>();
    List<Document> museoDoc = (List) document.get("museo");
    for( Document docMuseo : museoDoc){
            Museo museo = jsonb.fromJson(docMuseo.toJson(),Museo.class);
            museoList.add(museo);
    };
    tiposdatos.setMuseo(museoList);
}
```
\normalsize



## ¿Cómo se generan RepositoryImpl?
 En esta sección se muestra el código generado para las implementaciones de las
interfaces repositorio. Es recomendable que los repositorios hereden de 
**CrudRepository**, ya que permite al desarrollador contar con los métodos que 
realizan las operaciones C.R.U.D.. Reduciendo la cantidad de métodos que debe
escribir en la interface.

Recordemos que **CrudRepository** cuenta con la siguiente estructura:

\small
```java
public interface CrudRepository<T, PK> {

  @Save
  public Optional<T> save(T t);

  @Update
  public Boolean update(T t);

  @Find()
  public List<T> findAll();

  @Find()
  public List<T> findAllPagination(Pagination pagination);

  @Find()
  public List<T> findAllSorted(Sorted sorted);

  @Find()
  public List<T> findAllPaginationSorted(Pagination pagination, Sorted sorted);
   
  public Optional<T> findByPk(PK id);
    
  @DeleteBy
  public Long deleteByPk(PK id);
}
```
\normalsize 

 Cuando se procesa la anotación @Repository, que hereda de CrudRepository, esta 
generara la implementación de los métodos definidos en la interface. El método
findByPk(), será utilizado también desde los **EntitySupplier**, para realizar
las búsquedas de un documento por su llave primaria.


#### Llave primaria String
    Acontinuación se define una interface sin métodos cuyo campo llave es de 
tipo String.

\small
```java
@Repository(entity = Oceano.class, jakartaSource = JakartaSource.JAKARTA,
        database = "{mongodb.database}", collection = "oceano")
public interface OceanoRepository extends CrudRepository<Oceano, String> {
}
```
\normalsize 

Generaria el código para realizar el C.R.U.D. completo, a continuación se muestra
por secciones detallando de forma general, los aspectos más relevantes del mismo.

* En la primera fase se crea una clase de alcance @ApplicationScoped, la cual implementa
la interfaces Repository.
* La segunda etapa consiste en inyectar, la configuración que establecimos en el archivo
microprofile-config.properties, obteniendo el nombre de la base de datos y de la colección, 
si estos fueron especificados en la interface Repositorio, estos tendrán precedencia sobre
los establecidos en el archivo de configuración.
* La tercera fase se inyectarán el repositorio que genera autoincremento y
la clase Supplier correspondiente a la entidad respectivamente.

\small
```java
@ApplicationScoped
public class OceanoRepositoryImpl  implements OceanoRepository{
// <editor-fold defaultstate="collapsed" desc="inject">

  @Inject
  MongoClient mongoClient;
/**
* Microprofile Config
*/
 @Inject
 private Config config;
 @Inject
 @ConfigProperty(name = "mongodb.database")
 private String mongodbDatabase;

 private String mongodbCollection = "oceano";
/**
* AutogeneratedRepository
*/
 @Inject
 AutogeneratedRepository autogeneratedRepository;
/**
* Supplier
*/
 @Inject
  OceanoSupplier oceanoSupplier;
// </editor-fold>

```
\normalsize 


 Para todos los métodos que se generan, requerirán de una comunicación con la 
base de datos y trabajar con la colección. Por lo tanto, observará un segmento 
de código muy similar al siguiente:

\small
```java
@Override
public Optional<Oceano> save(Oceano oceano) {
   try {
       MongoDatabase database = mongoClient.getDatabase(mongodbDatabase);
       MongoCollection<Document> collection = database.getCollection(
                                                       mongodbCollection);
   } catch (Exception e) {
       MessagesUtil.error(MessagesUtil.nameOfClassAndMethod() + " " +
                         e.getLocalizedMessage());
   }
   return Optional.empty();
}

```
\normalsize 

 El tipo de valor de retorno puede variar dependiendo de la definición del método.
Para verificar si un documento existe en la colección se realiza una consulta 
mediante el método findByPK().

\small
```java
if (findByPk(oceano.getIdoceano()).isPresent()) { 
  MessagesUtil.warning("There is already a record with that id");
  return Optional.of(oceano);
}               
```
\normalsize 

 El framework en esta versión soporta la base de datos MongoDB, por tal razón, 
es necesario convertir las entidades Java en documentos para ser insertados dentro
de la colección.
Utilizamos el API JSON-B, para este proceso, lo que facilita su uso para 
operaciones como inserciones o actualizaciones.

\small
```java
   Jsonb jsonb = JsonbBuilder.create();
   Document.parse(jsonb.toJson(oceano));
   
```
\normalsize 


Para insertar registros en MongoDB utilizamos **collection,insertOne()**

\small
```java
 Jsonb jsonb = JsonbBuilder.create();
 InsertOneResult insertOneResult = collection.insertOne(Document.parse(
                                                        jsonb.toJson(oceano)));
 if (insertOneResult.getInsertedId() != null) {
    return Optional.of(oceano);
 }
   
```
\normalsize 

Si se trata de actualizaciones , primero verificar que exista el documento en la
colección. La comprobación se realiza mediante filtros, que definimos utilizando 
la llave primaria y luego ejecutar **collection.updateOne()**

\small
```java
Bson filter = Filters.empty();
filter = Filters.eq("idoceano",oceano.getIdoceano());
Jsonb jsonb = JsonbBuilder.create();
UpdateResult result = collection.updateOne(filter, Document.parse(jsonb.toJson(oceano)));
if (result.getModifiedCount() > 0) {
   return Boolean.TRUE;
}
```
\normalsize 

 Para las consultas obtenemos los valores de retorno en un objeto **Cursor** de 
MongoDB, este objeto lo podemos recorrer para obtener los valores que posee. 
Por ejemplo, para una implementación del método findAll(), recorremos el cursor 
para convertir el documento a una entidad Java se ejecuta  mediante el Supplier 
de la entidad correspondiente. Si es un documento embebido o referenciado,
todo el control y manejo se realiza en el Supplier correspondiente.

\small
```java
MongoCursor<Document> cursor;
cursor = collection.find().iterator();
try{
  while (cursor.hasNext()) {
        list.add(oceanoSupplier.get(Oceano::new, cursor.next()));
  }
} finally {
     cursor.close();
} 
```
\normalsize 


Para aplicar paginación y ordenación, se utilizan mediante objetos Paginator, 
Sorted,  que son aplicados a skip(), limit() y sort() de la clase Collection de MongoDB,

\small
```java
MongoCursor<Document> cursor;
cursor = collection.find()
        .skip(pagination.skip())
        .limit(pagination.limit())
        .sort(sorted.getSort())
        .iterator();

```
\normalsize 


El método **findByPk()** genera el filtro obteniendo el nombre del atributo
identificado como llave primaria en la entidad, es decir, el que contiene la
anotación @Id. El tipo de parámetro puede ser String o Long, dependiendo
de la declaración del tipo de datos en la entidad.

\small
```java
Document doc = collection.find(eq("idoceano", id)).first();
Oceano oceano = oceanoSupplier.get(Oceano::new, doc);
return Optional.of(oceano);

```


Para eliminar un registro MongoDB nos ofrece la instrucción collecction.deleteOne(),
que debe ser utilizada aplicando un filtro del documento respectivo a eliminar, 
generalmente se utiliza la llave primaria para su identificación.

\small
```java
Bson filter = Filters.eq("idoceano",id);
DeleteResult deleteResult = collection.deleteOne(filter);

```
\normalsize 


Los **Filtros** corresponden a condiciones en las que tenemos más de un operador, 
ya sea que se han creado mediante cláusulas where() o mediante el uso de nombres 
de métodos.  MongoDB posee una sintaxis particular para manejarlos usando 
[Operadores Query y Projections](https://www.mongodb.com/docs/manual/reference/operator/aggregation/filter/)
Jmoordb-core genera estas instrucciones en orden de precedencia de izquierda a derecha, 
por lo tanto debe prestar mucha atención al momento de construir sus consultas

Por ejemplo:

\small
```java
@Find()
public List<Oceano> findByIdOceanoAndOceanoNotFecha(String idoceano,
                                                    String oceano, Date fecha);
```
\normalsize 
Genera el siguiente filtro 

\small
```java
Bson filter =Filters.and(
                 Filters.eq("idOceano",idoceano)
                ,Filters.eq("oceano",oceano)
                ,Filters.not(
                   Filters.eq("fecha",fecha)
                 )
	    );
```
\normalsize 


En el siguiente ejemplo manejos más condiciones para el filtro que debe ser generado.

\small
```java
@Query(where = "idoceano .eq. @idoceano .and. oceano .eq. @oceano .not. fecha 
               .gt. @fecha .or. activo .eq. @activo .and. km .gt. @km")
    public List<Oceano> queryByIdOceanoAndOceanoNotFechaOrActivoAndKmSorted(
                        String idoceano, String oceano, Date fecha, 
                        String activo, Integer km, Sorted sorted);
```
\normalsize 


Jmoordb-core, procesa las anotaciones, verifica que los parámetros coincidan con
los definidos en la anotación **@Query** y genera los filtros en base a estas
condiciones. Continuación el segmento de código correspondiente:

\small
```java
Bson filter = Filters.and(
        Filters.eq("idoceano", idoceano),
         Filters.eq("oceano", oceano),
         Filters.not(
                Filters.gt("fecha", fecha)
        ),
         Filters.or(
                Filters.eq("activo", activo)
        ),
         Filters.and(
                Filters.gt("km", km)
        )
);

```
\normalsize 


#### Llave primaria Long
 Las llaves primarias de tipo Long, pueden manejarse de dos formas diferentes
una en la cual el desarrollador establece que valores serán indicados en 
la aplicación y en la otra mediante el uso de valores con autoincremento o 
secuencias numéricas. Para el primer caso se comporta como una entidad con un tipo
String, ya que no hay condiciones especiales, Para la segunda condición debemos
permitir que se incorpore el control te auto secuencia que vimos en los capítulos
previos a este.
Asumimos que existe una entidad con un atributo @Id definido como autoincrementable

\small
```java
@Entity
public class Tiposdatos {
  @Id(autogeneratedActive = AutogeneratedActive.ON)
  private Long idtiposdatos;
}
```
\normalsize 


Se define el  repositorio indicando que la llave primaria es tipo Long.

\small
```java
@Repository(entity = Tiposdatos.class)
public interface TiposdatosRepository extends  CrudRepository<Tiposdatos, Long>{   
}

```
\normalsize 

Al compilar el proyecto los dos métodos que varian un poco , el método findByPk()

\small
```java
public Optional<Tiposdatos> findByPk(Long id ) {
try {
MongoDatabase database = mongoClient.getDatabase(mongodbDatabase);
MongoCollection<Document> collection = database.getCollection(mongodbCollection);
Document doc = collection.find(eq("idtiposdatos", id)).first();
Tiposdatos tiposdatos = tiposdatosSupplier.get(Tiposdatos::new, doc);
return Optional.of(tiposdatos);
} catch (Exception e) {
 MessagesUtil.error(MessagesUtil.nameOfClassAndMethod() + " " 
                    + e.getLocalizedMessage());
}
return Optional.empty();
}

```
\normalsize 


El cambio principal se produce en el método save, ya que antes de insertar el
documento se utiliza el método genérate del repositorio autogenerate, que toma
el valor de esa colección como un auto-incrementable y lo incrementa en una única
operación, de manera que nos permite gestionar los valores secuenciales con 
facilidad.
En el siguiente segmento contiene el código completo del método save(), que previamente
fue mencionado en el capítulo 5.

\small
```java
@Override
public Optional<Tiposdatos> save(Tiposdatos tiposdatos) {
    try {
   MongoDatabase database = mongoClient.getDatabase(mongodbDatabase);
   MongoCollection<Document> collection = database.getCollection(
                                          mongodbCollection);
   if (findByPk(tiposdatos.getIdtiposdatos()).isPresent()) { 
       MessagesUtil.warning("There is already a record with that id");
      return Optional.of(tiposdatos);
   }
   tiposdatos.setIdtiposdatos(autogeneratedRepository.generate(mongodbDatabase, 
                            mongodbCollection));
   Jsonb jsonb = JsonbBuilder.create();
   InsertOneResult insertOneResult = collection.insertOne(Document.parse(
                                                    jsonb.toJson(tiposdatos)));
   if (insertOneResult.getInsertedId() != null) {
      return Optional.of(tiposdatos);
   }
 } catch (Exception e) {
      MessagesUtil.error(MessagesUtil.nameOfClassAndMethod() + " " +
       e.getLocalizedMessage());
 }
 return Optional.empty();
}
```
\normalsize 

Si desea ver todos los métodos generados, puede compilar el proyecto de ejemplo
que se distribuye con este libro.


## Resumen
 En este capítulo se muestra el código que genera el framework, para las entidades
y repositorios, Se explica de manera general las funcionalidades internas.
En el siguiente capitulo se tratara sobre
**pendiente hasta que se defina el capitulo**