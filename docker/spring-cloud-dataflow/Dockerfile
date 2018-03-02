FROM openjdk:8-jre-alpine

ENV SPRING_OUTPUT_ANSI_ENABLED=ALWAYS \
    JHIPSTER_SLEEP=0 \
    JAVA_OPTS="" \
    TMP=/tmp/spring-flow/logs

# Download spring cloud data flow server
RUN wget -O app.jar http://repo.spring.io/release/org/springframework/cloud/spring-cloud-dataflow-server-local/1.3.0.RELEASE/spring-cloud-dataflow-server-local-1.3.0.RELEASE.jar

# Path for logs
RUN mkdir -p ${TMP}

VOLUME ${TMP}

# Port range for streams
EXPOSE 7000-7010 9393

# Run application
CMD echo "The application will start in ${JHIPSTER_SLEEP}s..." && \
    sleep ${JHIPSTER_SLEEP} && \
    java ${JAVA_OPTS} -Djava.security.egd=file:/dev/./urandom -Djava.io.tmpdir=${TMP} -jar /app.jar \
#    --spring.cloud.dataflow.applicationProperties.stream.spring.cloud.stream.kafka.binder.brokers=kafka:9092 \
#    --spring.cloud.dataflow.applicationProperties.stream.spring.cloud.stream.kafka.binder.zknodes=zookeeper:2181
    --spring.cloud.deployer.local.envVarsToInherit=TMP,LANG,LANGUAGE,"LC_.*.","SPRING_CLOUD_STREAM_KAFKA.*" \
    --logging.level.org.springframework.cloud.deployer=TRACE
