## agent组件

### 实现原理
agent有两种方法实现。1、premain，2、attach。可在网上查询两种方式的区别，此处不进行说明

### 提供功能
在org.dee.AgentMain中有两种字节码增强的方式。  
1、通过添加注解方式，即在指定类/方法中添加自定义注解，采用aop的方式做其他业务处理。   
2、直接修改类方法的内容，将编写好的逻辑插入到方法中。

注：   
1、两种方式可以共用   
2、org.dee.AgentMain是主方法，在resources/META-INF/MAINIFEST.MF文件中可以指定agent的主方法


### 使用说明

#### 1、引用agent包
该工程package后，会打成agent的jar包。在目标工程中通过vm options将agent包添加到工程中
```properties
-javaagent:/Users/.../spring-boot-start-dee-agent-0.0.1-SNAPSHOT.jar
```
#### 2、修改配置文件
在resources目录下新增agent.yml文件。

###### agent.yml
```yaml
org:
  dee:
    agent:
      aop:
        method:
          # 需要添加注解的包/类路径
          basePackages:
            - com/test/controller
          # 需要添加的注解类路径
          annotationClasses:
            - com.test.annotation.DeeLog
```

###### 需要添加注解的类
```java
import com.test.controller;

@RestController
public class TestController {
    
    @RequestMapping("/demo")
    public String api() {
        return "success";
    }
    
}
```
###### 需要添加注解
```java
import com.test.annotation;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface DeeLog {

    String value() default "";

}
```

注：Controller和DeeLog都可以在目标工程中自定义，agent包只是将定义好的注解添加到指定的地方去。

