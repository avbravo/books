# Encabezado y pie páginas

[Latex](https://manualdelatex.com/tutoriales/encabezado-y-pie-de-pagina)
***

# Encabezados y pies de página personalizados con el paquete fancyhdr

se agrega a la seccion de configuracion del libro
header-includes: |
    \usepackage{fancyhdr}  

***
Existen principalmente dos opciones para dar formato al encabezado y al pie de página 
de un documento escrito en LaTeX.
La opción más sencilla no requiere ningún paquete adicional y consiste únicamente 
en definir un estilo de página en el preámbulo mediante el comando \pagestyle.
 Con este comando podemos escoger entre cuatro estilos diferentes para el encabezado y pie de página:
## empty
En este caso tanto el encabezado como el pie de página aparecen totalmente vacíos.
```
\pagestyle{empty}
```


## plain
Este es el estilo que aparece por defecto si no se indica nada. Incluye únicamente el número de página centrado en el pie de página.
```
\pagestyle{plain}
```

## headins
Este estilo muestra el número de página y títulos de capítulos o secciones. La distribución de estos elementos depende de la clase de documento. Normalmente incluye el número de página situado a la derecha del encabezado y el título del capítulo o sección a la izquierda.
\pagestyle{headings}

## myheadings
El estilo myheadings es equivalente al headings pero permite definir el texto que aparece en el encabezado.
\pagestyle{myheadings}


