# 目的
该项目为了实现动态控制不同类包下的日志开启关闭

## 原理
Spring Boot 在 spring-boot-starter-actuator 模块中提供了日志相关的 EndPoint，通过该 EndPoint 可以在运行时不需要重启服务就可以修改日志的打印级别。
解决了以前修改日志打印级别必须要重启服务的烦恼。

## 参考链接
https://blog.csdn.net/peace_hehe/article/details/81333332

## 操作步骤
一、通过链接教程引入
```xml
<dependencys>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-actuator</artifactId>
    </dependency>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-devtools</artifactId>
    </dependency>
</dependencys>
```

二、在配置文件application.yml中，配置
```yaml
management:
    endpoints:
        web:
            exposure:
              include: loggers,sessions
```

三、请求http://localhost:8080/actuator/loggers，可以获取各包路径的日志级别。<br/>
下面截取一部分内容
```json
{
  "_org.springframework.web.servlet.HandlerMapping": {"configuredLevel":null,"effectiveLevel":"INFO"},
  "_org.springframework.web.servlet.HandlerMapping.Mappings": {"configuredLevel":null,"effectiveLevel":"INFO"},
  "com":{"configuredLevel":null,"effectiveLevel":"INFO"},
  "com.alibaba":{"configuredLevel":null,"effectiveLevel":"INFO"},
  "com.alibaba.cloud":{"configuredLevel":null,"effectiveLevel":"INFO"},
  "com.alibaba.cloud.nacos":{"configuredLevel":null,"effectiveLevel":"INFO"},
  "com.alibaba.cloud.nacos.NacosConfigManager":{"configuredLevel":null,"effectiveLevel":"INFO"},
  "com.alibaba.cloud.nacos.NacosConfigProperties":{"configuredLevel":null,"effectiveLevel":"INFO"}
}
```


四、修改某包路径的日志级别，请求 http://localhost:8080/actuator/loggers/包路径

如 http://localhost:8080/actuator/loggers/com.dee

body
```json
{
    "configuredLevel":"INFO",
    "effectiveLevel":"INFO"
}
```
就将com.dee包的日志级别都改为INFO



