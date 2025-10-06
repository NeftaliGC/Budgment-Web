FROM gradle:jdk23-corretto AS build
WORKDIR /backend
COPY . .
RUN gradle build --no-daemon

FROM eclipse-temurin:23.0.2_7-jre-ubi9-minimal
WORKDIR /app
COPY --from=build /backend/build/libs/*.jar app.jar
COPY entrypoint.sh /app/entrypoint.sh
RUN chmod +x /app/entrypoint.sh
ENTRYPOINT ["/app/entrypoint.sh"]