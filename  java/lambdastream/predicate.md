# Predicate
```java
 public static void main(String[] args)
    {
        List<String> cities = Arrays.asList("New York", "Tokyo", "New Delhi");
 
        Predicate<String> predicate = new Predicate<String>() {
            @Override
            public boolean test(String s) {
                // filtrar ciudades que comienzan con `N`
                return s.startsWith("N");
            }
        };
 
        cities.stream()
                .filter(predicate)
                .forEach(System.out::println);
    }
```
