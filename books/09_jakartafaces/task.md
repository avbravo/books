
Pasar los componentes del template a lña libreria jmoordbcoreui

crear una pagina template.xhtml en WEB-INF.

Alli pasasr los parametros a <jmoordcoreui:templatemaster
menuleft =#{DashboardController.menuLeftComponent}"


``

- []  topheadertemplate
- [] leftmenu
- [] comprobar la validacion de images al pasar <c:if>


- [] PASAR IMAGENES DE TIPO primefaces, svg, resources

- [] pasar todos los archivos svg como iconos 



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
