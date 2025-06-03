FROM eclipse-temurin:17-jdk-jammy as builder
WORKDIR /app
COPY pom.xml .
COPY mvnw .
COPY .mvn .mvn
RUN ./mvnw dependency:go-offline
COPY src src
RUN ./mvnw clean package -DskipTests

FROM eclipse-temurin:17-jre-jammy
WORKDIR /app
COPY --from=builder /app/target/*.jar app.jar
ENV JAVA_OPTS="-XX:+UseContainerSupport -XX:MaxRAMPercentage=75 -XX:+HeapDumpOnOutOfMemoryError -XX:HeapDumpPath=/heap-dump.hprof"
RUN addgroup --system spring && \
    adduser --system --ingroup spring spring && \
    chown spring:spring /app
USER spring:spring
ENTRYPOINT ["sh", "-c", "java ${JAVA_OPTS} -jar app.jar"]