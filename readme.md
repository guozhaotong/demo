### 日志记录
- 使用log4j进行日志记录
- 控制台输出 + 写日志文件

### 数据库
- 使用h2数据库，在各个平台上使用无需提前安装。
- 注意：当Entity字段改变时，数据库不会相应改变表中字段，需要人工drop表。

### 定时任务
- 使用timer实现  quartz(以后可以用更专业的)   springboot自带的schedule只支持编码设定时间
- 实现了定时任务的设置和删除（考虑任务不存在、任务已经执行完成等异常情况）
- 注意：月份从0开始计数。其他参数都是跟生活中一样的正常计数

### http调用
- 使用okhttp进行http调用
- 功能按照英语成绩排序，在调用人员列表时，通过listPerson的url进行http调用

### redis缓存
- 从数据库中读取成绩列表后，存在redis中。过期时间设置为10秒
- 下次调用列表时先检查redis中是否存在

### docker
- 写docker file
- docker内容器之间没法相互通过网络进行调用（原因：容器相互独立， ip不是localhost了，所以需要用link进行域名映射）
    - 使用了不同的配置文件，部署在docker里一套文件，本地dev一套文件，因为域名不一样。
- redis的重启策略：除非人工关闭，否则自动重启
- docker logs 前台打印log，通过这个查找问题

```
docker run --name some-redis -d -p 6379:6379 --restart=unless-stopped redis:3.2 
./mvnw package
docker build -t hi .
docker run -p 8081:8080 --link some-redis:redis  hi
```



