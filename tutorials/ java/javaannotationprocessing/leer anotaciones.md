# leer anotaciones de un archivo .Java

En este caso lo haremos desde un plugin netbeans que generará archivos, por lo cual contamos con una clase Java que representa el modelo (entidad en jmoordbcore). por lo cual seguiremos éstos pasos:
1. Compoilar la clase Java
2. Usar reflexión para leer la clase compilada
3. Leer las anotaciones por cada campo y generar un Lis<FieldAnnotation> que contendrá información de la anotacion.
```java
FieldAnnotation{

String field;
String type;
Columna columna;
Embbeded embbeded 
Referenced refereenced 
Autoincrement autoincrement;


}
```

4. Recorrer el List<FieldAnnotation>
5. Eliminar el archivo compilado.clas


# Compilararchivo Java

To compile a Java class at runtime, you can use the `ToolProvider` class available in Java 6 and later versions. This class provides methods for locating tool providers, including compilers, and complements the functionality of `ServiceLoader`. Here's a basic example of how to use `ToolProvider` to compile a Java class at runtime:

```java
import javax.tools.JavaCompiler;
import javax.tools.ToolProvider;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;

public class CompileAtRuntime {
    public static void main(String[] args) {
        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
        int result = compiler.run(null, null, null, "path/to/your/JavaFile.java");

        if (result == 0) {
            System.out.println("Compilation successful");
        } else {
            System.out.println("Compilation failed");
        }
    }
}
```

In this example, replace `"path/to/your/JavaFile.java"` with the actual path to the Java file you want to compile. The `run` method of the `JavaCompiler` returns `0` if the compilation is successful, and a non-zero value otherwise.

For more complex scenarios, such as compiling a class that depends on external JAR libraries, you might need to set up a custom `JavaFileManager` to handle the classpath. This involves creating a `JavaCompiler` instance with a custom `JavaFileManager` that includes the necessary classpath entries.

Additionally, if you're interested in using a library like jOOR for runtime compilation, it simplifies the process by providing a higher-level API for compiling and instantiating classes from strings. However, it's important to note that jOOR doesn't officially support Android, as it relies on parts of the JDK API not available on Android [2][3].

For Android development, you would need to look into alternative approaches or libraries that are compatible with the Android runtime environment.

Citations:
[1] https://stackoverflow.com/questions/2946338/how-do-i-programmatically-compile-and-instantiate-a-java-class
[2] https://blog.jooq.org/how-to-compile-a-class-at-runtime-with-java-8-and-9/
[3] https://blog.frankel.ch/compilation-java-code-on-the-fly/
[4] https://www.infoq.com/news/2023/01/sourcebuddy/
[5] https://docs.oracle.com/javase/8/docs/api/java/lang/Runtime.html
[6] https://www.baeldung.com/java-string-compile-execute-code
[7] https://34codefactory.medium.com/java-how-to-dynamically-compile-and-load-external-java-classes-code-factory-dd517eec9b3
[8] https://www.workfall.com/learning/blog/how-to-analyze-java-class-at-runtime-using-java-reflection-api/
[9] https://www.digitalocean.com/community/tutorials/compile-run-java-program-another-java-program
---

# ejemplo 0


Para leer anotaciones a nivel de atributos de campo en Java, puedes utilizar la API de reflexión de Java junto con la API de anotaciones. A continuación, te muestro un ejemplo paso a paso de cómo hacerlo:

1. **Importa las clases necesarias**: Necesitarás importar `java.lang.reflect.Field` para trabajar con los campos de una clase y `java.lang.annotation.Annotation` para trabajar con anotaciones.

2. **Obtén la clase objetivo**: Utiliza `Class.forName()` para obtener la clase objetivo de la cual deseas leer las anotaciones de sus campos.

3. **Obtén los campos de la clase**: Utiliza el método `getDeclaredFields()` de la clase para obtener un arreglo de objetos `Field`, cada uno representando un campo de la clase.

4. **Itera sobre los campos y lee las anotaciones**: Para cada campo, utiliza el método `getAnnotation(Class<T> annotationClass)` para obtener la anotación específica que deseas leer. Si el campo tiene la anotación, este método devolverá un objeto de la anotación; de lo contrario, devolverá `null`.

Aquí tienes un ejemplo de cómo implementar estos pasos:

```java
import java.lang.reflect.Field;
import java.lang.annotation.Annotation;

public class AnnotationReader {

    public static void main(String[] args) {
        try {
            // Obtén la clase objetivo
            Class<?> clazz = Class.forName("com.example.TopTalentData");
            
            // Obtén los campos de la clase
            Field[] fields = clazz.getDeclaredFields();
            
            // Itera sobre los campos
            for (Field field : fields) {
                // Intenta obtener la anotación @MyAnnotation
                MyAnnotation myAnnotation = field.getAnnotation(MyAnnotation.class);
                
                // Si la anotación está presente, imprime su valor
                if (myAnnotation != null) {
                    System.out.println("Campo: " + field.getName() + ", Valor de @MyAnnotation: " + myAnnotation.value());
                }
                
                // Intenta obtener la anotación @NotNull
                NotNull notNull = field.getAnnotation(NotNull.class);
                
                // Si la anotación está presente, imprime un mensaje
                if (notNull != null) {
                    System.out.println("Campo: " + field.getName() + " tiene la anotación @NotNull");
                }
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
```

Este ejemplo asume que tienes una clase `TopTalentData` con campos anotados como se muestra en el ejemplo de la fuente [4]. Recuerda que necesitarás importar las clases de anotación (`MyAnnotation`, `NotNull`, etc.) en tu código para que este ejemplo funcione correctamente.

Citations:
[1] http://static1.1.sqspcdn.com/static/f/923743/27781161/1513615088287/Anotaciones+Java+-+Manual+de+Uso+Avanzado+-+v1.1.pdf?token=fmqXBvlU3NKSxvsBRlw0jO2313c%3D
[2] https://docs.jboss.org/hibernate/orm/3.5/reference/es-ES/html/mapping.html
[3] https://www.etsisi.upm.es/sites/default/files/curso_2013_14/MASTER/MIW.JEE.POOJ.pdf
[4] https://www.toptal.com/spring/los-10-errores-mas-comunes-de-spring-framework
[5] https://docs.jboss.org/hibernate/core/3.5/reference/es-ES/html_single/
[6] https://docs.aws.amazon.com/es_es/amazondynamodb/latest/developerguide/GettingStarted.UpdateItem.html
[7] https://aulavirtual.um.es/access/content/group/3871_G_2011_N_N/Teoria/T5B%20-%20JPA.pdf
[8] https://core.ac.uk/download/pdf/76486473.pdf
[9] https://learn.microsoft.com/es-es/dotnet/api/documentformat.openxml.spreadsheet.datafield?view=openxml-3.0.1


---
# cargar archivo desde path
Para cargar una clase desde un archivo en un directorio arbitrario, no puedes usar directamente `Class.forName()` con una ruta de archivo, ya que `Class.forName()` espera un nombre de clase completamente calificado (Fully Qualified Class Name, FQCN) como argumento, no una ruta de archivo [1]. Sin embargo, puedes lograr esto utilizando un `ClassLoader` personalizado que pueda cargar clases desde un directorio específico.

Aquí te muestro cómo puedes hacerlo:

1. **Crear un `ClassLoader` personalizado**: Necesitas crear una subclase de `ClassLoader` que pueda cargar clases desde un directorio específico.

2. **Definir el método `findClass`**: En tu `ClassLoader` personalizado, sobrescribe el método `findClass` para que lea el archivo de clase desde el directorio especificado y lo cargue.

3. **Usar el `ClassLoader` personalizado para cargar la clase**: Utiliza el método `loadClass` de tu `ClassLoader` personalizado para cargar la clase por su nombre.

Aquí tienes un ejemplo de cómo implementar un `ClassLoader` personalizado:

```java
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Paths;

public class CustomClassLoader extends ClassLoader {

    private String directory;

    public CustomClassLoader(String directory) {
        this.directory = directory;
    }

    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {
        try {
            String fileName = name.replace('.', File.separatorChar) + ".class";
            File file = Paths.get(directory, fileName).toFile();
            FileInputStream fis = new FileInputStream(file);
            byte[] classData = new byte[(int) file.length()];
            fis.read(classData);
            fis.close();
            return defineClass(name, classData, 0, classData.length);
        } catch (IOException e) {
            throw new ClassNotFoundException("No se pudo cargar la clase " + name, e);
        }
    }
}
```

Para usar este `ClassLoader` personalizado, puedes hacer lo siguiente:

```java
public class Main {
    public static void main(String[] args) {
        CustomClassLoader customClassLoader = new CustomClassLoader("D:\\miDirectorio");
        try {
            Class<?> clazz = customClassLoader.loadClass("com.miPaquete.MiClase");
            System.out.println("Clase cargada: " + clazz.getName());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
```

Este ejemplo asume que tienes una clase `com.miPaquete.MiClase` en el directorio `D:\\miDirectorio`. Recuerda que necesitas ajustar la ruta del directorio y el nombre de la clase según tu caso de uso.

Este enfoque te permite cargar clases desde cualquier directorio arbitrario, pero ten en cuenta que este método no es el estándar para cargar clases en Java y puede tener implicaciones en términos de seguridad y mantenimiento del código.

Citations:
[1] https://stackoverflow.com/questions/26966889/class-load-from-absolute-path
[2] https://media.techtarget.com/tss/static/articles/content/dm_classForname/DynLoad.pdf
[3] https://coderanch.com/t/383010/java/dynamically-create-file-load-Class
[4] https://access.redhat.com/documentation/en-us/red_hat_jboss_enterprise_application_platform/6.4/html/development_guide/sect-use_the_class_loader_programmatically_in_a_deployment
[5] https://www.baeldung.com/java-classloaders
[6] https://www.geeksforgeeks.org/class-forname-method-in-java-with-examples/
[7] http://web.deu.edu.tr/doc/oreily/java/langref/ch10_05.htm
[8] https://beanshell.github.io/javadoc/bsh/classpath/ClassManagerImpl.html
[9] https://www.reddit.com/r/javahelp/comments/yjhenm/eli5_why_do_we_need/
[10] https://forums.codeguru.com/showthread.php?500414-problem-with-the-path-with-Class-forname()-method


---
# ejemplo 1
Para leer anotaciones a nivel de atributos (fields) en Java, puedes utilizar la API de Reflection. A continuación, te muestro cómo hacerlo paso a paso, basándome en los ejemplos proporcionados en las fuentes:

1. **Definir la anotación**: Primero, necesitas definir la anotación que deseas aplicar a los atributos de tus clases. Por ejemplo, una anotación `StringValid` que valida la longitud de un string:

```java
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.annotation.ElementType;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface StringValid {
    int minLength() default 0;
    int maxLength() default Integer.MAX_VALUE;
}
```

2. **Aplicar la anotación a los atributos**: Luego, aplica esta anotación a los atributos de tus clases. Por ejemplo, en una clase `User`:

```java
public class User {
    private int id;

    @StringValid(minLength = 1, maxLength = 120)
    private String name;

    @StringValid(minLength = 6, maxLength = 20)
    private String password;

    // Getters y setters...
}
```

3. **Leer las anotaciones en tiempo de ejecución**: Para leer las anotaciones aplicadas a los atributos de una clase, puedes utilizar la API de Reflection. Aquí hay un ejemplo de cómo hacerlo:

```java
public class AnnotationReader {
    public static void main(String[] args) {
        User user = new User();
        // Suponiendo que tienes un método para obtener el nombre del campo
        String fieldName = "name";
        try {
            Field field = User.class.getDeclaredField(fieldName);
            field.setAccessible(true); // Permite acceder a campos privados
            StringValid annotation = field.getAnnotation(StringValid.class);
            if (annotation != null) {
                System.out.println("MinLength: " + annotation.minLength());
                System.out.println("MaxLength: " + annotation.maxLength());
            }
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
    }
}
```

Este código busca la anotación `StringValid` en el atributo `name` de la clase `User` y luego imprime los valores de `minLength` y `maxLength` definidos en la anotación.

Recuerda que para acceder a campos privados (o protegidos) de una clase, necesitas llamar a `field.setAccessible(true)` antes de obtener la anotación. Esto es necesario porque la API de Reflection no permite acceder directamente a campos privados por defecto.

Este enfoque te permite leer y procesar anotaciones aplicadas a atributos de clases en tiempo de ejecución, lo cual es útil para validaciones, configuración dinámica, entre otros usos [4][5].

Citations:
[1] https://rodriguezizquierdo.com/guia-completa-para-implementar-validaciones-en-java-con-anotaciones-y-grupos-de-restricciones/
[2] https://es.stackoverflow.com/questions/79397/cu%C3%A1l-es-la-funci%C3%B3n-de-las-anotaciones-en-java
[3] https://jarroba.com/annotations-anotaciones-en-java/
[4] https://refactorizando.com/crear-anotaciones-java/
[5] https://gustavopeiretti.com/anotaciones-en-java/
[6] https://barcelonageeks.com/personalizar-la-anotacion-de-java-con-ejemplos/
[7] https://www.aluracursos.com/blog/crear-anotaciones-en-java
[8] https://codegym.cc/es/quests/lectures/es.cgu.module2.lecture39
[9] https://medium.com/el-acordeon-del-programador/acceder-y-crear-anotaciones-en-java-69249f3d6de
[10] https://delawen.com/es/2012/04/anotaciones-en-java/
[11] https://www.devzv.com/es/annotations-in-java.html
[12] https://www.arquitecturajava.com/el-concepto-de-java-annotations/
[13] https://www.openxava.org/OpenXavaDoc/docs/jpa_es.html
[14] https://www.tokioschool.com/noticias/anotaciones-en-java/
[15] https://www.adictosaltrabajo.com/2019/02/07/crear-anotacion-con-validacion-basada-en-jsr-380-y-serializacion-personalizada/

---
# otro ejemplo 
Para leer anotaciones a nivel de atributos de campo en Java, puedes utilizar la API de reflexión de Java junto con la API de anotaciones. A continuación, te muestro cómo hacerlo paso a paso:

1. **Importa las clases necesarias**: Necesitarás importar `java.lang.reflect.Field` para trabajar con campos de clase y `java.lang.annotation.Annotation` para trabajar con anotaciones.

2. **Obtén el campo**: Utiliza el método `getField(String name)` de la clase `Class` para obtener el objeto `Field` que representa el campo de interés.

3. **Lee las anotaciones**: Utiliza el método `getAnnotations()` del objeto `Field` para obtener un array de objetos `Annotation`. Luego, puedes iterar sobre este array para acceder a las anotaciones específicas que estás buscando.

Aquí tienes un ejemplo de cómo leer anotaciones en un campo específico:

```java
import java.lang.reflect.Field;
import java.lang.annotation.Annotation;

public class AnnotationReader {

    public static void main(String[] args) {
        try {
            // Obtén el objeto Class de la clase que contiene el campo anotado
            Class<?> clazz = MyClass.class;
            
            // Obtén el objeto Field del campo anotado
            Field field = clazz.getField("myField");
            
            // Obtiene todas las anotaciones del campo
            Annotation[] annotations = field.getAnnotations();
            
            // Itera sobre las anotaciones y procesa cada una
            for (Annotation annotation : annotations) {
                System.out.println("Anotación encontrada: " + annotation.annotationType().getName());
                // Aquí puedes procesar la anotación según sea necesario
            }
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
    }
}

class MyClass {
    @MyAnnotation
    private String myField;
}

@interface MyAnnotation {
    // Definición de la anotación
}
```

En este ejemplo, `MyClass` es la clase que contiene el campo `myField` anotado con `@MyAnnotation`. El código lee y muestra el nombre de todas las anotaciones aplicadas a `myField`. Puedes adaptar este código para procesar las anotaciones de la manera que necesites, por ejemplo, para obtener valores específicos de las anotaciones o realizar acciones basadas en las anotaciones encontradas.

Citations:
[1] https://es.stackoverflow.com/questions/79397/cu%C3%A1l-es-la-funci%C3%B3n-de-las-anotaciones-en-java
[2] https://jarroba.com/annotations-anotaciones-en-java/
[3] https://rodriguezizquierdo.com/guia-completa-para-implementar-validaciones-en-java-con-anotaciones-y-grupos-de-restricciones/
[4] https://refactorizando.com/crear-anotaciones-java/
[5] https://stackoverflow.com/questions/23162777/how-do-i-mock-an-autowired-value-field-in-spring-with-mockito
[6] https://gustavopeiretti.com/anotaciones-en-java/
[7] https://www.aluracursos.com/blog/crear-anotaciones-en-java
[8] https://codegym.cc/es/quests/lectures/es.cgu.module2.lecture39
[9] https://medium.com/el-acordeon-del-programador/acceder-y-crear-anotaciones-en-java-69249f3d6de
[10] https://delawen.com/es/2012/04/anotaciones-en-java/
[11] https://barcelonageeks.com/personalizar-la-anotacion-de-java-con-ejemplos/
[12] https://www.devzv.com/es/annotations-in-java.html
[13] https://www.openxava.org/OpenXavaDoc/docs/jpa_es.html
[14] https://www.tokioschool.com/noticias/anotaciones-en-java/
[15] https://stackoverflow.com/questions/2154622/why-does-jpa-have-a-transient-annotation

---
To read Java annotations from a Java file source on disk, you can use the JavaParser library, as demonstrated in Source 1. This library allows you to parse Java source files and extract various information, including annotations. Here's a step-by-step guide based on the provided code example:

1. **Add JavaParser Dependency**: First, ensure you have JavaParser added to your project. If you're using Maven, add the following dependency to your `pom.xml`:

```xml
<dependency>
    <groupId>com.github.javaparser</groupId>
    <artifactId>javaparser-core</artifactId>
    <version>3.23.0</version> <!-- Use the latest version -->
</dependency>
```

2. **Parse the Java File**: Use JavaParser to parse the Java source file. This involves creating a `FileInputStream` for the file and then parsing it into a `CompilationUnit` object.

3. **Visit Nodes**: Implement a visitor to visit the nodes of interest in the parsed `CompilationUnit`. In the provided example, a `MethodVisitor` is used to visit `MethodDeclaration` nodes and print out the method names and their annotations.

Here's a simplified version of the code that focuses on reading annotations from a Java file:

```java
import com.github.javaparser.JavaParser;
import com.github.javaparser.ParseException;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.expr.AnnotationExpr;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class JavaSourceUtils {

    public static void main(String[] args) throws Exception {
        File file = new File("/path/to/your/JavaFile.java");
        inspectJavaFile(file);
    }

    public static void inspectJavaFile(File file) throws IOException, ParseException {
        CompilationUnit cu;
        try (FileInputStream in = new FileInputStream(file)) {
            cu = JavaParser.parse(in);
        }
        cu.findAll(ClassOrInterfaceDeclaration.class).forEach(classOrInterface -> {
            classOrInterface.getAnnotations().forEach(annotation -> {
                System.out.println("Found annotation: " + annotation.getName());
                // You can further process the annotation here
            });
        });
    }
}
```

This code snippet demonstrates how to read and print the names of all annotations found in the class or interface declarations of a given Java source file. You can modify the visitor logic to suit your specific needs, such as extracting specific annotation values or processing other types of nodes.

Remember, JavaParser is a powerful tool for analyzing and manipulating Java source code. It supports a wide range of Java features and can be extended to handle more complex scenarios as needed.

Citations:
[1] https://stackoverflow.com/questions/39772189/how-to-read-file-from-src-main-resources-with-annotation-processor
[2] https://stackoverflow.com/questions/5410193/get-class-annotations-from-java-source-file
[3] https://stackoverflow.com/questions/4296910/is-it-possible-to-read-the-value-of-a-annotation-in-java
[4] https://medium.com/javarevisited/reading-annotations-at-compile-time-using-java-annotation-processors-f5a7604bb2f7
[5] https://www.baeldung.com/java-asm
[6] https://www.baeldung.com/reading-file-in-java
[7] https://mkyong.com/java/java-read-a-file-from-resources-folder/
[8] https://howtodoinjava.com/java/io/read-file-from-resources-folder/
[9] https://docs.oracle.com/javase/8/docs/api/javax/annotation/processing/Filer.html
[10] https://stackabuse.com/reading-and-writing-files-in-java/
[11] https://towardsdev.com/exploring-java-i-o-reading-and-writing-files-for-beginners-f43f7cd673f2
[12] https://medium.com/@AlexanderObregon/java-annotation-processors-enhancing-code-at-compile-time-633b40e63521
[13] https://docs.oracle.com/javase/tutorial/java/package/managingfiles.html
[14] https://dev.java/learn/java-io/reading-writing/small-files/
[15] https://marketsplash.com/tutorials/java/how-to-read-a-file-in-java/