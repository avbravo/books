
Pasar los componentes del template a lña libreria jmoordbcoreui

crear una pagina template.xhtml en WEB-INF.

Alli pasasr los parametros a <jmoordcoreui:templatemaster
menuleft =#{DashboardController.menuLeftComponent}"


## Menu

```java

MenuBar menuBar = new MenuBar();
menuBar.addThemeVariants(MenuBarVariant.LUMO_ICON);
MenuItem share = createIconItem(menuBar, VaadinIcon.SHARE, "Share",
        null);
SubMenu shareSubMenu = share.getSubMenu();`


MenuBarBasic.java
Expand code
,Copyto clipboard
MenuBar menuBar = new MenuBar();
Text selected = new Text("");
ComponentEventListener<ClickEvent<MenuItem>> listener = e -> selected
        .setText(e.getSource().getText());
Div message = new Div(new Text("Clicked item: "), selected);

menuBar.addItem("View", listener);
menuBar.addItem("Edit", listener);

MenuItem share = menuBar.addItem("Share");
SubMenu shareSubMenu = share.getSubMenu();
MenuItem onSocialMedia = shareSubMenu.addItem("On social media");
SubMenu socialMediaSubMenu = onSocialMedia.getSubMenu();
socialMediaSubMenu.addItem("Facebook", listener);
socialMediaSubMenu.addItem("Twitter", listener);
socialMediaSubMenu.addItem("Instagram", listener);
shareSubMenu.addItem("By email", listener);
shareSubMenu.addItem("Get Link", listener);

MenuItem move = menuBar.addItem("Move");
SubMenu moveSubMenu = move.getSubMenu();
moveSubMenu.addItem("To folder", listener);
moveSubMenu.addItem("To trash", listener);

menuBar.addItem("Duplicate", listener);

```

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
