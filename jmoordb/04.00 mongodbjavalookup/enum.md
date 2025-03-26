#Enum
# 01.01.06 enum
Define los enums que se usaran
# LookupSupplierLevel
Es un enum que se usa para indicar el nivel en que se encuentra la entidad

Aplican reglas cuando se esta en nivel 3, se debe usar para los lookup siguiente el nivel 2 
Observe un lookup de corregimiento->provincia-->pais-->planeta

```java
public enum LookupSupplierLevel {
    ZERO, ONE, TWO, THREE, FOUR, FIVE, SIX, SEVEN, EIGTH
    
    
}

```

***
## Diagramas
```mermaid
flowchart LR
  LookupSupplierLevel --> EntityRepository --> EntityLookupSupplier -->  LookupSupplier
```