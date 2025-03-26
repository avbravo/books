


```java
private Boolean loadMisPlantillas() {
        Boolean result = Boolean.FALSE;
        try {

            /**
             * Cargo los Sprint
             */
            Integer page = 0;
            Integer size = 0;
            List<Bson> grupoFilter = new ArrayList<>();
/**
 * Busca en MongoDB un elemento en una lista
 */
            List<Long> searchGrupoTipoTarjeta = new ArrayList<>();
            proyectoSelected.getGrupoTipoTarjeta().forEach(gtp -> {
                searchGrupoTipoTarjeta.add(gtp.getIdgrupotipotarjeta());
            });

            Bson allComparison = in("grupoTipoTarjeta.idgrupotipotarjeta", searchGrupoTipoTarjeta);
            Bson filter0 = new Document("iduser", userLogged.getIduser()).append("active", Boolean.TRUE);
            Bson filter = and(filter0, allComparison);

            Document sort = new Document("plantilla", 1);

            plantillaTarjetaList = plantillaTarjetaServices.lookup(
                    filter,
                    sort,
                    page, size);

        } catch (Exception e) {
            FacesUtil.errorMessage(FacesUtil.nameOfClassAndMethod() + " " + e.getLocalizedMessage());
        }
        return result;
    }
// </editor-fold>



```

