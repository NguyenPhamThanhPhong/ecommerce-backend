# Use the official OpenJDK image to build the app
FROM openjdk:21-jdk-slim as builder

# Set the working directory
WORKDIR /app

# Copy the Maven Wrapper files (mvnw and mvnw.cmd) and the pom.xml
COPY mvnw .
COPY mvnw.cmd .
COPY .mvn .mvn
COPY pom.xml .

# Give execution rights to the wrapper
RUN chmod +x mvnw

# Download dependencies using the Maven Wrapper
RUN ./mvnw dependency:go-offline

# Copy the source code
COPY src ./src

# Build the application using Maven Wrapper
RUN ./mvnw clean install -DskipTests

# Use a smaller base image to run the app
FROM openjdk:21-jdk-slim

# Set the working directory
WORKDIR /app

# Copy the jar file from the builder image
COPY --from=builder /app/target/*.jar app.jar

# Expose the port the app will run on
EXPOSE 8081

# Command to run the app
ENTRYPOINT ["java", "-jar", "app.jar"]
