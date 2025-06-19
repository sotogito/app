FROM openjdk:17

LABEL maintainer = "sotogito <sotogitoarchive@gamil.com"
LABEL version = "1.0.0"

ARG JAR_FILE_PATH=build/libs/*.jar
COPY ${JAR_FILE_PATH} app.jar

ENTRYPOINT ["java","-jar", "app.jar"]