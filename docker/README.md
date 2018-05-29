# Docker operations

### Start local docker

- Start docker compositions
```
docker-compose -f elasticsearch.yml up -d
docker-compose -f postgres.yml up -d
```

- Set active spring profile: local 
- Run ch.admin.seco.jobs.services.jobadservice.Application