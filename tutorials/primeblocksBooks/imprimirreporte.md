# Imprimir reporte

## invocarlo desde un commandButton
Utilice onclick="this.form.target = '_blank'"

```java
   <p:commandButton title="#{core['button.print']}"
                                                                 icon="pi pi-print"
                                                                 id="buttonPrint"
                                                                 process="buttonPrint"
                                                                 ajax="false"
                                                                 immediate="true"
                                                                 rendered="#{reporteDepartamentalFaces.proyectoList.size() gt 0}"
                                                                 onclick="this.form.target = '_blank'"
                                                                 action="#{reporteDepartamentalFaces.printAll()}"
                                                                 update=":form:growl"
                                                                 styleClass="rounded-button ui-button-secondary" 
                                                                 style="margin-right: .5rem">

```

## Inovocarlo desde un <p:menuitem
cambie a    target="_blank"
```
         <p:menuitem
                                                 title="#{core['button.print']}"
                                                 value="#{core['button.print']}"
                                                                 icon="pi pi-print"
                                                                 id="buttonPrint"
                                                                 process="buttonPrint"
                                                                 ajax="false"
                                                                 immediate="true"
                                                                 target="_blank"
                                                                 action="#{roleFaces.printAll()}"
                                                                 update=":form:growl"
                                                                 styleClass="rounded-button ui-button-secondary" 
                                                                 style="margin-right: .5rem"/>
```



