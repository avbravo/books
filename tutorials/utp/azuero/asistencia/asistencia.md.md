# 40.1 Asistencia
Para jmoordb tiene el problema con las librerias de jaspertReport que fueron removidas
Pasos:
1. Descargar el jar desde maven central
2. Agregar el jar a maven mediante
'''
mvn install:install-file -DgroupId=net.sf.jasperreports -DartifactId=jasperreports -Dversion=5.5.0  -Dpackaging=jar -DgeneratePom=true -Dfile=jasperreports-5.5.0.jar

  <dependency>
            <groupId>net.sf.jasperreports</groupId>
            <artifactId>jasperreports</artifactId>
            <version>5.5.0</version>
        </dependency>

'''
