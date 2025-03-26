calcular tiempo que demora en ejecutarse un proceso

```java

long startTime = System.nanoTime();
// Aquí va el código cuyo tiempo de ejecución quieres medir
long endTime = System.nanoTime();

// Calcula la duración en nanosegundos
long duration = endTime - startTime;

// Si quieres convertir la duración a milisegundos, puedes hacerlo así:
long durationInMilliseconds = duration / 1_000_000;
System.out.println("Duración: " + durationInMilliseconds + " ms");

```

