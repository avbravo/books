# Interfaces funcionales
[Java 8 — Functional Interface— the Feature That You Must Know](https://medium.com/javarevisited/java-8-lambda-expression-the-feature-that-you-must-know-1fc380b6d5cb)

@FunctionalInterface
public interface ExampleInterface {
    int randomCalculate(int a, int b);

    default void print(int result) {
        System.out.println(result);
    }
}


import java.util.Random;

public class TestFunctionalInterface {

    public static void main(String[] args) {

        ExampleInterface exampleInterface = (a, b) -> {
            int randomCal = a * b / 20;
            Random random = new Random();
            randomCal = randomCal + random.nextInt(1000);
            return randomCal;
        };

        exampleInterface.print(exampleInterface.randomCalculate(10, 20));
    }
}
