# Použitie oficiálneho Java 17 runtime obrazu
FROM eclipse-temurin:17-jdk

# Nastavenie pracovného adresára
WORKDIR /app

# Skopíruj iba potrebné súbory pre Gradle build
COPY build.gradle settings.gradle gradlew* ./
COPY src ./src
COPY .mvn .mvn

# Uisti sa, že `gradlew` má správne oprávnenia
RUN chmod +x ./gradlew

# Spusti Gradle build a vytvor JAR
RUN ./gradlew bootJar --no-daemon

# Expose port pre Render
EXPOSE 8080

# Nastav príkaz na spustenie aplikácie
CMD ["sh", "-c", "java -jar build/libs/mye-liquid-api-0.0.1-SNAPSHOT.jar --server.port=${PORT:-8080}"]
