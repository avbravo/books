
# ConfirmDialog

[https://vaadin.com/docs/latest/components/confirm-dialog](https://vaadin.com/docs/latest/components/confirm-dialog)


Ejemplo

* Observe el capitulo 08.grid muestra un ConfirmDialog

```java

 private void createConfirmDialog() {

        dialog = new ConfirmDialog();
        dialog.setHeader("Eliminar \"Icono\"?");
        dialog.setText(
                "Desea eliminar este icono?");

        dialog.setCancelable(true);
        dialog.addCancelListener(event -> setStatus("Canceled"));

        dialog.setConfirmText("Delete");
        dialog.setConfirmButtonTheme("error primary");
        dialog.addConfirmListener(event -> {
            setStatus("Deleted");
            this.removeIcono(iconoSelected);
        }
        );

        Button button = new Button("Open confirm dialog");
        button.addClickListener(event -> {
            dialog.open();
            statusConfirmDialog.setVisible(false);
        });

        add(statusConfirmDialog);
    }

    private void setStatus(String value) {
        statusConfirmDialog.setText("Status: " + value);
        statusConfirmDialog.setVisible(true);
    }


```

