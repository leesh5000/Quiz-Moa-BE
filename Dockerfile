FROM amazoncorretto:17-alpine AS builder

COPY gradlew .
COPY settings.gradle .
COPY build.gradle .
COPY gradle gradle
COPY src src
RUN chmod +x ./gradlew
RUN ./gradlew clean build

FROM amazoncorretto:17-alpine
RUN mkdir /app
COPY --from=builder /build/libs/*.jar /app/quiz-api-server.jar
EXPOSE 8080
ENV PROFILE prod
ENV JASYPT_ENCRYPTOR_PASSWORD ""

ENTRYPOINT ["java", "-jar", "-Dspring.profiles.active=${PROFILE}", "-Djasypt.encryptor.password=${JASYPT_ENCRYPTOR_PASSWORD}", "/app/quiz-api-server.jar"]