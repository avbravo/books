## 


```

   <div class="field col-12 md:col-10">
                                        <p:outputLabel value="LazyModel" for="lazyModel"/>
                                        <p:autoComplete id="lazyModel" 
                                                        widgetVar="lazyModel"
                                                       
                                                        value="#{buscadorTarjetasFaces.tarjetaLazyDataModelSelected}"
                                                      
                                             
                                                        var="tarjeta" itemLabel="#{tarjeta.tarjeta}" 
                                                        itemValue="#{tarjeta}"
                                                        lazyModel="#{buscadorTarjetasFaces.autocompleteByTarjetaLazyDataModel}"
                                                        lazyField="tarjeta"
                                                        forceSelection="true" emptyMessage="sorry, no suggestions"
                                                        converter="#{tarjetaConverter}"
                                                        moreText="show more results"
                                                        maxResults="#{buscadorTarjetasFaces.rowPageSmall.get()}">
                                                <p:ajax event="moreTextSelect" listener="#{buscadorTarjetasFaces.onMoreText}" />
                                            <p:ajax event="itemSelect"   listener="#{buscadorTarjetasFaces.autocompleteSelectedEvent}"  update=":form:growl, lazyModel, dataTable" />  
                                            <p:ajax event="itemUnselect"  process="@this" listener="#{buscadorTarjetasFaces.autocompleteUnselectListener}" update=":form:growl, lazyModel,dataTable" />
                                            <f:facet name="footer">
                                                <div class="ui-fluid" style="padding:0.5rem 1rem 1rem 1rem">
 <p:commandButton icon="pi pi-chevron-circle-left" styleClass="rounded-button ui-button-outlined" 
                                                                     action="#{buscadorTarjetasFaces.back()}"
                                                                       update=":form:lazyModel, :form:growl"/>
                                                 
                                                    <p:spacer width="10"/>
                                                    <p:commandButton icon="pi pi-chevron-circle-right" styleClass="rounded-button ui-button-secondary ui-button-outlined" 
                                                                     action="#{buscadorTarjetasFaces.next()}"
                                                                     update=":form:lazyModel, :form:growl"
                                                                     />
                                                </div>
                                            </f:facet>
                                        </p:autoComplete>
                                    </div>
```



```
 /**
             * Autocomplete con Pagination
             */
            this.autocompleteByTarjetaLazyDataModel = new LazyDataModel<Tarjeta>() {
                @Override
                public List<Tarjeta> load(int offset, int pageSize, Map<String, SortMeta> sortBy, Map<String, FilterMeta> filterBy) {
                    ConsoleUtil.test("\t ===================================================");
                    ConsoleUtil.test("\t LLEGO AL LOAD DE autocompleteByTarjetaLazyDataModel");
                    ConsoleUtil.test("\t ===================================================");
//                    completeTarjeta(message);
                    if (filterBy.isEmpty()) {
                        // filterBy = table.getFilterByAsMap();
                        ConsoleUtil.test("\t {}filterBy.isEmpty()");
                    }

                    String query = "";
                    if (!filterBy.isEmpty()) {
                        for (FilterMeta entry : filterBy.values()) {

                            String filterProperty = entry.getField();
                            String filterValueX = entry.getFilterValue().toString();

                            query = entry.getFilterValue().toString();

                            ConsoleUtil.test("\t filtrando por filterProperty: " + filterProperty + " filterValue " + filterValueX);
                            FilterConstraint constF = entry.getConstraint();
                            Object filterValue = entry.getFilterBy();

                            if (filterProperty.equals("customer")) {
                                List<Object> selectedCustomers = Arrays.asList(filterValue);
                            }
                        }
                    }
                    /**
                     * Aqui le digo la pagina y cantidad de registros guardar en
                     * un objeto que lleve el control de las paginas para ese
                     * autocmplete Pagination paginationAutocompleteTarjeta se
                     * cambia en base al ,metodo en la pagina
                     */

                    /**
                     * Aqui definir un countByLikePagination para que cuente los
                     * registros por paginas
                     */
//                    totalRecordsAutocompleteTarjeta = 10;
totalRecordsAutocompleteTarjeta =tarjetaServices.searchCountLikeByTarjeta(query, paginator.getFilter(), paginator.getSort(), 0, 0).intValue();
                    ConsoleUtil.test("\t {totalRecordsAutocompleteTarjeta} "+totalRecordsAutocompleteTarjeta);
                    /**
                     * Ejecuta la consulta por pagina en la pagina .xhtml se
                     * asocia a pagination
                     */
                    ConsoleUtil.test("\t call{query...}" + query);
                    List<Tarjeta> result = completeTarjetaPagination(query, paginationAutocompleteTartjeta);

                    ConsoleUtil.test("\t {result.size() ...}" + result.size());
//                    switch (paginator.getName()) {
//
//                        case "findByProyecto":
//
//                            totalRecords = tarjetaServices.count(paginator.getFilter(),
//                                    paginator.getSort(), 0, 0).intValue();
//                            break;
//
//                    }
//
//                    List<Paginator> list = new ArrayList<>();
//                    if (!isRowPageSmall) {
//
//                        /**
//                         * Utiliza rowPage
//                         */
//                        list = processLazyDataModel(paginator, paginatorOld, offset, rowPage.get(), totalRecords, sortBy);
//
//                    } else {
//
//                        /**
//                         * Utiliza rowPageWithOverlayPanel para el OverlayPanel
//                         */
//                        list = processLazyDataModel(paginator, paginatorOld, offset, rowPageSmall.get(), totalRecords, sortBy);
//
//                    }
////
//
//                    paginator = list.get(0);
//                    paginatorOld = list.get(1);
//                    Pagination pagination = new Pagination();
//                    if (!isRowPageSmall) {
//
//                        paginator.setNumberOfPage(numberOfPages(totalRecords, rowPage.get()));
//                        pagination = new Pagination(paginator.getPage(), rowPage.get());
//                    } else {
//
//                        paginator.setNumberOfPage(numberOfPages(totalRecords, rowPageSmall.get()));
//                        pagination = new Pagination(paginator.getPage(), rowPageSmall.get());
//                    }
//
//                    List<Tarjeta> result = new ArrayList<>();
//                    switch ((paginator.getName())) {
//                        case "findByProyectoAndSprint":
//
//                            result = tarjetaServices.lookup(paginator.getFilter(),
//                                    paginator.getSort(), paginator.getPage(), rowPageSmall.get());
//
//                            break;
//                        case "findByProyecto":
//
//                            result = tarjetaServices.lookup(paginator.getFilter(),
//                                    paginator.getSort(), paginator.getPage(), rowPageSmall.get());
//
//                            break;
//                        default:
//
//                    }
                    ConsoleUtil.test("\t {totalRecordsAutocompleteTarjeta} " + totalRecordsAutocompleteTarjeta);
                    autocompleteByTarjetaLazyDataModel.setRowCount(totalRecordsAutocompleteTarjeta);

                    PrimeFaces.current().executeScript("setDataTableWithPageStart()");
                    PrimeFaces.current().executeScript("widgetVardataTable.getPaginator().setPage(0);");
                    tarjetaLazyDataModelList = result;

                    return result;
                }

                @Override
                public int count(Map<String, FilterMeta> map) {

                    return totalRecordsAutocompleteTarjeta;

                }

                @Override
                public String getRowKey(Tarjeta object) {
                    if (object == null || object.getIdtarjeta() == null) {
                        return "";
                    }
                    return object.getIdtarjeta().toString();
                }

                @Override
                public Tarjeta getRowData(String rowKey) {
                    for (Tarjeta t : tarjetaLazyDataModelList) {
                        if (t != null) {
                            if (t.getIdtarjeta().equals(rowKey)) {
                                return t;
                            }
                        }
                    }
                    return null;
                }

            };

```






```
 // <editor-fold defaultstate="collapsed" desc="String back()">
    public void back() {
        try {
            ConsoleUtil.test("\t **********************************");
            ConsoleUtil.test("\t llego a back");
           // FacesUtil.infoDialog("desde el back","from back");
             if (autoComplete != null) {
                 paginator.setPage(paginator.getPage()+1);
            
            autoComplete.setLazyModel(tarjetaLazyDataModel);
        }
             else{
                 ConsoleUtil.test("\t autoComplete es null");
             }
            PrimeFaces.current().ajax().update(":form:lazyModel");
            //  PrimeFaces.current().ajax().update(":form:growl");
            
              PrimeFaces.current().ajax().update("dataTable");
        } catch (Exception e) {
            FacesUtil.errorMessage(FacesUtil.nameOfClassAndMethod() + " " + e.getLocalizedMessage());
        }
    
    }
    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="String next()">
    public String next() {
        try {
            ConsoleUtil.test("\t **********************************");
            ConsoleUtil.test("\t llego a next() ");
        } catch (Exception e) {
            FacesUtil.errorMessage(FacesUtil.nameOfClassAndMethod() + " " + e.getLocalizedMessage());
        }
        return "";
    }
    // </editor-fold>
    

```









```

    public void onMoreText(jakarta.faces.event.AjaxBehaviorEvent event) {
        ConsoleUtil.test("\t ******************  onMoreText");
    org.primefaces.component.autocomplete.AutoComplete ac = (AutoComplete) event.getSource();
    ac.setMaxResults(ac.getMaxResults() + 30);
    ac.setValue(ac);
    if (autoComplete != null) {
                 paginator.setPage(paginator.getPage()+1);
                 tarj();
            autoComplete.setLazyModel(tarjetaLazyDataModel);
        }
             else{
                 ConsoleUtil.test("\t autoComplete es null");
             }
//    executeJS("PF('widgetVarName').search('" + getQryString() + "')");
    executeJS("PF('lazyModel').search('" + "dia"+ "')");
//    executeJS("PF('lazyModel').search('" + getQryString() + "')");
}

public void executeJS(String source) {
    
        PrimeFaces.current().executeScript(source);
}

```