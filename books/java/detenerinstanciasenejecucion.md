# Detener instancias en ejecucion

Para saber qué aplicaciones o procesos de Java (`.jar`, servidores de aplicaciones como Tomcat, Glassfish, etc.) se están ejecutando en Linux, tienes varias herramientas a tu disposición ordenadas de la más rápida a la más detallada.

### 1. Usar `jps` (Java Virtual Machine Process Status Tool)

Es la herramienta nativa del kit de desarrollo de Java (JDK) diseñada específicamente para esto. Muestra los procesos de la máquina virtual de Java (JVM) junto con su identificador de proceso (PID) y el nombre de la clase principal o archivo JAR.

Ejecuta el siguiente comando en la terminal:

```bash
jps -l

```

* **Qué hace:** El parámetro `-l` muestra la ruta completa del paquete o del archivo `.jar` en ejecución.
* *Verificación:* Si la salida muestra una lista de PIDs seguidos de nombres de clases o rutas, sabrás exactamente qué procesos Java están activos. *(Nota: Si el comando no se encuentra, es posible que solo tengas instalado el JRE en lugar del JDK, o que necesites instalar `openjdk-*-jdk`).*

---

### 2. Usar `ps` junto con `grep` para buscar procesos Java

Si no tienes `jps` instalado o quieres ver más detalles a nivel de sistema operativo (como el usuario que los ejecuta, consumo de recursos o los argumentos completos con los que se inició la aplicación), puedes filtrar los procesos del sistema.

Ejecuta el siguiente comando:

```bash
ps aux | grep java

```

* **Qué hace:** Filtra la lista de todos los procesos en ejecución (`ps aux`) buscando la palabra `java`.
* *Verificación:* Verás las líneas que contienen `java` junto con el comando completo de arranque, incluyendo las propiedades del sistema y la memoria asignada (`-Xmx`, `-Xms`). Ignora la línea que dice `grep java` (es el propio comando que acabas de lanzar).

---

### 3. Ver los puertos en los que escuchan las aplicaciones Java

Muchas aplicaciones Java son servidores web o APIs que operan en puertos específicos (como el `8080` de Tomcat o Spring Boot). Puedes ver qué proceso Java está usando qué puerto con `netstat` o `ss`:

```bash
sudo ss -tulpn | grep java

```

* **Qué hace:** Muestra los puertos TCP/UDP abiertos y qué PID de Java los está controlando.
* *Verificación:* Podrás identificar el puerto exacto (por ejemplo, `:::8080`) y asociarlo con el PID de la aplicación Java correspondiente.

---

Para supervisar el consumo de memoria RAM y CPU de los procesos de Java de forma detallada en Linux, puedes utilizar las herramientas nativas del sistema operativo, optimizadas para mostrar métricas en tiempo real.

### 1. Usar `top` o `htop` filtrando por Java

Las herramientas interactivas por excelencia del sistema te permiten ver el porcentaje exacto de CPU y memoria (`%MEM`, `%CPU`, RSS y VSZ) que consume cada hilo y proceso de la JVM.

Ejecuta el siguiente comando en la terminal:

* **Con `top`:**
```bash
top -p $(pgrep -d ',' -f java)

```


* **Qué hace:** Lanza `top` restringiendo la vista únicamente a los PIDs devueltos por `pgrep -f java`.
* *Verificación:* Verás una lista dinámica en tiempo real que se actualiza cada pocos segundos, mostrando el uso de CPU (`%CPU`) y memoria RAM (`%MEM`) de cada instancia de Java. Presiona `q` para salir.


* **Con `htop` (más visual y recomendado):**
Si tienes `htop` instalado, puedes filtrar directamente presionando la tecla `F4` y escribiendo `java`.
* *Verificación:* La interfaz resaltará todos los procesos Java activos junto con barras gráficas de consumo de CPU y memoria RAM.



---

### 2. Usar `ps` para obtener un reporte rápido de recursos

Si prefieres un listado estático y rápido en formato de texto (ideal para scripts o análisis rápido), puedes usar el comando `ps` ordenado por consumo:

```bash
ps -eo pid,ppid,%cpu,%mem,comm,args --sort=-%mem | grep java

```

* **Qué hace:** Muestra el PID, porcentaje de CPU, porcentaje de memoria RAM y los argumentos de arranque de todos los procesos que contengan `java`, ordenados de mayor a menor consumo de memoria.
* *Verificación:* La salida te entregará una tabla clara con el porcentaje exacto de RAM (`%MEM`) que cada aplicación Java está acaparando en tu sistema.

---

### 3. Monitoreo avanzado a nivel de JVM con `jcmd`

Si necesitas saber exactamente cómo se está distribuyendo esa memoria RAM *internamente* dentro de la máquina virtual de Java (Heap, metaspace, off-heap, hilos), puedes usar la herramienta nativa del JDK:

```bash
jcmd <PID> VM.native_memory summary

```

* **Qué hace:** Muestra un desglose completo del uso de memoria nativa y asignaciones de la JVM para un proceso específico (reemplaza `<PID>` por el identificador numérico del proceso obtenido con `jps`).
* *Verificación:* Obtendrás un reporte detallado que separa la memoria reservada frente a la memoria comprometida por la aplicación Java.

---

Matar el proceso
sudo kill -9 
$(sudo lsof -t -i:8080)
