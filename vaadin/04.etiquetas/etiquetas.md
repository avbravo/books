# Etiquetas

Las etiquetas se definen mediante Span

```java
   Span label = new Span("");
        label.setVisible(true);
        label.setText("mongodb.uri");
        add(label);

        Span mongodbUriSpan = new Span(mongodbUri);
        mongodbUriSpan.getElement().getThemeList().add("badge small");

        add(mongodbUriSpan);


```


