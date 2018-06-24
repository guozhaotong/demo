```
docker run --name some-redis -d -p 6379:6379 --restart=unless-stopped redis:3.2 

#   --restart=unless-stopped
./mvnw package
docker build -t hi .
docker run -p 8081:8080 --link some-redis:redis  hi

```

java 传参数
`java -Dspring.profiles.active=dev -jar target/demo-0.0.1-SNAPSHOT.jar`



