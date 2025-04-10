
- [] Integrar Payaramicro
- [] Descargar los css y js de tailwindcss
- [] Crear el dashboard
- [] Pasarlo a payara-micro:dev
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


Kanban

[https://coderthemes.com/hyper-admin/saas/apps-kanban.html](https://coderthemes.com/hyper-admin/saas/apps-kanban.html)

