<p class="titulo-capitulo">CAPÍTULO 5</p>

## REST

<figure>
    ![Entity](imagenes/entity.png)
    <figcaption></figcaption>
</figure>


01.04.03.10 @Rest
- Define los endpoints en los métodos
- Use APIDocumentation para documentar el API
- repositoryMethod() --> Es la firma del método del repositorio que se usara si esta vació o se indica {method.similar} indoca que es la misma firma del método. Si se usa un valor diferente este debe existir en la interface Repository. Por ejemplo: {List<Entity> findAll().
- Analizar cuando es un método que usa Paginatio, Sorted y Search que viajan de manera diferente desde el RestClient.

```java
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.SOURCE)
public @interface Rest {
  String  path() ;
  String description() default "";
  ResponseType responseType() default ResponseType.GET;
  ProducesType[] producesType();
 //  MediaType[] produces() default [MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON];
  APIDocumentation[]  apiDocumentation() default {@APIDocumentation(code="",description = "")} ;
  /**
   * {method.similar} Indica que la firma del método del repositorio es la misma que el método declarado
   * {fima_methodo_repositorio} --> Es la firma del método del repositorio que se usara.
   * @return 
   */

  String repositoryMethod() default "{method.similar}";
}

```


```java
@Target(value = {ElementType.METHOD})
@Retention(value = RetentionPolicy.RUNTIME)
@Inherited
public @interface APIDocumentation {

    String code() default "";

    String description() default "";

    String name() default "";

    ProducesType producesType() default ProducesType.JSON;
    
     boolean required() default false;
     boolean readOnly() default false;
 public Class<?> implementation() default Void.class;


}
```



Ejemplo

```java
import com.jmoordb.core.annotation.controller.APIDocumentation;
import com.jmoordb.core.annotation.controller.WebResouce;
import com.jmoordb.core.annotation.controller.enumerations.ProducesType;
import com.jmoordb.core.annotation.controller.enumerations.ResponseType;
import com.jmoordb.core.annotation.enumerations.JakartaSource;
import com.jmoordbcore.processor.example.model.Oceano;
import com.jmoordbcore.processor.example.repository.OceanoRepository;
import java.util.Collection;
import java.util.List;
import com.jmoordb.core.annotation.controller.Rest;
import java.util.Optional;

/**
 *
 * @author avbravo
 */
@WebResouce(path = "oceano", name = "Retrievel Oceano", descripcion = "Get value of Oceano", repository = OceanoRepository.class,
        jakartaSource = JakartaSource.JAKARTA, roles = {"admin", "manager"})
public interface OceanoController {

    @Rest(  path = "/findall",
            repositoryMethod =  "{List<Oceano> findAll()}",
            responseType = ResponseType.GET, producesType = {ProducesType.XML, ProducesType.JSON},
            apiDocumentation = {
                @APIDocumentation(code = "200", description = "The oceanos", producesType = ProducesType.JSON, required = true, implementation = Collection.class, readOnly = true),
                @APIDocumentation(code = "500", description = "Server unavailable")
            }
    )
    public List<Oceano> findAll();
    
    /**
     * Sin documentación
     * @param idoceano
     * @return 
     */
    @Rest(  path = "/findbyidoceano", 
            repositoryMethod =  "Optional<Oceano> findByIdoeano(String idoceano)",
            responseType = ResponseType.GET, producesType = {ProducesType.XML, ProducesType.JSON}            
    )
    public Optional<Oceano> findByIdoceano(String idoceano);
    
    
    @Rest(  path = "/findall", 
            repositoryMethod =  "{method.similar}",
            responseType = ResponseType.GET, producesType = {ProducesType.XML, ProducesType.JSON},
            apiDocumentation = {
                @APIDocumentation(code = "200", description = "The oceanos", producesType = ProducesType.JSON, required = true, implementation = Collection.class, readOnly = true),
                @APIDocumentation(code = "500", description = "Server unavailable")
            }
    )
    public List<Oceano> findByIceeano(String idoceano);

}


```


## @WebResouce
@WebResource se implementa a nivel de clases
- Genera Resoruces JAX-RS
```java
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.SOURCE)
public @interface WebResouce {

    Class<?> repository();

    JakartaSource jakartaSource() default JakartaSource.JAKARTA;

    String path() default "";

   String[] roles() default {};

    String name() default "";

    String descripcion() default "";

    /**
     * database_name --> Es un nombre de base de datos que indique el
     * desarrollador {mongodb.database} --> Es el parametro en
     * Microorofile-config.properties
     *
     * @return
     */
}

```
