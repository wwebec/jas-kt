FROM openjdk:8-jre-alpine

ENV SPRING_OUTPUT_ANSI_ENABLED=ALWAYS \
    JHIPSTER_SLEEP=0 \
    JAVA_OPTS="" \
    TMP=/tmp/spring-flow/logs

# Download spring cloud data flow server
RUN wget -O app.jar http://repo.spring.io/release/org/springframework/cloud/spring-cloud-skipper-server/1.0.0.RELEASE/spring-cloud-skipper-server-1.0.0.RELEASE.jar

# Port range for streams
EXPOSE 7577

# Run application
CMD echo "The application will start in ${JHIPSTER_SLEEP}s..." && \
    sleep ${JHIPSTER_SLEEP} && \
    java ${JAVA_OPTS} -Djava.security.egd=file:/dev/./urandom -jar /app.jar
