<p class="titulo-capitulo">CAPÍTULO 4</p>
<div id="04_agregationframework_1"></div>
## AGREGATION FRAMEWORK

<figure>
    ![Entity](imagenes/entity.png)
    <figcaption></figcaption>
</figure>

<div id="04_agregationframework_1_0_descripcion"></div>
### 4.0 Descripcion
Un framework de agregación permite ejecutar operaciones como un flujo, donde
la primera se convierte en un flujo para la siguiente con la finalidad
de ejecutar operaciones y devolver algun resultado.
- Esta anotación permitira el uso de agregaciones en los repositorios
- El manejo es diferente a las anotaciones estandar de jmoordb-core
- El resultado puede ser de diversos tipos



**Referencia**

[Aggregation](https://studio3t.com/knowledge-base/articles/mongodb-aggregation-framework/)
[Java - Aggregation Pipeline](https://www.mongodb.com/developer/languages/java/java-aggregation-pipeline/)
```java
@Agregation (AgregationType = AgregationType.SUM / AVG/ MEDIA, 
field="poblacion", sorted="", 
public Integer|Long|Double agregationSumOfPoblacionWhereIdOceanoAndOceano(String idoceano, String oceano);


@Agregation (AgregationType = AgregationType.SUM / AVG/ MEDIA, 
field="poblacion", sorted="", 
public Integer|Long|Double agregationGroupByOceano(String idoceano, String oceano);

```

\newpage
<div id="04_agregationframework_4_1_sumOf"></div>
### 4.1 SumOf 
- Realiza suma de un campo
- resultView = es la lista de campos y variables calculadas a mostrar         
- Devuelve un solo valor (Integer, Long, Double) si es un sumOf simple es decir no devuelve mas campos resultView =""
- Devuelve un Result si la lista de campos a mostrar incluye campos y variables
- Primero se descompone en dos cadenas separadas por el Where
- La primera cadena contiene el componente de agregacion hasta el Where
- La segunda cadena contiene del Where en adelante y aplica las reglas de Find para generar consultas         
         
```java         
@Aggregation(resultView="")
public Double  sumOfVentasWhereOceanoAndPlaneta(String oceano, String planeta);
```
- sumOfVentaswhere => se identifica sumOf => campo seria ventas que se desea aplicar y se elimina el Where
- OceanoAndOceano  => aplica las reglas de Find para crear un filtro
- Observe que el valor de retorno es uno solo (Double|Integer|Long) y que resultView="" esta vació

```java         
@Aggregation(resultView="@ventas,oceano, idioma")
public Result sumOfVentasWhereOceanoAndPlaneta(String oceano, String planeta);
```      
- resultView => indica que se devolvera el campo calculado @ denota que es una variable y los campos oceano, idioma en el objeto Result.
         
         
         
```java         
@Aggregation()
public Result sumOfVentasGroupByOceanoWhereOceanoAndPlaneta(String oceano, String planeta);
```             
 Pasos
- Separar en dos cadenas mediante el Where
- Descomponeer por las mayusculas
- verificar si tiene sunOf
      si tiene Where es valido
      si tiene GroupBy es valido para ordenar (Indica que debe devolver un hash de dos valores la suma y el atributo que agrupa
      crear un Arreglo separado con los campos del sumOf y GroupBy
      después del Where se toman esos valores y se precesan como un find los anteriores se quitan de ese array es decir no se usa sumOf ni su campo , ni  el agrupado ni la palabra reservada GroupBy.
- resultVew indica que se va a devolver la suma de ventas y el campo oceano que fue el agrupado, cuando se usa GroupBy se devolveran esos dos campos
         


***
## Ejemplo  
- As you can see, we have one document for each zip code in the USA and for each, we have the associated population.  
- To calculate the population of New York, I would have to sum the population of each zip code to get the population of the entire city.  
- Let's try to find the 3 biggest cities in the state of Texas. Let's design this on paper first.  
- I don't need to work with the entire collection. I need to filter only the cities in Texas.  
- Once this is done, I can regroup all the zip code from a same city together to get the total population.  
- Then I can order my cities by descending order or population.   
- Finally I can keep the first 3 cities of my list.  

```java
/**
 * find the 3 most densely populated cities in Texas.
 * @param zips sample_training.zips collection from the MongoDB Sample Dataset in MongoDB Atlas.
 */
private static void threeMostPopulatedCitiesInTexas(MongoCollection<Document> zips) {
    Bson match = match(eq("state", "TX"));
    Bson group = group("$city", sum("totalPop", "$pop"));
    Bson project = project(fields(excludeId(), include("totalPop"), computed("city", "$_id")));
    Bson sort = sort(descending("totalPop"));
    Bson limit = limit(3);

    List<Document> results = zips.aggregate(Arrays.asList(match, group, project, sort, limit))
                                 .into(new ArrayList<>());
    System.out.println("==> 3 most densely populated cities in Texas");
    results.forEach(printDocuments());
}
```

salida
```json
==> 3 most densely populated cities in Texas
{
  "totalPop": 2095918,
  "city": "HOUSTON"
}
{
  "totalPop": 940191,
  "city": "DALLAS"
}
{
  "totalPop": 811792,
  "city": "SAN ANTONIO"
}
```

Se observa que se genera un List<> con dos elementos nombre de la ciudad y la población

RepositorySeria
```java
@Agregation
public List< > agregationMatchStateGroupByCitySumOfPopAsTotalPopProjectIncludeTotalPopProjectIncludeCitySortTotalPopDESC
```
o
```java
@Agregation(var="totalPop", Include="City, $totalPop",Exclude="_Id", sort="$totalPop", sort.DESC )
public List< > agregationSumOfPopGroupByCityMatchStatePagination(String state, Pagination pagination)
```

***
## Ejemplo 2

This collection of 500 posts has been generated artificially but it contains arrays and I want to show you how we can manipulate arrays in a pipeline.
Let's try to find the three most popular tags and for each tag, I also want the list of post titles they are tagging.  
```java
/**
 * find the 3 most popular tags and their post titles
 * @param posts sample_training.posts collection from the MongoDB Sample Dataset in MongoDB Atlas.
 */
private static void threeMostPopularTags(MongoCollection<Document> posts) {
    Bson unwind = unwind("$tags");
    Bson group = group("$tags", sum("count", 1L), push("titles", "$title"));
    Bson sort = sort(descending("count"));
    Bson limit = limit(3);
    Bson project = project(fields(excludeId(), computed("tag", "$_id"), include("count", "titles")));
    List<Document> results = posts.aggregate(Arrays.asList(unwind, group, sort, limit, project)).into(new ArrayList<>());
    System.out.println("==> 3 most popular tags and their posts titles");
    results.forEach(printDocuments());
}
```

```java
@Agregation(GroupPush="titles", GroupPushField="$title" )
public List< > agregationUnwindTagsSumOfTagsGroupByTagsGroupVarCountPagination(pagination)

```


SI es integer,Double, Long

o puede ser un conjunto nuevo

un QueryEsoecial que se extraigan los datos
