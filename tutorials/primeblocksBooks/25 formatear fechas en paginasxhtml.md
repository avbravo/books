
#25. Formatear fechas en paginas.xhtml



```

En el Faces implemente la interface JmoordbCoreXHTMLUtil
```java
@Named
@ViewScoped
@Data
public class DashboardFaces implements Serializable, JmoordbCoreXHTMLUtil {
}

```
## Desde la pagina web
Utilice showDate() y showHour()
```xml

<p:outputLabel value="#{dashboardFaces.showDate(dashboardFaces.userLogged.dateofbirth)} #{dashboardFaces.showHour(dashboardFaces.userLogged.dateofbirth)}"  />

```
