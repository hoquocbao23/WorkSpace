FROM maven:3.9.9-openjdk-17 AS build
COPY . .
RUN mvn clean package

FROM openjdk:17-alpine
COPY --from=build /target/workspace-0.0.1-SNAPSHOT.jar workspace.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","workspace.jar" ]