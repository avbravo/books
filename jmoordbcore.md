# Jmoordbcore



<details>
 <summary>Nuevos Features</summary>

<p>

- [] Soporte para MongoDB 8.0
- [x] Actualizar jmoordbcore-genesis a java 21+ 
- [] Seguridad con user y password

- [] Java Records

- [] Fluent API

- [] Generación de indices

- [] Obtener la lista de colecciones


</p>

</details>

<details>
 <summary>Wizard jmoordb</summary>

<p>
  
[https://github.com/avbravo/jmoordbwizard](https://github.com/avbravo/jmoordbwizard)

[https://github.com/avbravo/wizardjmoordb](https://github.com/avbravo/wizardjmoordb)

</p>

</details>


<details>
<summary>JSON relational</summary>

<p>

[Oracle’s new JSON relational capability helps solve a big IT challenge](https://www.oracle.com/database/json-relational-solves-it-challenges/?source=:em:nw:mt::::RC_WMK200429P00044C00181:NSL400346326&elq_mid=254604&sh=2691887182322851815152699222615152241008&cmid=WWMK200429P00044C00181)

</p>

</details>


---

# Jmoordb-core-faces

[https://github.com/avbravo/jmoordb-core-faces/](https://github.com/avbravo/jmoordb-core-faces/)

Framework Java para generar aplicaciones Jakarta Faces que trabajan con Jmoordb-core

Objetivos

* Generar converter a partir de un entity

* Generar ConverterServices a partir de un entity

* Generar Services y ServicesImplementation a partir de un RestClient

* Generar Controller

* Generar Paginas xhtml


```java

@Entity
@Faces(name="Persona", layout="basic.json",controller="PersonaController", accessRole="ADMIN,COLABORADOR", deleteRole="ADMIN")
public class Persona {

@ViewHidden(hidden=true")
@Id
private Long idpersona;

@ViewTextField(name="nombre", label="field.nombre")
@Column
private String nombre;


@ViewAutocomplete(name="pais",label="label.pais",value="idpais", display="idpais, pais")
@Referenced
Pais pais;


@ViewDialogTable(name="deportes",label="label.deportes")
@Embedded
List<Deportes> deportes;

@ViewDialogForm(name="habilidades", label="label.habilidades")
@Embedded
List<Habilidades> habilidades;

```



# Controller

```java

public interface PersonaController extends FacesController<Persona,Long>{

public default beforeSave(){

}

public default afterSave(){

}


}


```
