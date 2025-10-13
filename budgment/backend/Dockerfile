FROM gradle:jdk23-corretto AS build
WORKDIR /backend
COPY . .
ARG JWT_SECRET
ENV JWT_SECRET=$JWT_SECRET
RUN gradle build --no-daemon -x test

FROM eclipse-temurin:23.0.2_7-jre-ubi9-minimal
WORKDIR /app
COPY --from=build /backend/build/libs/*.jar app.jar
COPY --from=build /backend/data /app/data
COPY entrypoint.sh /app/entrypoint.sh
EXPOSE 8080
RUN chmod +x /app/entrypoint.sh
ENTRYPOINT ["/app/entrypoint.sh"]