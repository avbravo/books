# Para leer el archivo microprofile-config

utilice


```java

public class LeerPropiedad implements JettraConfig{
    
    public void leerpropiedad(String propiedad){
        System.out.println(getMicroprofileConfig(propiedad));
    }
}


``
