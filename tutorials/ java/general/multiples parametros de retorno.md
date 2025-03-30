# Multiples parametros de retorno


## Ejemplo 1

Faces
```java

  @Inject
    ClienteServices clienteServices;


 Map.Entry<Boolean, String> valid = sedeServices.isValid(sedeSelected);

if (!valid.getKey()) {
    FacesUtil.warningDialog(rf.fromCore("warning.warning"), rf.fromMessage(valid.getValue()));
    return;
}

```

Services

```java
public interface SedeServices {
      public Map.Entry<Boolean, String> isValid(Sede sede);  
       public Boolean validEmail(String email);
}

```


Implementacion

```java
@ApplicationScoped
public class SedeServicesImpl implements SedeServices {

    @Override
    public Map.Entry<Boolean, String> isValid(Sede sede) {
        Map.Entry<Boolean, String> result;
        try {
            if (sede.getIdsede() == null || sede.getIdsede().equals("")) {
                result = new AbstractMap.SimpleEntry<>(Boolean.FALSE, "warning.ingreseidsede");
            } else {
                if (sede.getNombre() == null || sede.getNombre().equals("")) {
                    result = new AbstractMap.SimpleEntry<>(Boolean.FALSE, "warning.ingresesede");
                } else {
                    if (sede.getDireccion() == null || sede.getDireccion().equals("")) {
                        result = new AbstractMap.SimpleEntry<>(Boolean.FALSE, "warning.ingresedireccion");
                    } else {
                        if (sede.getTelefono() == null || sede.getTelefono().equals("")) {
                            result = new AbstractMap.SimpleEntry<>(Boolean.FALSE, "warning.ingresetelefono");
                        } else {
                            if (sede.getEmail() == null || sede.getEmail().equals("")) {
                                result = new AbstractMap.SimpleEntry<>(Boolean.FALSE, "warning.ingreseemail");
                            } else {
                                   if(!validEmail(sede.getEmail())){
                                       result = new AbstractMap.SimpleEntry<>(Boolean.FALSE, "warning.emailnovalido");
                                   }else{
                                       result = new AbstractMap.SimpleEntry<>(Boolean.TRUE, "");
                                   }
                                
                            }
                        }
                    }
                }

            }
            return result;

        } catch (Exception e) {
        }
        return new AbstractMap.SimpleEntry<>(Boolean.FALSE, "");
    }

    
    
     @Override
    public Boolean validEmail(String email) {
        Boolean result = Boolean.FALSE;
        try {
            Pattern pattern = Pattern.compile("^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$", Pattern.CASE_INSENSITIVE);
            Matcher matcher = pattern.matcher(email);
            result = matcher.find();

        } catch (Exception e) {
            FacesUtil.errorMessage(FacesUtil.nameOfClassAndMethod() + " " + e.getLocalizedMessage());
        }
        return result;
    }
}



```




---

## Ejemplo 2


Usar un Map.

```
 Map.Entry<String, Optional<Sprint>> operation = loadOpenSprint(p);
                            System.out.println("\t\t key " + operation.getKey() + " value " + operation.getValue().get());
                            
                   
```

Metodo

```java

  // <editor-fold defaultstate="collapsed" desc="Map.Entry<String, Optional<Sprint>> loadOpenSprint(Proyecto p)">
    /**
     * Carga los sprint abiertos por proyectos
     *
     * @param p
     * @return
     */
    private Map.Entry<String, Optional<Sprint>> loadOpenSprint(Proyecto p) {
        Map.Entry<String, Optional<Sprint>> result;
        try {

            /**
             * Cargo los Sprint
             */
            Integer page = 0;
            Integer size = 0;
            Bson filter = new Document("proyecto.idproyecto", p.getIdproyecto()).append("active", Boolean.TRUE)
                    .append("open", Boolean.TRUE);
            Document sort = new Document("proyecto.idproyecto", 1);
            Search search = DocumentUtil.convertForLookup(filter, sort, 0, 0);
            List<Sprint> sprintList = sprintRepository.lookup(search);

            if (!sprintList.isEmpty()) {

                if (!isOpenSprintBetweenDateNow(sprintList.getFirst())) {
                    return new AbstractMap.SimpleEntry<>("not between date", Optional.of(sprintList.getFirst()));
                } else {
                    return new AbstractMap.SimpleEntry<>("valid between date", Optional.of(sprintList.getFirst()));
                }

            }

            return new AbstractMap.SimpleEntry<>("not found", Optional.empty());
        } catch (Exception e) {
            MessagesUtil.error(MessagesUtil.nameOfClassAndMethod() + "error: " + e.getLocalizedMessage());
        }
        return new AbstractMap.SimpleEntry<>("not found", Optional.empty());
    }
// </editor-fold>


```

