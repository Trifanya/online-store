FROM openjdk:20-jdk-slim
COPY target/*.jar online-store-1.0.0.jar
ENTRYPOINT [ "java", "-jar", "online-store-1.0.0.jar" ]
