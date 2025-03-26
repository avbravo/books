## Antes de iniciar
### MongoDB 
Inicié MongoDB desde el Shell de docker
## Ejectuamos MonogDB
```shell
docker-compose up -d

Ver la imagen

docker ps -a

Entrar al bash
docker exec -it e321ee10e65e bash

Ejecutar 

mongo
```
---
### Quarkus 
Tener instalado Quarkus cómo se muestra en el capítulo 7 del libro.
# Crear proyecto
 
quarkus create && cd quarkusdemo

**Ejecutar el proyecto**

quarkus dev

""Ir al navegador**
http://localhost:8080/


##  clonar el proyecto

https://github.com/avbravo/getting-started

## Observar la clase generada GreetingResource,java
Revisar que quarkus genera el archivo de comfiguracion application.properties

### Proyecto NetBeans 
Clonar el proyecto 
> configurar el archivo micro profile config.properties
> Revisar el nombre de basé de datos usado
> Tener creado el producer
> Tener creado el AutoincrementRepository
> Revisar las dependencias maven
---
## Cuando inicia la presentación

> Mostrar el archivo Producer

> Mostrar el repository Autosequence

> Cree la entidad persona
- Primero hacerlo sin JakartarSource y luego con legacy

@Entity(jakartaSource = JakartaSource.JAVAEE_LEGACY)

public class Persona {
    @Id
    private String idpersona;
    
    @Column
    private String nombre;

    public Persona() {
    }

## Repository
- Crear repositorio simple-
- Primero sin JakartaSource
@Repository(entity = Persona.class, jakartaSource = JakartaSource.JAVAEE_LEGACY)
public interface PersonaRepository extends CrudRepository<Persona, String> {
    
}


## crear PersonaContoller
@Path("/persona")
public class PersonaController {
    @Inject
    PersonaRepository personaRepository;
    
    @GET
  @Produces(MediaType.APPLICATION_JSON)
    public List<Persona> findAll(){
        return personaRepository.findAll();
    }
}




use practicadb

db.persona.insertOne({idpersona:"1", nombre:"Ana"})

db.persona.insertOne({idpersona:"2",  nombre:"Maria"})

```


Luego ejecutar desde el navegador
http://localhost:8080/persona


Buscar por el id

Browser
http://localhost:8080/persona/2



No obtendremos nada hasta que la base de datos este activa, pero podemos crear un end-point
que nos indique el estatus, dentro de respository


@Ping
public Boolean ping();

## Ahora crearlo en el endpoint

@GET
    @Path("/ping")
    @Produces(MediaType.APPLICATION_JSON)
    public Boolean ping(){
        return personaRepository.ping();
    }

Busqueda por nomnbre

@GET
    @Path("/find/{nombre}")
    @Produces(MediaType.APPLICATION_JSON)
    public Persona findByName(@PathParam("nombre") String nombre ){
        return personaRepository.findByNombre(nombre).get();
    }



Repository

  @Find
    public Optional<Persona> findByNombre(String nombre);


Browser
http://localhost:8080/persona/find/Ana


## Agregar otro con @Query
   @Query(where = "idpersona .eq. @idpersona")
    public Optional<Persona> queryIdpersona(String idpersona);

En el endpoint
 @GET
    @Path("/query/{idpersona}")
    @Produces(MediaType.APPLICATION_JSON)
    public Persona queryByIdpersona(@PathParam("idpersona") String idpersona ){
        return personaRepository.queryIdpersona(idpersona).get();
    }




import java.util.List;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import org.acme.model.Persona;
import org.acme.repository.PersonaRepository;

/**
 *
 * @author avbravo
 */
@Path("/persona")
public class PersonaController {
    @Inject
    PersonaRepository personaRepository;
    
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Persona> findAll(){
        return personaRepository.findAll();
    }
    
    @GET
    @Path("{idpersona}")
    @Produces(MediaType.APPLICATION_JSON)
    public Persona queryByIdpersona(@PathParam("idpersona") String idpersona){
        return personaRepository.queryByIdpersona(idpersona).get();
    }
    
    @GET
    @Path("/find/{nombre}")
    @Produces(MediaType.APPLICATION_JSON)
    public Persona findByNombre(@PathParam("nombre") String nombre){
        return personaRepository.findByNombre(nombre).get();
    }
}



import java.util.List;
import javax.inject.Inject;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.acme.model.Persona;
import org.acme.repository.PersonaRepository;

@Path("/persona")
public class PersonaController {

    @Inject
    PersonaRepository personaRepository;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Persona> findAll() {
        return personaRepository.findAll();
    }

    @GET
    @Path("{idpersona}")
    public Persona queryById(@PathParam("idpersona") String idpersona) {
        return personaRepository.queryIdpersona(idpersona).orElseThrow(
                () -> new WebApplicationException("No hay persona con el id " + idpersona, Response.Status.NOT_FOUND));
    }
    
    @GET
    @Path("/find/{nombre}")
    @Produces(MediaType.APPLICATION_JSON)
    public Persona findByNombre(@PathParam("nombre") String nombre ){
        return personaRepository.findByNombre(nombre).orElseThrow(
                () -> new WebApplicationException("No hay persona con el nombre " + nombre, Response.Status.NOT_FOUND));
    }

    @GET
    @Path("/ping")
    public Boolean ping() {
        return personaRepository.ping().booleanValue();
    }
    
    @POST
    public Response save(Persona persona) {
        return Response.status(Response.Status.CREATED).entity(personaRepository.save(persona)).build();
    }

    @DELETE
    @Path("{idpersona}")
    public Response delete(@PathParam("idpersona") String idpersona) {
      personaRepository.deleteByPk(idpersona);
        return Response.status(Response.Status.NO_CONTENT).build();
    }

}
