
## Remark
[remark](https://remarkjs.com/#1)  
[em github](https://github.com/gnab/remark) 
[presentaciones-con-markdown](https://atareao.es/como/presentaciones-con-markdown/)
[Online and Offline Slides Using Markdown and Remark](https://galdebert.github.io/posts/remark-1/)

 

Cree una pagina html
```html
<!DOCTYPE html>
<html>
  <head>
    <title>Title</title>
    <meta charset="utf-8">
    <style>
      @import url(https://fonts.googleapis.com/css?family=Yanone+Kaffeesatz);
      @import url(https://fonts.googleapis.com/css?family=Droid+Serif:400,700,400italic);
      @import url(https://fonts.googleapis.com/css?family=Ubuntu+Mono:400,700,400italic);

      body { font-family: 'Droid Serif'; }
      h1, h2, h3 {
        font-family: 'Yanone Kaffeesatz';
        font-weight: normal;
      }
      .remark-code, .remark-inline-code { font-family: 'Ubuntu Mono'; }
    </style>
  </head>
  <body>
    <textarea id="source">

class: center, middle

# Title

---

# Agenda

1. Introduction
2. Deep-dive
3. ...

---

# Introduction

    </textarea>
    <script src="https://remarkjs.com/downloads/remark-latest.min.js">
    </script>
    <script>
      var slideshow = remark.create();
    </script>
  </body>
</html>
```
## Salto de Slide
use ---
```
---
```

## Notas
use ??? para indicar que es una nota

```
---
# Slide 1
This is slide 1.
**Bold text**

???
Slide 1 notes

---
# Slide 2
## Subtitle
*Italic text*
* Item 1
* Item 2

???
Slide 2 notes
```
## Para ver las notas presione la tecla P
   para ocultar las notas de la presentación presione nuevamente P
   
## Para ver la presentación sin las barras del navegador presione la tecla C.



***

## Solo tiene que abrir el archivo html en su navegador

## Estilos

```
name: agenda
class: middle, center
# Agenda
The name of this slide is {{ name }}.
```

***
## Recomendable descargar remark y colocarla en la ruta del archivo html

```html
<!DOCTYPE html>
<html>
  <head>
    <style type="text/css">
      /* Slideshow styles */
    </style>
  </head>
  <body>
     <textarea id="source">
      <!-- Slideshow Markdown -->
    </textarea>
     <script src="remark.js">
    </script>
    <script>
       var slideshow = remark.create();
    </script>
  </body>
</html>
```

***
## Indicar nombre del slide 
 use
 ```
 name: introduccion
 ```
 Hacer un vinculo a ese slide
 ```
 [Introduccion](#introduccion)
```


***
## Aplicar hojas de estilo en la misma pagina
```html
<!DOCTYPE html>
<html>
  <head>
    <style type="text/css">
      /* Slideshow styles */
    </style>
  </head>
  <body>
     <textarea id="source">
      <!-- Slideshow Markdown -->
    </textarea>
     <script src="remark.js">
    </script>
    <script>
       var slideshow = remark.create();
    </script>
  </body>
</html>
```

## Aplicar hojas de estilo desde otra pagina

```html
<!DOCTYPE html>
<html>
  <head>
    <link rel="stylesheet" type="text/css" href="stylesheet.css">
  </head>
  <body>
    <textarea id="source">
        <!-- aquí va la presentación>
    </textarea>
    <script src="remark.js"></script>
    <script>
       var slideshow = remark.create();
    </script>
  </body>
</html>
```

***
## Mi presentacion de ejemplo

```html
<!DOCTYPE html>
<html>
  <head>
    <title>Title</title>
    <meta charset="utf-8">
    <style>
      @import url(https://fonts.googleapis.com/css?family=Yanone+Kaffeesatz);
      @import url(https://fonts.googleapis.com/css?family=Droid+Serif:400,700,400italic);
      @import url(https://fonts.googleapis.com/css?family=Ubuntu+Mono:400,700,400italic);

      body { font-family: 'Droid Serif'; }
      h1, h2, h3 {
        font-family: 'Yanone Kaffeesatz';
        font-weight: normal;
      }
      .remark-code, .remark-inline-code { font-family: 'Ubuntu Mono'; }
    </style>
  </head>
  <body>
    <textarea id="source">

class: center, middle

# Jmoordb-core

---

# Agenda

1. [Introduction](#introduccion)
2. [Slide1](#slide1)
3. [Slide2](#slide2)

---
name: introduccion
# Introduction
Jmoordbcore es un framework java

---
name: slide1
# Slide 1
This is slide 1.
**Bold text**

???
Slide 1 notes

---
name: slide2
class: background-color: coral;
# Slide 2
## Subtitle
*Italic text*
* Item 1
* Item 2

???
Slide 2 notes

    </textarea>
    <script src="https://remarkjs.com/downloads/remark-latest.min.js">
    </script>
    <script>
      var slideshow = remark.create();
    </script>
  </body>
</html>
```
***
## color fondo
```html
<!DOCTYPE html>
<html>
  <head>
    <title>Title</title>
    <meta charset="utf-8">
<style>
.theBlackBackground {background-color:#000;color: red;}
</style>
     </head>
  <body>

<textarea id="source">

class: middle, center, theBlackBackground
# Title
</textarea>

 <script src="http://gnab.github.io/remark/downloads/remark-latest.min.js" type="text/javascript">
</script>
 <script type="text/javascript">
      var slideshow = remark.create();
 </script>
  </body>
</html>
```

*** 
## Exportar presentacion html a pdf
[decktape](https://github.com/astefanutti/decktape)

***
## OTRA FORMA Crear presentaciones en Visual Studio code
[Marp](https://marp.app/)
[Creating Professional Presentation from Markdown](https://levelup.gitconnected.com/creating-professional-presentation-from-markdown-975c65211359)
[marp-team](https://github.com/marp-team/marp)
---
marp: true
---

# Slide 1
This is slide 1.
**Bold text**

---
# Slide 2
## Subtitle
*Italic text*
* Item 1
* Item 2

