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
