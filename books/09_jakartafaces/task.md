- [] identity store obtener el perfil seleccionado desde formulario login
- [] validar la seguridad de acceso en base al perfil de usuario.
- [] crear un <login: > <logincustomized>
- [] Crear formularios desde codigo java en tiempo de compilacion
- [] Sesion Expirada no regrese al inde,cambiuar el icono
- [] Cambiar imagen de session expired
- [] actualizar cargo de con las paginas sesion expirada, template y login.
```java
@Page(controller="PersonaController")
@PageCrud
public Persona {
@Hidden
Long id;
@InputText
String name;
@SelectOneMenu(converter="PaisConverter",action="",selection="")`
Pais pais;

}




``

- [] cerrar sesion index
- [] cerrar sesion template usan metodos diferentes

-[] Validar los roles en las paginas y configurar la seguridad en web.xml
-[] Documentar security
-[] Validar el tiempo de sesion

- [] Crear un arquetipo


-[] Pasar los iconos de primefaces a un enum

Tomarlo de la base de datos de icons en MongoDB
PRIMEFACES.SAVE

Crear para jmoordbcore
JMOORDBCOREUI.SAVE

Icon(PRIMEFACES.SAVE)

Icon personalized = new Icon("jmoordbcore","image/icons/save.png")

---


- [ ] Crear componentes para el menu desplegable cambas



Pasar los componentes del template a lña libreria jmoordbcoreui

crear una pagina template.xhtml en WEB-INF.

Alli pasasr los parametros a <jmoordcoreui:templatemaster
menuleft =#{DashboardController.menuLeftComponent}"



```

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
