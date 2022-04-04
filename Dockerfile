# Build stage
#
FROM maven:3.8.1-openjdk-17-slim AS build
COPY src /home/xbot/src
COPY pom.xml /home/xbot
RUN mvn -f /home/xbot/pom.xml clean install

#
# Package stage
#
FROM openjdk:17-alpine
COPY --from=build /home/xbot/target/telegramxbot*.jar /usr/local/lib/telegramxbot.jar
ENTRYPOINT ["java","-jar","/usr/local/lib/telegramxbot.jar"]
