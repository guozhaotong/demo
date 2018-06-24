```
docker run --name some-redis -d -p 6379  redis:3.2


./mvnw package
docker build -t hi .
docker run -p 8081:8080 --link some-redis:redis  hi

```


