# Iconos 
<details><summary>Iconos CSS gratis</summary>

<p>
 

* [css.gg](https://css.gg/)
* [Guia de uso](https://github.com/astrit/css.gg#get-started)

1. Descargar Iconos
```
wget https://unpkg.com/css.gg/icons/icons.css
```
2. Copiarlo a la carpeta resources/css

```
resources/css/icons.css


```
3. Agregarlo al template.xhtml
```
  <h:outputStylesheet name="iconos.css" library="css"/>

```

4. En las paginas que se desea incluir

```
        <i class="gg-apple-watch"></i>

```

Agregarlo a los componentes

```
  <p:commandButton id="leftMenuCommandButtonProyectos" iconPos="right" value="  " title="#{msg['menubar.proyectos']}"  
                                     icon="gg-apple-watch"/> 
```


5. Para revisar como se usa de clic en el icono deseado
[https://css.gg/app](https://css.gg/app)



</p>

</details>
