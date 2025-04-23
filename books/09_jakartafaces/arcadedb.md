# Ejemplo de Java con ArcadeDB: Personas y Perfiles Relacionados

Aquí tienes un ejemplo completo de cómo usar ArcadeDB en Java con dos tipos de registros (Persona y Perfil) relacionados entre sí.

## Configuración inicial

Primero, asegúrate de tener la dependencia de ArcadeDB en tu proyecto (Maven):

```xml
<dependency>
  <groupId>com.arcadedb</groupId>
  <artifactId>arcadedb-engine</artifactId>
  <version>23.10.1</version> <!-- Usa la versión más reciente -->
</dependency>
```

## Código Java completo

```java
import com.arcadedb.database.Database;
import com.arcadedb.database.DatabaseFactory;
import com.arcadedb.database.Record;
import com.arcadedb.schema.DocumentType;
import com.arcadedb.schema.Schema;
import com.arcadedb.schema.Type;

import java.util.Date;

public class ArcadeDBPersonaPerfilExample {

    public static void main(String[] args) {
        // Ruta donde se almacenará la base de datos
        final String dbPath = "./databases/personasPerfiles";

        try {
            // Crear o abrir la base de datos
            try (DatabaseFactory factory = new DatabaseFactory(dbPath)) {
                if (!factory.exists())
                    factory.create();
                
                try (Database database = factory.open()) {
                    // Definir el esquema si no existe
                    definirEsquema(database);

                    // Insertar datos de ejemplo
                    insertarDatosEjemplo(database);

                    // Consultar los datos
                    consultarDatos(database);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void definirEsquema(Database database) {
        database.transaction(() -> {
            Schema schema = database.getSchema();

            // Crear tipo Persona si no existe
            DocumentType personaType = schema.getType("Persona");
            if (personaType == null) {
                personaType = schema.createDocumentType("Persona");
                personaType.createProperty("nombre", Type.STRING);
                personaType.createProperty("apellido", Type.STRING);
                personaType.createProperty("fechaNacimiento", Type.DATE);
                personaType.createProperty("email", Type.STRING);
            }

            // Crear tipo Perfil si no existe
            DocumentType perfilType = schema.getType("Perfil");
            if (perfilType == null) {
                perfilType = schema.createDocumentType("Perfil");
                perfilType.createProperty("usuario", Type.STRING);
                perfilType.createProperty("contrasena", Type.STRING);
                perfilType.createProperty("fechaCreacion", Type.DATE);
                perfilType.createProperty("activo", Type.BOOLEAN);
            }

            // Crear relación entre Persona y Perfil
            if (schema.getType("TienePerfil") == null) {
                schema.createEdgeType("TienePerfil");
            }
        });
    }

    private static void insertarDatosEjemplo(Database database) {
        database.transaction(() -> {
            // Insertar personas
            Record juan = database.newDocument("Persona")
                    .set("nombre", "Juan")
                    .set("apellido", "Pérez")
                    .set("fechaNacimiento", new Date(90, 0, 15)) // 15-Ene-1990
                    .set("email", "juan.perez@example.com")
                    .save();

            Record maria = database.newDocument("Persona")
                    .set("nombre", "María")
                    .set("apellido", "Gómez")
                    .set("fechaNacimiento", new Date(85, 5, 22)) // 22-Jun-1985
                    .set("email", "maria.gomez@example.com")
                    .save();

            // Insertar perfiles
            Record perfilJuan = database.newDocument("Perfil")
                    .set("usuario", "jperez")
                    .set("contrasena", "abc123")
                    .set("fechaCreacion", new Date())
                    .set("activo", true)
                    .save();

            Record perfilMaria = database.newDocument("Perfil")
                    .set("usuario", "mgomez")
                    .set("contrasena", "def456")
                    .set("fechaCreacion", new Date())
                    .set("activo", true)
                    .save();

            // Crear relaciones entre personas y perfiles
            database.newEdge("TienePerfil", juan, perfilJuan).save();
            database.newEdge("TienePerfil", maria, perfilMaria).save();
        });
    }

    private static void consultarDatos(Database database) {
        database.transaction(() -> {
            System.out.println("=== Listado de Personas con sus Perfiles ===");

            // Consultar todas las personas
            Iterable<Record> personas = database.iterateType("Persona", true);
            
            for (Record persona : personas) {
                System.out.println("\nPersona: " + persona.getString("nombre") + " " + 
                        persona.getString("apellido"));
                System.out.println("Email: " + persona.getString("email"));
                
                // Obtener el perfil relacionado
                Iterable<Record> perfiles = database.query("SQL", 
                        "SELECT expand(out('TienePerfil')) FROM " + persona.getIdentity()).elementIterator();
                
                for (Record perfil : perfiles) {
                    System.out.println("Perfil:");
                    System.out.println("  Usuario: " + perfil.getString("usuario"));
                    System.out.println("  Fecha creación: " + perfil.getDate("fechaCreacion"));
                    System.out.println("  Activo: " + perfil.getBoolean("activo"));
                }
            }
        });
    }
}
```

## Explicación del código

1. **Configuración inicial**: Se crea o abre una base de datos ArcadeDB en el directorio especificado.

2. **Definición del esquema**:
   - Se crean dos tipos de documentos: `Persona` y `Perfil`
   - Cada tipo tiene sus propiedades específicas
   - Se crea una relación de arista llamada `TienePerfil` que conecta Personas con Perfiles

3. **Inserción de datos**:
   - Se crean dos personas (Juan y María)
   - Se crean dos perfiles asociados a esas personas
   - Se establecen las relaciones usando aristas `TienePerfil`

4. **Consulta de datos**:
   - Se listan todas las personas
   - Para cada persona, se consulta su perfil relacionado usando una consulta SQL de ArcadeDB
   - Se muestra la información combinada de personas y perfiles

## Características destacadas

- Uso de transacciones para todas las operaciones
- Tipos de documentos fuertemente tipados
- Relaciones mediante aristas (edges)
- Consultas SQL embebidas para navegar por las relaciones

Este ejemplo muestra una relación uno-a-uno entre Persona y Perfil, pero ArcadeDB también soporta relaciones uno-a-muchos o muchos-a-muchos fácilmente.

___

¡Entendido! Aquí tienes un ejemplo completo de operaciones CRUD (Crear, Leer, Actualizar, Eliminar) en Java utilizando ArcadeDB para las entidades Persona y Perfil que vimos antes. Este ejemplo incluirá métodos para cada una de las operaciones básicas.
import com.arcadedb.database.Database;
import com.arcadedb.database.DatabaseFactory;
import com.arcadedb.graph.MutableVertex;
import com.arcadedb.graph.Vertex;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ArcadeDBPersonaPerfilCRUD {

    private final String DATABASE_NAME = "mi_basedatos_crud";

    // Método para obtener una instancia de la base de datos
    private Database getDatabase() {
        return new DatabaseFactory(DATABASE_NAME).openOrCreate();
    }

    // Método para crear los tipos de vértice si no existen
    private void crearEsquema() {
        Database db = getDatabase();
        try {
            if (!db.getSchema().existsType("Persona")) {
                db.getSchema().createVertexType("Persona");
            }
            if (!db.getSchema().existsType("Perfil")) {
                db.getSchema().createVertexType("Perfil");
            }
        } finally {
            db.close();
        }
    }

    // Crear una nueva Persona con su Perfil asociado
    public Vertex crearPersonaConPerfil(String nombre, int edad, String[] intereses, String nivelExperiencia) {
        Database db = getDatabase();
        MutableVertex personaCreada = null;
        try {
            MutableVertex persona = db.newVertex("Persona");
            persona.set("nombre", nombre);
            persona.set("edad", edad);
            personaCreada = persona.save();

            MutableVertex perfil = db.newVertex("Perfil");
            perfil.set("intereses", intereses);
            perfil.set("nivel_experiencia", nivelExperiencia);
            Vertex perfilCreado = perfil.save();

            personaCreada.newEdge("TienePerfil", perfilCreado).save();

            return personaCreada;
        } finally {
            db.close();
        }
    }

    // Leer una Persona por su RID (Record ID)
    public Optional<Vertex> leerPersona(String rid) {
        Database db = getDatabase();
        try {
            return Optional.ofNullable(db.lookupByRID(rid));
        } finally {
            db.close();
        }
    }

    // Leer todas las Personas
    public List<Vertex> leerTodasLasPersonas() {
        Database db = getDatabase();
        List<Vertex> personas = new ArrayList<>();
        try {
            db.query("SELECT FROM Persona").forEach(result -> personas.add(result.toVertex()));
            return personas;
        } finally {
            db.close();
        }
    }

    // Actualizar la información de una Persona
    public Optional<Vertex> actualizarPersona(String rid, String nuevoNombre, Integer nuevaEdad) {
        Database db = getDatabase();
        try {
            Optional<Vertex> personaOptional = Optional.ofNullable(db.lookupByRID(rid));
            personaOptional.ifPresent(persona -> {
                if (nuevoNombre != null) {
                    persona.set("nombre", nuevoNombre);
                }
                if (nuevaEdad != null) {
                    persona.set("edad", nuevaEdad);
                }
                persona.save();
            });
            return personaOptional;
        } finally {
            db.close();
        }
    }

    // Actualizar el Perfil asociado a una Persona
    public boolean actualizarPerfilDePersona(String personaRid, String[] nuevosIntereses, String nuevoNivelExperiencia) {
        Database db = getDatabase();
        try {
            Optional<Vertex> personaOptional = Optional.ofNullable(db.lookupByRID(personaRid));
            if (personaOptional.isPresent()) {
                Vertex persona = personaOptional.get();
                persona.getEdges("TienePerfil", com.arcadedb.query.sql.executor.Result.DIRECTION.OUT).forEach(edge -> {
                    Vertex perfil = edge.getTo();
                    if (nuevosIntereses != null) {
                        perfil.set("intereses", nuevosIntereses);
                    }
                    if (nuevoNivelExperiencia != null) {
                        perfil.set("nivel_experiencia", nuevoNivelExperiencia);
                    }
                    perfil.save();
                });
                return true;
            }
            return false;
        } finally {
            db.close();
        }
    }

    // Eliminar una Persona por su RID (esto también eliminará el Perfil asociado debido a la relación)
    public boolean eliminarPersona(String rid) {
        Database db = getDatabase();
        try {
            Optional<Vertex> personaOptional = Optional.ofNullable(db.lookupByRID(rid));
            if (personaOptional.isPresent()) {
                Vertex persona = personaOptional.get();
                // Eliminar el borde hacia el Perfil
                persona.getEdges("TienePerfil", com.arcadedb.query.sql.executor.Result.DIRECTION.OUT).forEach(edge -> edge.delete());
                // Eliminar el vértice de la Persona
                persona.delete();
                return true;
            }
            return false;
        } finally {
            db.close();
        }
    }

    public static void main(String[] args) {
        ArcadeDBPersonaPerfilCRUD crud = new ArcadeDBPersonaPerfilCRUD();
        crud.crearEsquema();

        // Crear
        Vertex alice = crud.crearPersonaConPerfil("Alice", 30, new String[]{"Programación", "Viajes"}, "Avanzado");
        System.out.println("Persona creada con RID: " + alice.getIdentity());

        // Leer por RID
        Optional<Vertex> personaLeida = crud.leerPersona(alice.getIdentity().toString());
        personaLeida.ifPresent(p -> System.out.println("Persona leída: Nombre=" + p.getProperty("nombre") + ", Edad=" + p.getProperty("edad")));

        // Leer todas
        List<Vertex> todasLasPersonas = crud.leerTodasLasPersonas();
        System.out.println("\nTodas las personas:");
        todasLasPersonas.forEach(p -> System.out.println("RID=" + p.getIdentity() + ", Nombre=" + p.getProperty("nombre")));

        // Actualizar Persona
        crud.actualizarPersona(alice.getIdentity().toString(), "Alicia", 31);
        Optional<Vertex> personaActualizada = crud.leerPersona(alice.getIdentity().toString());
        personaActualizada.ifPresent(p -> System.out.println("\nPersona actualizada: Nombre=" + p.getProperty("nombre") + ", Edad=" + p.getProperty("edad")));

        // Actualizar Perfil de Persona
        crud.actualizarPerfilDePersona(alice.getIdentity().toString(), new String[]{"Programación", "Música"}, "Intermedio");
        Optional<Vertex> personaConPerfilActualizado = crud.leerPersona(alice.getIdentity().toString());
        personaConPerfilActualizado.ifPresent(p -> {
            System.out.println("\nPersona con perfil actualizado:");
            p.getEdges("TienePerfil", com.arcadedb.query.sql.executor.Result.DIRECTION.OUT).forEach(edge -> {
                Vertex perfil = edge.getTo();
                System.out.println("  Intereses=" + perfil.<String[]>getProperty("intereses") + ", Nivel=" + perfil.getProperty("nivel_experiencia"));
            });
        });

        // Eliminar
        boolean eliminada = crud.eliminarPersona(alice.getIdentity().toString());
        System.out.println("\nPersona eliminada: " + eliminada);

        // Verificar que se eliminó
        Optional<Vertex> personaVerificada = crud.leerPersona(alice.getIdentity().toString());
        System.out.println("Persona después de eliminar (debería ser vacío): " + personaVerificada);
    }
}

Explicación del Código:
 * DATABASE_NAME: Una constante para el nombre de la base de datos.
 * getDatabase(): Un método privado para obtener una instancia de la base de datos, abriéndola o creándola si no existe.
 * crearEsquema(): Un método para crear los tipos de vértice Persona y Perfil si aún no existen en la base de datos.
 * crearPersonaConPerfil(String nombre, int edad, String[] intereses, String nivelExperiencia):
   * Crea un nuevo vértice Persona con los datos proporcionados.
   * Crea un nuevo vértice Perfil con los intereses y el nivel de experiencia.
   * Crea un borde "TienePerfil" que conecta la Persona con el Perfil.
   * Devuelve el vértice Persona creado.
 * leerPersona(String rid):
   * Toma el RID (Record ID, identificador único de un registro en ArcadeDB) de una Persona.
   * Utiliza db.lookupByRID(rid) para buscar el vértice por su ID.
   * Devuelve un Optional<Vertex> que contendrá la Persona si se encuentra, o estará vacío si no.
 * leerTodasLasPersonas():
   * Ejecuta una consulta simple "SELECT FROM Persona" para obtener todos los vértices del tipo Persona.
   * Itera sobre los resultados y los convierte a objetos Vertex, agregándolos a una lista.
   * Devuelve la lista de todas las Personas.
 * actualizarPersona(String rid, String nuevoNombre, Integer nuevaEdad):
   * Busca la Persona por su RID.
   * Si la Persona existe, actualiza las propiedades nombre y/o edad si se proporcionan nuevos valores.
   * Guarda los cambios en la base de datos.
   * Devuelve un Optional<Vertex> con la Persona actualizada.
 * actualizarPerfilDePersona(String personaRid, String[] nuevosIntereses, String nuevoNivelExperiencia):
   * Busca la Persona por su RID.
   * Si la Persona existe, obtiene el borde "TienePerfil" saliente para acceder al Perfil asociado.
   * Actualiza las propiedades intereses y/o nivel_experiencia del Perfil si se proporcionan nuevos valores.
   * Guarda los cambios en el Perfil.
   * Devuelve true si la actualización fue exitosa, false en caso contrario.
 * eliminarPersona(String rid):
   * Busca la Persona por su RID.
   * Si la Persona existe:
     * Elimina todos los bordes "TienePerfil" que salen de ella (esto desvincula el Perfil).
     * Elimina el propio vértice de la Persona.
   * Devuelve true si la eliminación fue exitosa, false en caso contrario. (Nota: En este ejemplo, no eliminamos explícitamente el Perfil huérfano. Podrías añadir lógica para buscar y eliminar perfiles sin bordes entrantes si fuera necesario un borrado en cascada más estricto).
 * main(String[] args):
   * Crea una instancia de ArcadeDBPersonaPerfilCRUD.
   * Llama a crearEsquema() para asegurarse de que los tipos de vértice existan.
   * Realiza una secuencia de operaciones CRUD: crear, leer por RID, leer todas, actualizar (persona y perfil), y eliminar.
   * Imprime información en la consola para mostrar los resultados de cada operación.
Para ejecutar este código:
 * Asegúrate de tener la dependencia de ArcadeDB en tu proyecto (como se mencionó en el ejemplo anterior).
 * Guarda el código Java en un archivo llamado ArcadeDBPersonaPerfilCRUD.java.
 * Compila y ejecuta el código.
Este ejemplo proporciona una base sólida para realizar operaciones CRUD en ArcadeDB con entidades relacionadas. Puedes adaptarlo y extenderlo según las necesidades específicas de tu aplicación.

___

A continuación, te muestro un ejemplo de cómo trabajar con **ArcadeDB** en Java utilizando dos tablas relacionadas: `Persona` y `Perfil`. En este caso, asumimos que cada `Persona` tiene un perfil asociado mediante una relación uno a uno.

---

### **Ejemplo de Código Java con ArcadeDB**

#### **1. Dependencias necesarias**
Antes de comenzar, asegúrate de incluir las dependencias de ArcadeDB en tu archivo `pom.xml` si estás utilizando Maven:

```xml
<dependencies>
    <dependency>
        <groupId>com.arcadedb</groupId>
        <artifactId>arcadedb-client</artifactId>
        <version>23.9.0</version> <!-- Usa la versión más reciente -->
    </dependency>
</dependencies>
```

#### **2. Código Java**

```java
import com.arcadedb.database.Database;
import com.arcadedb.database.DatabaseFactory;
import com.arcadedb.database.MutableDocument;

public class ArcadeDBExample {

    public static void main(String[] args) {
        // Ruta de la base de datos
        String databasePath = "data/mydatabase";

        // Crear o abrir la base de datos
        try (Database database = new DatabaseFactory(databasePath).create()) {
            // Crear esquemas para las tablas Persona y Perfil
            if (!database.getSchema().existsType("Persona")) {
                database.getSchema().createDocumentType("Persona");
                database.getSchema().createProperty("Persona.nombre", String.class);
                database.getSchema().createProperty("Persona.edad", Integer.class);
                database.getSchema().createProperty("Persona.perfil", "Perfil"); // Relación con Perfil
            }

            if (!database.getSchema().existsType("Perfil")) {
                database.getSchema().createDocumentType("Perfil");
                database.getSchema().createProperty("Perfil.descripcion", String.class);
                database.getSchema().createProperty("Perfil.ocupacion", String.class);
            }

            // Iniciar transacción
            database.begin();

            try {
                // Crear un nuevo perfil
                MutableDocument perfil = database.newDocument("Perfil");
                perfil.set("descripcion", "Entusiasta de la tecnología");
                perfil.set("ocupacion", "Ingeniero de software");
                perfil.save(); // Guardar el perfil

                // Crear una nueva persona
                MutableDocument persona = database.newDocument("Persona");
                persona.set("nombre", "Juan Pérez");
                persona.set("edad", 30);
                persona.set("perfil", perfil); // Asociar el perfil a la persona
                persona.save(); // Guardar la persona

                // Confirmar la transacción
                database.commit();
                System.out.println("Datos insertados correctamente.");
            } catch (Exception e) {
                // Revertir la transacción en caso de error
                database.rollback();
                System.err.println("Error al insertar datos: " + e.getMessage());
            }

            // Consultar datos
            System.out.println("\nConsultando datos...");
            database.query("sql", "SELECT * FROM Persona").forEach(result -> {
                System.out.println("Nombre: " + result.getProperty("nombre"));
                System.out.println("Edad: " + result.getProperty("edad"));

                // Obtener el perfil asociado
                MutableDocument perfilAsociado = result.getProperty("perfil");
                if (perfilAsociado != null) {
                    System.out.println("Perfil - Descripción: " + perfilAsociado.get("descripcion"));
                    System.out.println("Perfil - Ocupación: " + perfilAsociado.get("ocupacion"));
                }
            });
        }
    }
}
```

---

### **Explicación del Código**

1. **Creación de la Base de Datos**:
   - Se utiliza `DatabaseFactory` para crear o abrir una base de datos en la ruta especificada (`data/mydatabase`).
   - Si no existen los tipos `Persona` y `Perfil`, se crean utilizando el método `createDocumentType`.

2. **Definición de Propiedades**:
   - Para `Persona`, se definen las propiedades `nombre`, `edad` y `perfil`.
   - Para `Perfil`, se definen las propiedades `descripcion` y `ocupacion`.
   - La propiedad `perfil` en `Persona` es una relación hacia el tipo `Perfil`.

3. **Inserción de Datos**:
   - Se crea un documento de tipo `Perfil` y se guarda.
   - Luego, se crea un documento de tipo `Persona` y se asocia el perfil previamente creado.
   - Ambos documentos se guardan dentro de una transacción para garantizar la integridad de los datos.

4. **Consulta de Datos**:
   - Se utiliza una consulta SQL para recuperar todos los registros de la tabla `Persona`.
   - Para cada registro, se accede al perfil asociado mediante la propiedad `perfil`.

---

### **Salida Esperada**

Al ejecutar el programa, deberías ver una salida similar a la siguiente:

```
Datos insertados correctamente.

Consultando datos...
Nombre: Juan Pérez
Edad: 30
Perfil - Descripción: Entusiasta de la tecnología
Perfil - Ocupación: Ingeniero de software
```

---

### **Notas Importantes**

1. **Relaciones**:
   - En ArcadeDB, las relaciones entre documentos se manejan mediante referencias directas. En este caso, la propiedad `perfil` en `Persona` apunta al documento de tipo `Perfil`.

2. **Transacciones**:
   - Las transacciones son importantes para garantizar que los cambios se apliquen de manera atómica. Si algo falla durante la inserción, se realiza un rollback.

3. **Escalabilidad**:
   - Este ejemplo es básico pero puede escalarse para manejar relaciones más complejas, como uno a muchos o muchos a muchos.

Si tienes dudas adicionales o necesitas un ejemplo más avanzado, no dudes en preguntar.
---
A continuación, te muestro cómo implementar operaciones CRUD (Crear, Leer, Actualizar, Eliminar) utilizando **Java Records**, **Jakarta Faces** y **ArcadeDB**. Este ejemplo incluye una interfaz gráfica simple para gestionar las tablas `Persona` y `Perfil`.

---

### **Estructura del Proyecto**

1. **Modelo**: Definimos los registros (`records`) para representar `Persona` y `Perfil`.
2. **Servicio**: Implementamos un servicio para manejar las operaciones CRUD.
3. **Controlador**: Usamos Jakarta Faces para interactuar con la interfaz de usuario.
4. **Interfaz de Usuario**: Creamos una vista básica en XHTML para realizar operaciones CRUD.

---

### **1. Modelo con Java Records**

Definimos los registros `Persona` y `Perfil`:

```java
public record Persona(String id, String nombre, int edad, Perfil perfil) {}

public record Perfil(String id, String descripcion, String ocupacion) {}
```

---

### **2. Servicio CRUD**

Creamos un servicio para manejar las operaciones CRUD con ArcadeDB:

```java
import com.arcadedb.database.Database;
import com.arcadedb.database.DatabaseFactory;

import java.util.ArrayList;
import java.util.List;

public class PersonaService {

    private static final String DATABASE_PATH = "data/mydatabase";

    public void crearPersona(Persona persona) {
        try (Database database = new DatabaseFactory(DATABASE_PATH).open()) {
            database.begin();
            var doc = database.newDocument("Persona");
            doc.set("nombre", persona.nombre());
            doc.set("edad", persona.edad());
            doc.set("perfil", database.newDocument("Perfil")
                    .set("descripcion", persona.perfil().descripcion())
                    .set("ocupacion", persona.perfil().ocupacion()));
            doc.save();
            database.commit();
        } catch (Exception e) {
            throw new RuntimeException("Error al crear persona", e);
        }
    }

    public List<Persona> obtenerPersonas() {
        List<Persona> personas = new ArrayList<>();
        try (Database database = new DatabaseFactory(DATABASE_PATH).open()) {
            database.query("sql", "SELECT * FROM Persona").forEach(result -> {
                var perfil = result.getProperty("perfil");
                personas.add(new Persona(
                        result.getIdentity().toString(),
                        result.getProperty("nombre"),
                        result.getProperty("edad"),
                        new Perfil(
                                perfil.getIdentity().toString(),
                                perfil.get("descripcion"),
                                perfil.get("ocupacion")
                        )
                ));
            });
        }
        return personas;
    }

    public void actualizarPersona(Persona persona) {
        try (Database database = new DatabaseFactory(DATABASE_PATH).open()) {
            database.begin();
            var doc = database.lookupById(persona.id());
            if (doc != null) {
                doc.set("nombre", persona.nombre());
                doc.set("edad", persona.edad());
                var perfilDoc = doc.getProperty("perfil");
                perfilDoc.set("descripcion", persona.perfil().descripcion());
                perfilDoc.set("ocupacion", persona.perfil().ocupacion());
                doc.save();
            }
            database.commit();
        } catch (Exception e) {
            throw new RuntimeException("Error al actualizar persona", e);
        }
    }

    public void eliminarPersona(String id) {
        try (Database database = new DatabaseFactory(DATABASE_PATH).open()) {
            database.begin();
            var doc = database.lookupById(id);
            if (doc != null) {
                doc.delete();
            }
            database.commit();
        } catch (Exception e) {
            throw new RuntimeException("Error al eliminar persona", e);
        }
    }
}
```

---

### **3. Controlador con Jakarta Faces**

Creamos un controlador para manejar las interacciones entre la interfaz y el servicio:

```java
import jakarta.faces.bean.ManagedBean;
import jakarta.faces.bean.ViewScoped;

import java.util.List;

@ManagedBean
@ViewScoped
public class PersonaController {

    private final PersonaService personaService = new PersonaService();

    private String nombre;
    private int edad;
    private String descripcionPerfil;
    private String ocupacionPerfil;
    private String idSeleccionado;

    public void crearPersona() {
        var perfil = new Perfil(null, descripcionPerfil, ocupacionPerfil);
        var persona = new Persona(null, nombre, edad, perfil);
        personaService.crearPersona(persona);
        limpiarCampos();
    }

    public List<Persona> obtenerPersonas() {
        return personaService.obtenerPersonas();
    }

    public void seleccionarPersona(Persona persona) {
        this.idSeleccionado = persona.id();
        this.nombre = persona.nombre();
        this.edad = persona.edad();
        this.descripcionPerfil = persona.perfil().descripcion();
        this.ocupacionPerfil = persona.perfil().ocupacion();
    }

    public void actualizarPersona() {
        var perfil = new Perfil(null, descripcionPerfil, ocupacionPerfil);
        var persona = new Persona(idSeleccionado, nombre, edad, perfil);
        personaService.actualizarPersona(persona);
        limpiarCampos();
    }

    public void eliminarPersona(String id) {
        personaService.eliminarPersona(id);
    }

    private void limpiarCampos() {
        this.nombre = "";
        this.edad = 0;
        this.descripcionPerfil = "";
        this.ocupacionPerfil = "";
        this.idSeleccionado = null;
    }

    // Getters y Setters
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public int getEdad() { return edad; }
    public void setEdad(int edad) { this.edad = edad; }

    public String getDescripcionPerfil() { return descripcionPerfil; }
    public void setDescripcionPerfil(String descripcionPerfil) { this.descripcionPerfil = descripcionPerfil; }

    public String getOcupacionPerfil() { return ocupacionPerfil; }
    public void setOcupacionPerfil(String ocupacionPerfil) { this.ocupacionPerfil = ocupacionPerfil; }
}
```

---

### **4. Interfaz de Usuario (XHTML)**

Creamos una vista básica en XHTML para interactuar con el controlador:

```xml
<!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/xhtml"
      xmlns:h="http://xmlns.jcp.org/jsf/html">
<h:head>
    <title>CRUD Persona</title>
</h:head>
<h:body>
    <h1>Gestión de Personas</h1>

    <!-- Formulario -->
    <h:form>
        <h:panelGrid columns="2">
            <h:outputLabel value="Nombre:" />
            <h:inputText value="#{personaController.nombre}" />

            <h:outputLabel value="Edad:" />
            <h:inputText value="#{personaController.edad}" />

            <h:outputLabel value="Descripción del Perfil:" />
            <h:inputText value="#{personaController.descripcionPerfil}" />

            <h:outputLabel value="Ocupación del Perfil:" />
            <h:inputText value="#{personaController.ocupacionPerfil}" />
        </h:panelGrid>

        <h:commandButton value="Crear" action="#{personaController.crearPersona}" />
        <h:commandButton value="Actualizar" action="#{personaController.actualizarPersona}" rendered="#{not empty personaController.idSeleccionado}" />
    </h:form>

    <!-- Lista de Personas -->
    <h:dataTable value="#{personaController.obtenerPersonas()}" var="persona">
        <h:column>
            <f:facet name="header">Nombre</f:facet>
            #{persona.nombre}
        </h:column>
        <h:column>
            <f:facet name="header">Edad</f:facet>
            #{persona.edad}
        </h:column>
        <h:column>
            <f:facet name="header">Perfil</f:facet>
            #{persona.perfil.descripcion} - #{persona.perfil.ocupacion}
        </h:column>
        <h:column>
            <h:commandButton value="Editar" action="#{personaController.seleccionarPersona(persona)}" />
            <h:commandButton value="Eliminar" action="#{personaController.eliminarPersona(persona.id)}" />
        </h:column>
    </h:dataTable>
</h:body>
</html>
```

---

### **Explicación**

1. **Java Records**:
   - Simplifican la definición de modelos inmutables.

2. **Jakarta Faces**:
   - Proporciona una capa de abstracción para la interfaz de usuario.

3. **Operaciones CRUD**:
   - Crear, leer, actualizar y eliminar se implementan en el servicio y se exponen a través del controlador.

4. **Interfaz de Usuario**:
   - La vista XHTML permite interactuar con el sistema de manera intuitiva.

---

Este ejemplo es modular y puede ampliarse según sea necesario. Si necesitas más detalles o ajustes, no dudes en preguntar.



A continuación, te muestro cómo extender el ejemplo anterior para incluir operaciones CRUD (Crear, Leer, Actualizar, Eliminar) utilizando **Java Records** y una interfaz basada en **Jakarta Faces**. Este enfoque combina la simplicidad de los records con la potencia de Jakarta Faces para crear una aplicación web.

---

### **1. Dependencias Necesarias**

Primero, asegúrate de que tu proyecto tenga las dependencias necesarias. Si usas Maven, añade lo siguiente a tu `pom.xml`:

```xml
<dependencies>
    <!-- ArcadeDB -->
    <dependency>
        <groupId>com.arcadedb</groupId>
        <artifactId>arcadedb-client</artifactId>
        <version>23.9.0</version>
    </dependency>

    <!-- Jakarta Faces -->
    <dependency>
        <groupId>jakarta.faces</groupId>
        <artifactId>jakarta.faces-api</artifactId>
        <version>4.0.0</version>
    </dependency>

    <!-- Jakarta Servlets -->
    <dependency>
        <groupId>jakarta.servlet</groupId>
        <artifactId>jakarta.servlet-api</artifactId>
        <version>6.0.0</version>
        <scope>provided</scope>
    </dependency>
</dependencies>
```

---

### **2. Estructura del Proyecto**

Organiza tu proyecto de la siguiente manera:

```
src/main/java/
    ├── com.example.arcadedb
    │   ├── model/
    │   │   ├── Persona.java
    │   │   └── Perfil.java
    │   ├── service/
    │   │   └── PersonaService.java
    │   └── controller/
    │       └── PersonaController.java
    └── resources/
        └── META-INF/
            └── faces-config.xml
webapp/
    └── WEB-INF/
        └── web.xml
    └── index.xhtml
```

---

### **3. Código Fuente**

#### **a. Modelos con Java Records**

Define los modelos `Persona` y `Perfil` como registros inmutables.

```java
package com.example.arcadedb.model;

public record Persona(String id, String nombre, int edad, Perfil perfil) {}

public record Perfil(String id, String descripcion, String ocupacion) {}
```

---

#### **b. Servicio CRUD**

Implementa un servicio para manejar las operaciones CRUD con ArcadeDB.

```java
package com.example.arcadedb.service;

import com.arcadedb.database.Database;
import com.arcadedb.database.DatabaseFactory;
import com.example.arcadedb.model.Persona;
import com.example.arcadedb.model.Perfil;

import java.util.ArrayList;
import java.util.List;

public class PersonaService {

    private static final String DATABASE_PATH = "data/mydatabase";

    public void crearPersona(Persona persona) {
        try (Database database = new DatabaseFactory(DATABASE_PATH).open()) {
            database.begin();
            var perfilDoc = database.newDocument("Perfil")
                    .set("descripcion", persona.perfil().descripcion())
                    .set("ocupacion", persona.perfil().ocupacion())
                    .save();

            var personaDoc = database.newDocument("Persona")
                    .set("nombre", persona.nombre())
                    .set("edad", persona.edad())
                    .set("perfil", perfilDoc)
                    .save();

            database.commit();
        }
    }

    public List<Persona> obtenerPersonas() {
        List<Persona> personas = new ArrayList<>();
        try (Database database = new DatabaseFactory(DATABASE_PATH).open()) {
            database.query("sql", "SELECT * FROM Persona").forEach(result -> {
                var perfilDoc = result.getProperty("perfil");
                var perfil = new Perfil(
                        perfilDoc.getIdentity().toString(),
                        perfilDoc.get("descripcion"),
                        perfilDoc.get("ocupacion")
                );
                personas.add(new Persona(
                        result.getIdentity().toString(),
                        result.getProperty("nombre"),
                        result.getProperty("edad"),
                        perfil
                ));
            });
        }
        return personas;
    }

    public void actualizarPersona(Persona persona) {
        try (Database database = new DatabaseFactory(DATABASE_PATH).open()) {
            database.begin();
            var personaDoc = database.lookupById(persona.id());
            if (personaDoc != null) {
                personaDoc.set("nombre", persona.nombre());
                personaDoc.set("edad", persona.edad());
                personaDoc.save();

                var perfilDoc = personaDoc.getProperty("perfil");
                perfilDoc.set("descripcion", persona.perfil().descripcion());
                perfilDoc.set("ocupacion", persona.perfil().ocupacion());
                perfilDoc.save();
            }
            database.commit();
        }
    }

    public void eliminarPersona(String id) {
        try (Database database = new DatabaseFactory(DATABASE_PATH).open()) {
            database.begin();
            var personaDoc = database.lookupById(id);
            if (personaDoc != null) {
                personaDoc.delete();
            }
            database.commit();
        }
    }
}
```

---

#### **c. Controlador Jakarta Faces**

Crea un controlador para manejar las interacciones del usuario.

```java
package com.example.arcadedb.controller;

import com.example.arcadedb.model.Persona;
import com.example.arcadedb.model.Perfil;
import com.example.arcadedb.service.PersonaService;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Named;

import java.io.Serializable;
import java.util.List;

@Named
@ViewScoped
public class PersonaController implements Serializable {

    private final PersonaService personaService = new PersonaService();
    private Persona personaSeleccionada;
    private String nombre;
    private int edad;
    private String descripcion;
    private String ocupacion;

    public List<Persona> getPersonas() {
        return personaService.obtenerPersonas();
    }

    public void crearPersona() {
        var perfil = new Perfil(null, descripcion, ocupacion);
        var persona = new Persona(null, nombre, edad, perfil);
        personaService.crearPersona(persona);
        limpiarCampos();
    }

    public void seleccionarPersona(Persona persona) {
        this.personaSeleccionada = persona;
        this.nombre = persona.nombre();
        this.edad = persona.edad();
        this.descripcion = persona.perfil().descripcion();
        this.ocupacion = persona.perfil().ocupacion();
    }

    public void actualizarPersona() {
        var perfil = new Perfil(personaSeleccionada.perfil().id(), descripcion, ocupacion);
        var persona = new Persona(personaSeleccionada.id(), nombre, edad, perfil);
        personaService.actualizarPersona(persona);
        limpiarCampos();
    }

    public void eliminarPersona() {
        personaService.eliminarPersona(personaSeleccionada.id());
        limpiarCampos();
    }

    private void limpiarCampos() {
        this.personaSeleccionada = null;
        this.nombre = null;
        this.edad = 0;
        this.descripcion = null;
        this.ocupacion = null;
    }

    // Getters y setters
    public Persona getPersonaSeleccionada() {
        return personaSeleccionada;
    }

    public void setPersonaSeleccionada(Persona personaSeleccionada) {
        this.personaSeleccionada = personaSeleccionada;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getEdad() {
        return edad;
    }

    public void setEdad(int edad) {
        this.edad = edad;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getOcupacion() {
        return ocupacion;
    }

    public void setOcupacion(String ocupacion) {
        this.ocupacion = ocupacion;
    }
}
```

---

#### **d. Interfaz de Usuario (index.xhtml)**

Crea una página XHTML para interactuar con el usuario.

```xml
<!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/xhtml"
      xmlns:h="http://xmlns.jcp.org/jsf/html">
<h:head>
    <title>Gestión de Personas</title>
</h:head>
<h:body>
    <h1>Gestión de Personas</h1>

    <h:form>
        <h:panelGrid columns="2">
            <h:outputLabel value="Nombre:" />
            <h:inputText value="#{personaController.nombre}" />

            <h:outputLabel value="Edad:" />
            <h:inputText value="#{personaController.edad}" />

            <h:outputLabel value="Descripción del Perfil:" />
            <h:inputText value="#{personaController.descripcion}" />

            <h:outputLabel value="Ocupación:" />
            <h:inputText value="#{personaController.ocupacion}" />
        </h:panelGrid>

        <h:commandButton value="Crear" action="#{personaController.crearPersona}" />
        <h:commandButton value="Actualizar" action="#{personaController.actualizarPersona}" rendered="#{not empty personaController.personaSeleccionada}" />
        <h:commandButton value="Eliminar" action="#{personaController.eliminarPersona}" rendered="#{not empty personaController.personaSeleccionada}" />
    </h:form>

    <h:dataTable value="#{personaController.personas}" var="persona">
        <h:column>
            <f:facet name="header">Nombre</f:facet>
            #{persona.nombre}
        </h:column>
        <h:column>
            <f:facet name="header">Edad</f:facet>
            #{persona.edad}
        </h:column>
        <h:column>
            <f:facet name="header">Perfil</f:facet>
            #{persona.perfil.descripcion} - #{persona.perfil.ocupacion}
        </h:column>
        <h:column>
            <h:commandButton value="Seleccionar" action="#{personaController.seleccionarPersona(persona)}" />
        </h:column>
    </h:dataTable>
</h:body>
</html>
```

---

### **4. Explicación**

- **Java Records**: Simplifican la definición de modelos inmutables.
- **Servicio CRUD**: Centraliza las operaciones de base de datos.
- **Controlador**: Actúa como intermediario entre la vista y el servicio.
- **Interfaz de Usuario**: Permite al usuario interactuar con la aplicación mediante formularios y tablas.

Si tienes dudas adicionales o necesitas ajustes, no dudes en preguntar.

___

Los `record` en Java son inmutables por diseño, lo que significa que sus campos no pueden ser modificados después de su creación. Esto puede parecer un obstáculo cuando se trabaja con formularios CRUD en Jakarta Faces (anteriormente conocido como JavaServer Faces), ya que el enlace de datos (`binding`) requiere que los valores puedan ser actualizados desde la interfaz de usuario.

Sin embargo, hay varias soluciones para trabajar con `record` en este contexto sin perder la inmutabilidad ni comprometer la funcionalidad del formulario CRUD. A continuación, te muestro una solución estructurada:

---

### **Problema**
- Los `record` son inmutables, lo que significa que no puedes modificar directamente sus campos.
- El framework Jakarta Faces necesita poder actualizar los valores de los campos a través del enlace de datos (`binding`).

---

### **Solución**
La solución consiste en utilizar un patrón de diseño donde los `record` permanezcan inmutables, pero se utilicen objetos mutables (como clases convencionales o `DTO`) para manejar los cambios en el formulario. Estos objetos mutables actúan como intermediarios entre la interfaz de usuario y los `record`.

#### **Pasos para implementar la solución**

1. **Definir el `record` inmutable:**
   Este será el modelo principal de tu aplicación. Por ejemplo:
   ```java
   public record Producto(Long id, String nombre, double precio) {
   }
   ```

2. **Crear una clase mutable (DTO):**
   Esta clase servirá como un contenedor temporal para los datos que se editan en el formulario. Por ejemplo:
   ```java
   public class ProductoDTO {
       private Long id;
       private String nombre;
       private double precio;

       // Getters y setters
       public Long getId() {
           return id;
       }

       public void setId(Long id) {
           this.id = id;
       }

       public String getNombre() {
           return nombre;
       }

       public void setNombre(String nombre) {
           this.nombre = nombre;
       }

       public double getPrecio() {
           return precio;
       }

       public void setPrecio(double precio) {
           this.precio = precio;
       }

       // Método para convertir DTO a Record
       public Producto toProducto() {
           return new Producto(id, nombre, precio);
       }

       // Método para inicializar DTO desde un Record
       public static ProductoDTO fromProducto(Producto producto) {
           ProductoDTO dto = new ProductoDTO();
           dto.setId(producto.id());
           dto.setNombre(producto.nombre());
           dto.setPrecio(producto.precio());
           return dto;
       }
   }
   ```

3. **Implementar el controlador de Jakarta Faces:**
   En el controlador, utiliza el `ProductoDTO` para manejar las operaciones del formulario. Luego, convierte el DTO al `record` cuando sea necesario guardar los datos. Por ejemplo:
   ```java
   import jakarta.faces.view.ViewScoped;
   import jakarta.inject.Named;

   @Named
   @ViewScoped
   public class ProductoController implements Serializable {
       private ProductoDTO productoDTO = new ProductoDTO();
       private List<Producto> productos = new ArrayList<>();

       // Método para cargar un producto en el formulario (editar)
       public void cargarProducto(Producto producto) {
           this.productoDTO = ProductoDTO.fromProducto(producto);
       }

       // Método para guardar un producto
       public void guardarProducto() {
           Producto producto = productoDTO.toProducto();
           productos.add(producto); // Guardar en la lista (simulando persistencia)
           productoDTO = new ProductoDTO(); // Limpiar el formulario
       }

       // Métodos getter y setter
       public ProductoDTO getProductoDTO() {
           return productoDTO;
       }

       public void setProductoDTO(ProductoDTO productoDTO) {
           this.productoDTO = productoDTO;
       }

       public List<Producto> getProductos() {
           return productos;
       }
   }
   ```

4. **Vincular el formulario en la vista XHTML:**
   Utiliza el `ProductoDTO` para el enlace de datos en el formulario. Por ejemplo:
   ```xml
   <h:form>
       <h:inputText value="#{productoController.productoDTO.nombre}" />
       <h:inputText value="#{productoController.productoDTO.precio}" />
       <h:commandButton value="Guardar" action="#{productoController.guardarProducto}" />
   </h:form>

   <h:dataTable value="#{productoController.productos}" var="producto">
       <h:column>
           <f:facet name="header">Nombre</f:facet>
           #{producto.nombre()}
       </h:column>
       <h:column>
           <f:facet name="header">Precio</f:facet>
           #{producto.precio()}
       </h:column>
       <h:column>
           <h:commandButton value="Editar" action="#{productoController.cargarProducto(producto)}" />
       </h:column>
   </h:dataTable>
   ```

---

### **Explicación de la Solución**
1. **Inmutabilidad preservada:** El `record` sigue siendo inmutable, ya que no se modifica directamente en el formulario.
2. **Flexibilidad en el formulario:** El `ProductoDTO` permite que los campos sean mutables y compatibles con el enlace de datos de Jakarta Faces.
3. **Transformación segura:** La conversión entre `ProductoDTO` y `Producto` garantiza que los datos se manejen correctamente sin comprometer la inmutabilidad del modelo.

---

### **Beneficios**
- Mantienes la inmutabilidad de los `record`, lo que es beneficioso para la seguridad y la integridad de los datos.
- Simplificas el manejo de formularios al usar un objeto mutable (`DTO`) como intermediario.
- Facilitas la integración con frameworks como Jakarta Faces sin sacrificar las ventajas de los `record`.

Si tienes más preguntas o necesitas ampliar algún aspecto, no dudes en comentarlo. ¡Buena suerte con tu proyecto!