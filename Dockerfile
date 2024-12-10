# Použitie oficiálneho Java 17 runtime obrazu
FROM eclipse-temurin:17-jdk

# Nastavenie pracovného adresára
WORKDIR /app

# Skopíruj súbory projektu do kontajnera
COPY . .

# Spusti Gradle build a vytvor JAR
RUN ./gradlew bootJar

# Nastav príkaz na spustenie aplikácie
CMD ["java", "-jar", "build/libs/mye-liquid-api-0.0.1-SNAPSHOT.jar"]
