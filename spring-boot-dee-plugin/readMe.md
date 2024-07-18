## 插件主类

### 实现原理
利用classload和spring的托管实现springBean的动态注入

### 工程目录
plugin的主启动类作为父工程，其他插件工程作为子工程。子工程需要依赖的包都需要在父工程体现。

## 问题
做plugin的目的就是，在主服务不停机的情况下，通过控制加载和卸载jar包，实现模块的添加和删除。
但是如果主服务没有包含某些类，如主服务没有包含mongo，但是子模块需要用到mongo，在模块的加载使用过程中，会因为主服务没有mongo功能包导致mongo相关功能都使用不了并报错。
要如何解决这个问题？
解1: 如果将主程序没有的jar包，跟着子模块jar包一起加载
解2: 分成web项目和server项目，web项目可以通过plugin方式动态添加，所需要的jar提前在plugin-main项目准备好。
server作为单独的微服务实现。

## 具体做法
插件工程按正常的spring项目来写，只是没有启动类，使用mvn package打包成jar包，在plugin主项目中注册插件。

如：
现有插件工程spring-plugin-demo,结构目录如下：
```markdown
spring-plugin-demo  
       L src  
          L  main
              L controller
                  L TestController
```

通过mvn package将spring-plugin-demo打成jar包spring-plugin-demo-1.0-SNAPSHOT.jar
plugin主项目是一个引用了spring-boot-dee-plugin的项目，主项目启动后，调用PluginController的/controller/add接口，传入spring-plugin-demo-1.0-SNAPSHOT.jar的路径加载jar包