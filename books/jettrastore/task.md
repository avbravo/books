
* Actualiza el proyecto JettraStoreShell para el soporte de paginacion que muestre en consola opciones para desplazarse
al primero anterior siguiente y ultima pagina, es decir se puede indicar un tamaño de registros por paginas a mostrar

* Actualizar JettraStoreDriver con todas las implementaciones realizadas y actualiza JettraStoreDriver/guide/book.md


* Actualiza la interface del proyecto JettraStoreFX para que soporte todas las operaciones establecidas y tenga
una vista mas profesional

* Actualiza la vista de JettraStorePoliceFX para que muestre los eventos y estado y el mapa cartesiano se vea mejor

* Actualiza JettraStore/guide/book.md con todas las caracteristicas. 

===

* Integrar JettraMemory con JettraStore, JettraStoreDriver, JettraStoreShell, JettraStoreFX.
Establecer la opcion de habilitar la carga de datos desde memoria RAM como normalmente se esta haciendo usando areas de Stack y Head de la maquina virtual de java
o activar el modo DiskMemory que trabaja directamente en disco usando JettraMemory.
Es decir se cuenta con las dos opciones JVM-RAM o DISK-MEMORY(JettraMemory)

* Actualizar JettraStoreFX para que use el driver JettraStoreDriver para comunicarse con JettraStore

Actualizar la documentacion de todos los proyectos indicando esta caracteristica.

Actualizar JettraMeter con pruebas de alto rendimiento para monitorear la JettraStore.
