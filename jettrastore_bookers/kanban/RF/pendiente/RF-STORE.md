# PROGRESO# Contexto y Objetivo



Generar imagen docker

[0] En JettraStore cuando se detiene el nodo PRIMARY envia a consola el mensaje [broadcastFrameWithQuorum] ⚡ Consenso Dinámico ajustado: 2 de 3 nodos configurados disponibles. Acks: 2/2 (quórum requerido: 2, resultado: true)
[JettraStoreServer] 👑 ¡NODO LOCAL 'node-02' PROMOVIDO A NUEVO PRIMARY TRAS CAÍDA DE 'node-01'!

lo que es correcto , pero al ejecutar desde JettraShell el comando cluster-distributed info

=======================================================================================================================
                                   JETTRASTORE CLUSTER DATABASE DISTRIBUTION INFO                                       
========================================================================================================================
+----------+----------------------+-------+-----------+----------+---------------+--------------------------------------+
| Nodo ID  | Dirección IP         | Puerto| Rol       | Estado   | Cantidad BDs  | Bases de Datos                       |
+----------+----------------------+-------+-----------+----------+---------------+--------------------------------------+
| node-01  | 127.0.0.2            | 9091  | PRIMARY   | RUNNING  | 2             | default_db, mydb                     |
| node-02  | 127.0.0.3            | 9092  | SECONDARY | RUNNING  | 2             | default_db, mydb                     |
| node-03  | 127.0.0.4            | 9093  | SECONDARY | RUNNING  | 0             | (0 bases de datos)                   |

pero muestra como el nodo-01 este aun activo y lo sigue marcando como PRIMARY, debes corregis esto


1). En JettraStorePolice3D en la ventana MUNDO INTERIOR: SERVIDOR hay un seccion en la esquina siperior izquierda
con la etiqueta HOST debe mostrar el IP con que este ejecutando el nodo.


---

3. En JettraSToreShell al presionar la flecha hacia arriba muestra el comando anterior y al presionar la flecha siguiente muestra el siguiente comando ejecutado.

4, Añadir a JettraStoreShell los comandos de backup y restore y añadelos al comando help tamben

actualiza la documentacion de JettraStoreShell/guide/book.md



---

    0. en JettraStorePolice3D Inicia con la configuracion de conexion debe Añadir los nodos con informacion de jettra.config

    1. Cuando se ejecuta java -jar JettraStoreShell, se muestra las conexiones, 

    ================================================================================
                   JETTRASTORE INTERACTIVE DISTRIBUTED SHELL (JAVA 25+)
    ================================================================================
    =========================== MENÚ DE CONEXIONES =================================
    Seleccione una conexión para iniciar:
      [1] local_master         (127.0.0.1:9091 - Usuario: admin)  [Predeterminado]
      [2] node-02-replica      (127.0.0.1:9092 - Usuario: admin)
      [3] node-03-replica      (127.0.0.1:9093 - Usuario: admin)
      [4] node-1               (127.0.0.2:9091 - Usuario: admin)
      [5] node-2               (127.0.0.3:9091 - Usuario: admin)
      [N] Conexión nueva (donde el usuario ejecuta el connect)
      [0] Salir del Shell
    ================================================================================

    estas se deben tener como referencia inicial las configuraciones de jettra.config en lugar de las que muestra actualmente.


    2. Revisar JettraStore el algoritmo Raft no esta funcionando no distribuye a los demas nodos las bases de datos y sus contenidos de manera automatica, es decir al crear una base de datos se debe distribuir a los demas nodos, si se crea un engine dentro de la base de datos se debe distribuir a los demas nodos, si crean registros o se hacen operaciones sobre ellos se deben distribuir de manera automatica a los demas nodos.

    3. En JettraStoreShell el comando cluster live (debe mostrar estos procesos de transferencia)

    4. En JettraStoreDriver el comando cluster live (debe mostrar estos procesos de transferencia)



