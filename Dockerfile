FROM maven:3.8.6-openjdk:11

COPY . .

RUN mvn build

ENTRYPOINT ["java", "-jar", "/Homebanking-0.0.1-SNAPSHOT.jar"]
