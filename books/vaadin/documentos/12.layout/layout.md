
# Layout

[https://vaadin.com/docs/latest/getting-started/tutorial/flow/layout](https://vaadin.com/docs/latest/getting-started/tutorial/flow/layout)


## Cree la clase RootLayout.java

* Utilice H2 para el titulo

* Cree objetos de tipo DrawerToggle

* Agregue un logo al Header

* Utilice los archivos de propiedades para los textos de las diversas opciones

* Añada SideNav y SideNavItem para los menus y sus opciones.


```java

package org.vaadin.example.view.layout;

import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;

/*-
 * #%L
 * EclipseStore BookStore Demo
 * %%
 * Copyright (C) 2023 MicroStream Software
 * %%
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 * 
 * SPDX-License-Identifier: EPL-2.0
 * #L%
 */
import com.vaadin.flow.component.dependency.StyleSheet;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Header;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.Scroller;
import com.vaadin.flow.component.sidenav.SideNav;
import com.vaadin.flow.component.sidenav.SideNavItem;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.theme.lumo.LumoUtility;
import org.vaadin.example.MainView;
import org.vaadin.example.view.IconoSimpleGridView;

/**
 * Root layout for all views, containing the main menu.
 *
 */
@StyleSheet("context://frontend/styles/shared-styles.css")
public class RootLayout extends AppLayout {

    private H2 viewTitle;

    public RootLayout() {
        this.setPrimarySection(Section.DRAWER);
        this.addDrawerContent();
        this.addHeaderContent();
        //<theme-editor-local-classname>
        this.addClassName("MainLayout-app-layout-1");
    }

    private void addHeaderContent() {
        final DrawerToggle toggle = new DrawerToggle();
        toggle.getElement().setAttribute("aria-label", "Menu toggle");

        this.viewTitle = new H2();
        this.viewTitle.addClassNames(LumoUtility.FontSize.LARGE, LumoUtility.Margin.NONE);

        this.addToNavbar(true, toggle, this.viewTitle);
    }

    private void addDrawerContent() {
        final var image = new H2(this.getTranslation("application.title"));
        image.addClassNames("app-name");


          final Header header = new Header(new Image("frontend/images/vaadin32.png", "Logo"), image);

  
        final Scroller scroller = new Scroller(this.createNavigation());

        this.addToDrawer(header, scroller);
        	
    }

    private SideNav createNavigation() {
        final SideNav nav = new SideNav();
        nav.addItem(new SideNavItem(this.getTranslation("menubar.home"), MainView.class, VaadinIcon.HOME.create()));
        nav.addItem(new SideNavItem(this.getTranslation("menubar.iconosimple"), IconoSimpleGridView.class, VaadinIcon.AIRPLANE.create()));
        nav.addItem(new SideNavItem(this.getTranslation("menubar.iconoavanzado"), IconoSimpleGridView.class, VaadinIcon.AUTOMATION.create()));
//        nav.addItem(new SideNavItem(this.getTranslation("shops")     , ViewShops.class         , VaadinIcon.SHOP.create()));
//        nav.addItem(new SideNavItem(this.getTranslation("inventory") , ViewInventory.class     , VaadinIcon.STORAGE.create()));
//        nav.addItem(new SideNavItem(this.getTranslation("customers") , ViewCustomers.class     , LineAwesomeIcon.PERSON_BOOTH_SOLID.create()));
//        nav.addItem(new SideNavItem(this.getTranslation("purchases") , ViewPurchases.class     , LineAwesomeIcon.SHOPPING_BASKET_SOLID.create()));
        return nav;
    }

    @Override
    protected void afterNavigation() {
        super.afterNavigation();
        this.viewTitle.setText(this.getCurrentPageTitle());
    }

    private String getCurrentPageTitle() {
        final PageTitle title = this.getContent().getClass().getAnnotation(PageTitle.class);
        return title == null ? "" : title.value();
    }
}



```

## Agregar el layout a las vistas

* Edite la clase MainView.java,òIconAdvancedGridView.java e IconoSimpleGridView.java y añada  layout=RootLayout.class en el @Route

```java

@Route(value = "nombre-view",layout = RootLayout.class)


```


Por ejemplo



```java

@Route(value = "iconosimplegrid",layout = RootLayout.class)
@CdiComponent
@PageTitle("Icono Simple")
public class IconoSimpleGridView extends VerticalLayout {
// <editor-fold defaultstate="collapsed" desc="Config">

    @Inject
    private Config config;
    @Inject
    @ConfigProperty(name = "mongodb.uri")
    private String mongodbUri;
// </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="services">
    @Inject
    IconoServices iconoServices;

// </editor-fold>
    List<Icono> iconos = new ArrayList<>();
    private static Grid<Icono> grid = new Grid<>(Icono.class, false);

    public IconoSimpleGridView() {

    }

    @PostConstruct
    public void init() {
        findAll();
        createHomeButton();

        createGrid();

    }

    private List<Icono> findAll() {
        iconos = iconoServices.findAll();
        return iconos;
    }

    private void createHomeButton() {

        var buttonLogin = new Button("Go Home");
        buttonLogin.setIcon(VaadinIcon.HOME.create());
        buttonLogin.addClickListener(event -> {
            UI.getCurrent().navigate(MainView.class);
        }
        );
        add(buttonLogin);
    }

    private void createGrid() {
        try {

            grid.setItems(iconos);
            grid.addColumn(Icono::getIdicono).setHeader("#").setAutoWidth(true);
            grid.addColumn(Icono::getIcono).setHeader("Icono").setAutoWidth(true);
            grid.addColumn(Icono::getActionHistory).setHeader("History").setVisible(false);

            add(grid);

        } catch (Exception e) {
            System.out.println("\terror " + e.getLocalizedMessage());
            Notification.show("error " + e.getLocalizedMessage());
        }
    }

}



```


Al ejecutarlo podemos verlo dentro del layout

![]image/00_layout.png)