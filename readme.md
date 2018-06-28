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
- 在docker中对部署在本机上的服务进行http调用的时候，要用本工程的端口，而不是映射到宿主机上的端口

### 缓存
- 由Redis改成了Ehcache
- 从数据库中读取成绩列表后，存在redis中。过期时间设置为10秒。
- 下次调用列表时先检查redis中是否存在
- 当对数据库进行增删改操作后要删除对应的redis缓存

### docker
- 写Dockerfile，进行必要配置。注意docker中时间可能不对（时区问题），可以直接从配置文件中配置为跟宿主机一样的时间，或配置时区
- docker内容器之间没法相互通过网络进行调用（原因：容器相互独立， ip不是localhost了，所以需要用link进行域名映射）
    - 使用了不同的配置文件，部署在docker里一套文件，本地dev一套文件，因为域名不一样。
- redis的重启策略：除非人工关闭，否则自动重启
- docker logs 前台打印log，通过这个查找问题

### 返回的Responce类
- 首先自定义枚举状态码和状态描述
- Responce类中包括状态码、状态描述和真正函数返回的body

```
docker run --name some-redis -d -p 6379:6379 --restart=unless-stopped redis:3.2 
./mvnw package
docker build -t hi .
docker run --name etltest -p 8081:8080 --link some-redis:redis  hi // 现在不用redis了，就不用link了用下面语句
docker run --name etltest -p 8081:8080 hi 
```



