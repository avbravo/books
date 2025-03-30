
# Vaadin

## Guia de Payara

[Watch the Recording: Enterprise Java Application Development With Jakarta EE and Vaadin](https://blog.payara.fish/watch-the-recording-enterprise-java-app-development-with-jakartaee-vaadin)
Mi proyecto a partir de MAven

[https://github.com/avbravo/myvaadin](https://github.com/avbravo/myvaadin)

## Started

[https://start.vaadin.com/app](https://start.vaadin.com/app)

Mi Proyecto con Started

[https://github.com/avbravo/vaadinpayaramicro.git](https://github.com/avbravo/vaadinpayaramicro.git)
Pasos:

1. Crear el proyecto selecionado JakartaEE

![](001_started.png)


Descargue el archivo zip y descomprimalo

2. Abra el proyecto con el IDE


3. Seleccione el proyecto --> Clic derecho --> New -->Other --> Payara --> PayaraMicroPlugin


![](002_payaraplugin.png)

seleccione la version de Payara y de clic en Finish

![](003_payaraversion.png)

4. Edite el archivo beans.xml

![[(004_beans.png)

y reemplace el contenido por

```xml

<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="https://jakarta.ee/xml/ns/jakartaee"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xsi:schemaLocation="https://jakarta.ee/xml/ns/jakartaee https://jakarta.ee/xml/ns/jakartaee/beans_4_0.xsd"
       bean-discovery-mode="all"
version="4.0">
</beans>


```

5. Edite el archivo web.xml

```xml

<?xml version="1.0" encoding="UTF-8"?>
<web-app id="it-test-v8-compatibility-mode" version="3.0"
         xmlns="http://java.sun.com/xml/ns/j2ee"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://java.sun.com/xml/ns/j2ee http://java.sun.com/xml/ns/j2ee/web-app_3_0.xsd">

</web-app>

```

cambielo por

```xml
<?xml version="1.0" encoding="UTF-8"?>
<web-app xmlns="https://jakarta.ee/xml/ns/jakartaee"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="https://jakarta.ee/xml/ns/jakartaee https://jakarta.ee/xml/ns/jakartaee/web-app_5_0.xsd"
         version="5.0">

</web-app>

```

6. Edite el archivo pom.xml 

* Agregue  la propiedad final.name 
* Cambie source y target a Java 21.

```
    <properties>
        
      <final.name>vaadinpayaramicro</final.name>
      <maven.compiler.source>21</maven.compiler.source>
     <maven.compiler.target>21</maven.compiler.target>
    </properties>

```
* En la seccion 

```
<build>
  <finalName>${final.name}</finalName>
<\build>
```



7. Ejecute el proyecto 

```shell

mvn clean verify payara-micro:start


```


Puede que envie errores

![](005_error.png)






8. Edite la clase MainView.java

* Proceda a comentar las lineas de manera que el codigo quede de la siguiente manera, y agregue add(textField);

```java


@Route("")
@CdiComponent
public class MainView extends VerticalLayout {

//    @Inject
//    private GreetService greetService;

    @PostConstruct
    public void init() {
        // Use TextField for standard text input
        TextField textField = new TextField("Your name");
        textField.addThemeName("bordered");

        // Button click listeners can be defined as lambda expressions
//        Button button = new Button("Say hello", e -> Notification
//                .show(greetService.greet(textField.getValue())));

        // Theme variants give you predefined extra styles for components.
        // Example: Primary button is more prominent look.
//        button.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        // You can specify keyboard shortcuts for buttons.
        // Example: Pressing enter in this view clicks the Button.
//        button.addClickShortcut(Key.ENTER);

        // Use custom CSS classes to apply styling. This is defined in
        // shared-styles.css.
        addClassName("centered-content");
add(textField);
//        add(textField, button);
    }

}


```

Puede observar que la aplicación se ejecuta perfectamente.

etenga la ejecución del proyecto

9. Remueva los comentarios y remueva add(textField);

```java

import com.vaadin.cdi.annotation.CdiComponent;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;

import jakarta.annotation.PostConstruct;
import jakarta.inject.Inject;

/**
 * The main view contains a text field for getting the user name and a button
 * that shows a greeting message in a notification.
 */
@Route("")
@CdiComponent
public class MainView extends VerticalLayout {

    @Inject
    private GreetService greetService;

    @PostConstruct
    public void init() {
        // Use TextField for standard text input
        TextField textField = new TextField("Your name");
        textField.addThemeName("bordered");

        // Button click listeners can be defined as lambda expressions
        Button button = new Button("Say hello", e -> Notification
                .show(greetService.greet(textField.getValue())));

        // Theme variants give you predefined extra styles for components.
        // Example: Primary button is more prominent look.
         button.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        // You can specify keyboard shortcuts for buttons.
        // Example: Pressing enter in this view clicks the Button.
        button.addClickShortcut(Key.ENTER);
  
        // Use custom CSS classes to apply styling. This is defined in
        // shared-styles.css.
        addClassName("centered-content");

        add(textField, button);
    }

}


```



10. Ingrese al navegador

[http://localhost:8080/vaadinpayaramicro/](http://localhost:8080/vaadinpayaramicro/)


Se puede observar la aplicación ejecutandose

![[(006_run.png)


Detenga la ejecución del proyecto

9. Edite el archivo pom.xml y modifique el plugin de payara-micro

Cambie
```
 <plugin>
                <groupId>fish.payara.maven.plugins</groupId>
                <artifactId>payara-micro-maven-plugin</artifactId>
                <configuration>
                    <payaraVersion>${version.payara}</payaraVersion>
                    <deployWar>false</deployWar>
                    <commandLineOptions>
                        <option>
                            <key>--autoBindHttp</key>
                        </option>
                        <option>
                            <key>--deploy</key>
                            <value>${project.build.directory}/${project.build.finalName}</value>
                        </option>
                    </commandLineOptions>
                </configuration>
                <version>1.3.0</version>
  </plugin>

```

Por

```

 <plugin>
                <groupId>fish.payara.maven.plugins</groupId>
                <artifactId>payara-micro-maven-plugin</artifactId>
                <configuration>
                    <payaraVersion>${version.payara}</payaraVersion>
                    <deployWar>false</deployWar>
                    <commandLineOptions>
                        <option>
                            <key>--autoBindHttp</key>
                        </option>
                        <!--puerto 9001 -->
                     
                        <!-- desabilita Hazelcas -->
                        <option>
                            <key>--noHazelcast</key>
                        </option>
                        <option>
                            <key>--logo</key>
                        </option>

                        <option>
                            <key>--deploy</key>
                            <value>${project.build.directory}/${project.build.finalName}</value>
                        </option>                     
                    </commandLineOptions>
                    <!--
                    JDK 17+ Soluciona error con EJB
                    -->                         
                    <javaCommandLineOptions>
                        <option>
                            <key>--add-opens</key>
                            <value>java.base/java.io=ALL-UNNAMED</value>
                        </option>
                        <option>
                            <key></key>
                            <value>-Djdk.util.zip.disableZip64ExtraFieldValidation=true</value>
                        </option>
                    </javaCommandLineOptions>                
                </configuration>
                <version>2.0</version>
            </plugin>

```



11. Ejecute el proyecto 

```shell

mvn clean verify payara-micro:start


```




## Revisar EclipseStore
[https://github.com/eclipse-store/bookstore-demo](https://github.com/eclipse-store/bookstore-demo)