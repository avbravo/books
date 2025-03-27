# ComboBox

[ComboBox](https://vaadin.com/docs/latest/components/combo-box)


Ejemplo

* Observe el capitulo 08.grid muestra el uso del comboBox

```java

  private void createComboBox() {
        List<Icono> iconos = iconoServices.findAll();
        ComboBox<Icono> comboBox = new ComboBox<>();
        comboBox.setItems(iconos);
        comboBox.setItemLabelGenerator(Icono::getIcono);

        Button button = new Button("Add");
        button.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        button.addClickListener(e -> {
            sendIcono(comboBox.getValue());
            comboBox.setValue(null);

        });

        HorizontalLayout layout = new HorizontalLayout(comboBox, button);
        layout.setFlexGrow(1, comboBox);

        add(layout);
    }

```




