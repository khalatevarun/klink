# Use a Maven image with JDK 23 for building the application
FROM eclipse-temurin:23-jdk as build

# Set the working directory in the container
WORKDIR /app

# Copy the Maven wrapper and pom.xml to the container
COPY mvnw ./
COPY .mvn/ .mvn/

# Make the Maven wrapper executable
RUN chmod +x mvnw


# Copy the Maven project files to the container
COPY pom.xml ./
RUN ./mvnw dependency:go-offline

COPY src ./src
RUN ./mvnw clean package -DskipTests

# Use a Java 23 runtime image for running the application
FROM eclipse-temurin:23-jre

# Set the working directory in the container
WORKDIR /app

# Copy the built application from the build stage
COPY --from=build /app/target/*.jar app.jar

# Expose the application port
EXPOSE 8080

# Set the command to run the application
CMD ["java", "-jar", "app.jar"]