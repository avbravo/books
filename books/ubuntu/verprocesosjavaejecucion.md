# VER PROCESOS EN JAVA EJECUCION

Para ver los procesos de Java activos en Ubuntu, usa `jps` (del JDK) para identificar fácilmente aplicaciones Java y sus argumentos, o `ps` y `pgrep` para listar cualquier proceso `java` en ejecución aunque no tengas el JDK en el `PATH`.

1. **Listar procesos Java con jps:** 30 s.
Ejecuta `jps -lvm` en la terminal para ver el PID, la clase principal o el JAR y los argumentos de la JVM.
*Verification:* Aparece una lista con el ID numérico del proceso (PID) y el nombre del JAR, clase o `Jps` en ejecución.


2. **Verificar con ps o pgrep si jps no muestra alguno:** 30 s.
Ejecuta `ps -ef | grep '[j]ava'` o `pgrep -af java` para detectar procesos Java que corran bajo otro usuario o con rutas distintas.
*Verification:* Se muestran las líneas de comando completas de los procesos `java` activos, incluyendo el usuario propietario y el PID.


3. **Revisar puertos y consumo del proceso:** 1 min.
Si necesitas identificar qué proceso escucha en un puerto o consume más recursos, usa `ss -tlpn | grep <PID>` o `top -p <PID>`.
*Verification:* La salida confirma el puerto TCP/UDP asociado al PID o muestra el porcentaje de CPU y memoria del proceso.


### Referencia rápida de comandos

| Comando | Uso principal |
| --- | --- |
| `jps -l` | Muestra PID y nombre completo del JAR o clase principal. |
| `jps -lvm` | Añade argumentos de la JVM (`-Xmx`, `-XX:...`, propiedades del sistema). |
| `ps -eo pid,user,%cpu,%mem,cmd --sort=-%mem | grep '[j]ava'` | Ordena procesos Java por consumo de memoria. |
| `pgrep -a java` | Lista de forma compacta el PID y la línea de comando de cada instancia `java`. |