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

三、请求http://localhost:8080/actuator/loggers，可以获取各包路径的日志级别。   
接口由org.springframework.boot.actuate.logging.LoggersEndpoint提供

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

## 日志级别
```
日志级别（Log Level）的等级优先级从高到低依次为：OFF、FATAL、ERROR、WARN、INFO、DEBUG、TRACE、ALL。这些级别用于控制日志记录的详细程度。

1. **OFF**：这是最高等级的日志级别，用于关闭所有日志记录。当设置为OFF时，不会记录任何日志信息。
2. **FATAL**：指出每个严重的错误事件将会导致应用程序的退出。这个级别通常用于记录那些可能导致应用程序崩溃或无法继续运行的严重错误。
3. **ERROR**：指出虽然发生错误事件，但仍然不影响系统的继续运行。这个级别用于记录那些需要关注但不一定导致应用程序崩溃的错误。
4. **WARN**：表明会出现潜在错误的情形，但不影响应用程序的运行。这个级别用于记录那些可能需要注意或调查的潜在问题。
5. **INFO**：消息在粗粒度级别上突出强调应用程序的运行过程。这个级别通常用于记录应用程序的正常运行信息，如启动、关闭、主要业务逻辑的执行等。
6. **DEBUG**：指出细粒度信息事件对调试应用程序是非常有帮助的。这个级别用于记录详细的调试信息，帮助开发人员定位和解决问题。
7. **TRACE**：比DEBUG更详细，通常只在诊断问题时使用。这个级别记录的信息非常详细，包括方法调用的入参、出参、内部状态等。
8. **ALL**：最低等级的日志级别，用于打开所有日志记录。当设置为ALL时，会记录所有级别的日志信息。

在实际应用中，开发人员通常会根据需求设置合适的日志级别。例如，在生产环境中，为了减少日志量和提高性能，可能会将日志级别设置为WARN或ERROR；而在开发或测试环境中，为了便于调试和定位问题，可能会将日志级别设置为DEBUG或TRACE。通过合理设置日志级别，可以平衡日志的详细程度和性能需求。
```



