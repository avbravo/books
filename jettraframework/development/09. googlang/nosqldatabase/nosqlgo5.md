Para mejorar la base de datos para que soporte documentos JSON, necesitamos implementar características específicas que permitan almacenar, consultar y manipular documentos JSON de manera eficiente. Esto incluye:

1. **Almacenamiento de documentos JSON**: Permitir que los valores sean documentos JSON.
2. **Consultas anidadas**: Soportar consultas sobre campos anidados dentro de los documentos JSON.
3. **Actualización parcial de documentos**: Permitir modificar solo partes específicas de un documento JSON.
4. **Índices para campos JSON**: Crear índices para campos dentro de los documentos JSON para optimizar las consultas.

A continuación, te guiaré a través de cada uno de estos aspectos.

---

### **1. Almacenamiento de Documentos JSON**
Los documentos JSON se representarán como `map[string]interface{}` en Go. Esto permite trabajar con estructuras de datos dinámicas.

#### **Implementación**

```go
type KeyValueStore struct {
	data       map[string]map[string]interface{} // Clave -> Documento JSON
	indexes    map[string]*sync.Map              // Índice: campo -> valor -> claves
	mu         sync.RWMutex
	filePath   string
	commandCh  chan Command
}

// SetDocument guarda un documento JSON asociado a una clave.
func (kvs *KeyValueStore) SetDocument(key string, document map[string]interface{}) error {
	kvs.mu.Lock()
	defer kvs.mu.Unlock()

	kvs.data[key] = document
	kvs.updateIndexes(key, document)
	return kvs.saveToFile()
}

// GetDocument recupera un documento JSON asociado a una clave.
func (kvs *KeyValueStore) GetDocument(key string) (map[string]interface{}, bool) {
	kvs.mu.RLock()
	defer kvs.mu.RUnlock()

	document, exists := kvs.data[key]
	return document, exists
}
```

---

### **2. Consultas Anidadas**
Las consultas anidadas permiten buscar documentos basados en campos dentro del JSON. Por ejemplo, dado un documento `{ "user": { "name": "John" } }`, queremos poder buscar por `user.name`.

#### **Implementación**

```go
// QueryByField consulta documentos basados en un campo específico.
func (kvs *KeyValueStore) QueryByField(field string, value interface{}) ([]map[string]interface{}, error) {
	kvs.mu.RLock()
	defer kvs.mu.RUnlock()

	index, exists := kvs.indexes[field]
	if !exists {
		return nil, fmt.Errorf("index '%s' does not exist", field)
	}

	keys, exists := index.Load(value)
	if !exists {
		return nil, fmt.Errorf("no data found for field '%s' with value '%v'", field, value)
	}

	results := []map[string]interface{}{}
	for _, key := range keys.([]string) {
		if doc, exists := kvs.data[key]; exists {
			results = append(results, doc)
		}
	}

	return results, nil
}

// getField extrae el valor de un campo anidado en un documento JSON.
func getField(document map[string]interface{}, field string) interface{} {
	parts := strings.Split(field, ".")
	current := document

	for _, part := range parts {
		if val, ok := current[part]; ok {
			if next, ok := val.(map[string]interface{}); ok {
				current = next
			} else {
				return val
			}
		} else {
			return nil
		}
	}

	return current
}
```

---

### **3. Actualización Parcial de Documentos**
Permitir actualizaciones parciales significa que podemos modificar solo ciertos campos dentro de un documento JSON sin sobrescribir todo el documento.

#### **Implementación**

```go
// UpdateField actualiza un campo específico dentro de un documento JSON.
func (kvs *KeyValueStore) UpdateField(key string, field string, value interface{}) error {
	kvs.mu.Lock()
	defer kvs.mu.Unlock()

	document, exists := kvs.data[key]
	if !exists {
		return fmt.Errorf("key '%s' not found", key)
	}

	parts := strings.Split(field, ".")
	current := document

	for i, part := range parts {
		if i == len(parts)-1 {
			current[part] = value
			break
		}

		if _, ok := current[part].(map[string]interface{}); !ok {
			current[part] = make(map[string]interface{})
		}

		current = current[part].(map[string]interface{})
	}

	kvs.updateIndexes(key, document)
	return kvs.saveToFile()
}
```

---

### **4. Índices para Campos JSON**
Los índices permiten acelerar las consultas sobre campos específicos dentro de los documentos JSON. Podemos crear índices para cualquier campo, incluso si está anidado.

#### **Implementación**

```go
// AddIndex añade un índice para un campo específico en los documentos JSON.
func (kvs *KeyValueStore) AddIndex(field string) {
	kvs.mu.Lock()
	defer kvs.mu.Unlock()

	if _, exists := kvs.indexes[field]; !exists {
		kvs.indexes[field] = new(sync.Map)
	}

	// Indexar todos los documentos existentes
	for key, document := range kvs.data {
		fieldValue := getField(document, field)
		if fieldValue != nil {
			kvs.addToIndex(field, fieldValue, key)
		}
	}
}

// updateIndexes actualiza los índices cuando se modifica un documento.
func (kvs *KeyValueStore) updateIndexes(key string, document map[string]interface{}) {
	for field, index := range kvs.indexes {
		fieldValue := getField(document, field)

		// Eliminar la clave antigua si existe
		index.Range(func(k, v interface{}) bool {
			keys := v.([]string)
			newKeys := []string{}
			for _, k := range keys {
				if k != key {
					newKeys = append(newKeys, k)
				}
			}
			index.Store(k, newKeys)
			return true
		})

		// Agregar la nueva clave
		if fieldValue != nil {
			kvs.addToIndex(field, fieldValue, key)
		}
	}
}

// addToIndex agrega una clave al índice.
func (kvs *KeyValueStore) addToIndex(field string, fieldValue interface{}, key string) {
	if index, exists := kvs.indexes[field]; exists {
		if keys, exists := index.Load(fieldValue); exists {
			index.Store(fieldValue, append(keys.([]string), key))
		} else {
			index.Store(fieldValue, []string{key})
		}
	}
}
```

---

### **Ejemplo de Uso**

```go
func main() {
	db, err := NewKeyValueStore("data.json", "log.json")
	if err != nil {
		fmt.Println("Error initializing database:", err)
		return
	}

	// Añadir un índice para el campo "user.name"
	db.AddIndex("user.name")

	// Guardar un documento JSON
	doc := map[string]interface{}{
		"user": map[string]interface{}{
			"name": "John",
			"age":  30,
		},
		"email": "john@example.com",
	}
	db.SetDocument("user1", doc)

	// Consultar documentos por campo
	results, err := db.QueryByField("user.name", "John")
	if err != nil {
		fmt.Println("Error querying documents:", err)
	} else {
		fmt.Println("Query Results:", results)
	}

	// Actualizar un campo específico
	err = db.UpdateField("user1", "user.age", 31)
	if err != nil {
		fmt.Println("Error updating field:", err)
	}

	// Recuperar el documento actualizado
	updatedDoc, _ := db.GetDocument("user1")
	fmt.Println("Updated Document:", updatedDoc)
}
```

---

### **Conclusión**
Hemos mejorado la base de datos para:
1. **Almacenar documentos JSON**.
2. **Consultar campos anidados**.
3. **Actualizar campos específicos**.
4. **Crear índices para campos JSON**.

Esto convierte la base de datos en una solución más flexible y potente para manejar datos JSON. Si necesitas más detalles o quieres agregar funcionalidades adicionales, ¡házmelo saber!