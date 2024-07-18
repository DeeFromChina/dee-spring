## dee-agent组件在项目中的实践

### 1、背景
在spring项目开发过程中，有很多业务场景需要通过aop方式进行实现。如日志、提醒监听等。
通常的做法是在对应的方法上添加相关业务的对应注解来实现，假设在系统建设趋于完善时，需要在特定类方法上添加注解，就需要开发人员找到对应的代码一个个添加上去，很容易遗漏也不便于控制。
有没有一种解耦并且能通过配置方式来实现aop注解的添加和关闭呢？

### 2、解决方案
通过agent方式实现代码的无侵入改造；通过配置文件实现注入对象的控制。

### 3、基本概念
#### 3.1、agent实现原理
agent有两种方法实现。1、premain，2、attach。可在网上查询两种方式的区别，此处不进行说明

### 4、功能说明
### 4.1、提供功能
在org.dee.AgentMain中有两种字节码增强的方式。  
1、通过添加注解方式，即在指定类/方法中添加自定义注解，采用aop的方式做其他业务处理。   
2、直接修改类方法的内容，将编写好的逻辑插入到方法中。

注：   
1、两种方式可以共用   
2、org.dee.AgentMain是主方法，在resources/META-INF/MAINIFEST.MF文件中可以指定agent的主方法


### 5、使用说明

#### 5.1、引用agent包
该工程package后，会打成agent的jar包。在目标工程中通过vm options将agent包添加到工程中
```markdown
-javaagent:/Users/.../spring-boot-start-dee-agent-0.0.1-SNAPSHOT.jar
```
#### 5.2、修改配置文件
在resources目录下新增agent.yml文件。
targer/classes中需要有agent.yml

###### agent.yml
```yaml
org:
  dee:
    agent:
      aop:
        method:
          # 需要添加注解的包/类路径
          basePackages:
            - com.test.controller
          # 需要添加的注解类路径（注解类可以是本地项目自定义的注解类）
          annotationClasses:
            - org.dee.logging.annotation.AgentLog
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

###### 项目启动时jvm读取的TestController.class文件
```java
import com.test.controller;

@RestController
public class TestController {
    
    @DeeLog
    @RequestMapping("/demo")
    public String api() {
        return "success";
    }
    
}
```
注：Controller和DeeLog都可以在目标工程中自定义，DeeLogAspect需要被spring托管，agent包只是将定义好的注解添加到指定的地方去。

