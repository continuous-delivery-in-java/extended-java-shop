FROM openjdk:8-jre
ADD target/featureflags-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8040
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/app.jar"]
