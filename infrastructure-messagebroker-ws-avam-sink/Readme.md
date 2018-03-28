AVAM Web Service Integration 
============================

Deployment to Spring Cloud DataFlow
-----------------------------------

a) **curl**

```bash
# Register 'App'
curl 'http://dev.job-room.ch:9393/apps/sink/ws-avam' -i -X POST -d 'uri=maven%3A%2F%2Fch.admin.seco.jobs.services.jobadservice%3Ainfrastructure-messagebroker-ws-avam-sink%3A0.0.1-SNAPSHOT'

# Create stream and deploy
curl 'http://dev.job-room.ch:9393/streams/definitions?deploy=true' -X POST -d 'name=ws-avam-export&definition=jobad.event > ws-avam-export: ws-avam --end-point-url=http://dev.job-room.ch:8180/AVAM_Web/services/EgovService --password=dummy --username=dummy --spring.profiles.active=v2'
```

b) **Dashboard**

Register app:

- goto: http://dev.job-room.ch:9393/dashboard/#/apps
- `Register Application(s)`
  - name = ws-avam
  - type = sink
  - App URI = `maven://ch.admin.seco.jobs.services.jobadservice:infrastructure-messagebroker-ws-avam-sink:jar:<version>` \
    **ATTENTION**: References to SNAPSHOT version must define the _unique_ version e.g. 0.0.1-20180322.175223-14 \
    **TODO**: Verify if deployments works also with public snapshot version
  - `Register` 

Create Stream:
- goto http://dev.job-room.ch:9393/dashboard/#/streams/definitions
- `Create Stream`
```bash
:jobad.event > ws-avam-export: ws-avam --end-point-url=http://dev.job-room.ch:8180/AVAM_Web/services/EgovService --password=dummy --username=dummy --spring.profiles.active=v2

```
- `Create Stream`
- Stream Name = ws-avam-export
- Deploy Stream(s) = true
- `OK`

c) **Spring DataFlow Shell** \
tbd

   
Deployment form command line
----------------------------
Run **ws-avam-sink** as seperate task from command-line:

**TODO** add properties for SSL keystore

```bash
curl https://alvch.jfrog.io/alvch/global/ch/admin/seco/jobs/services/jobadservice/infrastructure-messagebroker-ws-avam-sink/<version>/infrastructure-messagebroker-ws-avam-sink-<version>.jar

java -jar infrastructure-messagebroker-ws-avam-sink-<version>.jar \
  --spring.profiles.active=<avam-interface-version: v1,v2>
  --spring.cloud.dataflow.stream.app.label=ws-avam \
  --spring.cloud.dataflow.stream.name=ws-avam-export \
  --spring.cloud.stream.bindings.input.destination=jobad.event \ 
  --spring.cloud.dataflow.stream.app.type=sink \
  --spring.cloud.stream.kafka.binder.brokers=<kafka-hostname>:9092 \ 
  --spring.cloud.stream.kafka.binder.zk-nodes=<zookeeper-hostname>:2181 \
  --spring.cloud.application.group=ws-avam-export \
  --jobroom.ws.avam.endPointUrl=https://<avam-webservice-hostname>/AVAM_Web/services/EgovService \
  --jobroom.ws.avam.username=<username> \
  --jobroom.ws.avam.password=<password>
  --server.port=20001  
```


Example: Run in _development_ environment

```bash
curl https://alvch.jfrog.io/alvch/global/ch/admin/seco/jobs/services/jobadservice/infrastructure-messagebroker-ws-avam-sink/<version>/infrastructure-messagebroker-ws-avam-sink-<version>.jar

java -jar infrastructure-messagebroker-ws-avam-sink-<version>.jar \
  --spring.profiles.active=v2 \
  --spring.cloud.dataflow.stream.app.label=ws-avam \
  --spring.cloud.dataflow.stream.name=ws-avam-export \
  --spring.cloud.stream.bindings.input.destination=jobad.event \ 
  --spring.cloud.dataflow.stream.app.type=sink \
  --spring.cloud.stream.kafka.binder.brokers=dev.job-room.ch:9092 \ 
  --spring.cloud.stream.kafka.binder.zk-nodes=dev.job-room.ch:2181 \
  --spring.cloud.application.group=ws-avam-export \
  --jobroom.ws.avam.endPointUrl=http://dev.job-room.ch:8180/AVAM_Web/services/EgovService \
  --jobroom.ws.avam.username=dummy \
  --jobroom.ws.avam.password=dummy \  
  --server.port=20001
```
