
# findfirts

https://kevcodez.de/posts/2015-07-19-java-8-lambda-tutorial-beginning-with-lambda-und-streams/

if (proyectoEstadisticaList == null || proyectoEstadisticaList.isEmpty()) {
                return proyectoEstadistica;
            } else {
                 Optional<ProyectoEstadistica>  proyectoEstadisticaOptional = proyectoEstadisticaList.stream().filter(x -> x.getIdproyecto().equals(proyecto.getIdproyecto())).findFirst();
                if(proyectoEstadisticaOptional.isPresent()){
                    return proyectoEstadisticaOptional.get();
                }
