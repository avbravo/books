# Filtros entre fechas con JSONQUERY

### Cliente
- Genere un filtro de tipo Bson mediante 
```java
 DocumentUtil.createBsonBetweenDateWithoutHours(String fieldnamestart, Date datestartvalue, String fieldlimitname, Date datelimitvalue)
```
- Invoque el metodo en el enpoint pasandole el filtro convertido a Json
```java
  Integer totalRecords = boletasServices.countJsonQuery(DocumentUtil.bsonToJson(filter));
```
- En el cliente del endpoint usamos el encode ocumentUil.encodeJson(query), para hacer la petición al microservicio.
- Si usa Microprofile Client tambien debe usar el encode.
```java
   WebTarget webTarget
              = client.target(microservicesProducer.microservicesHost() + "/autentificacion/resources/boletas/countjsonquery")
               .queryParam("query", DocumentUil.encodeJson(query));

```
### Microservicios
- Recibe un String en la consulta
- Conbierte a Document mediante DocumentUtil.jsonToDocument(query)
- Ejecuta la consulta mediante entityRepository.count();


***
# Ejemplo de uso CONTADOR
## En el Controller del Cliente
- Se usa un objeto de tipo Paginator que se usa para moverse entre paginas en Jakarta Server 
```java
 public String filterDateSimple() {

        try {
/**
 * Filtra entre fechas y un filtro adicional
 */
            Bson filter0
                    = DocumentUtil.createBsonBetweenDateWithoutHours(
                            "fechainicial", startDate, "fechafinal", endDate);
            
             Bson filter = and(filter0, eq("departament.iddepartament", profile.getIddepartament()));
            
            
            Document sort = new Document("idboleta", -1);

            Integer totalRecords = boletasServices.countJsonQuery(DocumentUtil.bsonToJson(filter));
            paginator
                    = new Paginator.Builder()
                            .nameOfController(this.getClass().getSimpleName())
                            .page(1)
                            .totalRecords(totalRecords)
                            .numberOfPage(numberOfPages(totalRecords, rowPage.get()))
                            .rowsForPage(rowPage.get())
                            .query(JmoordbDocument.jsonToDocument(DocumentUtil.bsonToJson(filter)))
                            .sort(sort)
                            .title(rf.getMessage("filter.from") + DateUtil.dateFormatToString(startDate, "dd/MM/yyyy")
                                    + " " + rf.getMessage("filter.to") + DateUtil.dateFormatToString(endDate, "dd/MM/yyyy"))
                            .build();
            move(paginator);

        } catch (Exception e) {
            loggerServices.processException(JsfUtil.nameOfClass(), JsfUtil.nameOfMethod(), e, true);
        }
        return "";
    }
    
   ```
    
  ## En el services del Cliente
  BoletasServices.java
  - Necesitamos usar el encode para convertir las fechas
  - Note que este recive el parametro convertido a String
   ```java
  public Integer countJsonQuery(String query) {
        Integer total = 0;
        try {

            Client client = ClientBuilder.newClient();
            client.register(authentificationProducer.httpAuthenticationFeature());

            WebTarget webTarget
                    = client.target(microservicesProducer.microservicesHost() + "/autentificacion/resources/boletas/countjsonquery")
                             .queryParam("query", DocumentUil.encodeJson(query));

            Invocation.Builder invocationBuilder = webTarget.request(MediaType.APPLICATION_JSON);
            Response response = invocationBuilder.get();
            if (response.getStatus() == 201) {
                total = Integer.parseInt(response.readEntity(String.class));

            }

            if (response.getStatus() == 400) {
                exception = new Exception(response.readEntity(String.class));
                return 0;
            }

        } catch (Exception e) {
               exception =loggerServices.processException(JmoordbUtil.nameOfClass(),JmoordbUtil.nameOfMethod(), e,false);
             
             
             
        }

        return total;
    }
   ```
***
## Microservicios
- Recibe un String en la consulta
- Conbierte a Document mediante DocumentUtil.jsonToDocument(query)
- Ejecuta la consulta mediante entityRepository.count();
 ```java
 @GET
    @Path("/countjsonquery")
    @RolesAllowed({"admin"})
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})

    public Response countJsonQuery(@QueryParam("query") String query) {
        List<Boletas> suggestions = new ArrayList<>();
        try {

            Document docQuery = DocumentUtil.jsonToDocument(query);
            Integer total = 0;

            total = boletasRepository.count(docQuery);
            return Response.status(201).entity(total).build();

        } catch (Exception e) {

              loggerServices.processException(JsfUtil.nameOfClass(), JsfUtil.nameOfMethod(), e, true); 
             
     
            return Response.status(400).entity("error: " + boletasRepository.getException().getLocalizedMessage()).build();
        }
    }
```

***
# CONSULTA 

```java
@Override
    public void move(Paginator paginator) {
        try {
//Porque puede ser actualizado desde los botones del paginator
            this.paginator = paginator;

            //Si son varios paginator en la misma pagina
            /*
           1. Se declara un paginator para cada entidad
           2. En el xhtml se pasa cada paginator a cada componente
           3. En los metodos de filtros en los controlle que use el desarrollador coloca el paginator respectivo
           4. En el move puede validar mediante el getNameOfcontroller() cual es la entidad
           Paginator paginator = new Paginator(); Paginator paginatorOld = new Paginator();
           Paginator paginator User = new Paginator();
           
           
            switch (paginator.getNameOfController()) {
                case "boletas":

                    break;
                case "user":
                    
                    break;
            }
             */
            boletasList
                    = boletasServices.jsonQuery(
                            DocumentUtil.documentToJson(paginator.getQuery()),
                            DocumentUtil.documentToJson(paginator.getSort()),
                            paginator.getPage(),
                            paginator.getRowsForPage());
            if (boletasList == null || boletasList.isEmpty()) {
                JsfUtil.successMessage(rf.getMessage("warning.nohayboletas"));
            }
            boletasDataModel = new BoletasDataModel(boletasList);

        } catch (Exception e) {
            loggerServices.processException(JsfUtil.nameOfClass(), JsfUtil.nameOfMethod(), e, true);
        }
    } // </editor-fold>
```

Services
```java
public  List<Boletas> jsonQuery( String query ,  String sort,
     Integer pageNumber,  Integer rowForPage ){
        List<Boletas> suggestions = new ArrayList<>();
        try { 
            Client client = ClientBuilder.newClient();
            client.register(authentificationProducer.httpAuthenticationFeature());
            suggestions = client
                    .target(microservicesProducer.microservicesHost() + "/autentificacion/resources/boletas/jsonquery/")                    
                    .queryParam("query", DocumentUtil.encodeJson(query))
                    .queryParam("sort",DocumentUtil.encodeJson(sort))
                    .queryParam("pagenumber", pageNumber)
                    .queryParam("rowforpage", rowForPage)
                    .request(MediaType.APPLICATION_JSON)
                    .get(new GenericType<List<Boletas>>() {
                    });

        } catch (Exception e) {
               exception =loggerServices.processException(JmoordbUtil.nameOfClass(),JmoordbUtil.nameOfMethod(), e,false);                         
        }
        return suggestions;
    }

```

## MICROSERVICES
revisar
cambiar por los metodos correctos , revisar cuando se pasa la paginación
```java
 // <editor-fold defaultstate="collapsed" desc=" @Path("/jsonquery")">
    /**
     * Recive json para consulta y ordenaciones y paginacion y realiza la
     * busqueda
     *
     * @param query
     * @param sort
     * @param pageNumber
     * @param rowForPage
     * @return
     */
    @GET
    @Path("/jsonquery")
    @RolesAllowed({"admin"})
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})

    public List<Boletas> jsonQuery(@QueryParam("query") String query, @QueryParam("sort") String sort,
            @QueryParam("pagenumber") Integer pageNumber, @QueryParam("rowforpage") Integer rowForPage) {
        List<Boletas> suggestions = new ArrayList<>();
        try {

            Document docQuery = DocumentUtil.jsonToDocument(query);
            Document docSort = DocumentUtil.jsonToDocument(sort);

            suggestions = boletasRepository.findPagination(docQuery, pageNumber, rowForPage, docSort);

        } catch (Exception e) {
            loggerServices.processException(JsfUtil.nameOfClass(), JsfUtil.nameOfMethod(), e, true); 
             
     

        }

        return suggestions;
    }

// </editor-fold>
    // <editor-fold defaultstate="collapsed" desc="     @Path("/jsonquerywithoutpagination")">
    /**
     * Hace cosultas sin paginacion
     *
     * @param query
     * @param sort
     * @return
     */
    @GET
    @Path("/jsonquerywithoutpagination")
    @RolesAllowed({"admin"})
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})

    public List<Boletas> jsonQueryWithoutPagination(@QueryParam("query") String query, @QueryParam("sort") String sort) {
        List<Boletas> suggestions = new ArrayList<>();
        try {

            Document docQuery = DocumentUtil.jsonToDocument(query);
            Document docSort = DocumentUtil.jsonToDocument(sort);

            suggestions = boletasRepository.findBy(docQuery, docSort);

        } catch (Exception e) {
            loggerServices.processException(JsfUtil.nameOfClass(), JsfUtil.nameOfMethod(), e, true); 
             
     

        }

        return suggestions;
    }

// </editor-fold>
```
