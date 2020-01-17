FROM maven:3.6.3-jdk-11 AS MAVEN_BUILD

MAINTAINER Daniel Correa

COPY pom.xml /build/

COPY src /build/src/

WORKDIR /build/

RUN mvn package

FROM tomcat:9.0

COPY --from=MAVEN_BUILD /build/target/VariaMosServices.war /usr/local/tomcat/webapps/