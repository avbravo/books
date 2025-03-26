# StringTokeniz.nd

Fuente
[java-stringtokenizer)](https://www.baeldung.com/java-stringtokenizer)
```java
     List<String> tokenList = Collections.list(new StringTokenizer(str, " ")).stream()
                        .map(token -> (String) token)
                        .collect(Collectors.toList());
```                        
