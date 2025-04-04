# build
#
FROM maven:3.9.9-amazoncorretto-21 AS build

WORKDIR /app
COPY pom.xml .
COPY src ./src

RUN mvn package -DskipTests

#create images
FROM amazoncorretto:21.0.6


WORKDIR /app
COPY --from=build /app/target/*.jar app.jar
ENTRYPOINT ["java" ,"-jar", "app.jar"]


## test
#FROM openjdk:21
#WORKDIR /app
#
#COPY .mvn/ .mvn
#COPY mvnw pom.xml ./
#
#RUN ./mvnw dependency:go-offline
#COPY src ./src
#
#CMD ["./mvnw", "spring-boot:run"]