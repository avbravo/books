
#24. cortar texto de paginas.xhtml

Para recortar el texto de las paginas, defina en el archivo microprofile-config.properties las propiedades

```xml
#-----------------------------------------------------
#--- minimo de caracteres que se cortaran cuando se despliega texto muy largo
smallSizeOfTextForCut=10
mediumSizeOfTextForCut=35
largeSizeOfTextForCut=50


```

En el Faces implemente la interface JmoordbCoreXHTMLUtil
```java
@Named
@ViewScoped
@Data
public class DashboardFaces implements Serializable, JmoordbCoreXHTMLUtil {
}

```
Desde la pagina web

```xml

<span class="font-medium">#{dashboardFaces.cutText(dashboardFaces.userLogged.name,dashboardFaces.smallSizeOfTextForCut.get())}</span>

```
