# AVAM Export Web Service Integration

This app listens on message queue `jobad.event` for events (_JOB_ADVERTISEMENT_INSPECTING_ and _JOB_ADVERTISEMENT_CANCELLING_) and sends them via web service to AVAM.

### Spring Profiles:
- avam-wsdl-v1: AVAM Web Service Version 1 (Jobroom 1)  
- avam-wsdl-v2: AVAM Web Service Version 2 (Jobroom 2, with reportingObligation) **default**

### Deployment to Spring Cloud DataFlow

##### [REST API](https://docs.spring.io/spring-cloud-dataflow/docs/current/reference/htmlsingle/#api-guide-resources-index) 

```bash
# Register 'App'
curl 'http://dev.job-room.ch:9393/apps/sink/wsavam' -i -X POST \
  -d 'force=true&uri=maven://ch.admin.seco.jobs.services.jobadservice:app-ws-avam-sink:<version>'

# Delete existing stream
curl http://dev.job-room.ch:9393/streams/definitions/ws-avam-export -i -X DELETE

# Create stream and deploy
curl http://dev.job-room.ch:9393/streams/definitions -X POST \
  -d 'name=wsavam-export&deploy=true&definition=:jobad.event > ws-avam-export: ws-avam --end-point-url=http://dev.job-room.ch:9180/AVAM_Web/services/EgovService --password=dummy --username=dummy'
```

##### Dashboard

[Register app](http://dev.job-room.ch:9393/dashboard/#/apps):

- `Register Application(s)`
  - name = wsavam
  - type = sink
  - App URI = `maven://ch.admin.seco.jobs.services.jobadservice:app-ws-avam-sink:jar:<version>`    
  - `Register` 

[Create Stream](http://dev.job-room.ch:9393/dashboard/#/streams/definitions)
- `Create Stream`
```bash
:jobad.event > ws-avam-export: wsavam --end-point-url=http://dev.job-room.ch:9180/AVAM_Web/services/EgovService --password=dummy --username=dummy --spring.profiles.active=v2
```
- `Create Stream`
- Stream Name = ws-avam-export
- Deploy Stream(s) = true
- `OK`

### Deployment from command line
Run **ws-avam-sink** as seperate task from command-line:

```bash
curl https://alvch.jfrog.io/alvch/global/ch/admin/seco/jobs/services/jobadservice/app-ws-avam-sink/<version>/app-ws-avam-sink-<version>.jar

java -jar app-ws-avam-sink-<version>.jar \
  --spring.profiles.active=<avam-interface-version: avam-wsdl-v1,avam-wsdl-v2>
  --spring.cloud.dataflow.stream.app.label=ws-avam \
  --spring.cloud.dataflow.stream.name=ws-avam-export \
  --spring.cloud.stream.bindings.input.destination=jobad.event \ 
  --spring.cloud.dataflow.stream.app.type=sink \
  --spring.cloud.stream.kafka.binder.brokers=<kafka-hostname>:9092 \ 
  --spring.cloud.stream.kafka.binder.zk-nodes=<zookeeper-hostname>:2181 \
  --spring.cloud.application.group=ws-avam-export \
  --jobroom.ws.avam.sink.endPointUrl=https://<avam-webservice-hostname>/AVAM_Web/services/EgovService \
  --jobroom.ws.avam.sink.username=<username> \
  --jobroom.ws.avam.sink.password=<password>
  --server.port=0  
```


**Example**: Run in _development_ environment

```bash
curl https://alvch.jfrog.io/alvch/global/ch/admin/seco/jobs/services/jobadservice/app-ws-avam-sink/<version>/app-ws-avam-sink-<version>.jar

java -jar app-ws-avam-sink-<version>.jar \
  --spring.profiles.active=avam-wsdl-v2 \
  --spring.cloud.dataflow.stream.app.label=ws-avam \
  --spring.cloud.dataflow.stream.name=ws-avam-export \
  --spring.cloud.stream.bindings.input.destination=jobad.event \ 
  --spring.cloud.dataflow.stream.app.type=sink \
  --spring.cloud.stream.kafka.binder.brokers=dev.job-room.ch:9092 \ 
  --spring.cloud.stream.kafka.binder.zk-nodes=dev.job-room.ch:2181 \
  --spring.cloud.application.group=ws-avam-export \
  --jobroom.ws.avam.sink.endPointUrl=http://dev.job-room.ch:9180/AVAM_Web/services/EgovService \
  --jobroom.ws.avam.sink.username=dummy \
  --jobroom.ws.avam.sink.password=dummy \  
  --server.port=0
```
