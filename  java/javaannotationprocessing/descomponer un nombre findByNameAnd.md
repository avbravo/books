# descomponer un nombre findByNameAnd
[java-dividir-cadena-cuando-se-encuentra-una-letra-mayuscul](https://ajaxhispano.com/ask/java-dividir-cadena-cuando-se-encuentra-una-letra-mayuscula-61849/)

## Conocer si una cadena empieza por un String dado
```java
 /**
                 * Comprobar que empieza por findBy
                 */
 String text = "findByIdOceanoAndOceano"
                Pattern pat2 = Pattern.compile("^findBy*");
                Matcher mat2 = pat2.matcher(text);
                if (mat.matches()) {
                    System.out.println("Válido");
                } else {
                    System.out.println("No Válido");
                }
```

## Descomponer la cadena

```java
String text = "findByIdOceanoAndOceano"
String[] r = text.split("(?<=.)(?=\\p{Lu})");
System.out.println(Arrays.toString(r));
   
Imprime
[find, By, Idoceano, And, Oceano]
```
