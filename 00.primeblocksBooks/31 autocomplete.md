
# 31. Autocomplete

## En el Microservicio

Crear en repository likeBy

```java

@Repository(database = "{mongodb.database1}", entity = Area.class)
public interface AreaRepository extends CrudRepository<Area, Long>{
       @Lookup
public List<Area> lookup(Search search);
  @Count()
    public Long count(Search... search);
      @LikeBy(caseSensitive = CaseSensitive.NO, typeOrder = TypeOrder.ASC)
    public List<Area> likeByArea(String area);
    

}


```

Controller

```java
/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.sft.poa.controller;

import com.jmoordb.core.model.Search;
import com.jmoordb.core.util.ConsoleUtil;
import com.jmoordb.core.util.DocumentUtil;
import com.jmoordb.core.util.MessagesUtil;
import com.sft.model.Area;
import com.sft.model.History;
import com.sft.repository.AreaRepository;
import com.sft.repository.HistoryRepository;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import org.eclipse.microprofile.metrics.MetricUnits;
 
import org.eclipse.microprofile.metrics.annotation.Timed;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.enums.SchemaType;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.parameters.Parameter;
import org.eclipse.microprofile.openapi.annotations.parameters.RequestBody;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

/**
 *
 * @author avbravo
 */
@Path("area")
@Tag(name = "Información del area", description = "End-point para entidad Area")
@RolesAllowed({"admin"})
public class AreaController {

    
    // <editor-fold defaultstate="collapsed" desc="Inject">
    @Inject
    AreaRepository areaRepository;
    
      @Inject
HistoryRepository historyRepository;



// </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="findAll">
    @GET
    @RolesAllowed({"admin"})
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    @Timed(name = "areaesFindAll",
            description = "Monitorea el tiempo en que se obtiene la lista de todos los areaes",
            unit = MetricUnits.MILLISECONDS, absolute = true)
    @Operation(summary = "Obtiene todos los areaes", description = "Retorna todos los areaes disponibles")
    @APIResponse(responseCode = "500", description = "Servidor inalcanzable")
    @APIResponse(responseCode = "200", description = "Los areaes")
    @Tag(name = "BETA", description = "Esta api esta en desarrollo")
    @APIResponse(description = "Los areaes", responseCode = "200", content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = Collection.class, readOnly = true, description = "los areaes", required = true, name = "areaes")))
    public List<Area> findAll() {
        
        return areaRepository.findAll();
    }
// </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Area findByIdarea">
    @GET
    @RolesAllowed({"admin"})
    @Path("{idarea}")
    @Operation(summary = "Busca un area por el idarea", description = "Busqueda de area por idarea")
    @APIResponse(responseCode = "200", description = "El area")
    @APIResponse(responseCode = "404", description = "Cuando no existe el idarea")
    @APIResponse(responseCode = "500", description = "Servidor inalcanzable")
    @Tag(name = "BETA", description = "Esta api esta en desarrollo")
    @APIResponse(description = "El area", content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = Area.class)))
    public Area findByIdarea(
            @Parameter(description = "El idarea", required = true, example = "1", schema = @Schema(type = SchemaType.NUMBER)) @PathParam("idarea") Long idarea) {

      

        return areaRepository.findByPk(idarea).orElseThrow(
                () -> new WebApplicationException("No hay area con idarea " + idarea, Response.Status.NOT_FOUND));

    }
// </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Response save">
    @POST
    @RolesAllowed({"admin"})
    @Operation(summary = "Inserta un nuevo area", description = "Inserta un nuevo area")
    @APIResponse(responseCode = "201", description = "Cuanoo se crea un  area")
    @APIResponse(responseCode = "500", description = "Servidor inalcanzable")
    @Tag(name = "BETA", description = "Esta api esta en desarrollo")
    public Response save(
            @RequestBody(description = "Crea un nuevo area.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Area.class))) Area area) {

 Optional<Area> areaOptional=areaRepository.save(area);
        if(areaOptional.isPresent()){
                  saveHistory(area);
               return Response.status(201).entity(areaOptional.get()).build();
        }else{
              return Response.status(400).entity("Error " + areaRepository.getJmoordbException().getLocalizedMessage()).build();
        }
    }
// </editor-fold>
    // <editor-fold defaultstate="collapsed" desc="Response update">

    @PUT
    @RolesAllowed({"admin"})
    @Operation(summary = "Inserta un nuevo area", description = "Inserta un nuevo area")
    @APIResponse(responseCode = "201", description = "Cuanoo se crea un  area")
    @APIResponse(responseCode = "500", description = "Servidor inalcanzable")
    @Tag(name = "BETA", description = "Esta api esta en desarrollo")
    public Response update(
            @RequestBody(description = "Crea un nuevo area.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Area.class))) Area area) {


       if(areaRepository.update(area)){
              saveHistory(area);
               return Response.status(201).entity(area).build();
        }else{
              return Response.status(400).entity("Error " + areaRepository.getJmoordbException().getLocalizedMessage()).build();
        }
    }
// </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Response delete">
    @DELETE
    @RolesAllowed({"admin"})
    @Path("{idarea}")
    @Operation(summary = "Elimina un area por  idarea", description = "Elimina un area por su idarea")
    @APIResponse(responseCode = "200", description = "Cuando elimina el area")
    @APIResponse(responseCode = "500", description = "Servidor inalcanzable")
    @Tag(name = "BETA", description = "Esta api esta en desarrollo")
    public Response delete(
            @Parameter(description = "El elemento idarea", required = true, example = "1", schema = @Schema(type = SchemaType.NUMBER)) @PathParam("idarea") Long idarea) {

       if(areaRepository.deleteByPk(idarea) ==0L){
              return Response.status(201).entity(Boolean.TRUE).build();
        }else{
            return Response.status(400).entity("Error " + areaRepository.getJmoordbException().getLocalizedMessage()).build();
        }
    }
    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="List<Area> lookup(@QueryParam("filter") String filter, @QueryParam("sort") String sort, @QueryParam("page") Integer page, @QueryParam("size") Integer size)">

    @GET
    @Path("lookup")
    @RolesAllowed({"admin"})
    @Operation(summary = "Busca un area", description = "Busqueda de area por search")
    @APIResponse(responseCode = "200", description = "Area")
    @APIResponse(responseCode = "404", description = "Cuando no existe la condicion en el search")
    @APIResponse(responseCode = "500", description = "Servidor inalcanzable")
    @Tag(name = "BETA", description = "Esta api esta en desarrollo")
    @APIResponse(description = "El search", content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = Area.class)))

    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})

    public List<Area> lookup(@QueryParam("filter") String filter, @QueryParam("sort") String sort, @QueryParam("page") Integer page, @QueryParam("size") Integer size) {
        List<Area> suggestions = new ArrayList<>();
        try {

        Search search = DocumentUtil.convertForLookup(filter, sort, page, size);
        suggestions = areaRepository.lookup(search);

        } catch (Exception e) {
       
          MessagesUtil.error(MessagesUtil.nameOfClassAndMethod() + "error: " + e.getLocalizedMessage());
        }

        return suggestions;
    }

    // </editor-fold>
    
    
    // <editor-fold defaultstate="collapsed" desc="Long count(@QueryParam("filter") String filter, @QueryParam("sort") String sort, @QueryParam("page") Integer page, @QueryParam("size") Integer size)">

    @GET
    @Path("count")
    @RolesAllowed({"admin"})
    @Operation(summary = "Cuenta ", description = "Cuenta area")
    @APIResponse(responseCode = "200", description = "contador")
    @APIResponse(responseCode = "404", description = "Cuando no existe la condicion en el search")
    @APIResponse(responseCode = "500", description = "Servidor inalcanzable")
    @Tag(name = "BETA", description = "Esta api esta en desarrollo")
    @APIResponse(description = "El search", content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = Area.class)))

    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})

    public Long count(@QueryParam("filter") String filter, @QueryParam("sort") String sort, @QueryParam("page") Integer page, @QueryParam("size") Integer size) {
       Long result = 0L;
        try {

        Search search = DocumentUtil.convertForLookup(filter, sort, page, size);
        result = areaRepository.count(search);

        } catch (Exception e) {
       
          MessagesUtil.error(MessagesUtil.nameOfClassAndMethod() + "error: " + e.getLocalizedMessage());
        }

        return result;
    }

    // </editor-fold>
    
    
    
      // <editor-fold defaultstate="collapsed" desc="private void saveHistory(Area area)">
    
    private void saveHistory(Area area){
        try {
                History history = new History.Builder()                 
               .collection("area")
                    .idcollection(area.getIdarea().toString())
                    .database("sft")
                    .data(area.toString())
                    .actionHistory(area.getActionHistory().get(area.getActionHistory().size()-1)                  )
                     .build();
            historyRepository.save(history);
        } catch (Exception e) {
           ConsoleUtil.error("saveHistory() "+e.getLocalizedMessage());
        }
    }
     
    
// </editor-fold>
    
    
    
     // <editor-fold defaultstate="collapsed" desc="List<Area> likeByName(@QueryParam("area") String area)">

    @GET
    @Path("likearea")
    @RolesAllowed({"admin"})
    @Operation(summary = "Busca un user", description = "Busqueda de user usando like%")
    @APIResponse(responseCode = "200", description = "Area")
    @APIResponse(responseCode = "404", description = "Cuando no existe la condicion en el search")
    @APIResponse(responseCode = "500", description = "Servidor inalcanzable")
    @Tag(name = "BETA", description = "Esta api esta en desarrollo")
    @APIResponse(description = "El search", content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = Area.class)))

    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})

    public List<Area> likeByArea(@QueryParam("area") String area) {
        List<Area> suggestions = new ArrayList<>();
        try {

       
        suggestions = areaRepository.likeByArea(area);

        } catch (Exception e) {
       
          MessagesUtil.error(MessagesUtil.nameOfClassAndMethod() + "error: " + e.getLocalizedMessage());
        }

        return suggestions;
    }

    // </editor-fold>
}



````


## Cliente

AreaCliente

```java
RegisterRestClient()
@Path("/area")
@ClientHeaderParam(name = "Authorization", value = "{lookupAuth}")
//@ApplicationScoped
public interface AreaRestClient {

    // <editor-fold defaultstate="collapsed" desc="lookupAuth()">
    default String lookupAuth() {
        /**
         * *
         * Leer las configuraciones del archivo microprofile-config.properties
         */

        String secretKey = "SCox1jmWrkma$*opne2Amwz";

        Config config = ConfigProvider.getConfig();

        String userSecurity = config.getValue("userSecurity", String.class);

        // or
        ConfigValue passwordSecurity = config.getConfigValue("passwordSecurity");

        String userDecrypted = Encryptor.decrypt(userSecurity, secretKey);
        String passwordDecrypted = Encryptor.decrypt(passwordSecurity.getValue(), secretKey);

        return "Basic "
                + Base64.getEncoder().encodeToString((userDecrypted + ":" + passwordDecrypted).getBytes());
    }
// </editor-fold>
    // <editor-fold defaultstate="collapsed" desc="findAll">
    @GET
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public List<Area> findAll() ;
// </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Area findByIdarea">
    @GET
    @Path("{idarea}")
    public Area findByIdarea(
            @Parameter(description = "El idarea", required = true, example = "1", schema = @Schema(type = SchemaType.NUMBER)) @PathParam("idarea") Long idarea);
// </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Response save">
    @POST
   public Response save(
            @RequestBody(description = "Crea un nuevo area.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Area.class))) Area area);
    
// </editor-fold>
    // <editor-fold defaultstate="collapsed" desc="Response update">

    @PUT
    public Response update( @RequestBody(description = "Crea un nuevo area.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Area.class))) Area area);
// </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Response delete">
    @DELETE
    @Path("{idarea}")
       public Response delete(
            @Parameter(description = "El elemento idarea", required = true, example = "1", schema = @Schema(type = SchemaType.NUMBER)) @PathParam("idarea") Long idarea) ;

    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="List<Area> lookup(@QueryParam("filter") String filter, @QueryParam("sort") String sort, @QueryParam("page") Integer page, @QueryParam("size") Integer size)">

    @GET
    @Path("lookup")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public List<Area> lookup(@QueryParam("filter") String filter, @QueryParam("sort") String sort, @QueryParam("page") Integer page, @QueryParam("size") Integer size);
        

    // </editor-fold>
    
    
    // <editor-fold defaultstate="collapsed" desc="Long count(@QueryParam("filter") String filter, @QueryParam("sort") String sort, @QueryParam("page") Integer page, @QueryParam("size") Integer size)">

    @GET
    @Path("count")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public Long count(@QueryParam("filter") String filter, @QueryParam("sort") String sort, @QueryParam("page") Integer page, @QueryParam("size") Integer size) ;

    // </editor-fold>
    
    
    
   
     // <editor-fold defaultstate="collapsed" desc="List<Area> likeByName(@QueryParam("area") String area)">

    @GET
    @Path("likearea")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})

    public List<Area> likeByArea(@QueryParam("area") String area);

    // </editor-fold>
}



```

AreaServices.java

```java
import com.sft.model.Area;
import java.util.List;
import java.util.Optional;
import org.bson.Document;
import org.bson.conversions.Bson;

/**
 *
 * @author avbravo
 */

public interface AreaServices {

  
// <editor-fold defaultstate="collapsed" desc="findAll">
      public List<Area> findAll() ;
// </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Area findByIdarea(Long idarea)">
     public Area findByIdarea(Long idarea);
// </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Optional<Area> save(Area area)">

   public Optional<Area> save(Area area);
    
// </editor-fold>
    // <editor-fold defaultstate="collapsed" desc="Boolean update(  Area area)">

    public Boolean update(  Area area);
// </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Response delete">
       public Boolean delete( Long idarea) ;

    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="List<Area> lookup( Bson filter, Document sort,  Integer page,  Integer size">
        public List<Area> lookup( Bson filter, Document sort,  Integer page,  Integer size);
        

    // </editor-fold>
    
    
    // <editor-fold defaultstate="collapsed" desc="Long count( Bson filter, Document sort, Integer page,  Integer size)">

    public Long count(Bson filter, Document sort,  Integer page,  Integer size) ;

    // </editor-fold>
    
    
    
    
    // <editor-fold defaultstate="collapsed" desc="List<Area> likeByArea( String area)">

    public List<Area> likeByArea(String area);
    // </editor-fold>
    
  
  
}


```


AreaServicesImpl.java


```java

import com.avbravo.jmoordbutils.FacesUtil;
import com.avbravo.jmoordbutils.JmoordbResourcesFiles;
import com.avbravo.jmoordbutils.encode.EncodeUtil;
import com.sft.model.Area;
import com.sft.services.AreaServices;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.eclipse.microprofile.openapi.annotations.enums.SchemaType;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.parameters.Parameter;
import com.sft.restclient.AreaRestClient;

/**
 *
 * @author avbravo
 */
@ApplicationScoped
public class AreaServicesImpl implements AreaServices{ 
  // <editor-fold defaultstate="collapsed" desc="@Inject">
    @Inject
    JmoordbResourcesFiles rf;
   // </editor-fold>
    // <editor-fold defaultstate="collapsed" desc="Microprofile Rest Client">
    @Inject
   AreaRestClient areaRestClient;
// </editor-fold>
  
// <editor-fold defaultstate="collapsed" desc="findAll">
      public List<Area> findAll() {
          return areaRestClient.findAll();
      }
// </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Area findByIdarea">
     public Area findByIdarea( @Parameter(description = "El idarea", required = true, example = "1", schema = @Schema(type = SchemaType.NUMBER)) @PathParam("idarea") Long idarea){
         return areaRestClient.findByIdarea(idarea);
     }
// </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Response save">

    @Override
   public Optional<Area> save( Area area){
        try {

            Response response = areaRestClient.save(area);

            if (response.getStatus() == 400) {

                String error = (response.readEntity(String.class));

                return Optional.empty();
            }

          Area result = (Area) (response.readEntity(Area.class));

            return Optional.of(result);

        } catch (Exception e) {
            FacesUtil.errorMessage(FacesUtil.nameOfClassAndMethod() + " " + e.getLocalizedMessage());
        }
        return Optional.empty();
        
   }
    
// </editor-fold>
    // <editor-fold defaultstate="collapsed" desc="Response update">

    public Boolean update( Area area){
         Boolean result = Boolean.FALSE;
         try {
             
        
        Integer status = areaRestClient.update(area).getStatus();
        
        if(status == 201){
            result = Boolean.TRUE;
        }
             
        } catch (Exception e) {
         FacesUtil.errorMessage(FacesUtil.nameOfClassAndMethod() + " " + e.getLocalizedMessage());
        }
        return result;
    }
// </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Boolean delete(Long idarea)">
       public Boolean delete(Long idarea) {
           Boolean result = Boolean.FALSE;
         try {
             
        
        Integer status = areaRestClient.delete(idarea).getStatus();
        
        if(status == 201){
            result = Boolean.TRUE;
        }
             
        } catch (Exception e) {
         FacesUtil.errorMessage(FacesUtil.nameOfClassAndMethod() + " " + e.getLocalizedMessage());
        }
        return result;
       }

    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="List<Area> lookup(@QueryParam("filter") String filter, @QueryParam("sort") String sort, @QueryParam("page") Integer page, @QueryParam("size") Integer size)">
    @Override
    public List<Area> lookup( Bson filter, Document sort,  Integer page,  Integer size){
    List<Area> areaList = new ArrayList<>();
        try {
            areaList = areaRestClient.lookup(
                        EncodeUtil.encodeBson(filter),
                        EncodeUtil.encodeBson(sort),
                        page, size);
        } catch (Exception e) {
         FacesUtil.errorMessage(FacesUtil.nameOfClassAndMethod() + " " + e.getLocalizedMessage());
        }
        return areaList;
}
        

    // </editor-fold>
    
    
    // <editor-fold defaultstate="collapsed" desc="Long count(Bson filter, Document sort, Integer page, Integer size)">

    public Long count(Bson filter, Document sort, Integer page, Integer size) {
         Long result = 0L;
        try {
             result = areaRestClient.count(
                        EncodeUtil.encodeBson(filter),
                        EncodeUtil.encodeBson(sort),
                        page, size);
        } catch (Exception e) {
         FacesUtil.errorMessage(FacesUtil.nameOfClassAndMethod() + " " + e.getLocalizedMessage());
        }
        return result;
    }

    // </editor-fold>
    
    
    
    
    // <editor-fold defaultstate="collapsed" desc="List<Area> likeByArea( String area)">

    public List<Area> likeByArea( String area){
             return areaRestClient.likeByArea(area);
    }
    // </editor-fold>
    

}



```



### xhtml

```xhtml

<p:tab title="#{msg['tab.area']}">
                        <div class="field col-12 md:col-4">
                            <span class="font-medium text-500">#{msg['field.area']}</span>
                            <p:autoComplete id="area" multiple="true" value="#{dashboardFaces.areaList}"
                                            completeMethod="#{dashboardFaces.completeArea}"
                                            var="area" itemLabel="#{area.area}" itemValue="#{area}"
                                            forceSelection="true"
                                            title="#{msg['autocomplete,minimo3caracteres']}"
                                            dropdown="true"
                                            minQueryLength="3"
                                            style = "width: 450px !important;"
                                            inputStyle = "width: 450px !important;" 
                                            converter="#{areaConverter}"
                                            >
                                <p:column>
                                    <h:outputText style="vertical-align: middle; margin-left: .5rem" value="#{area.area}"/>
                                </p:column>
                                <p:ajax event="itemSelect"   listener="#{dashboardFaces.autocompleteSelectedEvent}"  update=":form:growl" />  
                            </p:autoComplete>


                        </div>
                    </p:tab>

```



### DashboardFaces.java

```java
    @Inject
    AreaServices areaServices;

  private List<Area> areaList = new ArrayList<>();


   // <editor-fold defaultstate="collapsed" desc="List<Area> cargarArea(String query)">
    private List<Area> cargarArea(String query) {
       List<Area> result = new ArrayList<>();
        Boolean found = Boolean.FALSE;
        try {
            List<Area> list = areaServices.likeByArea(query);
            if (areaList.isEmpty()) {
                return list;
            } else {
                for (Area aDB : list) {
                    found = Boolean.FALSE;
                    for (Area dv : areaList) {
                        if (aDB.getIdarea().equals(dv.getIdarea())) {
                            found = Boolean.TRUE;
                            break;
                        }
                    }
                  
                    if (!found) {
                        result.add(aDB);
                    }
                }

            }
        } catch (Exception e) {
            FacesUtil.errorMessage(FacesUtil.nameOfClassAndMethod() + " " + e.getLocalizedMessage());
        }
        return result;
    }
// </editor-fold>

  // <editor-fold defaultstate="collapsed" desc="List<Area> completeArea(String query)">

    public List<Area> completeArea(String query) {

        List<Area> result = new ArrayList<>();
        try {
            query = query.trim();
            result = cargarArea(query);

            //result = userViewList.stream().filter(t -> t.getName().toLowerCase().contains(query)).collect(Collectors.toList());
        } catch (Exception e) {

            FacesUtil.errorMessage(FacesUtil.nameOfClassAndMethod() + " " + e.getLocalizedMessage());
        }

        return result;
    }
// </editor-fold>
    



```
