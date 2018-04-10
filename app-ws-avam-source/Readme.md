# AVAM Import Web Service Integration

This app provides an Web Service endpoint and publish the data into the `jobad.action` message queue.

### Spring Profiles:
- avam-wsdl-v1: AVAM Web Service Version 1 (Jobroom 1)
- avam-wsdl-v2: AVAM Web Service Version 2 (Jobroom 2, with reportingObligation) **default**

### Deployment to Spring Cloud DataFlow

##### [REST API](https://docs.spring.io/spring-cloud-dataflow/docs/current/reference/htmlsingle/#api-guide-resources-index) 

```bash
# Register 'App'
curl 'http://dev.job-room.ch:9393/apps/source/wsavam' -i -X POST \
  -d 'force=true&uri=maven%3A%2F%2Fch.admin.seco.jobs.services.jobadservice%3Aapp-ws-avam-source%3A<version>'

# Delete existing stream
curl http://dev.job-room.ch:9393/streams/definitions/ws-avam-import -i -X DELETE

# Create stream and deploy
curl http://dev.job-room.ch:9393/streams/definitions -X POST \
  -d 'name=ws-avam-import&deploy=true&definition=wsavam --server.port=20000 > :jobad.action'
```

##### Dashboard

[Register app](http://dev.job-room.ch:9393/dashboard/#/apps):

- `Register Application(s)`
  - name = wsavam
  - type = source
  - App URI = `maven://ch.admin.seco.jobs.services.jobadservice:app-ws-avam-source:jar:<version>`    
  - `Register` 

[Create Stream](http://dev.job-room.ch:9393/dashboard/#/streams/definitions)
- `Create Stream`
```bash
wsavam --server.port=20000 --spring.profiles.active=avam-wsdl-v1 > :jobad.action
```
- `Create Stream`
- Stream Name = ws-avam-import
- Deploy Stream(s) = true
- `OK`

### Deployment from command line
Run **ws-avam-source** as seperate task from command-line:

> TODO: Add SSL properties for https support

```bash
curl https://alvch.jfrog.io/alvch/global/ch/admin/seco/jobs/services/jobadservice/app-ws-avam-source/<version>/app-ws-avam-source-<version>.jar

java -jar app-ws-avam-source-<version>.jar \
  --spring.profiles.active=<avam-interface-version: avam-wsdl-v1,avam-wsdl-v2>
  --spring.cloud.dataflow.stream.app.label=ws-avam \
  --spring.cloud.dataflow.stream.app.type=source \
  --spring.cloud.dataflow.stream.name=ws-avam-import \
  --spring.cloud.stream.bindings.output.destination=jobad.action \ 
  --spring.cloud.stream.kafka.binder.brokers=<kafka-hostname>:9092 \ 
  --spring.cloud.stream.kafka.binder.zk-nodes=<zookeeper-hostname>:2181 \
  --spring.cloud.application.group=ws-avam-import \
  --server.port=20000  
```

**Example**: Run in _development_ environment

```bash
curl https://alvch.jfrog.io/alvch/global/ch/admin/seco/jobs/services/jobadservice/app-ws-avam-source/<version>/app-ws-avam-source-<version>.jar

java -jar app-ws-avam-source-<version>.jar \
  --spring.profiles.active=<avam-interface-version: avam-wsdl-v1,avam-wsdl-v2>
  --spring.cloud.dataflow.stream.app.label=ws-avam \
  --spring.cloud.dataflow.stream.app.type=source \
  --spring.cloud.dataflow.stream.name=ws-avam-import \
  --spring.cloud.stream.bindings.output.destination=jobad.action \ 
  --spring.cloud.stream.kafka.binder.brokers=dev.job-room.ch:9092 \ 
  --spring.cloud.stream.kafka.binder.zk-nodes=dev.job-room.ch:2181 \
  --spring.cloud.application.group=ws-avam-import \
  --server.port=20000 
```
