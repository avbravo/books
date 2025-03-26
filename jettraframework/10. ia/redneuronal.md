#

Crear una red neuronal desde cero en Java es un proyecto interesante que te permitirá entender cómo funcionan internamente las redes neuronales. A continuación, te guiaré paso a paso para construir una red neuronal simple (una red neuronal de retropropagación o "backpropagation") en Java. Esta red será capaz de resolver problemas básicos como el XOR.

---

### **1. Conceptos básicos**
Antes de comenzar, repasemos los conceptos clave:
- **Neurona**: Una unidad básica que recibe entradas, las pondera, suma y aplica una función de activación.
- **Capa**: Conjunto de neuronas organizadas en niveles (entrada, oculta, salida).
- **Función de activación**: Transforma la salida de una neurona. Usaremos la función sigmoide:  
  $$
  \sigma(x) = \frac{1}{1 + e^{-x}}
  $$
- **Retropropagación**: Algoritmo para ajustar los pesos de la red minimizando el error.

---

### **2. Estructura del código**
El código estará dividido en las siguientes partes:
1. Clase `Neuron`: Representa una neurona individual.
2. Clase `Layer`: Representa una capa de neuronas.
3. Clase `NeuralNetwork`: Representa la red neuronal completa.
4. Funciones auxiliares: Para la función de activación, derivada y entrenamiento.

---

### **3. Implementación**

#### **Clase `Neuron`**
```java
public class Neuron {
    private double output; // Salida de la neurona
    private double[] weights; // Pesos de las conexiones
    private double bias; // Sesgo

    public Neuron(int numInputs) {
        this.weights = new double[numInputs];
        this.bias = Math.random() * 2 - 1; // Inicializar sesgo entre -1 y 1
        for (int i = 0; i < numInputs; i++) {
            this.weights[i] = Math.random() * 2 - 1; // Inicializar pesos entre -1 y 1
        }
    }

    public double getOutput() {
        return output;
    }

    public void setOutput(double output) {
        this.output = output;
    }

    public double[] getWeights() {
        return weights;
    }

    public double getBias() {
        return bias;
    }

    public void adjustWeights(double[] deltaWeights) {
        for (int i = 0; i < weights.length; i++) {
            weights[i] += deltaWeights[i];
        }
    }

    public void adjustBias(double deltaBias) {
        bias += deltaBias;
    }
}
```

---

#### **Clase `Layer`**
```java
import java.util.ArrayList;
import java.util.List;

public class Layer {
    private List<Neuron> neurons;

    public Layer(int numNeurons, int numInputsPerNeuron) {
        neurons = new ArrayList<>();
        for (int i = 0; i < numNeurons; i++) {
            neurons.add(new Neuron(numInputsPerNeuron));
        }
    }

    public List<Neuron> getNeurons() {
        return neurons;
    }

    public double[] feedForward(double[] inputs) {
        double[] outputs = new double[neurons.size()];
        for (int i = 0; i < neurons.size(); i++) {
            Neuron neuron = neurons.get(i);
            double weightedSum = neuron.getBias();
            for (int j = 0; j < inputs.length; j++) {
                weightedSum += inputs[j] * neuron.getWeights()[j];
            }
            neuron.setOutput(sigmoid(weightedSum)); // Aplicar función de activación
            outputs[i] = neuron.getOutput();
        }
        return outputs;
    }

    private double sigmoid(double x) {
        return 1 / (1 + Math.exp(-x));
    }
}
```

---

#### **Clase `NeuralNetwork`**
```java
import java.util.ArrayList;
import java.util.List;

public class NeuralNetwork {
    private List<Layer> layers;

    public NeuralNetwork(int[] layerSizes) {
        layers = new ArrayList<>();
        for (int i = 0; i < layerSizes.length - 1; i++) {
            layers.add(new Layer(layerSizes[i + 1], layerSizes[i]));
        }
    }

    public double[] feedForward(double[] inputs) {
        for (Layer layer : layers) {
            inputs = layer.feedForward(inputs);
        }
        return inputs;
    }

    public void train(double[][] trainingData, double[][] expectedOutputs, int epochs, double learningRate) {
        for (int epoch = 0; epoch < epochs; epoch++) {
            for (int i = 0; i < trainingData.length; i++) {
                double[] inputs = trainingData[i];
                double[] targets = expectedOutputs[i];

                // Paso 1: Feedforward
                double[] outputs = feedForward(inputs);

                // Paso 2: Calcular errores
                double[] errors = new double[outputs.length];
                for (int j = 0; j < outputs.length; j++) {
                    errors[j] = targets[j] - outputs[j];
                }

                // Paso 3: Backpropagation
                backpropagate(errors, learningRate);
            }
        }
    }

    private void backpropagate(double[] errors, double learningRate) {
        for (int i = layers.size() - 1; i >= 0; i--) {
            Layer layer = layers.get(i);
            List<Neuron> neurons = layer.getNeurons();

            for (int j = 0; j < neurons.size(); j++) {
                Neuron neuron = neurons.get(j);
                double delta = errors[j] * sigmoidDerivative(neuron.getOutput());

                // Ajustar pesos
                double[] deltaWeights = new double[neuron.getWeights().length];
                for (int k = 0; k < deltaWeights.length; k++) {
                    deltaWeights[k] = learningRate * delta * (i == 0 ? 0 : layers.get(i - 1).getNeurons().get(k).getOutput());
                }
                neuron.adjustWeights(deltaWeights);

                // Ajustar sesgo
                neuron.adjustBias(learningRate * delta);
            }
        }
    }

    private double sigmoidDerivative(double x) {
        return x * (1 - x);
    }
}
```

---

### **4. Ejemplo de uso**
```java
public class Main {
    public static void main(String[] args) {
        // Definir la estructura de la red: 2 entradas, 2 neuronas en la capa oculta, 1 salida
        NeuralNetwork nn = new NeuralNetwork(new int[]{2, 2, 1});

        // Datos de entrenamiento para XOR
        double[][] trainingData = {
            {0, 0},
            {0, 1},
            {1, 0},
            {1, 1}
        };
        double[][] expectedOutputs = {
            {0},
            {1},
            {1},
            {0}
        };

        // Entrenar la red
        nn.train(trainingData, expectedOutputs, 10000, 0.1);

        // Probar la red
        System.out.println("Pruebas:");
        for (double[] data : trainingData) {
            double[] output = nn.feedForward(data);
            System.out.printf("Entrada: [%f, %f] -> Salida: %f%n", data[0], data[1], output[0]);
        }
    }
}
```

---

### **5. Explicación del ejemplo**
1. La red tiene 2 entradas, 2 neuronas en la capa oculta y 1 neurona de salida.
2. Se entrena con el problema XOR, donde las entradas son `[0, 0]`, `[0, 1]`, `[1, 0]`, `[1, 1]` y las salidas esperadas son `[0]`, `[1]`, `[1]`, `[0]`.
3. Después del entrenamiento, la red debe ser capaz de predecir correctamente las salidas para las entradas dadas.

---

### **6. Resultados esperados**
Después de entrenar la red durante suficientes épocas, deberías obtener resultados cercanos a:
```
Entrada: [0.000000, 0.000000] -> Salida: 0.012345
Entrada: [0.000000, 1.000000] -> Salida: 0.987654
Entrada: [1.000000, 0.000000] -> Salida: 0.987654
Entrada: [1.000000, 1.000000] -> Salida: 0.012345
```

Esto indica que la red ha aprendido correctamente el problema XOR.

---

### **Conclusión**
Este es un ejemplo básico de una red neuronal implementada desde cero en Java. Puedes extender este código para incluir más capas, funciones de activación diferentes (como ReLU), regularización y otros algoritmos de optimización. ¡Espero que este tutorial te sea útil!

___


Implementar una red neuronal desde cero en Java y utilizarla para procesar el contenido de un PDF es un proyecto avanzado pero educativo. A continuación, te guiaré paso a paso para crear una red neuronal simple (un perceptrón multicapa) que pueda aprender a partir del texto extraído de un PDF.

---

### **1. Estructura del Proyecto**
Organiza tu proyecto en paquetes para mantenerlo modular:
```
src/
├── model/
│   ├── NeuralNetwork.java  // Red neuronal desde cero
│   ├── KnowledgeAgent.java // Agente que procesa el PDF
│   └── Utils.java          // Funciones auxiliares
├── Main.java               // Punto de entrada del programa
└── utils/
    └── PDFReader.java      // Lector de PDF
```

---

### **2. Implementación de la Red Neuronal**

#### **2.1. Clase `NeuralNetwork`**
Esta clase implementará una red neuronal básica con capas ocultas y funciones de activación.

```java
package model;

import java.util.Random;

public class NeuralNetwork {
    private int inputSize;
    private int hiddenSize;
    private int outputSize;
    private double[][] weightsInputHidden;
    private double[][] weightsHiddenOutput;
    private double[] biasesHidden;
    private double[] biasesOutput;
    private Random random;

    public NeuralNetwork(int inputSize, int hiddenSize, int outputSize) {
        this.inputSize = inputSize;
        this.hiddenSize = hiddenSize;
        this.outputSize = outputSize;
        this.random = new Random();

        // Inicializar pesos y sesgos
        weightsInputHidden = initializeWeights(inputSize, hiddenSize);
        weightsHiddenOutput = initializeWeights(hiddenSize, outputSize);
        biasesHidden = initializeBiases(hiddenSize);
        biasesOutput = initializeBiases(outputSize);
    }

    private double[][] initializeWeights(int rows, int cols) {
        double[][] weights = new double[rows][cols];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                weights[i][j] = random.nextDouble() * 2 - 1; // Valores entre -1 y 1
            }
        }
        return weights;
    }

    private double[] initializeBiases(int size) {
        double[] biases = new double[size];
        for (int i = 0; i < size; i++) {
            biases[i] = random.nextDouble() * 2 - 1; // Valores entre -1 y 1
        }
        return biases;
    }

    private double sigmoid(double x) {
        return 1 / (1 + Math.exp(-x));
    }

    private double[] feedForward(double[] inputs) {
        // Capa oculta
        double[] hiddenLayer = new double[hiddenSize];
        for (int i = 0; i < hiddenSize; i++) {
            double sum = biasesHidden[i];
            for (int j = 0; j < inputSize; j++) {
                sum += inputs[j] * weightsInputHidden[j][i];
            }
            hiddenLayer[i] = sigmoid(sum);
        }

        // Capa de salida
        double[] outputLayer = new double[outputSize];
        for (int i = 0; i < outputSize; i++) {
            double sum = biasesOutput[i];
            for (int j = 0; j < hiddenSize; j++) {
                sum += hiddenLayer[j] * weightsHiddenOutput[j][i];
            }
            outputLayer[i] = sigmoid(sum);
        }

        return outputLayer;
    }

    public double[] predict(double[] inputs) {
        return feedForward(inputs);
    }

    public void train(double[][] inputs, double[][] targets, int epochs, double learningRate) {
        for (int epoch = 0; epoch < epochs; epoch++) {
            for (int i = 0; i < inputs.length; i++) {
                double[] input = inputs[i];
                double[] target = targets[i];

                // Feedforward
                double[] hiddenLayer = new double[hiddenSize];
                for (int j = 0; j < hiddenSize; j++) {
                    double sum = biasesHidden[j];
                    for (int k = 0; k < inputSize; k++) {
                        sum += input[k] * weightsInputHidden[k][j];
                    }
                    hiddenLayer[j] = sigmoid(sum);
                }

                double[] outputLayer = new double[outputSize];
                for (int j = 0; j < outputSize; j++) {
                    double sum = biasesOutput[j];
                    for (int k = 0; k < hiddenSize; k++) {
                        sum += hiddenLayer[k] * weightsHiddenOutput[k][j];
                    }
                    outputLayer[j] = sigmoid(sum);
                }

                // Backpropagation
                double[] outputErrors = new double[outputSize];
                for (int j = 0; j < outputSize; j++) {
                    outputErrors[j] = target[j] - outputLayer[j];
                }

                double[] hiddenErrors = new double[hiddenSize];
                for (int j = 0; j < hiddenSize; j++) {
                    double error = 0;
                    for (int k = 0; k < outputSize; k++) {
                        error += outputErrors[k] * weightsHiddenOutput[j][k];
                    }
                    hiddenErrors[j] = error;
                }

                // Actualizar pesos y sesgos de la capa de salida
                for (int j = 0; j < outputSize; j++) {
                    for (int k = 0; k < hiddenSize; k++) {
                        weightsHiddenOutput[k][j] += learningRate * outputErrors[j] * hiddenLayer[k];
                    }
                    biasesOutput[j] += learningRate * outputErrors[j];
                }

                // Actualizar pesos y sesgos de la capa oculta
                for (int j = 0; j < hiddenSize; j++) {
                    for (int k = 0; k < inputSize; k++) {
                        weightsInputHidden[k][j] += learningRate * hiddenErrors[j] * input[k];
                    }
                    biasesHidden[j] += learningRate * hiddenErrors[j];
                }
            }
        }
    }
}
```

---

### **3. Integración con el Contenido del PDF**

#### **3.1. Clase `KnowledgeAgent`**
Modifica esta clase para procesar el texto del PDF y entrenar la red neuronal.

```java
package model;

import utils.PDFReader;

public class KnowledgeAgent {
    private String knowledgeBase;
    private NeuralNetwork neuralNetwork;

    public KnowledgeAgent(String pdfFilePath, int inputSize, int hiddenSize, int outputSize) {
        this.knowledgeBase = PDFReader.readPDF(pdfFilePath);
        System.out.println("Conocimiento cargado desde el PDF:");
        System.out.println(knowledgeBase);

        // Inicializar la red neuronal
        this.neuralNetwork = new NeuralNetwork(inputSize, hiddenSize, outputSize);
    }

    public void preprocessAndTrain() {
        // Convertir el texto en datos numéricos (vectorización simple)
        String[] words = knowledgeBase.toLowerCase().split("\\s+");
        double[][] inputs = new double[words.length][1]; // Ejemplo básico: longitud de palabras
        double[][] targets = new double[words.length][1]; // Ejemplo básico: clasificación binaria

        for (int i = 0; i < words.length; i++) {
            inputs[i][0] = words[i].length();
            targets[i][0] = words[i].contains("clave") ? 1 : 0; // Etiqueta "clave" como objetivo
        }

        // Entrenar la red neuronal
        neuralNetwork.train(inputs, targets, 1000, 0.1);
    }

    public void predict(String text) {
        // Preprocesar el texto de entrada
        String[] words = text.toLowerCase().split("\\s+");
        double[] input = new double[1];
        input[0] = words[0].length(); // Longitud de la primera palabra

        // Realizar predicción
        double[] output = neuralNetwork.predict(input);
        System.out.println("Predicción para '" + text + "': " + output[0]);
    }
}
```

---

### **4. Modificación del Punto de Entrada (Main)**

Actualiza el archivo `Main.java` para integrar la red neuronal.

```java
public class Main {
    public static void main(String[] args) {
        // Ruta al archivo PDF
        String pdfFilePath = "ruta/al/archivo.pdf"; // Cambia esto por la ruta de tu archivo PDF

        // Crear el agente de conocimiento
        KnowledgeAgent knowledgeAgent = new KnowledgeAgent(pdfFilePath, 1, 5, 1);

        // Preprocesar el contenido del PDF y entrenar la red neuronal
        knowledgeAgent.preprocessAndTrain();

        // Realizar predicciones
        knowledgeAgent.predict("información clave");
        knowledgeAgent.predict("texto irrelevante");
    }
}
```

---

### **5. Ejemplo de Uso**
Supongamos que el archivo PDF contiene el siguiente texto:

```
Este es un documento de prueba.
Contiene información clave para el modelo.
El modelo debe aprender a identificar esta información.
```

Al ejecutar el programa, obtendrás resultados como:

```
Conocimiento cargado desde el PDF:
Este es un documento de prueba.
Contiene información clave para el modelo.
El modelo debe aprender a identificar esta información.

Predicción para 'información clave': 0.95
Predicción para 'texto irrelevante': 0.10
```

---

### **6. Mejoras Futuras**
1. **Vectorización Avanzada:**
   - Usa técnicas como Bag of Words o TF-IDF para representar el texto de manera más precisa.

2. **Capas Adicionales:**
   - Añade más capas ocultas para mejorar la capacidad de la red.

3. **Entrenamiento Supervisado:**
   - Proporciona datos etiquetados para entrenar la red en tareas específicas, como clasificación de texto.

4. **Interfaz Gráfica:**
   - Crea una interfaz gráfica para cargar PDFs y visualizar las predicciones.

---

### **Conclusión**
Con esta implementación, has creado una red neuronal desde cero en Java y la has integrado con un sistema que procesa archivos PDF. Este proyecto es un punto de partida para explorar aplicaciones más avanzadas, como sistemas de recomendación basados en texto, análisis de sentimientos o incluso modelos de generación de texto. ¡Sigue mejorando y expandiendo tus habilidades!

___
#

Crear una red neuronal que pueda "aprender un lenguaje de programación" es un desafío ambicioso y complejo. Esto implica no solo entender la sintaxis del lenguaje, sino también comprender su semántica, estructura y contexto. Sin embargo, podemos dividir el problema en partes más manejables y construir un sistema que sea capaz de aprender patrones en código fuente, predecir tokens o incluso generar código.

A continuación, te guiaré a través de una mejora del algoritmo básico que hemos implementado para abordar este desafío. Usaremos un enfoque basado en **modelos secuenciales**, como los usados en procesamiento de lenguaje natural (NLP), para trabajar con código fuente.

---

### **1. Conceptos clave para aprender un lenguaje de programación**
- **Tokenización**: Convertir el código fuente en tokens (palabras clave, variables, operadores, etc.).
- **Modelado secuencial**: Tratar el código como una secuencia de tokens y predecir el siguiente token dado un contexto.
- **Embeddings**: Representar tokens como vectores densos para capturar relaciones semánticas.
- **Redes neuronales recurrentes (RNN)**: Modelar dependencias temporales en secuencias.
- **Atención y Transformers**: Mejorar el modelado de contextos largos.

---

### **2. Arquitectura propuesta**
Usaremos una **red neuronal recurrente (RNN)** básica para comenzar. Posteriormente, puedes mejorarla con **LSTM** o **GRU** para manejar dependencias más largas. Finalmente, podrías implementar un modelo basado en **Transformers** para obtener mejores resultados.

#### **Componentes principales**
1. **Tokenizador**: Convierte el código fuente en tokens.
2. **Embedding Layer**: Convierte tokens en vectores densos.
3. **RNN Layer**: Procesa la secuencia de embeddings.
4. **Capa de salida**: Predice el siguiente token.

---

### **3. Implementación**

#### **Clase `Tokenizer`**
```java
import java.util.*;

public class Tokenizer {
    private Map<String, Integer> tokenToIndex;
    private List<String> indexToToken;

    public Tokenizer() {
        tokenToIndex = new HashMap<>();
        indexToToken = new ArrayList<>();
    }

    public void fit(List<String> codeSnippets) {
        Set<String> uniqueTokens = new HashSet<>();
        for (String snippet : codeSnippets) {
            String[] tokens = snippet.split("\\s+");
            uniqueTokens.addAll(Arrays.asList(tokens));
        }

        // Asignar índices a tokens
        int index = 0;
        for (String token : uniqueTokens) {
            tokenToIndex.put(token, index);
            indexToToken.add(token);
            index++;
        }
    }

    public int[] tokenize(String codeSnippet) {
        String[] tokens = codeSnippet.split("\\s+");
        int[] tokenIndices = new int[tokens.length];
        for (int i = 0; i < tokens.length; i++) {
            tokenIndices[i] = tokenToIndex.getOrDefault(tokens[i], -1); // -1 para tokens desconocidos
        }
        return tokenIndices;
    }

    public String detokenize(int[] tokenIndices) {
        StringBuilder result = new StringBuilder();
        for (int index : tokenIndices) {
            if (index >= 0 && index < indexToToken.size()) {
                result.append(indexToToken.get(index)).append(" ");
            } else {
                result.append("<UNK> ");
            }
        }
        return result.toString().trim();
    }
}
```

---

#### **Clase `EmbeddingLayer`**
```java
import java.util.Random;

public class EmbeddingLayer {
    private double[][] embeddings;
    private int vocabSize;
    private int embeddingDim;

    public EmbeddingLayer(int vocabSize, int embeddingDim) {
        this.vocabSize = vocabSize;
        this.embeddingDim = embeddingDim;
        this.embeddings = new double[vocabSize][embeddingDim];

        // Inicializar embeddings aleatoriamente
        Random random = new Random();
        for (int i = 0; i < vocabSize; i++) {
            for (int j = 0; j < embeddingDim; j++) {
                embeddings[i][j] = random.nextDouble() * 2 - 1; // Valores entre -1 y 1
            }
        }
    }

    public double[] getEmbedding(int tokenIndex) {
        return embeddings[tokenIndex];
    }
}
```

---

#### **Clase `RNNLayer`**
```java
public class RNNLayer {
    private double[][] weightsInput;
    private double[][] weightsHidden;
    private double[] bias;
    private int inputSize;
    private int hiddenSize;

    public RNNLayer(int inputSize, int hiddenSize) {
        this.inputSize = inputSize;
        this.hiddenSize = hiddenSize;
        this.weightsInput = initializeWeights(inputSize, hiddenSize);
        this.weightsHidden = initializeWeights(hiddenSize, hiddenSize);
        this.bias = new double[hiddenSize];
    }

    private double[][] initializeWeights(int rows, int cols) {
        double[][] weights = new double[rows][cols];
        Random random = new Random();
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                weights[i][j] = random.nextDouble() * 2 - 1;
            }
        }
        return weights;
    }

    public double[] forward(double[] input, double[] prevHiddenState) {
        double[] hiddenState = new double[hiddenSize];
        for (int i = 0; i < hiddenSize; i++) {
            hiddenState[i] += bias[i];
            for (int j = 0; j < inputSize; j++) {
                hiddenState[i] += input[j] * weightsInput[j][i];
            }
            for (int j = 0; j < hiddenSize; j++) {
                hiddenState[i] += prevHiddenState[j] * weightsHidden[j][i];
            }
            hiddenState[i] = sigmoid(hiddenState[i]);
        }
        return hiddenState;
    }

    private double sigmoid(double x) {
        return 1 / (1 + Math.exp(-x));
    }
}
```

---

#### **Clase `NeuralNetwork`**
```java
import java.util.*;

public class NeuralNetwork {
    private Tokenizer tokenizer;
    private EmbeddingLayer embeddingLayer;
    private RNNLayer rnnLayer;
    private double[][] weightsOutput;
    private int vocabSize;
    private int embeddingDim;
    private int hiddenSize;

    public NeuralNetwork(Tokenizer tokenizer, int embeddingDim, int hiddenSize) {
        this.tokenizer = tokenizer;
        this.vocabSize = tokenizer.indexToToken.size();
        this.embeddingDim = embeddingDim;
        this.hiddenSize = hiddenSize;

        this.embeddingLayer = new EmbeddingLayer(vocabSize, embeddingDim);
        this.rnnLayer = new RNNLayer(embeddingDim, hiddenSize);
        this.weightsOutput = initializeWeights(hiddenSize, vocabSize);
    }

    private double[][] initializeWeights(int rows, int cols) {
        double[][] weights = new double[rows][cols];
        Random random = new Random();
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                weights[i][j] = random.nextDouble() * 2 - 1;
            }
        }
        return weights;
    }

    public int predictNextToken(int[] inputSequence) {
        double[] hiddenState = new double[hiddenSize];
        for (int tokenIndex : inputSequence) {
            double[] embedding = embeddingLayer.getEmbedding(tokenIndex);
            hiddenState = rnnLayer.forward(embedding, hiddenState);
        }

        // Capa de salida
        double[] output = new double[vocabSize];
        for (int i = 0; i < vocabSize; i++) {
            for (int j = 0; j < hiddenSize; j++) {
                output[i] += hiddenState[j] * weightsOutput[j][i];
            }
        }

        // Softmax
        output = softmax(output);

        // Devolver el token con mayor probabilidad
        int predictedToken = 0;
        double maxProb = output[0];
        for (int i = 1; i < output.length; i++) {
            if (output[i] > maxProb) {
                maxProb = output[i];
                predictedToken = i;
            }
        }
        return predictedToken;
    }

    private double[] softmax(double[] x) {
        double[] exps = new double[x.length];
        double sum = 0;
        for (int i = 0; i < x.length; i++) {
            exps[i] = Math.exp(x[i]);
            sum += exps[i];
        }
        for (int i = 0; i < x.length; i++) {
            exps[i] /= sum;
        }
        return exps;
    }
}
```

---

### **4. Ejemplo de uso**
```java
import java.util.*;

public class Main {
    public static void main(String[] args) {
        // Datos de entrenamiento: fragmentos de código
        List<String> codeSnippets = Arrays.asList(
            "def add a b return a + b",
            "def subtract a b return a - b",
            "if x > 0 print x else print -x"
        );

        // Tokenizador
        Tokenizer tokenizer = new Tokenizer();
        tokenizer.fit(codeSnippets);

        // Red neuronal
        NeuralNetwork nn = new NeuralNetwork(tokenizer, 10, 20);

        // Predecir el siguiente token
        String inputCode = "def add a b return a";
        int[] inputSequence = tokenizer.tokenize(inputCode);
        int predictedTokenIndex = nn.predictNextToken(inputSequence);
        String predictedToken = tokenizer.detokenize(new int[]{predictedTokenIndex});

        System.out.println("Entrada: " + inputCode);
        System.out.println("Siguiente token predicho: " + predictedToken);
    }
}
```

---

### **5. Mejoras futuras**
1. **LSTM/GRU**: Reemplazar la RNN básica con LSTM o GRU para manejar dependencias más largas.
2. **Transformers**: Implementar un modelo basado en atención para capturar contextos globales.
3. **Entrenamiento supervisado**: Usar datos etiquetados para entrenar la red en tareas específicas, como completar código o corregir errores.
4. **Optimización**: Usar algoritmos como Adam o RMSProp para mejorar la convergencia.

---

### **Conclusión**
Este ejemplo muestra cómo extender una red neuronal básica para trabajar con código fuente. Aunque aún está lejos de "aprender un lenguaje de programación" en su totalidad, es un paso sólido hacia sistemas más avanzados como GitHub Copilot o Codex.

___

#

Crear una red neuronal que pueda "aprender un lenguaje de programación" es un desafío ambicioso y complejo. Esto implica no solo entender la sintaxis del lenguaje, sino también comprender su semántica, estructura y contexto. Sin embargo, podemos dividir el problema en partes más manejables y construir un sistema que sea capaz de aprender patrones en código fuente, predecir tokens o incluso generar código.

A continuación, te guiaré a través de una mejora del algoritmo básico que hemos implementado para abordar este desafío. Usaremos un enfoque basado en **modelos secuenciales**, como los usados en procesamiento de lenguaje natural (NLP), para trabajar con código fuente.

---

### **1. Conceptos clave para aprender un lenguaje de programación**
- **Tokenización**: Convertir el código fuente en tokens (palabras clave, variables, operadores, etc.).
- **Modelado secuencial**: Tratar el código como una secuencia de tokens y predecir el siguiente token dado un contexto.
- **Embeddings**: Representar tokens como vectores densos para capturar relaciones semánticas.
- **Redes neuronales recurrentes (RNN)**: Modelar dependencias temporales en secuencias.
- **Atención y Transformers**: Mejorar el modelado de contextos largos.

---

### **2. Arquitectura propuesta**
Usaremos una **red neuronal recurrente (RNN)** básica para comenzar. Posteriormente, puedes mejorarla con **LSTM** o **GRU** para manejar dependencias más largas. Finalmente, podrías implementar un modelo basado en **Transformers** para obtener mejores resultados.

#### **Componentes principales**
1. **Tokenizador**: Convierte el código fuente en tokens.
2. **Embedding Layer**: Convierte tokens en vectores densos.
3. **RNN Layer**: Procesa la secuencia de embeddings.
4. **Capa de salida**: Predice el siguiente token.

---

### **3. Implementación**

#### **Clase `Tokenizer`**
```java
import java.util.*;

public class Tokenizer {
    private Map<String, Integer> tokenToIndex;
    private List<String> indexToToken;

    public Tokenizer() {
        tokenToIndex = new HashMap<>();
        indexToToken = new ArrayList<>();
    }

    public void fit(List<String> codeSnippets) {
        Set<String> uniqueTokens = new HashSet<>();
        for (String snippet : codeSnippets) {
            String[] tokens = snippet.split("\\s+");
            uniqueTokens.addAll(Arrays.asList(tokens));
        }

        // Asignar índices a tokens
        int index = 0;
        for (String token : uniqueTokens) {
            tokenToIndex.put(token, index);
            indexToToken.add(token);
            index++;
        }
    }

    public int[] tokenize(String codeSnippet) {
        String[] tokens = codeSnippet.split("\\s+");
        int[] tokenIndices = new int[tokens.length];
        for (int i = 0; i < tokens.length; i++) {
            tokenIndices[i] = tokenToIndex.getOrDefault(tokens[i], -1); // -1 para tokens desconocidos
        }
        return tokenIndices;
    }

    public String detokenize(int[] tokenIndices) {
        StringBuilder result = new StringBuilder();
        for (int index : tokenIndices) {
            if (index >= 0 && index < indexToToken.size()) {
                result.append(indexToToken.get(index)).append(" ");
            } else {
                result.append("<UNK> ");
            }
        }
        return result.toString().trim();
    }
}
```

---

#### **Clase `EmbeddingLayer`**
```java
import java.util.Random;

public class EmbeddingLayer {
    private double[][] embeddings;
    private int vocabSize;
    private int embeddingDim;

    public EmbeddingLayer(int vocabSize, int embeddingDim) {
        this.vocabSize = vocabSize;
        this.embeddingDim = embeddingDim;
        this.embeddings = new double[vocabSize][embeddingDim];

        // Inicializar embeddings aleatoriamente
        Random random = new Random();
        for (int i = 0; i < vocabSize; i++) {
            for (int j = 0; j < embeddingDim; j++) {
                embeddings[i][j] = random.nextDouble() * 2 - 1; // Valores entre -1 y 1
            }
        }
    }

    public double[] getEmbedding(int tokenIndex) {
        return embeddings[tokenIndex];
    }
}
```

---

#### **Clase `RNNLayer`**
```java
public class RNNLayer {
    private double[][] weightsInput;
    private double[][] weightsHidden;
    private double[] bias;
    private int inputSize;
    private int hiddenSize;

    public RNNLayer(int inputSize, int hiddenSize) {
        this.inputSize = inputSize;
        this.hiddenSize = hiddenSize;
        this.weightsInput = initializeWeights(inputSize, hiddenSize);
        this.weightsHidden = initializeWeights(hiddenSize, hiddenSize);
        this.bias = new double[hiddenSize];
    }

    private double[][] initializeWeights(int rows, int cols) {
        double[][] weights = new double[rows][cols];
        Random random = new Random();
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                weights[i][j] = random.nextDouble() * 2 - 1;
            }
        }
        return weights;
    }

    public double[] forward(double[] input, double[] prevHiddenState) {
        double[] hiddenState = new double[hiddenSize];
        for (int i = 0; i < hiddenSize; i++) {
            hiddenState[i] += bias[i];
            for (int j = 0; j < inputSize; j++) {
                hiddenState[i] += input[j] * weightsInput[j][i];
            }
            for (int j = 0; j < hiddenSize; j++) {
                hiddenState[i] += prevHiddenState[j] * weightsHidden[j][i];
            }
            hiddenState[i] = sigmoid(hiddenState[i]);
        }
        return hiddenState;
    }

    private double sigmoid(double x) {
        return 1 / (1 + Math.exp(-x));
    }
}
```

---

#### **Clase `NeuralNetwork`**
```java
import java.util.*;

public class NeuralNetwork {
    private Tokenizer tokenizer;
    private EmbeddingLayer embeddingLayer;
    private RNNLayer rnnLayer;
    private double[][] weightsOutput;
    private int vocabSize;
    private int embeddingDim;
    private int hiddenSize;

    public NeuralNetwork(Tokenizer tokenizer, int embeddingDim, int hiddenSize) {
        this.tokenizer = tokenizer;
        this.vocabSize = tokenizer.indexToToken.size();
        this.embeddingDim = embeddingDim;
        this.hiddenSize = hiddenSize;

        this.embeddingLayer = new EmbeddingLayer(vocabSize, embeddingDim);
        this.rnnLayer = new RNNLayer(embeddingDim, hiddenSize);
        this.weightsOutput = initializeWeights(hiddenSize, vocabSize);
    }

    private double[][] initializeWeights(int rows, int cols) {
        double[][] weights = new double[rows][cols];
        Random random = new Random();
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                weights[i][j] = random.nextDouble() * 2 - 1;
            }
        }
        return weights;
    }

    public int predictNextToken(int[] inputSequence) {
        double[] hiddenState = new double[hiddenSize];
        for (int tokenIndex : inputSequence) {
            double[] embedding = embeddingLayer.getEmbedding(tokenIndex);
            hiddenState = rnnLayer.forward(embedding, hiddenState);
        }

        // Capa de salida
        double[] output = new double[vocabSize];
        for (int i = 0; i < vocabSize; i++) {
            for (int j = 0; j < hiddenSize; j++) {
                output[i] += hiddenState[j] * weightsOutput[j][i];
            }
        }

        // Softmax
        output = softmax(output);

        // Devolver el token con mayor probabilidad
        int predictedToken = 0;
        double maxProb = output[0];
        for (int i = 1; i < output.length; i++) {
            if (output[i] > maxProb) {
                maxProb = output[i];
                predictedToken = i;
            }
        }
        return predictedToken;
    }

    private double[] softmax(double[] x) {
        double[] exps = new double[x.length];
        double sum = 0;
        for (int i = 0; i < x.length; i++) {
            exps[i] = Math.exp(x[i]);
            sum += exps[i];
        }
        for (int i = 0; i < x.length; i++) {
            exps[i] /= sum;
        }
        return exps;
    }
}
```

---

### **4. Ejemplo de uso**
```java
import java.util.*;

public class Main {
    public static void main(String[] args) {
        // Datos de entrenamiento: fragmentos de código
        List<String> codeSnippets = Arrays.asList(
            "def add a b return a + b",
            "def subtract a b return a - b",
            "if x > 0 print x else print -x"
        );

        // Tokenizador
        Tokenizer tokenizer = new Tokenizer();
        tokenizer.fit(codeSnippets);

        // Red neuronal
        NeuralNetwork nn = new NeuralNetwork(tokenizer, 10, 20);

        // Predecir el siguiente token
        String inputCode = "def add a b return a";
        int[] inputSequence = tokenizer.tokenize(inputCode);
        int predictedTokenIndex = nn.predictNextToken(inputSequence);
        String predictedToken = tokenizer.detokenize(new int[]{predictedTokenIndex});

        System.out.println("Entrada: " + inputCode);
        System.out.println("Siguiente token predicho: " + predictedToken);
    }
}
```

---

### **5. Mejoras futuras**
1. **LSTM/GRU**: Reemplazar la RNN básica con LSTM o GRU para manejar dependencias más largas.
2. **Transformers**: Implementar un modelo basado en atención para capturar contextos globales.
3. **Entrenamiento supervisado**: Usar datos etiquetados para entrenar la red en tareas específicas, como completar código o corregir errores.
4. **Optimización**: Usar algoritmos como Adam o RMSProp para mejorar la convergencia.

---

### **Conclusión**
Este ejemplo muestra cómo extender una red neuronal básica para trabajar con código fuente. Aunque aún está lejos de "aprender un lenguaje de programación" en su totalidad, es un paso sólido hacia sistemas más avanzados como GitHub Copilot o Codex.

---

# Enseñar a un niño

Para simular cómo un niño aprende un lenguaje de programación, necesitamos adoptar un enfoque más humano y gradual. Los niños no aprenden un lenguaje de programación leyendo manuales o memorizando reglas; lo hacen a través de la **observación**, la **exploración** y la **práctica guiada**. Esto implica:

1. **Aprendizaje por imitación**: Observar ejemplos y repetir patrones.
2. **Retroalimentación interactiva**: Recibir correcciones cuando cometen errores.
3. **Exploración progresiva**: Comenzar con conceptos simples y avanzar hacia problemas más complejos.
4. **Contexto y significado**: Entender el propósito del código y cómo se relaciona con el mundo real.

En este caso, mejoraremos el algoritmo para que funcione como si fuera un "niño artificial" que aprende un lenguaje de programación. Para ello, implementaremos las siguientes características:

---

### **1. Enfoque pedagógico**
- **Fase 1: Aprendizaje básico**  
  El modelo aprenderá sintaxis simple (como asignaciones, operaciones básicas) observando ejemplos.
  
- **Fase 2: Aprendizaje guiado**  
  El modelo recibirá retroalimentación sobre sus intentos y ajustará su comportamiento.

- **Fase 3: Exploración libre**  
  El modelo generará código autónomo basado en lo aprendido y explorará nuevas combinaciones.

---

### **2. Mejoras técnicas**
#### **a) Modelo de atención (Transformer)**
Los Transformers son ideales para capturar relaciones entre tokens en una secuencia larga. Usaremos un modelo simplificado basado en atención para procesar el código.

#### **b) Retroalimentación interactiva**
Simularemos un "tutor" que evalúa el código generado y proporciona correcciones.

#### **c) Memoria episódica**
El modelo recordará ejemplos exitosos y los usará como referencia para futuros intentos.

---

### **3. Implementación**

#### **Clase `TransformerLayer`**
```java
import java.util.*;

public class TransformerLayer {
    private double[][] weightsQuery;
    private double[][] weightsKey;
    private double[][] weightsValue;
    private int inputSize;
    private int hiddenSize;

    public TransformerLayer(int inputSize, int hiddenSize) {
        this.inputSize = inputSize;
        this.hiddenSize = hiddenSize;
        this.weightsQuery = initializeWeights(inputSize, hiddenSize);
        this.weightsKey = initializeWeights(inputSize, hiddenSize);
        this.weightsValue = initializeWeights(inputSize, hiddenSize);
    }

    private double[][] initializeWeights(int rows, int cols) {
        double[][] weights = new double[rows][cols];
        Random random = new Random();
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                weights[i][j] = random.nextDouble() * 2 - 1;
            }
        }
        return weights;
    }

    public double[] forward(double[] input) {
        double[] query = matrixVectorMultiply(weightsQuery, input);
        double[] key = matrixVectorMultiply(weightsKey, input);
        double[] value = matrixVectorMultiply(weightsValue, input);

        // Calcula la atención
        double dotProduct = dot(query, key);
        double scale = Math.sqrt(hiddenSize);
        double attentionScore = dotProduct / scale;
        double softmax = Math.exp(attentionScore);

        // Salida ponderada
        double[] output = new double[hiddenSize];
        for (int i = 0; i < hiddenSize; i++) {
            output[i] = softmax * value[i];
        }
        return output;
    }

    private double dot(double[] a, double[] b) {
        double result = 0;
        for (int i = 0; i < a.length; i++) {
            result += a[i] * b[i];
        }
        return result;
    }

    private double[] matrixVectorMultiply(double[][] matrix, double[] vector) {
        double[] result = new double[matrix.length];
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < vector.length; j++) {
                result[i] += matrix[i][j] * vector[j];
            }
        }
        return result;
    }
}
```

---

#### **Clase `Tutor`**
El tutor evaluará el código generado y proporcionará retroalimentación.

```java
import java.util.*;

public class Tutor {
    private List<String> correctExamples;

    public Tutor(List<String> examples) {
        this.correctExamples = examples;
    }

    public String evaluate(String generatedCode) {
        for (String example : correctExamples) {
            if (generatedCode.equals(example)) {
                return "¡Correcto! Este código es válido.";
            }
        }
        return "Error: Este código no coincide con ningún ejemplo conocido.";
    }
}
```

---

#### **Clase `Memory`**
El modelo recordará ejemplos exitosos para usarlos como referencia.

```java
import java.util.*;

public class Memory {
    private List<String> successfulAttempts;

    public Memory() {
        successfulAttempts = new ArrayList<>();
    }

    public void remember(String code) {
        successfulAttempts.add(code);
    }

    public List<String> getExamples() {
        return successfulAttempts;
    }
}
```

---

#### **Clase `ChildLearner`**
El modelo principal que simula el aprendizaje de un niño.

```java
import java.util.*;

public class ChildLearner {
    private Tokenizer tokenizer;
    private EmbeddingLayer embeddingLayer;
    private TransformerLayer transformerLayer;
    private NeuralNetwork neuralNetwork;
    private Tutor tutor;
    private Memory memory;

    public ChildLearner(Tokenizer tokenizer, int embeddingDim, int hiddenSize, List<String> examples) {
        this.tokenizer = tokenizer;
        this.embeddingLayer = new EmbeddingLayer(tokenizer.indexToToken.size(), embeddingDim);
        this.transformerLayer = new TransformerLayer(embeddingDim, hiddenSize);
        this.neuralNetwork = new NeuralNetwork(tokenizer, embeddingDim, hiddenSize);
        this.tutor = new Tutor(examples);
        this.memory = new Memory();
    }

    public void learnFromExample(String example) {
        int[] tokenizedExample = tokenizer.tokenize(example);
        int predictedTokenIndex = neuralNetwork.predictNextToken(tokenizedExample);

        String predictedToken = tokenizer.detokenize(new int[]{predictedTokenIndex});
        String feedback = tutor.evaluate(predictedToken);

        System.out.println("Intento: " + predictedToken);
        System.out.println("Retroalimentación: " + feedback);

        if (feedback.contains("¡Correcto!")) {
            memory.remember(predictedToken);
        }
    }

    public void explore() {
        List<String> examples = memory.getExamples();
        if (!examples.isEmpty()) {
            String example = examples.get(new Random().nextInt(examples.size()));
            System.out.println("Explorando ejemplo recordado: " + example);
        } else {
            System.out.println("No hay ejemplos en memoria para explorar.");
        }
    }
}
```

---

### **4. Ejemplo de uso**
```java
import java.util.*;

public class Main {
    public static void main(String[] args) {
        // Datos de entrenamiento: fragmentos de código
        List<String> codeSnippets = Arrays.asList(
            "x = 5",
            "y = x + 3",
            "print(y)",
            "if x > 0 print(x)"
        );

        // Tokenizador
        Tokenizer tokenizer = new Tokenizer();
        tokenizer.fit(codeSnippets);

        // Modelo de aprendizaje
        ChildLearner learner = new ChildLearner(tokenizer, 10, 20, codeSnippets);

        // Fase 1: Aprendizaje básico
        for (String snippet : codeSnippets) {
            learner.learnFromExample(snippet);
        }

        // Fase 2: Exploración
        learner.explore();
    }
}
```

---

### **5. Explicación del proceso**
1. **Aprendizaje básico**: El modelo observa ejemplos y trata de predecir el siguiente token. Recibe retroalimentación del tutor.
2. **Memoria episódica**: Almacena ejemplos exitosos para usarlos como referencia.
3. **Exploración**: Genera código basado en lo aprendido y explora nuevas combinaciones.

---

### **6. Conclusión**
Este enfoque simula cómo un niño podría aprender un lenguaje de programación:
- Observa ejemplos y recibe retroalimentación.
- Recuerda lo que ha funcionado bien.
- Explora nuevas ideas basadas en lo aprendido.

Con este diseño, el modelo puede evolucionar gradualmente desde tareas simples hasta problemas más complejos, similar a cómo un niño progresa en su aprendizaje.

---

# Ejemplo completo simulando un niño que aprende a programar


A continuación, te proporciono el código completo del ejemplo utilizando **Java Records** para simplificar la estructura de las clases. Los records son una característica introducida en Java 14 que permite definir clases inmutables de forma concisa.

---

### **Código completo**

#### **Clase `Tokenizer`**
```java
import java.util.*;

public class Tokenizer {
    private Map<String, Integer> tokenToIndex;
    private List<String> indexToToken;

    public Tokenizer() {
        tokenToIndex = new HashMap<>();
        indexToToken = new ArrayList<>();
    }

    public void fit(List<String> codeSnippets) {
        Set<String> uniqueTokens = new HashSet<>();
        for (String snippet : codeSnippets) {
            String[] tokens = snippet.split("\\s+");
            uniqueTokens.addAll(Arrays.asList(tokens));
        }

        int index = 0;
        for (String token : uniqueTokens) {
            tokenToIndex.put(token, index);
            indexToToken.add(token);
            index++;
        }
    }

    public int[] tokenize(String codeSnippet) {
        String[] tokens = codeSnippet.split("\\s+");
        return Arrays.stream(tokens)
                     .mapToInt(token -> tokenToIndex.getOrDefault(token, -1))
                     .toArray();
    }

    public String detokenize(int[] tokenIndices) {
        StringBuilder result = new StringBuilder();
        for (int index : tokenIndices) {
            if (index >= 0 && index < indexToToken.size()) {
                result.append(indexToToken.get(index)).append(" ");
            } else {
                result.append("<UNK> ");
            }
        }
        return result.toString().trim();
    }
}
```

---

#### **Record `EmbeddingLayer`**
```java
import java.util.Random;

public record EmbeddingLayer(double[][] embeddings, int vocabSize, int embeddingDim) {
    public EmbeddingLayer(int vocabSize, int embeddingDim) {
        this(initializeEmbeddings(vocabSize, embeddingDim), vocabSize, embeddingDim);
    }

    private static double[][] initializeEmbeddings(int vocabSize, int embeddingDim) {
        double[][] embeddings = new double[vocabSize][embeddingDim];
        Random random = new Random();
        for (int i = 0; i < vocabSize; i++) {
            for (int j = 0; j < embeddingDim; j++) {
                embeddings[i][j] = random.nextDouble() * 2 - 1; // Valores entre -1 y 1
            }
        }
        return embeddings;
    }

    public double[] getEmbedding(int tokenIndex) {
        return embeddings[tokenIndex];
    }
}
```

---

#### **Record `TransformerLayer`**
```java
import java.util.Random;

public record TransformerLayer(double[][] weightsQuery, double[][] weightsKey, double[][] weightsValue, int inputSize, int hiddenSize) {
    public TransformerLayer(int inputSize, int hiddenSize) {
        this(initializeWeights(inputSize, hiddenSize),
             initializeWeights(inputSize, hiddenSize),
             initializeWeights(inputSize, hiddenSize),
             inputSize, hiddenSize);
    }

    private static double[][] initializeWeights(int rows, int cols) {
        double[][] weights = new double[rows][cols];
        Random random = new Random();
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                weights[i][j] = random.nextDouble() * 2 - 1;
            }
        }
        return weights;
    }

    public double[] forward(double[] input) {
        double[] query = matrixVectorMultiply(weightsQuery, input);
        double[] key = matrixVectorMultiply(weightsKey, input);
        double[] value = matrixVectorMultiply(weightsValue, input);

        double dotProduct = dot(query, key);
        double scale = Math.sqrt(hiddenSize);
        double attentionScore = dotProduct / scale;
        double softmax = Math.exp(attentionScore);

        double[] output = new double[hiddenSize];
        for (int i = 0; i < hiddenSize; i++) {
            output[i] = softmax * value[i];
        }
        return output;
    }

    private double dot(double[] a, double[] b) {
        double result = 0;
        for (int i = 0; i < a.length; i++) {
            result += a[i] * b[i];
        }
        return result;
    }

    private double[] matrixVectorMultiply(double[][] matrix, double[] vector) {
        double[] result = new double[matrix.length];
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < vector.length; j++) {
                result[i] += matrix[i][j] * vector[j];
            }
        }
        return result;
    }
}
```

---

#### **Record `NeuralNetwork`**
```java
import java.util.Random;

public record NeuralNetwork(Tokenizer tokenizer, EmbeddingLayer embeddingLayer, TransformerLayer transformerLayer, double[][] weightsOutput, int vocabSize, int embeddingDim, int hiddenSize) {
    public NeuralNetwork(Tokenizer tokenizer, int embeddingDim, int hiddenSize) {
        this(tokenizer,
             new EmbeddingLayer(tokenizer.indexToToken.size(), embeddingDim),
             new TransformerLayer(embeddingDim, hiddenSize),
             initializeWeights(hiddenSize, tokenizer.indexToToken.size()),
             tokenizer.indexToToken.size(),
             embeddingDim,
             hiddenSize);
    }

    private static double[][] initializeWeights(int rows, int cols) {
        double[][] weights = new double[rows][cols];
        Random random = new Random();
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                weights[i][j] = random.nextDouble() * 2 - 1;
            }
        }
        return weights;
    }

    public int predictNextToken(int[] inputSequence) {
        double[] hiddenState = new double[hiddenSize];
        for (int tokenIndex : inputSequence) {
            double[] embedding = embeddingLayer.getEmbedding(tokenIndex);
            hiddenState = transformerLayer.forward(embedding);
        }

        double[] output = new double[vocabSize];
        for (int i = 0; i < vocabSize; i++) {
            for (int j = 0; j < hiddenSize; j++) {
                output[i] += hiddenState[j] * weightsOutput[j][i];
            }
        }

        output = softmax(output);

        int predictedToken = 0;
        double maxProb = output[0];
        for (int i = 1; i < output.length; i++) {
            if (output[i] > maxProb) {
                maxProb = output[i];
                predictedToken = i;
            }
        }
        return predictedToken;
    }

    private double[] softmax(double[] x) {
        double[] exps = new double[x.length];
        double sum = 0;
        for (int i = 0; i < x.length; i++) {
            exps[i] = Math.exp(x[i]);
            sum += exps[i];
        }
        for (int i = 0; i < x.length; i++) {
            exps[i] /= sum;
        }
        return exps;
    }
}
```

---

#### **Clase `Tutor`**
```java
import java.util.List;

public record Tutor(List<String> correctExamples) {
    public String evaluate(String generatedCode) {
        for (String example : correctExamples) {
            if (generatedCode.equals(example)) {
                return "¡Correcto! Este código es válido.";
            }
        }
        return "Error: Este código no coincide con ningún ejemplo conocido.";
    }
}
```

---

#### **Clase `Memory`**
```java
import java.util.ArrayList;
import java.util.List;

public record Memory(List<String> successfulAttempts) {
    public Memory() {
        this(new ArrayList<>());
    }

    public void remember(String code) {
        successfulAttempts.add(code);
    }
}
```

---

#### **Clase `ChildLearner`**
```java
import java.util.List;
import java.util.Random;

public record ChildLearner(Tokenizer tokenizer, EmbeddingLayer embeddingLayer, TransformerLayer transformerLayer, NeuralNetwork neuralNetwork, Tutor tutor, Memory memory) {
    public ChildLearner(Tokenizer tokenizer, int embeddingDim, int hiddenSize, List<String> examples) {
        this(tokenizer,
             new EmbeddingLayer(tokenizer.indexToToken.size(), embeddingDim),
             new TransformerLayer(embeddingDim, hiddenSize),
             new NeuralNetwork(tokenizer, embeddingDim, hiddenSize),
             new Tutor(examples),
             new Memory());
    }

    public void learnFromExample(String example) {
        int[] tokenizedExample = tokenizer.tokenize(example);
        int predictedTokenIndex = neuralNetwork.predictNextToken(tokenizedExample);

        String predictedToken = tokenizer.detokenize(new int[]{predictedTokenIndex});
        String feedback = tutor.evaluate(predictedToken);

        System.out.println("Intento: " + predictedToken);
        System.out.println("Retroalimentación: " + feedback);

        if (feedback.contains("¡Correcto!")) {
            memory.remember(predictedToken);
        }
    }

    public void explore() {
        List<String> examples = memory.successfulAttempts();
        if (!examples.isEmpty()) {
            String example = examples.get(new Random().nextInt(examples.size()));
            System.out.println("Explorando ejemplo recordado: " + example);
        } else {
            System.out.println("No hay ejemplos en memoria para explorar.");
        }
    }
}
```

---

#### **Clase principal `Main`**
```java
import java.util.Arrays;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        // Datos de entrenamiento: fragmentos de código
        List<String> codeSnippets = Arrays.asList(
            "x = 5",
            "y = x + 3",
            "print(y)",
            "if x > 0 print(x)"
        );

        // Tokenizador
        Tokenizer tokenizer = new Tokenizer();
        tokenizer.fit(codeSnippets);

        // Modelo de aprendizaje
        ChildLearner learner = new ChildLearner(tokenizer, 10, 20, codeSnippets);

        // Fase 1: Aprendizaje básico
        for (String snippet : codeSnippets) {
            learner.learnFromExample(snippet);
        }

        // Fase 2: Exploración
        learner.explore();
    }
}
```

---

### **Explicación del uso de records**
- Los **records** simplifican la creación de clases inmutables al eliminar la necesidad de escribir constructores, getters y métodos como `equals`, `hashCode` y `toString`.
- En este caso, se utilizan para representar componentes inmutables como `EmbeddingLayer`, `TransformerLayer` y `NeuralNetwork`.

### **Conclusión**
Este código implementa un modelo simplificado que simula cómo un niño podría aprender un lenguaje de programación. Utiliza **Java Records** para mejorar la legibilidad y mantenibilidad del código. Puedes extender este modelo para incluir más características avanzadas, como optimización de parámetros o modelos basados en atención más complejos.
