



* Arquitectura

Cliente: Envia el archivo
         Archivo <25mb> 

Server<Master>: 
       * Recibe el archivo
       * Divide el archivo en chunks
       * Verifica los nodos disponibles de esa rama y espacio libre
       * Envia los chunks a esos nodos y actualiza el Directory Services Federated
       
       Nodo :
            * Recibe el chunk enviado a esa rama
            * Si tiene espacio lo guarda
            * Actualiza su Directory Services Federated
            * Envia un mensaje de confirmacion al Server<Master> de su rama.



Proyectos:
 * archivos: Muestra como dividir un archivo en chunks y luego volver a unirlos.
