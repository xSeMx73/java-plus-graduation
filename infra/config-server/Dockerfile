FROM amazoncorretto:21-alpine-jdk
VOLUME /tmp
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} config-service.jar
ENTRYPOINT ["sh", "-c", "java ${JAVA_OPTS} -jar /config-service.jar"]