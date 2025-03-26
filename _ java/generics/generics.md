# Generics


```java
public class Printer<T> {
T thingToPrint;

public Printer(T thingToPrint){
this.thingToPrint = thingToPrint;
}

oubloc void print(){
System.out.println(thingToPrint);





```

Clase principal


```java
public class (){

public static void main(String args[]){

Printer<Integer> intPrint = new Printer<>(5);
intPrint.print();

Printer<Double> doublePrint = new Printer<>(85.36);
doublePrint.print();
}


```
