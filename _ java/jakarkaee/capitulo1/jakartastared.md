# Jakarta EE starter
Mi  ejemplo con Jakarta 9.1, PayaraMicro y Microprofile
https://github.com/avbravo/microjakartanosql


https://start.jakarta.ee/




Generate Jakarta EE Project
In order to run the Maven Archetype and generate a sample Jakarta EE project, please execute the following. Please ensure you have installed a Java SE 8+ implementation and Maven 3+ (we have tested with Java SE 8, Java SE 11 and Java SE 17).

mvn archetype:generate -DarchetypeGroupId="org.eclipse.starter" -DarchetypeArtifactId="jakarta-starter" -DarchetypeVersion="1.0.0"
If desired, you can easily use the Maven Archetype from a Maven capable IDE such as Eclipse.

If you use the defaults, this will generate the Jakarta EE project under a directory named jakartaee-cafe. You can then run the project by executing the following command from the jakartaee-cafe directory. Please ensure you have installed a Java SE 8+ implementation and Maven 3+ (we have tested with Java SE 8, Java SE 11 and Java SE 17).

mvn clean package payara-micro:start
Once Payara Micro starts, you can access the project at http://localhost:8080.

You can also run the project via Docker. To build the Docker image, execute the following commands from the jakartaee-cafe directory. Please ensure you have installed a Java SE 8+ implementation, Maven 3+ and Docker (we have tested with Java SE 8, Java SE 11 and Java SE 17).

mvn clean package
docker build -t jakartaee-cafe:v1 .
You can then run the Docker image by executing:

docker run -it --rm -p 8080:8080 jakartaee-cafe:v1
Once Payara starts, you can access the project at http://localhost:8080/jakartaee-cafe.

The generated starter code is simply a Maven project. You can easily load, explore and run the code in a Maven capable IDE such as Eclipse.

We hope you enjoy your Jakarta EE journey!

