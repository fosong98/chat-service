FROM adoptopenjdk/openjdk11
ENTRYPOINT ["java", "-jar", "build/libs/chat-service-0.0.1-SNAPSHOT.jar"]