# bellsoft/liberica-openjdk-alpine


[https://hub.docker.com/r/bellsoft/liberica-openjdk-alpine](https://hub.docker.com/r/bellsoft/liberica-openjdk-alpine)

[https://hub.docker.com/r/bellsoft/liberica-openjdk-alpine/tags](https://hub.docker.com/r/bellsoft/liberica-openjdk-alpine/tags1111)


Para usar Java 27 con las imágenes de Docker oficiales de BellSoft Liberica OpenJDK, la etiqueta exacta dependerá del tipo de arquitectura y sistema base que prefieras. Dado que Java 27 es la versión de lanzamiento más reciente en este momento, BellSoft ha actualizado sus repositorios con los tags específicos para esta versión. [1, 2, 3] 
La recomendación directa para tu Dockerfile se divide según tu entorno objetivo:
## 1. Opción recomendada para Producción (Ligeras y seguras)
Si buscas optimizar el almacenamiento y el rendimiento en la nube, debes usar imágenes basadas en Alpine Linux o las variantes con optimización CDS (Class Data Sharing): [4, 5] 

* 
* Para Alpine Linux (La más ligera):

FROM bellsoft/liberica-openjdk-alpine:27

* Con optimización CDS (Mejora el tiempo de arranque de Java 27):

FROM bellsoft/liberica-openjdk-alpine:27-cds

* 

## 2. Opción recomendada para Máxima Compatibilidad
Si tu aplicación utiliza librerías de infraestructura nativa o herramientas que requieren una distribución tradicional como Debian, utiliza la siguiente etiqueta: [3] 

* 
* Para Debian Linux:

FROM bellsoft/liberica-openjdk-debian:27

* 

------------------------------
## Ejemplo Completo de Implementación (Multi-stage Build)
Para aprovechar al máximo Java 27 sin arrastrar herramientas innecesarias a tu entorno final, es una excelente práctica implementar una estrategia de construcción en dos etapas:

# ETAPA 1: Compilación de la aplicaciónFROM maven:3.9-eclipse-temurin-27 AS buildWORKDIR /appCOPY . .RUN mvn clean package -DskipTests
# ETAPA 2: Entorno de ejecución optimizadoFROM bellsoft/liberica-openjdk-alpine:27-cdsWORKDIR /app# Copia el jar generado en la etapa anteriorCOPY --from=build /app/target/mi-app-java27.jar app.jar
# Comando para ejecutar usando las ventajas de hilos virtuales de Java 27ENTRYPOINT ["java", "-jar", "app.jar"]

Puedes consultar el listado completo de imágenes distribuidas y sus actualizaciones periódicas directamente en el [Docker Hub oficial de BellSoft](https://hub.docker.com/r/bellsoft/liberica-openjdk-alpine/tags). [1] 
¿Estás desarrollando un servicio web moderno (como Spring Boot) donde te interese reducir el tiempo de inicio al mínimo empleando CRaC o AppCDS? Si me das más detalles de tu arquitectura, te puedo guiar sobre qué modificadores adicionales de BellSoft te convienen más. [6, 7, 8] 

[1] [https://hub.docker.com](https://hub.docker.com/r/bellsoft/liberica-openjdk-alpine/tags)
[2] [https://bell-sw.com](https://bell-sw.com/blog/bellsoft-s-docker-hub-images-overview/)
[3] [https://github.com](https://github.com/bell-sw/Liberica/pkgs/container/liberica-openjdk-debian)
[4] [https://hub.docker.com](https://hub.docker.com/r/bellsoft/liberica-openjdk-alpine)
[5] [https://hub.docker.com](https://hub.docker.com/r/bellsoft/liberica-runtime-container)
[6] [https://hub.docker.com](https://hub.docker.com/r/bellsoft/liberica-openjdk-debian)
[7] [https://dev.to](https://dev.to/rawas_aditya/java-27-explained-new-features-jvm-changes-and-what-it-means-for-backend-developers-37g8)
[8] [https://bell-sw.com](https://bell-sw.com/blog/bellsoft-s-docker-hub-images-overview/)
