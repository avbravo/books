# JMeter


## Testing de Microservicios con Autentificacion Basica


Deseamos conectarnos a un endpoint

```

http://localhost:9002/cancerdetectorserver/api/applicative

```
Que tiene las soguientes credenciales
* username  iJYxBAmcCfr016hVry8MCKS0xA4S6SExizJM442E+18=
* password  2tbHQCWpe69CvNbAAmCXcJxTFEQtjRxXNA5yIYz7lbE=

Pasos:
1. Crear un plan Nuevo

![](images/threadgroup.png)

   
2. Cree un http request

![](images/httprequest.png)


3. Configurar

![](images/httprequestconf.png)

* Http Request: GET
* Path: http://localhost:9002/cancerdetectorserver/api/analisis/lookup

Agregar los parametros:

filter  {"user.iduser": {"$eq": 8}}

sort {"fecha": -1}

page  1

size 1

nhc  3.0


4. Ubiquese en Http Request y seleccione Add --> Listener --> View Result

![](images/viewresult.png)

5. Ubiquese en Tread Group Selecciona Add --> Config Element --> Http Header Manager

![](images/httpmanager.png)


6. Agregue los parametros

![](images/httpparameter.png)

Base URL : http://localhost:9002/cancerdetectorserver/api/

Username: iJYxBAmcCfr016hVry8MCKS0xA4S6SExizJM442E+18=

Password: 2tbHQCWpe69CvNbAAmCXcJxTFEQtjRxXNA5yIYz7lbE=

Mechanism: BASIC_DIGEST

7. Ejecute mediante start

![](images/start.png)


8. Puede ver el resultado

![](images/result.png)

9. Modifique las peticiones

![](images/configure.png)


10. Agregue un Receptor --> Informe Agregado

![](images/receptorInformeagregado.png)

Se muestra el informe



![](images/dialogoreceptorinformeagregado.png)


11. Mueva al top Http Autorization Manager

![](iamges/top.png)

12. Modifique el Tread

![](images/modifique.png)

Number Of Thread (Users): 500

Ram-up period (seconds): 5

Loop Count: 5000


13. Ejecutar

![](ejecutar.png)

puede ver los resultados

![](images/result_viewresult.png)

Resumen

![](images/result_reporteresumen.png)

Grafico

![](images/grafico.png)