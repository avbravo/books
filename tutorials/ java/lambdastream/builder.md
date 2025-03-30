# Builder
```java
public class Persona {
    private String name;
    private String foto;

    public Persona() {
    }

    public Persona(String name, String foto) {
        this.name = name;
        this.foto = foto;
    }
    
    
    

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }
    
     public static class Builder {
    private String name;
    private String foto;
    
  public Builder withName(String name) {
            this.name = name;
            return this;
        }

        public Builder withFoto(String foto) {
            this.foto =foto;
            return this;
        }

       

        public Persona build() {
            return new Persona(name, foto);
        }

    }
}



```


## Ejemplo

```java
Persona persona = new Persona.Builder()
                    .withName("Aris")
                    .withFoto("/home/avbravo/Documentos/logo.jpg")
                    .build();

```
