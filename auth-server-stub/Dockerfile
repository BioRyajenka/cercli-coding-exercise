FROM gradle:8.4-jdk11 AS build
WORKDIR /home/gradle/src
COPY --chown=gradle:gradle build.gradle.kts gradle.properties settings.gradle.kts ./
COPY --chown=gradle:gradle core-auth/ ./core-auth
COPY --chown=gradle:gradle auth-server-stub/ ./auth-server-stub
RUN gradle buildFatJar --no-daemon

FROM openjdk:11-jdk-slim
COPY --from=build /home/gradle/src/auth-server-stub/build/libs/fat.jar /app/app.jar
WORKDIR /app
CMD ["java", "-jar", "app.jar"]
