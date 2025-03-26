https://medium.com/@abhinavcv007/bittorrent-part-1-the-engineering-behind-the-bittorrent-protocol-04e70ee01d58


---

Create Java network for torrent architecture that chunck and shared files that incluyed server and client in Java using virtual trheads 

---


Creating a Java-based torrent-like architecture involves implementing both the server and client components that can handle file chunking, sharing, and downloading. The system will use virtual threads (introduced in Java 19 as part of Project Loom) to manage concurrent connections efficiently.

Below is a simplified implementation of such a system:

---

### **Overview**
1. **Server**:
   - Manages file chunking.
   - Tracks peers and their availability for specific chunks.
   - Handles incoming requests from clients.

2. **Client**:
   - Requests chunks from the server or other peers.
   - Downloads chunks concurrently using virtual threads.
   - Reassembles the file after all chunks are downloaded.

3. **Key Features**:
   - File chunking into smaller pieces.
   - Peer-to-peer communication for chunk sharing.
   - Virtual threads for efficient concurrency.

---

### **Code Implementation**

#### **1. Server Code**
The server splits the file into chunks, tracks which peers have which chunks, and responds to client requests.

```java
import java.io.*;
import java.net.*;
import java.util.*;
import java.util.concurrent.*;

public class TorrentServer {
    private static final int PORT = 5000;
    private static final String FILE_PATH = "example.txt";
    private static final int CHUNK_SIZE = 1024; // 1 KB chunks

    private final Map<Integer, byte[]> fileChunks = new HashMap<>();
    private final Map<String, Set<Integer>> peerChunks = new ConcurrentHashMap<>();

    public void start() throws IOException {
        // Split the file into chunks
        splitFileIntoChunks(FILE_PATH);

        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("Server started on port " + PORT);

            while (true) {
                Socket clientSocket = serverSocket.accept();
                Thread.ofVirtual().start(() -> handleClient(clientSocket));
            }
        }
    }

    private void splitFileIntoChunks(String filePath) throws IOException {
        try (InputStream inputStream = new FileInputStream(filePath)) {
            int chunkIndex = 0;
            byte[] buffer = new byte[CHUNK_SIZE];
            int bytesRead;

            while ((bytesRead = inputStream.read(buffer)) != -1) {
                byte[] chunk = Arrays.copyOf(buffer, bytesRead);
                fileChunks.put(chunkIndex++, chunk);
            }

            System.out.println("File split into " + fileChunks.size() + " chunks.");
        }
    }

    private void handleClient(Socket clientSocket) {
        try (ObjectOutputStream out = new ObjectOutputStream(clientSocket.getOutputStream());
             ObjectInputStream in = new ObjectInputStream(clientSocket.getInputStream())) {

            String command = (String) in.readObject();

            if ("GET_CHUNK".equals(command)) {
                int chunkIndex = in.readInt();
                if (fileChunks.containsKey(chunkIndex)) {
                    out.writeObject(fileChunks.get(chunkIndex));
                } else {
                    out.writeObject(null);
                }
            } else if ("REGISTER_PEER".equals(command)) {
                String peerId = (String) in.readObject();
                Set<Integer> chunks = (Set<Integer>) in.readObject();
                peerChunks.put(peerId, chunks);
                System.out.println("Peer registered: " + peerId);
            } else if ("GET_PEERS".equals(command)) {
                out.writeObject(peerChunks);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws IOException {
        new TorrentServer().start();
    }
}
```

---

#### **2. Client Code**
The client connects to the server, downloads chunks concurrently, and reassembles the file.

```java
import java.io.*;
import java.net.*;
import java.util.*;
import java.util.concurrent.*;

public class TorrentClient {
    private static final String SERVER_ADDRESS = "localhost";
    private static final int SERVER_PORT = 5000;
    private static final String OUTPUT_FILE = "downloaded_file.txt";

    public void start() throws IOException, ClassNotFoundException {
        try (Socket serverSocket = new Socket(SERVER_ADDRESS, SERVER_PORT);
             ObjectOutputStream out = new ObjectOutputStream(serverSocket.getOutputStream());
             ObjectInputStream in = new ObjectInputStream(serverSocket.getInputStream())) {

            // Get the list of chunks from the server
            out.writeObject("GET_PEERS");
            Map<String, Set<Integer>> peerChunks = (Map<String, Set<Integer>>) in.readObject();

            List<Integer> allChunks = new ArrayList<>(peerChunks.values().stream()
                    .flatMap(Set::stream)
                    .distinct()
                    .toList());

            System.out.println("Total chunks to download: " + allChunks.size());

            // Download chunks concurrently using virtual threads
            Map<Integer, byte[]> downloadedChunks = Collections.synchronizedMap(new HashMap<>());

            try (ExecutorService executor = Executors.newVirtualThreadPerTaskExecutor()) {
                for (int chunkIndex : allChunks) {
                    executor.submit(() -> downloadChunk(chunkIndex, downloadedChunks));
                }
            }

            // Reassemble the file
            reassembleFile(downloadedChunks, allChunks.size());
        }
    }

    private void downloadChunk(int chunkIndex, Map<Integer, byte[]> downloadedChunks) {
        try (Socket peerSocket = new Socket(SERVER_ADDRESS, SERVER_PORT);
             ObjectOutputStream out = new ObjectOutputStream(peerSocket.getOutputStream());
             ObjectInputStream in = new ObjectInputStream(peerSocket.getInputStream())) {

            out.writeObject("GET_CHUNK");
            out.writeInt(chunkIndex);

            byte[] chunk = (byte[]) in.readObject();
            if (chunk != null) {
                downloadedChunks.put(chunkIndex, chunk);
                System.out.println("Downloaded chunk " + chunkIndex);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void reassembleFile(Map<Integer, byte[]> downloadedChunks, int totalChunks) throws IOException {
        try (OutputStream outputStream = new FileOutputStream(OUTPUT_FILE)) {
            for (int i = 0; i < totalChunks; i++) {
                byte[] chunk = downloadedChunks.get(i);
                if (chunk != null) {
                    outputStream.write(chunk);
                }
            }
        }
        System.out.println("File reassembled successfully.");
    }

    public static void main(String[] args) throws IOException, ClassNotFoundException {
        new TorrentClient().start();
    }
}
```

---

### **How It Works**
1. **Server**:
   - Splits the file into chunks and stores them in memory.
   - Responds to client requests for chunks or peer information.

2. **Client**:
   - Connects to the server to get the list of available chunks and peers.
   - Uses virtual threads to download chunks concurrently.
   - Reassembles the file once all chunks are downloaded.

---

### **Key Points**
- **Concurrency**: Virtual threads ensure efficient handling of multiple connections without blocking.
- **Scalability**: The system can be extended to support multiple files and more sophisticated peer discovery mechanisms.
- **Error Handling**: Add retries and timeouts for robustness in real-world scenarios.

This implementation provides a basic foundation for a torrent-like architecture in Java. You can enhance it further by adding features like encryption, peer-to-peer communication, and better error handling.

---

Para almacenar el archivo en disco y distribuirlo entre varios nodos, necesitamos modificar la arquitectura para que los nodos (peers) puedan almacenar fragmentos del archivo en disco y compartirlos entre ellos. Esto implica:

1. **Almacenamiento en Disco**: Cada nodo almacena los fragmentos que ha descargado o que le han sido asignados.
2. **Distribución de Fragmentos**: Los fragmentos se distribuyen entre varios nodos para permitir la descarga paralela desde múltiples fuentes.
3. **Comunicación Peer-to-Peer (P2P)**: Los nodos deben comunicarse directamente para intercambiar fragmentos.

A continuación, se presenta una implementación mejorada que aborda estos aspectos.

---

### **Arquitectura**
1. **Servidor Central**:
   - Divide el archivo en fragmentos.
   - Asigna fragmentos a nodos específicos.
   - Actúa como un tracker para informar a los clientes sobre qué nodos tienen qué fragmentos.

2. **Nodos (Peers)**:
   - Almacenan fragmentos en disco.
   - Comparten fragmentos con otros nodos cuando se les solicita.

3. **Cliente**:
   - Consulta al servidor central para obtener la lista de nodos y sus fragmentos.
   - Descarga fragmentos de múltiples nodos concurrentemente.
   - Reensambla el archivo completo.

---

### **Implementación**

#### **1. Servidor Central**
El servidor divide el archivo en fragmentos, los asigna a nodos y actúa como un tracker.

```java
import java.io.*;
import java.net.*;
import java.util.*;

public class TrackerServer {
    private static final int PORT = 5000;
    private static final String FILE_PATH = "example.txt";
    private static final int CHUNK_SIZE = 1024; // Tamaño del fragmento

    private final Map<Integer, byte[]> fileChunks = new HashMap<>();
    private final Map<String, Set<Integer>> peerChunks = new ConcurrentHashMap<>();

    public void start() throws IOException {
        splitFileIntoChunks(FILE_PATH);

        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("Tracker server started on port " + PORT);

            while (true) {
                Socket clientSocket = serverSocket.accept();
                Thread.ofVirtual().start(() -> handleClient(clientSocket));
            }
        }
    }

    private void splitFileIntoChunks(String filePath) throws IOException {
        try (InputStream inputStream = new FileInputStream(filePath)) {
            int chunkIndex = 0;
            byte[] buffer = new byte[CHUNK_SIZE];
            int bytesRead;

            while ((bytesRead = inputStream.read(buffer)) != -1) {
                byte[] chunk = Arrays.copyOf(buffer, bytesRead);
                fileChunks.put(chunkIndex++, chunk);
            }

            System.out.println("File split into " + fileChunks.size() + " chunks.");
        }
    }

    private void handleClient(Socket clientSocket) {
        try (ObjectOutputStream out = new ObjectOutputStream(clientSocket.getOutputStream());
             ObjectInputStream in = new ObjectInputStream(clientSocket.getInputStream())) {

            String command = (String) in.readObject();

            if ("GET_CHUNK".equals(command)) {
                int chunkIndex = in.readInt();
                if (fileChunks.containsKey(chunkIndex)) {
                    out.writeObject(fileChunks.get(chunkIndex));
                } else {
                    out.writeObject(null);
                }
            } else if ("REGISTER_PEER".equals(command)) {
                String peerId = (String) in.readObject();
                Set<Integer> chunks = (Set<Integer>) in.readObject();
                peerChunks.put(peerId, chunks);
                System.out.println("Peer registered: " + peerId);
            } else if ("GET_PEERS".equals(command)) {
                out.writeObject(peerChunks);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws IOException {
        new TrackerServer().start();
    }
}
```

---

#### **2. Nodo (Peer)**
Cada nodo almacena fragmentos en disco y los comparte con otros nodos.

```java
import java.io.*;
import java.net.*;
import java.util.*;

public class PeerNode {
    private static final int PORT = 6000;
    private static final String STORAGE_DIR = "peer_storage/";

    private final Map<Integer, File> storedChunks = new HashMap<>();

    public void start(String peerId) throws IOException {
        File storageDir = new File(STORAGE_DIR + peerId);
        if (!storageDir.exists()) {
            storageDir.mkdirs();
        }

        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("Peer node " + peerId + " started on port " + PORT);

            while (true) {
                Socket clientSocket = serverSocket.accept();
                Thread.ofVirtual().start(() -> handleClient(clientSocket));
            }
        }
    }

    private void handleClient(Socket clientSocket) {
        try (ObjectOutputStream out = new ObjectOutputStream(clientSocket.getOutputStream());
             ObjectInputStream in = new ObjectInputStream(clientSocket.getInputStream())) {

            String command = (String) in.readObject();

            if ("STORE_CHUNK".equals(command)) {
                int chunkIndex = in.readInt();
                byte[] chunkData = (byte[]) in.readObject();
                storeChunk(chunkIndex, chunkData);
                out.writeObject("Chunk stored successfully.");
            } else if ("GET_CHUNK".equals(command)) {
                int chunkIndex = in.readInt();
                File chunkFile = storedChunks.get(chunkIndex);
                if (chunkFile != null) {
                    byte[] chunkData = Files.readAllBytes(chunkFile.toPath());
                    out.writeObject(chunkData);
                } else {
                    out.writeObject(null);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void storeChunk(int chunkIndex, byte[] chunkData) throws IOException {
        File chunkFile = new File(STORAGE_DIR + "peer_" + chunkIndex + ".chunk");
        try (FileOutputStream fos = new FileOutputStream(chunkFile)) {
            fos.write(chunkData);
        }
        storedChunks.put(chunkIndex, chunkFile);
        System.out.println("Stored chunk " + chunkIndex);
    }

    public static void main(String[] args) throws IOException {
        if (args.length < 1) {
            System.out.println("Usage: PeerNode <peerId>");
            return;
        }
        new PeerNode().start(args[0]);
    }
}
```

---

#### **3. Cliente**
El cliente consulta al tracker, descarga fragmentos de múltiples nodos y reensambla el archivo.

```java
import java.io.*;
import java.net.*;
import java.util.*;
import java.util.concurrent.*;

public class TorrentClient {
    private static final String TRACKER_ADDRESS = "localhost";
    private static final int TRACKER_PORT = 5000;
    private static final String OUTPUT_FILE = "downloaded_file.txt";

    public void start() throws IOException, ClassNotFoundException {
        try (Socket trackerSocket = new Socket(TRACKER_ADDRESS, TRACKER_PORT);
             ObjectOutputStream trackerOut = new ObjectOutputStream(trackerSocket.getOutputStream());
             ObjectInputStream trackerIn = new ObjectInputStream(trackerSocket.getInputStream())) {

            // Obtener la lista de nodos y sus fragmentos
            trackerOut.writeObject("GET_PEERS");
            Map<String, Set<Integer>> peerChunks = (Map<String, Set<Integer>>) trackerIn.readObject();

            List<Integer> allChunks = new ArrayList<>(peerChunks.values().stream()
                    .flatMap(Set::stream)
                    .distinct()
                    .toList());

            System.out.println("Total chunks to download: " + allChunks.size());

            // Descargar fragmentos concurrentemente usando hilos virtuales
            Map<Integer, byte[]> downloadedChunks = Collections.synchronizedMap(new HashMap<>());

            try (ExecutorService executor = Executors.newVirtualThreadPerTaskExecutor()) {
                for (int chunkIndex : allChunks) {
                    executor.submit(() -> downloadChunk(chunkIndex, peerChunks, downloadedChunks));
                }
            }

            // Reensamblar el archivo
            reassembleFile(downloadedChunks, allChunks.size());
        }
    }

    private void downloadChunk(int chunkIndex, Map<String, Set<Integer>> peerChunks, Map<Integer, byte[]> downloadedChunks) {
        for (Map.Entry<String, Set<Integer>> entry : peerChunks.entrySet()) {
            if (entry.getValue().contains(chunkIndex)) {
                String peerId = entry.getKey();
                try (Socket peerSocket = new Socket(peerId, 6000);
                     ObjectOutputStream out = new ObjectOutputStream(peerSocket.getOutputStream());
                     ObjectInputStream in = new ObjectInputStream(peerSocket.getInputStream())) {

                    out.writeObject("GET_CHUNK");
                    out.writeInt(chunkIndex);

                    byte[] chunk = (byte[]) in.readObject();
                    if (chunk != null) {
                        downloadedChunks.put(chunkIndex, chunk);
                        System.out.println("Downloaded chunk " + chunkIndex + " from " + peerId);
                        break;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void reassembleFile(Map<Integer, byte[]> downloadedChunks, int totalChunks) throws IOException {
        try (OutputStream outputStream = new FileOutputStream(OUTPUT_FILE)) {
            for (int i = 0; i < totalChunks; i++) {
                byte[] chunk = downloadedChunks.get(i);
                if (chunk != null) {
                    outputStream.write(chunk);
                }
            }
        }
        System.out.println("File reassembled successfully.");
    }

    public static void main(String[] args) throws IOException, ClassNotFoundException {
        new TorrentClient().start();
    }
}
```

---

### **Cómo Funciona**
1. **Servidor Central**:
   - Divide el archivo en fragmentos y asigna fragmentos a nodos.
   - Actúa como un tracker para informar a los clientes sobre qué nodos tienen qué fragmentos.

2. **Nodos**:
   - Almacenan fragmentos en disco.
   - Comparten fragmentos con otros nodos cuando se les solicita.

3. **Cliente**:
   - Consulta al tracker para obtener la lista de nodos y sus fragmentos.
   - Descarga fragmentos de múltiples nodos concurrentemente.
   - Reensambla el archivo completo.

---

### **Características Adicionales**
- **Persistencia**: Los fragmentos se almacenan en disco para evitar perder datos si el nodo se reinicia.
- **Escalabilidad**: Puedes agregar más nodos para mejorar la distribución y la velocidad de descarga.
- **Robustez**: Implementa reintentos y manejo de errores para mejorar la confiabilidad.

Este diseño proporciona una base sólida para un sistema de torrent distribuido con almacenamiento en disco y comunicación P2P.