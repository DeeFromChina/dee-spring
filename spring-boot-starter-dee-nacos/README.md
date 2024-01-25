# 目的
该项目为了实现一个服务在nacos上注册多个不同别名的实例。

## 原理
使用nacos的注册机制，将服务别名注册至nacos上。

## 操作步骤
在application.yml中配置
```yaml
#项目名
spring:
  application:
    name: server1

#nacos注册别名
nacos:
  proxy:
    #开关
    enabled: true
    configs[0]:
      server-name: server2
      group: DEFAULT_GROUP
    configs[1]:
      server-name: server3
      group: DEFAULT_GROUP
```
在服务启动后，在nacos上会出现server1、server2、server3的服务实例。