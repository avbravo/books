- [] pasar de yoss login a jmoordbcoreui
- [] Probar la conexión desde el movil
- [] pasar logginAnnotation de yoss a jmoordbcoreui y usar directamente el controller y atributos de loginFaces que se genera.
- [] Actualizar el proyecto cargodev con los cambios de vencotapp
- [] identity store obtener el perfil seleccionado desde formulario login
- [] validar la seguridad de acceso en base al perfil de usuario.
- [] crear un <login: > <logincustomized>
- [] Crear formularios desde codigo java en tiempo de compilacion
- [] actualizar cargo de con las paginas sesion expirada, template y login.
- [] Generar paginas .xhtml en tiempo de compilacion con algo como.

```java
@Page(controller="PersonaController")
@PageCrud
public class PersonaFaces implements Serializable, Crud {
@Hidden
Long id;
@InputText
String name;
@SelectOneMenu(converter="PaisConverter",action="",selection="")`
Pais pais;
@Table(columns="{id,name}", hidden="{id}", types="{label, label}",tile="{'field.id','field.name'}"
Deportes deportes;


@Override
public String save(){}

@Override
public String remove(){}

@Override
public String update(){}

@Override
public String search(){}

}

``


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
- [] Soporte taildwin css desde webjar

- [] Tablero Kanban
- [] ArcadeDB
- [] record en jmoordbcore




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


---
# Terminados

- [x] Pasar todos los li del dashboard a 
 <yoss:submenuli  normalLabel="Login" collapsedLabel="L" href="index.xhtml"/>
- [x] Configurar el template
- [x] componente imagen
- [x] componente login solo sepasa al controller
- [x] logincompleto con roles
- [x] cambiar por <p:password>
- [x] Integrar Payaramicro
- [x] Change Dark mode and White
- [x] Sesion Expirada no regrese al inde,cambiuar el icono
- [x] Cambiar imagen de session expired
- [x] cerrar sesion index
- [x] cerrar sesion template usan metodos diferentes
- [x] pasarlo como un template
- [x] Agregar profile de Payaramicro
- [x] Templates