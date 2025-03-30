Crear una base de datos nosql from scrach in golang 

Crear una base de datos NoSQL desde cero en Go (Golang) es un proyecto ambicioso pero muy interesante. A continuación, desglosaré los pasos necesarios para implementar una base de datos NoSQL simple en memoria, que puede ser extendida posteriormente para persistir datos en disco o agregar más funcionalidades.

---

### **1. Definición del problema y alcance**
Antes de comenzar, definamos el alcance:
- La base de datos será **en memoria** (sin persistencia en disco inicialmente).
- Será una base de datos **clave-valor** (el tipo más simple de NoSQL).
- Soportará operaciones básicas como `Set`, `Get` y `Delete`.
- Se implementará un servidor HTTP para interactuar con la base de datos mediante solicitudes RESTful.

---

### **2. Estructura del proyecto**
Organizaremos el proyecto de la siguiente manera:

```
/nosql-db
  ├── main.go          # Punto de entrada del programa
  ├── db.go            # Lógica principal de la base de datos
  ├── server.go        # Servidor HTTP para interactuar con la DB
  └── go.mod           # Archivo de módulo Go
```

---

### **3. Implementación paso a paso**

#### **a. Crear el archivo `go.mod`**
Primero, inicializamos el módulo Go:

```bash
go mod init nosql-db
```

Esto generará un archivo `go.mod` que gestionará las dependencias.

---

#### **b. Implementar la lógica de la base de datos (`db.go`)**
En este archivo, definiremos una estructura básica para almacenar los datos en memoria.

```go
package main

import (
	"sync"
)

// DB representa una base de datos clave-valor en memoria.
type DB struct {
	data map[string]string
	mu   sync.RWMutex // Mutex para manejar concurrencia
}

// NewDB crea una nueva instancia de DB.
func NewDB() *DB {
	return &DB{
		data: make(map[string]string),
	}
}

// Set almacena un valor asociado a una clave.
func (db *DB) Set(key, value string) {
	db.mu.Lock()
	defer db.mu.Unlock()
	db.data[key] = value
}

// Get recupera el valor asociado a una clave.
func (db *DB) Get(key string) (string, bool) {
	db.mu.RLock()
	defer db.mu.RUnlock()
	value, exists := db.data[key]
	return value, exists
}

// Delete elimina una clave de la base de datos.
func (db *DB) Delete(key string) {
	db.mu.Lock()
	defer db.mu.Unlock()
	delete(db.data, key)
}
```

Explicación:
- Usamos un mapa (`map[string]string`) para almacenar los datos.
- Un `sync.RWMutex` asegura que las operaciones sean seguras para concurrencia.
- Las funciones `Set`, `Get` y `Delete` proporcionan la funcionalidad básica.

---

#### **c. Crear un servidor HTTP (`server.go`)**
Implementaremos un servidor HTTP simple para interactuar con la base de datos.

```go
package main

import (
	"encoding/json"
	"net/http"
	"strconv"
)

// handleSet maneja la solicitud para almacenar un valor.
func handleSet(db *DB) http.HandlerFunc {
	return func(w http.ResponseWriter, r *http.Request) {
		key := r.URL.Query().Get("key")
		value := r.URL.Query().Get("value")

		if key == "" || value == "" {
			http.Error(w, "Key and value are required", http.StatusBadRequest)
			return
		}

		db.Set(key, value)
		w.WriteHeader(http.StatusOK)
		w.Write([]byte("Value set successfully"))
	}
}

// handleGet maneja la solicitud para recuperar un valor.
func handleGet(db *DB) http.HandlerFunc {
	return func(w http.ResponseWriter, r *http.Request) {
		key := r.URL.Query().Get("key")

		if key == "" {
			http.Error(w, "Key is required", http.StatusBadRequest)
			return
		}

		value, exists := db.Get(key)
		if !exists {
			http.Error(w, "Key not found", http.StatusNotFound)
			return
		}

		response := map[string]string{"value": value}
		jsonResponse(w, response, http.StatusOK)
	}
}

// handleDelete maneja la solicitud para eliminar una clave.
func handleDelete(db *DB) http.HandlerFunc {
	return func(w http.ResponseWriter, r *http.Request) {
		key := r.URL.Query().Get("key")

		if key == "" {
			http.Error(w, "Key is required", http.StatusBadRequest)
			return
		}

		db.Delete(key)
		w.WriteHeader(http.StatusOK)
		w.Write([]byte("Key deleted successfully"))
	}
}

// jsonResponse envía una respuesta JSON.
func jsonResponse(w http.ResponseWriter, data interface{}, statusCode int) {
	w.Header().Set("Content-Type", "application/json")
	w.WriteHeader(statusCode)
	json.NewEncoder(w).Encode(data)
}
```

Explicación:
- Cada función (`handleSet`, `handleGet`, `handleDelete`) maneja una ruta específica.
- Usamos `http.HandlerFunc` para simplificar la creación de manejadores.
- Las respuestas son enviadas en formato JSON.

---

#### **d. Configurar el punto de entrada (`main.go`)**
Finalmente, configuramos el servidor y lo iniciamos.

```go
package main

import (
	"log"
	"net/http"
)

func main() {
	// Crear una nueva instancia de la base de datos
	db := NewDB()

	// Configurar las rutas
	http.HandleFunc("/set", handleSet(db))
	http.HandleFunc("/get", handleGet(db))
	http.HandleFunc("/delete", handleDelete(db))

	// Iniciar el servidor HTTP
	port := 8080
	log.Printf("Starting server on port %d...\n", port)
	log.Fatal(http.ListenAndServe(":"+strconv.Itoa(port), nil))
}
```

---

### **4. Probar la base de datos**
Una vez que el código esté listo, puedes ejecutarlo con:

```bash
go run .
```

Luego, prueba las siguientes solicitudes usando `curl` o Postman:

1. **Almacenar un valor:**
   ```bash
   curl "http://localhost:8080/set?key=nombre&value=Juan"
   ```

2. **Recuperar un valor:**
   ```bash
   curl "http://localhost:8080/get?key=nombre"
   ```

3. **Eliminar un valor:**
   ```bash
   curl "http://localhost:8080/delete?key=nombre"
   ```

---

### **5. Mejoras futuras**
Este es un prototipo básico. Algunas mejoras que podrías implementar incluyen:
- **Persistencia en disco:** Guardar los datos en archivos para que no se pierdan al reiniciar.
- **Concurrencia avanzada:** Usar canales o patrones de diseño más robustos para manejar múltiples clientes.
- **Soporte para tipos de datos complejos:** Permitir almacenar JSON u otros formatos.
- **Autenticación y autorización:** Agregar seguridad a las solicitudes.

---

### **Conclusión**
Con este ejemplo, has creado una base de datos NoSQL clave-valor simple en Go. Este proyecto puede ser ampliado según tus necesidades, y sirve como una excelente introducción a la creación de sistemas distribuidos y bases de datos personalizadas. ¡Diviértete experimentando y mejorándolo!