#IntStream

```java
  int sum = IntStream.range(0, 1000000).sum(); 
```

sin Stream
```javca
 public void processData() { 

    List<Integer> data = new ArrayList<>(); 

    for (int i = 0; i < 1000000; i++) { 

      data.add(i); 

  } 

  int sum = 0; 

  for (Integer num : data) { 

    sum += num; 

  } 

  System.out.println("Sum: " + sum); 

  } 

``
