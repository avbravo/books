# Responsive

[Building responsive layouts with Vaadin utility classes](https://vaadin.com/blog/building-responsive-layouts-with-vaadin-utility-classes?utm_content=303069302&utm_medium=social&utm_source=twitter&hss_channel=tw-33905417)

Creating a responsive dashboard using Vaadin involves leveraging Vaadin's server-side Java framework along with its UI components designed for responsiveness. Here's a step-by-step guide on how to create a responsive dashboard in Vaadin:

### Step 1: Set Up Your Vaadin Project

First, ensure you have a Vaadin project set up. If you're starting from scratch, you can follow the official Vaadin documentation to create a new project. This typically involves adding Vaadin dependencies to your `pom.xml` file if you're using Maven.

### Step 2: Use Vaadin Board for Responsiveness

Vaadin Board is a powerful component for creating responsive layouts. It automatically adjusts its contents according to the screen size, making it ideal for dashboards that need to display well on various devices.

```java
import com.vaadin.flow.component.board.Board;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

public class MainView extends VerticalLayout {
    public MainView() {
        Board board = new Board();
        
        // Add rows and components to the board here
        
        add(board);
    }
}
```

### Step 3: Add Custom Components for Dashboard Elements

Create custom components for different elements of your dashboard, such as charts or stats. These components can then be added to the Vaadin Board.

```java
// Example of a custom chart component
public class DashboardChart extends Component {
    // Implementation of the chart component
}
```

### Step 4: Implement Responsiveness

Vaadin Board takes care of responsiveness by reordering components based on screen size. However, you can further customize the responsiveness by using CSS or Vaadin's built-in responsive utilities.

### Step 5: Additional Features

Consider adding features like a header, footer, and navigation based on core Vaadin components. Also, implement functionality for customizing the dashboard display, bookmarking views, and sharing URLs.

```java
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.router.HasUrlParameter;

// Example of implementing URL parameters for customization
public class MainView extends VerticalLayout implements HasUrlParameter<String> {
    @Override
    public void setParameter(BeforeEvent event, String parameter) {
        // Logic to show data corresponding to the URL parameter
    }
}
```

### Step 6: Make Your Dashboard a Progressive Web App (PWA)

Enhance your dashboard by making it installable as a PWA on mobile devices. Vaadin simplifies this process with annotations.

```java
import com.vaadin.flow.component.page.Push;
import com.vaadin.flow.router.Route;

@Push
@Route("")
@PWA(name = "Dashboard", shortName = "Dash")
public class MainView extends VerticalLayout {
    // Dashboard implementation
}
```

### Summary

Creating a responsive dashboard in Vaadin involves setting up a project, utilizing the Vaadin Board component for layout, creating custom components for dashboard elements, implementing additional features for customization and sharing, and optionally making the dashboard a PWA. Vaadin's server-side Java framework simplifies these steps, allowing developers to focus on business logic rather than UI details.

Citations:
[1] https://github.com/vaadin/dashboard-demo
[2] https://vaadin.com/blog/building-a-dashboard-for-a-rest-web-service-in-java
[3] https://vaadin.com/docs/latest/components/board
[4] https://github.com/vaadin/vaadin-board
[5] https://vaadin.com/blog/building-responsive-websites-with-java-and-vaadin-flow
[6] https://www.npmjs.com/package/@vaadin/board