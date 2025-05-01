FROM eclipse-temurin:21-jdk AS builder
WORKDIR /app
COPY . .
RUN ./mvnw clean package -DskipTests

FROM eclipse-temurin:21-jre AS runner
WORKDIR /app
COPY --from=builder /app/target/*.jar marketing.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "marketing.jar"]
