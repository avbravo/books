-  [] migrar a payara-micro: 6.2025.6 soporta java 24
- [] analizar payara-micro: 7 soporta jakartaee11 y mvc 3.0
- [] Estudiar Java 21,22,23,24
- [] Pasar a java Records
- [] Microservicio con Helidon.io
- [] Frontend Payara
- [] corregir logomain para que use el logo oficial
- [] Verificar el redireccionamiento del dashboard menu

* Ver el usuario y password

```java


 Principal user = ((HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest()).getUserPrincipal();
  if (user != null)
        {
            System.out.println("Principal:::Logged in: " + user.getName() );

        }
        else
        {
        System.out.println( "Principal:::Not logged in");
        }
      

   FacesContext context = FacesContext.getCurrentInstance();
        HttpServletRequest request = (HttpServletRequest) context.getExternalContext().getRequest();
        request.isUserInRole("admin");
 
       if( request.isUserInRole("DEVELOPERS")){
           System.out.println("es developers");
       }else{
           System.out.println("no es developers");
       }
        
                       
      
        System.out.println("==============================================");


     <f:view>
         <h:form>
             <!--  Content visible to users with the 'admin' role -->
             <h:outputText value="Welcome, Admin!" rendered="#{request.isUserInRole('ROLEA')}"/>
         </h:form>
     </f:view>

```

## Seguridad
-[] Validar los roles en las paginas y configurar la seguridad en web.xml
-[] Documentar security
-[] Validar el tiempo de sesion


---

## General


- [] Probar la conexión desde el movil
- [] Actualizar el proyecto cargodev con los cambios de vencotapp
- [] identity store obtener el perfil seleccionado desde formulario login
- [] validar la seguridad de acceso en base al perfil de usuario.

- [] Crear formularios desde codigo java en tiempo de compilacion
- [] actualizar cargo de con las paginas sesion expirada, template y login.
- [] Generar paginas .xhtml en tiempo de compilacion con algo como.

---

## Java Record

* Soporte para Record en el framework
* Entity como Java Record
* Crear una anotacion que genere DTO a partir de Java Record
- [] Java record
- [] RecordToDTO

---
## Controller
*  Crear una anotacion que genere controller

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
- [x] pasar de yoss login a jmoordbcoreui
- [x] pasar logginAnnotation de yoss a jmoordbcorel contui y usar directamente eroller y atributos de loginFaces que se genera.
- [x] crear un <login: > <logincustomized>