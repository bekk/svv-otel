FROM eclipse-temurin:17-jdk
VOLUME /tmp
COPY build/libs/*SNAPSHOT.jar app.jar
COPY ./OpenTelemetry-Java-Agent.jar .
COPY ./config.properties .
CMD ["java", "-javaagent:./OpenTelemetry-Java-Agent.jar", "-Dotel.javaagent.configuration-file=./config.properties", "-jar", "./app.jar"]
