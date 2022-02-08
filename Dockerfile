# Build stage
#
FROM maven:3.6.0-jdk-11-slim AS build
COPY src /home/xbot/src
COPY pom.xml /home/xbot
RUN mvn -f /home/xbot/pom.xml clean install

#
# Package stage
#
FROM openjdk:11-jre-slim
COPY --from=build /home/xbot/target/telegramxbot-1.4.1.jar /usr/local/lib/telegramxbot-1.4.1.jar
ENTRYPOINT ["java","-jar","/usr/local/lib/telegramxbot-1.4.1.jar"]
