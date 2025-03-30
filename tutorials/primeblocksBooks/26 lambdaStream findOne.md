# 26. lambda Stream findOne
```java

if (proyectoEstadisticaList == null || proyectoEstadisticaList.isEmpty()) {
                return proyectoEstadistica;
            } else {
                 Optional<ProyectoEstadistica>  proyectoEstadisticaOptional = proyectoEstadisticaList.stream().filter(x -> x.getIdproyecto().equals(proyecto.getIdproyecto())).findFirst();
                if(proyectoEstadisticaOptional.isPresent()){
                    return proyectoEstadisticaOptional.get();
                }

```
