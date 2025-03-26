
# 32 obteniendo el id de un componente jsf.md

```xml

                                                    title="#{core['button.add']}" process="@this"
                                                    update=":form:growl"
                                                    icon="pi pi-plus-circle" styleClass="rounded-button mr-2 ui-button-summary"
                                                    oncomplete="PF('dialogSprintAgregar').show('#{component.clientId}')"
                                                    action="#{dashboardFaces.agregarRowSprint(component.clientId)}" />


```


```java
   public void agregarRowSprint(String clientId) {

       PrimeFaces.current().ajax().update(clientId);
}


```

