FROM openjdk:8-jre
ADD target/external-adaptive-pricing-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8050
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/app.jar"]
