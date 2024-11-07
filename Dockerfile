# Use OpenJDK as the base image
FROM openjdk:17-jdk-slim

# Set the working directory in the container to /app
WORKDIR /app

# Copy the executable JAR file to the container's /app directory
COPY target/ProjApp-0.0.1-SNAPSHOT.jar ProjApp.jar

# Run the JAR file
ENTRYPOINT ["java", "-jar", "ProjApp.jar"]