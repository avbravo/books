\newpage
\begin{flushright}
\textbf{Prefacio}
\end{flushright}  

El desarrollo de aplicaciones Java especialmente en el  Back-End
ha tomado mucha relevancia en los últimos años. El surgimiento de 
microservicios, el incremento de bases de datos NoSQL. El uso
de contenedores y nuevos modelos de desarrollo han traído consigo
nuevos requerimientos. 




Este libro se enfoca en el desarrollo de aplicaciones empresariales Jakarta EE 
utilizando bases de datos NoSQL. Mediante el framework **Jmoordb-core** podras 
crear aplicaciones con un alto desempeñó ,ya que elimina el empleo de Genéricos 
y Reflexión. «Jmoordb-core» utiliza generación de código en tiempo de compilación
mediante Java Annotation Processing.




Este libro es un recorrido a las características del framework y su utilización 
en proyectos <i lang="lt"> proyectos reales</i>.Está orientado a desarrolladores
que inician su trabajo con NoSQL, Microservicios, por lo cual encontrarás
muchas referencias, códigos de ejemplo y consejos sobre temas generales.


**Quién puede leer este libro**  
Si eres un desarrollador Java con conocimientos basicos de Jakarta EE, NoSQL, y 
Microprofile y deseas generar
microservicios en Java, este libro te ayudará a desarrollar aplicaciones rápidamente.




**Que cubre el libro**  
Capítulo  1, Introducción básica a Jakarta EE, Microprofile, Bases de datos NoSQL, 
Genéricos y Reflexión,
además Java Annotation Processing y como podemos generar un Framework Java.
 
Capítulo  2, Introducción a Jmoordb aspectos relevantes y características. 
Conocer la arquitectura de jmoordb-core permitira obtener el máximo provecho al
framework.


Capítulo  3, Muestra como definir entidades con sus correspondientes anotaciones.
Podrás declarar documentos embebidos y producir referencias entre uno o varios 
documentos.


Capítulo 4, Aprenderás a definir repositorios y a utilizar las anotaciones.


Capítulo  5, Los campos autoincrementables son una limitación de las bases de
datos NoSQL. Este capítulo te ayudará a comprender como jmoordb-core implementa 
datos de tipo secuenciales.          

Capítulo 6. Java Driver MongoDB, entramos a ver el código que genera el framework
de manera que permita una fácil comprensión de como este interactúa con las 
colecciones y permita al desarrollador de definir de manera más sencilla sus 
interfaces.

**Código fuente**  
El código fuente del proyecto lo puedes encontrar en la siguiente dirección
[jmoordb-core-processor-example](https://github.com/avbravo/jmoordb-core-processor-example.git)




**Convenciones**  
Este libro contiene una seria de estilos de texto para ayudar a distinguir 
fácilmente las secciones y elementos que lo componen, Las palabras reservadas 
serán escritas en **negrita** y los segmentos de código tendrán la siguiente
 apariencia:
\small
```java
public class Prueba{  
   void save( ) {
   }
}
```
\normalsize

Cuando una linea de código excede el limite de la pagina se usara ```\``` para
indicar que la sintaxis en la siguiente linea debe escribirse en la misma linea.

En sus aplicaciones escriba las instrucciones en una sola linea.

Los enlaces se mostrarán de color azul [https://avbravo.blogspot.com](https://avbravo.blogspot.com)

Se usaran los terminos entity y entidad oara hacer referencia a las clases Java
que identifican una colección de la base  de datos.

Se usara el termino repository y repositorio para referirse a las interfaces
repositorios.

**Errata**  
Si encuentra errores en el libro, será un placer recibir sus comentarios al 
respecto.


**Preguntas**   
Si tiene alguna pregunta, por favor escriba email a [avbravo@gmail.com](avbravo@gmail.com)


<br>
<p class="right-align">Aristides Villarreal Bravo</p>
\newpage