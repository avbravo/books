
Pasar los componentes del template a lña libreria jmoordbcoreui

crear una pagina template.xhtml en WEB-INF.

Alli pasasr los parametros a <jmoordcoreui:templatemaster
menuleft =#{DashboardController.menuLeftComponent}"

/>


## en navheadertemplate

``` 

<div class="iq-header-img">
            <h:graphicImage library="jmoordbcoreui" name="images/dashboard/top-header.png" alt="header" class="theme-color-default-img img-fluid w-100 h-100 animated-scaleX"/>
            <h:graphicImage library="jmoordbcoreui" name="images/dashboard/top-header1.png" alt="header" class="theme-color-purple-img img-fluid w-100 h-100 animated-scaleX"/>
            <h:graphicImage library="jmoordbcoreui" name="images/dashboard/top-header2.png" alt="header" class="theme-color-blue-img img-fluid w-100 h-100 animated-scaleX"/>
            <h:graphicImage library="jmoordbcoreui" name="images/dashboard/top-header3.png" alt="header" class="theme-color-green-img img-fluid w-100 h-100 animated-scaleX"/>
            <h:graphicImage library="jmoordbcoreui" name="images/dashboard/top-header4.png" alt="header" class="theme-color-yellow-img img-fluid w-100 h-100 animated-scaleX"/>
            <h:graphicImage library="jmoordbcoreui" name="images/dashboard/top-header5.png" alt="header" class="theme-color-pink-img img-fluid w-100 h-100 animated-scaleX"/>
        </div>


``` 

**cambiarlo por**

``` 
<div class="iq-header-img">
    <ui:repeat value="#{templateBean.imageInfo}" var="item">
        <h:graphicImage library="#{item.library}" name="#{item.name}" alt="header" class="theme-color-default-img img-fluid w-100 h-100 animated-scaleX"/>
    </ui:repeat>
   </div>

``` 

**Controller**

```java
public class Template {
 private List<ImageInfo> navHeaderImageInfos;


}

@Named
@SessionScoped
public class DashboardFaces implements Serializable {

    public String createTemplate() {
template = new Template.Builder()
           navHeaderImageInfos(Arrays.asList(
             new ImageInfo.Builder().library("jmoordbcoreui").name("images/dashboard/top-header.png").build()

           .new ImageInfo.Builder().library("jmoordbcoreui").name("images/dashboard/top-header1.png").build()
           .new ImageInfo.Builder().library("jmoordbcoreui").name("images/dashboard/top-header2.png").build())
.build()

}

}




``




Menu Records and Submenu
estilo primefaces
- [] pasarlo como un template
- [] Agregar profile de Payaramicro
- [] Soporte taildwin css desde webjar
- [] Templates
- [] Tablero Kanban
- [] ArcadeDB
- [] record en jmoordbcore


- [] Pasar todos los li del dashboard a 
 <yoss:submenuli  normalLabel="Login" collapsedLabel="L" href="index.xhtml"/>

- [] Configurar el template

- [x] componente imagen
- [x] componente login solo sepasa al controller
- [] logincompleto con roles
- [] cambiar por <p:password>
- [] Integrar Payaramicro

- [] Change Dark mode and White


Taildwind Css

wget https://cdn.tailwindcss.com/3.4.16

renombrarlo como tailwind.cs
wget https://cdn.jsdelivr.net/npm/@tailwindcss/browser@4

crear una carpeta llamada tailwind en resources

agregar al header en WEB-INF/templates/common/admin.xhtml


```
<h:outputStylesheet name="tailwind/tailwind.css" />

<h:outputScript name="browser@4.js" library="tailwind" target="head"/>

```


* [Kanban]

[https://coderthemes.com/hyper-admin/saas/apps-kanban.html](https://coderthemes.com/hyper-admin/saas/apps-kanban.html)

[https://berrydashboard.io/codeignitor/default/public/kanban#](https://berrydashboard.io/codeignitor/default/public/kanban#)

[https://pipeline.mediumra.re/nav-side-kanban-board.html](https://pipeline.mediumra.re/nav-side-kanban-board.html)

[https://coderthemes.com/hyper-admin/saas/apps-kanban.html](https://coderthemes.com/hyper-admin/saas/apps-kanban.html)
