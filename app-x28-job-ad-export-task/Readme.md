# X28 JobAd XML Export Task 

The app downloads a zip file from the X28 sFtp server, extracts and process the XML file in a Spring Batch Task. 

### Deployment to Spring Cloud DataFlow


##### [REST API](https://docs.spring.io/spring-cloud-dataflow/docs/current/reference/htmlsingle/#api-guide-resources-index) 

```bash
# Register 'App'
curl 'http://dev.job-room.ch:9393/apps/task/x28jobadexport' -i -X POST \
  -d 'force=true&uri=maven%3A%2F%2Fch.admin.seco.jobs.services.jobadservice%3Aapp-x28-job-ad-export-task%3A<version>'

# Delete existing task
curl http://dev.job-room.ch:9393/tasks/definitions/x28-job-ad-export -i -X DELETE

# Create stream and deploy
curl http://dev.job-room.ch:9393/tasks/definitions -X POST \
  -d 'name=x28-job-ad-export-task&definition=x28jobadexport'
```

##### Dashboard

[Register Task](http://dev.job-room.ch:9393/dashboard/#/apps)

- `Register Application(s)`
  - name = x28jobadexport
  - type = tasks
  - App URI = `maven://ch.admin.seco.jobs.services.jobadservice:app-x28-job-ad-export-task:jar:<version>` \
  - `Register` 

[Create Task](http://dev.job-room.ch:9393/dashboard/#/tasks/apps)
- `Create Definition` of the registered task
- Enter all required properties to access the sFtp server
- `Submit`


### Deployment from command line
Run **x28-job-ad-export** as seperate task from command-line:

```bash
curl https://alvch.jfrog.io/alvch/global/ch/admin/seco/jobs/services/jobadservice/app-x28-job-ad-export-task/<version>/app-x28-job-ad-export-task-<version>.jar

java -Xmx256m -jar app-x28-job-ad-export-task-<version>.jar 
  --jobroom.x28.sftp.host=admin.x28.ch
  --jobroom.x28.sftp.port=22
  --jobroom.x28.sftp.username=<x28.username>
  --jobroom.x28.sftp.password=<x28.password>
  --jobroom.x28.sftp.xmlFileName=avamJobs_test_current.xml
  --spring.cloud.task.name=x28-job-ad-export-task
  --spring.datasource.driverClassName=org.postgresql.Driver
  --spring.datasource.username=spring-cloud-dataflow
  --spring.datasource.password=<db.password>
  --spring.datasource.url=jdbc:postgresql://dev.job-room.ch:5440/spring-cloud-dataflow
  --spring.datasource.jobadservice.driverClassName=org.postgresql.Driver
  --spring.datasource.jobadservice.username=jobadservice
  --spring.datasource.jobadservice.password=<db.password>
  --spring.datasource.jobadservice.url=jdbc:postgresql://dev.job-room.ch:5439/jobadservice
  --server.port=0
  --spring.cloud.task.executionid=<executionid>
```
