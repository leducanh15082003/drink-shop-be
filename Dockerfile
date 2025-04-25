FROM maven:3.8.5-openjdk-17 as BUILD
COPY . .
RUN mvn clean package -DskipTests

FROM eclipse-temurin:21-jdk
COPY --from=build target/htc-0.0.1-SNAPSHOT.jar htc.jar
EXPOSE 8080
CMD [ "java", "-jar", "htc.jar" ]