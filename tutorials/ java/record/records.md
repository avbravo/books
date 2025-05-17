# Records

[Unleashing Power of Java Interfaces](https://medium.com/codex/unleashing-power-of-java-interfaces-21a21989777b)

[A Comprehensive Journey from Java 8 to Java 21 with Code Examples of Essential API Enhancements”](https://levelup.gitconnected.com/a-comprehensive-journey-from-java-8-to-java-21-with-code-examples-of-essential-api-enhancements-6817d2ab3ba8)

[Exploring Java Records beyond Data Transfer Objects](https://www.infoq.com/articles/exploring-java-records/)

[Using Java 14 Records in JSF via Eclipse](https://balusc.omnifaces.org/2020/11/using-java-14-records-in-jsf-via-eclipse.html)

[Exploring Java Records In A Jakarta EE Context](https://blog.payara.fish/exploring-java-records-in-a-jakarta-ee-context)


[Java Withers - Inside Java Newscast #67](https://nipafx.dev/inside-java-newscast-67/)

[Exploring Java Record Types](https://dev.to/adaumircosta/exploring-java-record-types-o72)

---

## Java Records con Jakarta Faces

[Java Records con Jakarta Faces](https://github.com/eclipse-ee4j/glassfish/issues/25052)

[Ejemplo](https://github.com/hantsy/jakartaee11-sandbox/blob/master/faces/pom.xml)

[Exploring Java Records beyond Data Transfer Objects](https://www.infoq.com/articles/exploring-java-records/?utm_campaign=exploring-java-records-beyond-data-transfer-objects&utm_medium=social_link&utm_source=missinglettr-twitter)


¡Claro que sí! Los Java Records, introducidos en Java 14, son una forma concisa y transparente de crear clases que principalmente contienen datos. Imagínalos como una forma abreviada de escribir clases que se usan comúnmente para transportar información.
¿Qué los hace especiales?
A diferencia de las clases Java tradicionales donde tienes que escribir mucho código repetitivo (constructores, getters, equals(), hashCode(), toString()), los records generan automáticamente todo este código esencial basándose en la declaración de sus componentes de estado.
En esencia, un record es una clase inmutable cuyos componentes de estado se declaran en su encabezado.
Sintaxis básica:
record NombreDelRecord(Tipo componente1, Tipo componente2, ...) {
    // Aquí puedes añadir métodos estáticos, constructores compactos, etc. (opcional)
}

¿Qué se genera automáticamente?
Cuando declaras un record como el anterior, el compilador genera automáticamente:
 * Un constructor canónico con argumentos correspondientes a cada componente.
 * Métodos de acceso (getters) para cada componente, con el mismo nombre que el componente (sin el prefijo get).
 * Implementaciones estándar de los métodos equals() y hashCode(), basadas en el estado de todos los componentes.
 * Una implementación estándar del método toString(), que proporciona una representación legible del estado del record.
Ejemplos de uso:
Veamos algunos ejemplos prácticos para que veas cómo se utilizan los records:
1. Representación de un Punto 2D:
Imagina que necesitas representar un punto en un plano cartesiano. Con una clase tradicional, tendrías algo así:
class Punto {
    private final int x;
    private final int y;

    public Punto(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Punto punto = (Punto) o;
        return x == punto.x && y == punto.y;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }

    @Override
    public String toString() {
        return "Punto{" +
               "x=" + x +
               ", y=" + y +
               '}';
    }
}

Con un record, esto se simplifica enormemente:
record Punto(int x, int y) {}

¡Eso es todo! El record Punto automáticamente tendrá un constructor con argumentos x e y, métodos de acceso x() e y(), y las implementaciones de equals(), hashCode() y toString().
Uso:
Punto p1 = new Punto(10, 20);
Punto p2 = new Punto(10, 20);

System.out.println(p1); // Salida: Punto[x=10, y=20]
System.out.println(p1.x()); // Salida: 10
System.out.println(p1.equals(p2)); // Salida: true

2. Transferencia de Datos (DTO/Data Transfer Object):
Cuando trabajas con APIs o diferentes capas de una aplicación, a menudo necesitas objetos para transportar datos. Los records son ideales para esto:
record Cliente(int id, String nombre, String email) {}

Uso:
Cliente cliente = new Cliente(123, "Ana Pérez", "ana.perez@example.com");
System.out.println(cliente); // Salida: Cliente[id=123, nombre=Ana Pérez, email=ana.perez@example.com]
System.out.println(cliente.nombre()); // Salida: Ana Pérez

3. Representación de Resultados de una Operación:
Puedes usar records para representar el resultado de una operación, incluyendo un valor y un estado (por ejemplo, éxito o error):
record Resultado<T>(boolean exito, T valor, String mensajeError) {}

Uso:
Resultado<String> resultadoExito = new Resultado<>(true, "Operación completada con éxito", null);
Resultado<Integer> resultadoError = new Resultado<>(false, null, "Error al procesar la solicitud");

System.out.println(resultadoExito); // Salida: Resultado[exito=true, valor=Operación completada con éxito, mensajeError=null]
System.out.println(resultadoError.mensajeError()); // Salida: Error al procesar la solicitud

4. Claves Compuestas en Mapas:
Los records son excelentes para usar como claves en mapas cuando necesitas una clave compuesta por múltiples valores:
record Coordenada(int fila, int columna) {}

// ...

Map<Coordenada, String> tablero = new HashMap<>();
tablero.put(new Coordenada(0, 0), "Torre");
tablero.put(new Coordenada(7, 7), "Rey");

System.out.println(tablero.get(new Coordenada(0, 0))); // Salida: Torre

Ventajas de usar Java Records:
 * Concisión: Reduce significativamente la cantidad de código boilerplate.
 * Inmutabilidad: Por defecto, los records son inmutables, lo que los hace seguros para la concurrencia y más fáciles de razonar.
 * Claridad: La intención de la clase como un portador de datos es explícita.
 * Semántica clara: El compilador genera implementaciones estándar y consistentes para métodos comunes.
En resumen, los Java Records son una herramienta poderosa para simplificar la creación de clases que principalmente contienen datos. Son ideales para DTOs, representación de datos simples, claves compuestas y cualquier situación donde necesites una forma concisa y legible de agrupar valores.
