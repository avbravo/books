
# 28. Validar que no se repita nombre
Usamos un lookup para hacer la busqueda por las condiciones proyecto.idproyecto y nombre.

Usamos lookup para ello
```java
@RegisterRestClient()
@Path("/sprint")
@ClientHeaderParam(name = "Authorization", value = "{lookupAuth}")
//@ApplicationScoped
public interface SprintClient {

// <editor-fold defaultstate="collapsed" desc="List<Sprint> lookup(@QueryParam("filter") String filter, @QueryParam("sort") String sort,  @QueryParam("page") Integer page, @QueryParam("size") Integer size)">
    @GET
    @Path("lookup")
    public List<Sprint> lookup(@QueryParam("filter") String filter, @QueryParam("sort") String sort, @QueryParam("page") Integer page, @QueryParam("size") Integer size);

    // </editor-fold>    
    // <editor-fold defaultstate="collapsed" desc="public Long count(@QueryParam("filter") String filter, @QueryParam("sort") String sort, @QueryParam("page") Integer page, @QueryParam("size") Integer size);">
    @GET
    @Path("count")
    public Long count(@QueryParam("filter") String filter, @QueryParam("sort") String sort, @QueryParam("page") Integer page, @QueryParam("size") Integer size);
    // </editor-fold>    


}


```


Agregar al services
```java

public interface SprintServices {
    
    public List<Sprint> openSprintList(Proyecto proyecto);
    public Boolean haveOpenSprint(Proyecto proyecto);
    public Boolean save(Sprint sprint);
    
    
    public List<Sprint> lookup( Bson filter, Document sort, Integer page, Integer size);
    public Long count(Bson filter, Document sort, Integer page, Integer size);
    
    public Boolean haveDocumentsBySprintAndProject(Proyecto proyecto, Sprint sprint);
    
    
}

```

## SprintServiceImpl

```java

@ApplicationScoped
public class SprintServicesImpl implements SprintServices {

 // <editor-fold defaultstate="collapsed" desc="Boolean haveDocumentsBySprintAndProject(Proyecto proyecto, Sprint sprint) ">
/**
 * Verifica si tiene un Sprint con ese nombre para el proyecto
 * 
 * @param proyecto
 * @param sprint
 * @return 
 */
    @Override
    public Boolean haveDocumentsBySprintAndProject(Proyecto proyecto, Sprint sprint) {
        Boolean result = Boolean.FALSE;
        try {
             Bson filter = and(eq("proyecto.idproyecto", proyecto.getIdproyecto()),
                    eq("sprint", sprint.getSprint())
            );
            Document sort = new Document("idsprint", -1);
            Integer  total = count(filter,sort, 1, 1).intValue();

                    
           if(total >= 1){
             
               result = Boolean.TRUE;
           }
        } catch (Exception e) {
            FacesUtil.errorMessage(FacesUtil.nameOfClassAndMethod() + " " + e.getLocalizedMessage());
        }
        return result;
        

    }
    // </editor-fold>

}

```


## En la Pagina Faces

```java

  public String save() {
        try {
            if (sprintServices.haveDocumentsBySprintAndProject(proyectoSelected, sprintSelected)) {
                FacesUtil.warningDialog(rf.fromCore("warning.save"),rf.fromCore("warning.exitsotherdocumentwiththisname"));
                return "";

            }
...
}

```
