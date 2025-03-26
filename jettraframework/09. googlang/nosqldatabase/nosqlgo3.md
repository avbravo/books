Ampliar la base de datos NoSQL para incluir **persistencia en disco**, **concurrencia avanzada**, **índices** y **replicación** es un paso significativo hacia una implementación más robusta. A continuación, te guiaré a través de cada uno de estos aspectos.

---

### **1. Persistencia en Disco**
Para agregar persistencia en disco, guardaremos los datos en un archivo cuando se realicen cambios (`Set` o `Delete`) y los cargaremos al iniciar la aplicación.

#### **Implementación**
Usaremos archivos JSON para almacenar los datos. Cada vez que se modifique el mapa en memoria, escribiremos los datos en un archivo.

```go
import (
	"encoding/json"
	"fmt"
	"os"
	"sync"
)

type KeyValueStore struct {
	data       map[string]interface{}
	mu         sync.RWMutex
	filePath   string // Ruta del archivo de persistencia
}

// NewKeyValueStore crea una nueva instancia de KeyValueStore con persistencia.
func NewKeyValueStore(filePath string) (*KeyValueStore, error) {
	kvs := &KeyValueStore{
		data:     make(map[string]interface{}),
		filePath: filePath,
	}

	// Cargar datos desde el archivo si existe
	if err := kvs.loadFromFile(); err != nil {
		return nil, err
	}

	return kvs, nil
}

// loadFromFile carga los datos desde el archivo en disco.
func (kvs *KeyValueStore) loadFromFile() error {
	file, err := os.Open(kvs.filePath)
	if err != nil {
		if os.IsNotExist(err) {
			return nil // El archivo no existe, pero no es un error crítico
		}
		return err
	}
	defer file.Close()

	return json.NewDecoder(file).Decode(&kvs.data)
}

// saveToFile guarda los datos en el archivo en disco.
func (kvs *KeyValueStore) saveToFile() error {
	file, err := os.Create(kvs.filePath)
	if err != nil {
		return err
	}
	defer file.Close()

	return json.NewEncoder(file).Encode(kvs.data)
}

// Set guarda un valor asociado a una clave y lo persiste en disco.
func (kvs *KeyValueStore) Set(key string, value interface{}) error {
	kvs.mu.Lock()
	defer kvs.mu.Unlock()

	kvs.data[key] = value
	return kvs.saveToFile()
}

// Delete elimina una clave y su valor asociado y lo persiste en disco.
func (kvs *KeyValueStore) Delete(key string) error {
	kvs.mu.Lock()
	defer kvs.mu.Unlock()

	delete(kvs.data, key)
	return kvs.saveToFile()
}
```

---

### **2. Concurrencia Avanzada**
Para manejar concurrencia avanzada, usaremos un **patrón de actor** o **cola de comandos** para procesar operaciones de manera secuencial. Esto asegura que múltiples goroutines no corrompan los datos.

#### **Implementación**
Usaremos un canal para procesar las operaciones en una única goroutine.

```go
type Command struct {
	Action string      // "set" o "delete"
	Key    string
	Value  interface{}
}

type KeyValueStore struct {
	data       map[string]interface{}
	mu         sync.RWMutex
	filePath   string
	commandCh  chan Command
}

// NewKeyValueStore crea una nueva instancia de KeyValueStore con persistencia y concurrencia.
func NewKeyValueStore(filePath string) (*KeyValueStore, error) {
	kvs := &KeyValueStore{
		data:      make(map[string]interface{}),
		filePath:  filePath,
		commandCh: make(chan Command),
	}

	// Cargar datos desde el archivo si existe
	if err := kvs.loadFromFile(); err != nil {
		return nil, err
	}

	// Procesar comandos en una goroutine dedicada
	go kvs.processCommands()

	return kvs, nil
}

// processCommands procesa los comandos en una goroutine.
func (kvs *KeyValueStore) processCommands() {
	for cmd := range kvs.commandCh {
		switch cmd.Action {
		case "set":
			kvs.mu.Lock()
			kvs.data[cmd.Key] = cmd.Value
			kvs.saveToFile()
			kvs.mu.Unlock()
		case "delete":
			kvs.mu.Lock()
			delete(kvs.data, cmd.Key)
			kvs.saveToFile()
			kvs.mu.Unlock()
		}
	}
}

// Set envía un comando "set" al canal.
func (kvs *KeyValueStore) Set(key string, value interface{}) {
	kvs.commandCh <- Command{Action: "set", Key: key, Value: value}
}

// Delete envía un comando "delete" al canal.
func (kvs *KeyValueStore) Delete(key string) {
	kvs.commandCh <- Command{Action: "delete", Key: key}
}
```

---

### **3. Índices**
Los índices permiten realizar consultas eficientes sobre ciertos campos. Por ejemplo, podemos indexar valores por atributos específicos.

#### **Implementación**
Añadiremos un mapa adicional para almacenar índices.

```go
type KeyValueStore struct {
	data       map[string]interface{}
	indexes    map[string]map[interface{}][]string // Índice: campo -> valor -> claves
	mu         sync.RWMutex
	filePath   string
	commandCh  chan Command
}

// NewKeyValueStore inicializa la base de datos con índices.
func NewKeyValueStore(filePath string) (*KeyValueStore, error) {
	kvs := &KeyValueStore{
		data:      make(map[string]interface{}),
		indexes:   make(map[string]map[interface{}][]string),
		filePath:  filePath,
		commandCh: make(chan Command),
	}

	// Cargar datos desde el archivo si existe
	if err := kvs.loadFromFile(); err != nil {
		return nil, err
	}

	go kvs.processCommands()

	return kvs, nil
}

// AddIndex añade un índice para un campo específico.
func (kvs *KeyValueStore) AddIndex(field string) {
	kvs.mu.Lock()
	defer kvs.mu.Unlock()

	if _, exists := kvs.indexes[field]; !exists {
		kvs.indexes[field] = make(map[interface{}][]string)
	}
}

// QueryByIndex consulta datos usando un índice.
func (kvs *KeyValueStore) QueryByIndex(field string, value interface{}) ([]interface{}, error) {
	kvs.mu.RLock()
	defer kvs.mu.RUnlock()

	index, exists := kvs.indexes[field]
	if !exists {
		return nil, fmt.Errorf("index '%s' does not exist", field)
	}

	keys, exists := index[value]
	if !exists {
		return nil, fmt.Errorf("no data found for field '%s' with value '%v'", field, value)
	}

	results := []interface{}{}
	for _, key := range keys {
		results = append(results, kvs.data[key])
	}

	return results, nil
}
```

---

### **4. Replicación**
La replicación implica mantener copias de los datos en múltiples nodos para mejorar la disponibilidad y tolerancia a fallos.

#### **Implementación**
Usaremos una lista de nodos secundarios y propagaremos las operaciones a ellos.

```go
type Replica struct {
	Address string
}

type KeyValueStore struct {
	data       map[string]interface{}
	replicas   []Replica
	mu         sync.RWMutex
	filePath   string
	commandCh  chan Command
}

// Replicate envía una operación a todas las réplicas.
func (kvs *KeyValueStore) Replicate(cmd Command) {
	for _, replica := range kvs.replicas {
		// Simular envío a réplica (usar HTTP POST en producción)
		fmt.Printf("Replicating to %s: %+v\n", replica.Address, cmd)
	}
}

// processCommands procesa los comandos y los replica.
func (kvs *KeyValueStore) processCommands() {
	for cmd := range kvs.commandCh {
		switch cmd.Action {
		case "set":
			kvs.mu.Lock()
			kvs.data[cmd.Key] = cmd.Value
			kvs.saveToFile()
			kvs.Replicate(cmd)
			kvs.mu.Unlock()
		case "delete":
			kvs.mu.Lock()
			delete(kvs.data, cmd.Key)
			kvs.saveToFile()
			kvs.Replicate(cmd)
			kvs.mu.Unlock()
		}
	}
}
```

---

### **Conclusión**
Hemos ampliado la base de datos NoSQL para incluir:
1. **Persistencia en disco**: Guardar y cargar datos desde un archivo.
2. **Concurrencia avanzada**: Usar un canal para procesar operaciones de manera segura.
3. **Índices**: Permitir consultas eficientes basadas en campos específicos.
4. **Replicación**: Propagar cambios a nodos secundarios.

Este diseño puede mejorarse aún más, por ejemplo, implementando mecanismos de recuperación ante fallos, optimización de consultas o soporte para transacciones. Si necesitas más detalles sobre algún aspecto, ¡házmelo saber!