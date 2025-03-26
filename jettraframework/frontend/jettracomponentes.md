# jettracomponentes.md

Tablas como lo hace Vaadin

[HTML Table integration for Flow](https://vaadin.com/directory/component/html-table)

```java
Table table = new Table();

TableRow headerRow = table.addRow();
TableCell cell = headerRow.addHeaderCell();
cell.setText("Hello world, it's me.");
cell.setColSpan(3);
cell.getStyle().set("background-color", "#fdd");

TableRow detailsRow = table.addRow();
detailsRow.addDataCell().setText("Hello");
detailsRow.addDataCell().setText("World");

cell = detailsRow.addDataCell();
cell.setText("It's me.");
cell.setRowSpan(2);
cell.getStyle().set("background-color", "#dfd");

detailsRow = table.addRow();
cell = detailsRow.addDataCell();
cell.setText("Hello");
cell.setColSpan(2);
cell.getStyle().set("background-color", "#ddf");

table.getCaption().setText("Using col- and rowspan");

add(table);


```

