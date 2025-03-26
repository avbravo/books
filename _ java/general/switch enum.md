#Switch enum
```java
  switch(repositoryMethod.getAnnotationType()){
                    case SAVE:
                        break;
                    case PING:
                        break;
                    case UPDATE:
                    break;
                    case LOOKUP:
                        break;
                    case COUNT:
                        
                }
```
anotacion

```java
public enum AnnotationType {
    QUERY, SAVE, UPDATE, DELETE, LOOKUP, COUNT, PING, REGEX, REGEXCOUNT, NONE
}
```
