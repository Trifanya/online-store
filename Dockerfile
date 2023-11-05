FROM openjdk:20-jdk-slim
COPY target/*.jar online-store-1.0.0.jar
ENTRYPOINT [ "java", "-jar", "online-store-1.0.0.jar" ]
#WORKDIR
# Приложение в контейнере использует порт контейнера 8080
#EXPOSE 8080
