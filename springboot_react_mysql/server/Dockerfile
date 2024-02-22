ARG MAVEN_VERSION=3.9
ARG JAVA_VERSION=21

FROM maven:${MAVEN_VERSION}-eclipse-temurin-${JAVA_VERSION} AS builder

WORKDIR /springboot_react_mysql/server
COPY pom.xml /springboot_react_mysql/server/pom.xml
RUN mvn dependency:go-offline

COPY src /springboot_react_mysql/server/src
RUN mvn install -DskipTests

FROM builder as dev-envs

RUN apt-get update && apt-get install -y git
RUN useradd -s /bin/bash -m vscode && groupadd docker && usermod -aG docker vscode

# install Docker tools (cli, buildx, compose)
COPY --from=gloursdocker/docker / /
CMD ["mvn", "spring-boot:run"]

# FROM builder as prepare-production
# RUN mkdir -p target/dependency
# WORKDIR /springboot_react_mysql/server/target/dependency
# RUN jar -xf ../*.jar

# FROM eclipse-temurin:17-jre-focal

# EXPOSE 80
# VOLUME /tmp
# ARG DEPENDENCY=/springboot_react_mysql/server/target/dependency
# COPY --from=prepare-production ${DEPENDENCY}/BOOT-INF/lib /app/lib
# COPY --from=prepare-production ${DEPENDENCY}/META-INF /app/META-INF
# COPY --from=prepare-production ${DEPENDENCY}/BOOT-INF/classes /app
# ENTRYPOINT ["java","-cp","app:app/lib/*"]