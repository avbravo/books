https://www.primefaces.org/showcase/ui/menu/menu.xhtml?jfwid=2bd8b



Crear una java para crear submenú y menuitem que después los pueda usar para generar componentes jakarta faces 

____


A continuación, te proporciono una solución detallada para crear una clase en Java que permita generar un menú con submenús y elementos de menú (`MenuItem`). Esta estructura se puede utilizar posteriormente para generar componentes de Jakarta Faces (anteriormente conocido como JavaServer Faces o JSF).

---

### **Contexto**

Para trabajar con Jakarta Faces, necesitamos crear una estructura jerárquica que represente los menús y sus elementos. Cada menú puede contener submenús y elementos de menú (`MenuItem`), lo que permite una organización flexible. Luego, esta estructura se puede mapear a componentes de Jakarta Faces como `<h:form>`, `<h:panelGroup>`, `<h:commandLink>`, etc.

---

### **Solución Propuesta**

Vamos a crear una clase `Menu` que represente un menú principal, una clase `SubMenu` para los submenús y una clase `MenuItem` para los elementos individuales del menú. Estas clases estarán diseñadas para ser utilizadas en un contexto de Jakarta Faces.

#### **1. Clase `MenuItem`**
Esta clase representa un elemento individual del menú, como un enlace o botón.

```java
public class MenuItem {
    private String label;
    private String action;

    public MenuItem(String label, String action) {
        this.label = label;
        this.action = action;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }
}
```

- **`label`**: El texto que se mostrará en el menú.
- **`action`**: La acción asociada al elemento, que podría ser un método en un bean de JSF.

---

#### **2. Clase `SubMenu`**
Esta clase representa un submenú que puede contener múltiples elementos de menú.

```java
import java.util.ArrayList;
import java.util.List;

public class SubMenu {
    private String label;
    private List<MenuItem> menuItems;

    public SubMenu(String label) {
        this.label = label;
        this.menuItems = new ArrayList<>();
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public List<MenuItem> getMenuItems() {
        return menuItems;
    }

    public void addMenuItem(MenuItem menuItem) {
        menuItems.add(menuItem);
    }
}
```

- **`label`**: El título del submenú.
- **`menuItems`**: Una lista de elementos de menú contenidos en este submenú.

---

#### **3. Clase `Menu`**
Esta clase representa el menú principal, que puede contener tanto submenús como elementos de menú directamente.

```java
import java.util.ArrayList;
import java.util.List;

public class Menu {
    private String label;
    private List<SubMenu> subMenus;
    private List<MenuItem> menuItems;

    public Menu(String label) {
        this.label = label;
        this.subMenus = new ArrayList<>();
        this.menuItems = new ArrayList<>();
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public List<SubMenu> getSubMenus() {
        return subMenus;
    }

    public void addSubMenu(SubMenu subMenu) {
        subMenus.add(subMenu);
    }

    public List<MenuItem> getMenuItems() {
        return menuItems;
    }

    public void addMenuItem(MenuItem menuItem) {
        menuItems.add(menuItem);
    }
}
```

- **`label`**: El título del menú principal.
- **`subMenus`**: Una lista de submenús contenidos en este menú.
- **`menuItems`**: Una lista de elementos de menú directamente contenidos en este menú.

---

### **4. Generación de Componentes Jakarta Faces**

Una vez que tengas la estructura del menú, puedes usarla para generar componentes de Jakarta Faces dinámicamente. Por ejemplo, puedes renderizar el menú en una vista XHTML utilizando un `ui:repeat` o `c:forEach`.

Aquí tienes un ejemplo básico de cómo podrías integrarlo:

#### **Ejemplo de Uso en un Bean de JSF**

```java
import jakarta.annotation.PostConstruct;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Named;
import java.io.Serializable;

@Named
@ViewScoped
public class MenuBean implements Serializable {
    private Menu mainMenu;

    @PostConstruct
    public void init() {
        // Crear el menú principal
        mainMenu = new Menu("Main Menu");

        // Crear un submenú
        SubMenu fileSubMenu = new SubMenu("File");
        fileSubMenu.addMenuItem(new MenuItem("New", "newAction"));
        fileSubMenu.addMenuItem(new MenuItem("Open", "openAction"));
        fileSubMenu.addMenuItem(new MenuItem("Save", "saveAction"));

        // Agregar el submenú al menú principal
        mainMenu.addSubMenu(fileSubMenu);

        // Agregar un elemento de menú directamente al menú principal
        mainMenu.addMenuItem(new MenuItem("Exit", "exitAction"));
    }

    public Menu getMainMenu() {
        return mainMenu;
    }
}
```

#### **Ejemplo de Vista XHTML**

```xml
<h:form>
    <h:panelGroup layout="block" styleClass="menu">
        <h:outputText value="#{menuBean.mainMenu.label}" />
        <ui:repeat value="#{menuBean.mainMenu.subMenus}" var="subMenu">
            <h:panelGroup layout="block" styleClass="submenu">
                <h:outputText value="#{subMenu.label}" />
                <ui:repeat value="#{subMenu.menuItems}" var="menuItem">
                    <h:commandLink value="#{menuItem.label}" action="#{menuItem.action}" />
                </ui:repeat>
            </h:panelGroup>
        </ui:repeat>
        <ui:repeat value="#{menuBean.mainMenu.menuItems}" var="menuItem">
            <h:commandLink value="#{menuItem.label}" action="#{menuItem.action}" />
        </ui:repeat>
    </h:panelGroup>
</h:form>
```

---

### **Conclusión**

Este diseño modular permite crear menús complejos con submenús y elementos de menú de manera flexible. Puedes ampliar las clases para incluir más funcionalidades, como íconos, roles de usuario u otras propiedades específicas de tu aplicación. Además, la integración con Jakarta Faces es sencilla gracias a las capacidades de renderizado dinámico de componentes.

Si necesitas más detalles o ajustes específicos, no dudes en preguntar. ¡Buena suerte con tu proyecto!


____




¡Claro que sí! Aquí tienes una clase Java que te permite crear menús y submenús con elementos que puedes utilizar para generar componentes Jakarta Faces.
import jakarta.faces.component.UIComponent;
import jakarta.faces.component.html.HtmlCommandLink;
import jakarta.faces.component.html.HtmlOutputText;
import jakarta.faces.component.html.HtmlPanelGroup;
import jakarta.faces.component.html.HtmlSelectOneMenu;
import jakarta.faces.component.html.HtmlSelectItem;
import jakarta.faces.component.html.HtmlSeparator;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.facelets.FaceletContext;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MenuBuilder {

    public static class MenuItem {
        private String label;
        private String outcome;
        private String actionExpression;
        private List<MenuItem> children;

        public MenuItem(String label) {
            this.label = label;
            this.children = new ArrayList<>();
        }

        public MenuItem(String label, String outcome) {
            this.label = label;
            this.outcome = outcome;
            this.children = new ArrayList<>();
        }

        public MenuItem(String label, String actionExpression, boolean isAction) {
            this.label = label;
            if (isAction) {
                this.actionExpression = actionExpression;
            } else {
                this.outcome = actionExpression;
            }
            this.children = new ArrayList<>();
        }

        public String getLabel() {
            return label;
        }

        public void setLabel(String label) {
            this.label = label;
        }

        public String getOutcome() {
            return outcome;
        }

        public void setOutcome(String outcome) {
            this.outcome = outcome;
        }

        public String getActionExpression() {
            return actionExpression;
        }

        public void setActionExpression(String actionExpression) {
            this.actionExpression = actionExpression;
        }

        public List<MenuItem> getChildren() {
            return children;
        }

        public void setChildren(List<MenuItem> children) {
            this.children = children;
        }

        public void addChild(MenuItem child) {
            this.children.add(child);
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            MenuItem menuItem = (MenuItem) o;
            return Objects.equals(label, menuItem.label);
        }

        @Override
        public int hashCode() {
            return Objects.hash(label);
        }
    }

    public static HtmlPanelGroup createMenuComponent(FacesContext context, FaceletContext faceletContext, List<MenuItem> menuItems) {
        HtmlPanelGroup menuPanel = new HtmlPanelGroup();
        menuPanel.setLayout("list"); // Puedes usar "table" u otro layout si lo prefieres

        for (MenuItem item : menuItems) {
            UIComponent menuItemComponent = createMenuItemComponent(context, faceletContext, item, 0);
            menuPanel.getChildren().add(menuItemComponent);
        }

        return menuPanel;
    }

    private static UIComponent createMenuItemComponent(FacesContext context, FaceletContext faceletContext, MenuItem item, int level) {
        HtmlPanelGroup itemPanel = new HtmlPanelGroup();

        StringBuilder styleClass = new StringBuilder("menu-item");
        if (level > 0) {
            styleClass.append(" submenu-item level-").append(level);
        }

        if (item.getChildren().isEmpty()) {
            HtmlCommandLink link = new HtmlCommandLink();
            link.setValue(item.getLabel());
            if (item.getOutcome() != null) {
                link.setOutcome(item.getOutcome());
            }
            if (item.getActionExpression() != null) {
                link.setActionExpression(context.getApplication().getExpressionFactory()
                        .createMethodExpression(context.getELContext(), item.getActionExpression(), String.class, new Class[0]));
            }
            link.setStyleClass(styleClass.toString());
            itemPanel.getChildren().add(link);
        } else {
            HtmlPanelGroup submenuHeader = new HtmlPanelGroup();
            HtmlOutputText label = new HtmlOutputText();
            label.setValue(item.getLabel());
            submenuHeader.getChildren().add(label);
            submenuHeader.setStyleClass("submenu-header");
            itemPanel.getChildren().add(submenuHeader);

            HtmlPanelGroup submenuItems = new HtmlPanelGroup();
            submenuItems.setStyleClass("submenu");
            submenuItems.setVisible(false); // Inicialmente oculto, puedes manejar la visibilidad con JavaScript/CSS

            for (MenuItem child : item.getChildren()) {
                UIComponent childItemComponent = createMenuItemComponent(context, faceletContext, child, level + 1);
                submenuItems.getChildren().add(childItemComponent);
            }
            itemPanel.getChildren().add(submenuItems);
            itemPanel.setStyleClass(styleClass.toString() + " has-submenu");
        }

        return itemPanel;
    }

    // Ejemplo de uso
    public static void main(String[] args) {
        // Esto es solo para demostrar la creación de la estructura en Java.
        // La renderización real en FacesContext y FaceletContext ocurre dentro de un ciclo de vida de Faces.

        MenuItem item1 = new MenuItem("Inicio", "/index.xhtml");
        MenuItem item2 = new MenuItem("Productos");
        item2.addChild(new MenuItem("Electrónicos", "/productos/electronicos.xhtml"));
        item2.addChild(new MenuItem("Libros", "/productos/libros.xhtml"));
        MenuItem item3 = new MenuItem("Servicios");
        item3.addChild(new MenuItem("Consultoría", "#{bean.consultoria}"));
        item3.addChild(new MenuItem("Soporte", "#{bean.soporte}"));
        MenuItem item4 = new MenuItem("Contacto", "/contacto.xhtml");

        List<MenuItem> mainMenu = List.of(item1, item2, item3, item4);

        // En un entorno de Jakarta Faces, usarías algo como:
        // <h:panelGroup binding="#{menuBean.menuComponent}" layout="list" />
        // Y en el bean:
        // private UIComponent menuComponent;
        // @PostConstruct
        // public void init() {
        //     FacesContext context = FacesContext.getCurrentInstance();
        //     FaceletContext faceletContext = (FaceletContext) context.getAttributes().get(FaceletContext.FACELET_CONTEXT_KEY);
        //     menuComponent = MenuBuilder.createMenuComponent(context, faceletContext, createMenuItems());
        // }
        //
        // private List<MenuItem> createMenuItems() {
        //     MenuItem item1 = new MenuItem("Inicio", "/index.xhtml");
        //     MenuItem item2 = new MenuItem("Productos");
        //     item2.addChild(new MenuItem("Electrónicos", "/productos/electronicos.xhtml"));
        //     item2.addChild(new MenuItem("Libros", "/productos/libros.xhtml"));
        //     MenuItem item3 = new MenuItem("Servicios");
        //     item3.addChild(new MenuItem("Consultoría", "#{bean.consultoria}"));
        //     item3.addChild(new MenuItem("Soporte", "#{bean.soporte}"));
        //     MenuItem item4 = new MenuItem("Contacto", "/contacto.xhtml");
        //     return List.of(item1, item2, item3, item4);
        // }

        System.out.println("Estructura del menú creada en Java.");
        // Aquí podrías inspeccionar la estructura 'mainMenu' para ver los elementos creados.
    }
}

¿Cómo usar esta clase en Jakarta Faces?
 * Crea un Managed Bean: Necesitarás un bean administrado donde instanciar y almacenar la lista de MenuItem y el componente UIComponent del menú.
   import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.faces.component.UIComponent;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.facelets.FaceletContext;
import jakarta.inject.Named;
import java.util.List;

@Named("menuBean")
@ApplicationScoped // O el scope que necesites
public class MenuBean {

    private List<MenuBuilder.MenuItem> menuItems;
    private UIComponent menuComponent;

    @PostConstruct
    public void init() {
        menuItems = createMenuItems();
        FacesContext context = FacesContext.getCurrentInstance();
        FaceletContext faceletContext = (FaceletContext) context.getAttributes().get(FaceletContext.FACELET_CONTEXT_KEY);
        menuComponent = MenuBuilder.createMenuComponent(context, faceletContext, menuItems);
    }

    public List<MenuBuilder.MenuItem> getMenuItems() {
        return menuItems;
    }

    public UIComponent getMenuComponent() {
        return menuComponent;
    }

    private List<MenuBuilder.MenuItem> createMenuItems() {
        MenuBuilder.MenuItem item1 = new MenuBuilder.MenuItem("Inicio", "/index.xhtml");
        MenuBuilder.MenuItem item2 = new MenuBuilder.MenuItem("Productos");
        item2.addChild(new MenuBuilder.MenuItem("Electrónicos", "/productos/electronicos.xhtml"));
        item2.addChild(new MenuBuilder.MenuItem("Libros", "/productos/libros.xhtml"));
        MenuBuilder.MenuItem item3 = new MenuBuilder.MenuItem("Servicios");
        item3.addChild(new MenuBuilder.MenuItem("Consultoría", "#{bean.consultoria}"));
        item3.addChild(new MenuBuilder.MenuItem("Soporte", "#{bean.soporte}"));
        MenuBuilder.MenuItem item4 = new MenuBuilder.MenuItem("Contacto", "/contacto.xhtml");
        return List.of(item1, item2, item3, item4);
    }
}

 * Usa el componente en tu archivo Facelets (.xhtml):
   <html xmlns="http://www.w3.org/1999/xhtml"
      xmlns:h="http://xmlns.jcp.org/jsf/html"
      xmlns:ui="http://xmlns.jcp.org/jsf/facelets">

<h:head>
    <title>Mi Menú</title>
    <style type="text/css">
        .menu-item {
            /* Estilos para los elementos del menú */
        }
        .submenu-header {
            cursor: pointer;
        }
        .submenu {
            display: none; /* Inicialmente oculto */
            margin-left: 15px;
        }
        .has-submenu > .submenu-header::after {
            content: " ▼"; /* Indicador de submenú */
        }
        .submenu.active {
            display: block;
        }
        .submenu-item {
            /* Estilos para los elementos del submenú */
        }
        .level-1 {
            /* Estilos específicos para el primer nivel de submenú */
        }
        /* ... más estilos según necesites ... */
    </style>
    <script>
        function toggleSubmenu(element) {
            var submenu = element.nextElementSibling;
            if (submenu && submenu.classList.contains('submenu')) {
                submenu.classList.toggle('active');
            }
        }

        document.addEventListener('DOMContentLoaded', function() {
            var submenuHeaders = document.querySelectorAll('.submenu-header');
            submenuHeaders.forEach(function(header) {
                header.addEventListener('click', function() {
                    toggleSubmenu(this);
                });
            });
        });
    </script>
</h:head>

<h:body>
    <h1>Menú Dinámico</h1>
    <h:panelGroup binding="#{menuBean.menuComponent}" />

    <h2>Menú Alternativo (Iterando directamente)</h2>
    <ul class="main-menu">
        <ui:repeat value="#{menuBean.menuItems}" var="item">
            <li class="menu-item #{not empty item.children ? 'has-submenu' : ''}">
                <h:link outcome="#{item.outcome}" action="#{item.actionExpression}" value="#{item.label}"
                        rendered="#{empty item.children}" styleClass="menu-link"/>
                <div rendered="#{not empty item.children}">
                    <div class="submenu-header" onclick="toggleSubmenu(this)">#{item.label}</div>
                    <ul class="submenu">
                        <ui:repeat value="#{item.children}" var="child">
                            <li class="submenu-item">
                                <h:link outcome="#{child.outcome}" action="#{child.actionExpression}" value="#{child.label}" styleClass="submenu-link"/>
                            </li>
                        </ui:repeat>
                    </ul>
                </div>
            </li>
        </ui:repeat>
    </ul>
</h:body>
</html>

Explicación de la clase MenuBuilder:
 * MenuItem (Clase interna estática): Representa un elemento del menú o submenú. Contiene:
   * label: El texto que se mostrará en el menú.
   * outcome: Para navegación basada en reglas de navegación de Faces.
   * actionExpression: Para invocar métodos de bean al hacer clic.
   * children: Una lista de MenuItem que representan los submenús.
 * createMenuComponent(FacesContext, FaceletContext, List<MenuItem>):
   * Crea un HtmlPanelGroup como contenedor principal del menú.
   * Itera sobre la lista de MenuItem de nivel superior y llama a createMenuItemComponent para crear el componente visual para cada elemento.
 * createMenuItemComponent(FacesContext, FaceletContext, MenuItem, int):
   * Crea el componente Jakarta Faces correspondiente a un MenuItem.
   * Si el MenuItem no tiene hijos (es un elemento de menú simple), crea un HtmlCommandLink.
   * Si el MenuItem tiene hijos (es un submenú), crea:
     * Un HtmlPanelGroup para el encabezado del submenú.
     * Un HtmlOutputText para mostrar la etiqueta del submenú.
     * Otro HtmlPanelGroup para contener los elementos del submenú, llamando recursivamente a createMenuItemComponent para cada hijo.
   * Aplica clases CSS para facilitar el estilizado.
Puntos importantes:
 * Estilizado: Necesitarás definir estilos CSS (.menu-item, .submenu, etc.) para que el menú se vea como deseas. El ejemplo básico incluye algunas clases para empezar.
 * Interacción del submenú: El ejemplo HTML incluye un JavaScript básico para mostrar/ocultar los submenús al hacer clic en el encabezado. Puedes personalizar esta interacción con CSS puro (usando :hover y estructuras específicas) o con JavaScript más avanzado.
 * FacesContext y FaceletContext: Estos objetos son necesarios para crear correctamente los componentes de Jakarta Faces, especialmente para la resolución de expresiones EL (#{...}).
 * Alternativa con <ui:repeat>: El segundo ejemplo en el archivo .xhtml muestra cómo puedes iterar directamente sobre la lista de menuItems en tu bean usando <ui:repeat> y construir la estructura del menú directamente en la vista. Esto puede ser más sencillo para menús estáticos o donde la estructura no necesita ser manipulada dinámicamente en el bean como componentes UI.
El enfoque de crear los componentes UI dinámicamente en el bean te da más control si necesitas manipular la estructura del menú en tiempo de ejecución. La alternativa de iterar directamente en la vista es más declarativa y puede ser suficiente para muchos casos. ¡Elige el que mejor se adapte a tus necesidades!
