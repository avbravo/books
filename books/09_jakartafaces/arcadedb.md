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