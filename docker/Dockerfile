FROM openjdk:17-jdk-alpine

LABEL authors = "SamoilovDev" \
        maintainer = "vladimir.samoilov.dev@gmail.com" \
        version = "1.0-RELEASE" \
        description = "Antifraud API"

WORKDIR /antifraud

COPY ../build/libs/antifraud-system-1.0-RELEASE.jar /antifraud/spring-boot-app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "/antifraud/spring-boot-app.jar"]