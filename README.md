# job-ad-service

[![Build Status](https://travis-ci.org/alv-ch/job-ad-service.svg?branch=master)](https://travis-ci.org/alv-ch/job-ad-service)

This application was generated using JHipster 4.14.0, you can find documentation and help at [http://www.jhipster.tech/documentation-archive/v4.14.0](http://www.jhipster.tech/documentation-archive/v4.14.0).

This is a "microservice" application intended to be part of a microservice architecture, please refer to the [Doing microservices with JHipster][] page of the documentation for more information.

This application is configured for Service Discovery and Configuration with the JHipster-Registry. On launch, it will refuse to start if it is not able to connect to the JHipster-Registry at [http://localhost:8761](http://localhost:8761). For more information, read our documentation on [Service Discovery and Configuration with the JHipster-Registry][].

## Populate a database with JobAdvertisement data

To populate a local database with a job advertisement data, run the undermentioned *curl* script against REST APIs, under the ```/api``` URL with a corresponding payload.
e.g.
```
curl -H 'Content-Type: application/json' -H 'Accept: application/json' -H 'Authorization: Bearer eyJhbGciOiJIUzUxMiJ9.eyJhdXRoIjoiUk9MRV9VU0VSLFJPTEVfU1lTQURNSU4sUk9MRV9BRE1JTiIsInVzZXJJZCI6IjQ5NDZkMjljLWE1NDMtMTFlOC1iZjdhLTAwMTU1ZGU1MDEwOSIsImZpcnN0TmFtZSI6IkFkbWluaXN0cmF0b3IiLCJsYXN0TmFtZSI6IkFkbWluaXN0cmF0b3IiLCJlbWFpbCI6ImFkbWluQGxvY2FsaG9zdCIsImxhbmdLZXkiOiJkZSIsImV4dGVybmFsSWQiOiJhZG1pbiIsInN1YiI6ImFkbWluIiwiZXhwIjozMjYyODU3OTY1fQ.CCd97AlSNNIzYIAvWqbrRVxQfQX4IkFYp6sjPdisKiabaePwdhd50KE-oauKSbHOz_RJOR0Vh2-xWps59OPw4w' -d @src/curl/create_job_advertisement-payload.json http://localhost:8086/api/jobAdvertisements
```
The payload scripts can be found under ```/src/test-data```. Be aware of already existing job advertisement data and if there is such a need remove the old one.



[JHipster Homepage and latest documentation]: http://www.jhipster.tech
[JHipster 4.14.0 archive]: http://www.jhipster.tech/documentation-archive/v4.14.0
[Doing microservices with JHipster]: http://www.jhipster.tech/documentation-archive/v4.14.0/microservices-architecture/
[Using JHipster in development]: http://www.jhipster.tech/documentation-archive/v4.14.0/development/
[Service Discovery and Configuration with the JHipster-Registry]: http://www.jhipster.tech/documentation-archive/v4.14.0/microservices-architecture/#jhipster-registry
[Using Docker and Docker-Compose]: http://www.jhipster.tech/documentation-archive/v4.14.0/docker-compose
[Using JHipster in production]: http://www.jhipster.tech/documentation-archive/v4.14.0/production/
[Running tests page]: http://www.jhipster.tech/documentation-archive/v4.14.0/running-tests/
[Setting up Continuous Integration]: http://www.jhipster.tech/documentation-archive/v4.14.0/setting-up-ci/

[Gatling]: http://gatling.io/
