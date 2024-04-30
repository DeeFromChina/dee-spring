#### ImportBeanDefinitionRegistrar
```markdown
`ImportBeanDefinitionRegistrar`是Spring框架中的一个接口，它提供了强大的动态注册Bean定义的能力。这个接口允许开发者在Spring容器初始化时，根据特定的条件或需求来添加或修改Bean定义，从而实现更为精细的控制和扩展性。它是构建可扩展框架、插件系统或处理复杂配置场景的利器。

`ImportBeanDefinitionRegistrar`接口不是直接注册Bean到IOC容器，而是注册Bean的定义信息以便后续的Bean创建。它的执行时机较早，主要作用是在Spring容器启动过程中，动态地向容器中注册Bean定义。

使用`ImportBeanDefinitionRegistrar`的基本步骤包括：

1. 实现`ImportBeanDefinitionRegistrar`接口。
2. 通过`registerBeanDefinitions`方法实现具体的类初始化。
3. 在`@Configuration`注解的配置类上使用`@Import`导入实现类。

这个接口的应用场景通常是在需要动态扩展、插件化或者编程式配置Spring应用的场景中。通过它，开发者可以在运行时动态地向Spring容器中注册Bean定义，实现更为灵活和可配置的应用架构。

总的来说，`ImportBeanDefinitionRegistrar`是Spring框架中非常强大和灵活的工具，允许开发者对Spring容器的Bean定义进行动态的扩展和定制。
```

扩展点
1、帮controller创建对应的RequestMapping值(破产，spring自动装配除了加载自定义的CustomRequestMappingHandlerMapping，还是会加载RequestMappingHandlerMapping，导致url重复报错)
```markdown
例：
    @RequestMapping
    TestEntityWebController extends BaseWebController<TestEntity, TestEntityService>
想法：
如果@RequestMapping不填入参数，则由方法帮它生成对应RequestMapping值。
实现逻辑：
    获取TestEntityWebController对应的TestEntity，将testEntity作为新的RequestMapping值，
    替换进spring中的url管理map中
```

WebConfig.java
```java
@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Bean
    public RequestMappingHandlerMapping requestMappingHandlerMapping() {
        return new CustomRequestMappingHandlerMapping();
    }
    
}
```

CustomRequestMappingHandlerMapping.java
```java
import cn.hutool.core.util.StrUtil;
import org.dee.framework.controller.BaseWebController;
import org.dee.web.context.SpringContext;
import org.springframework.aop.framework.AopProxyUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.condition.PatternsRequestCondition;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import javax.annotation.Resource;
import java.lang.reflect.ParameterizedType;

import java.lang.reflect.Method;
import java.lang.reflect.Type;

public class CustomRequestMappingHandlerMapping extends RequestMappingHandlerMapping {

    @Resource
    private SpringContext context;

    @Override
    protected void registerHandlerMethod(Object handler, Method method, RequestMappingInfo mappingInfo) {
        // 检查mappingInfo是否存在，如果存在则进行自定义处理
        if (mappingInfo != null) {
            Class<?> targetClass = AopProxyUtils.ultimateTargetClass(context.getBean(handler.toString()));
            System.out.println(targetClass.getName());
            String controllerRequestMappingName = getControllerRequestMappingName(targetClass);

            if (StrUtil.isNotEmpty(controllerRequestMappingName)) {
                //替换掉原来的RequestMappingUrl
                RequestMappingInfo newMappingInfo = replaceRequestMappingUrl(mappingInfo, controllerRequestMappingName);
                // 返回新的RequestMappingInfo
                super.registerMapping(newMappingInfo, handler, method);
            }
        }else {
            // 如果没有修改，则返回原始的RequestMappingInfo
            super.registerHandlerMethod(handler, method, mappingInfo);
        }
    }


    /**
     * 帮controller创建对应的RequestMapping值
     * 例：
     * @RequestMapping
     * TestEntityWebController extends BaseWebController<TestEntity, TestEntityService>
     * 如果@RequestMapping不填入参数，则由方法帮它生成对应RequestMapping值。
     * 实现逻辑：
     * 获取TestEntityWebController对应的TestEntity，将testEntity作为新的RequestMapping值，替换进spring中的url管理map中
     * @param method
     * @param handlerType
     * @return
     */
    @Override
    protected RequestMappingInfo getMappingForMethod(Method method, Class<?> handlerType) {
        // 调用父类方法获取原始的RequestMappingInfo
        RequestMappingInfo mappingInfo = super.getMappingForMethod(method, handlerType);

        // 检查mappingInfo是否存在，如果存在则进行自定义处理
        if (mappingInfo != null) {
            String controllerRequestMappingName = getControllerRequestMappingName(handlerType);

            if (StrUtil.isNotEmpty(controllerRequestMappingName)) {
                //替换掉原来的RequestMappingUrl
                RequestMappingInfo newMappingInfo = replaceRequestMappingUrl(mappingInfo, controllerRequestMappingName);
                // 返回新的RequestMappingInfo
                return newMappingInfo;
            }
        }
        // 如果没有修改，则返回原始的RequestMappingInfo
        return mappingInfo;
    }

    /**
     * 获取Controller的RequestMapping值
     * @param controllerClass
     * @return
     */
    private static String getControllerRequestMappingName(Class<?> controllerClass) {
        RequestMapping requestMapping = controllerClass.getAnnotation(RequestMapping.class);
        String controllerRequestMappingName = "";
        if(requestMapping != null){
            if(requestMapping.value() != null && requestMapping.value().length == 0) {
                controllerRequestMappingName = createControllerRequestMappingName(controllerClass);
            }
        }
        return controllerRequestMappingName;
    }

    /**
     * 根据泛型参数，创建新的Controller的RequestMapping值
     * @param controllerClass
     * @return
     */
    private static String createControllerRequestMappingName(Class<?> controllerClass) {
        //判断是否继承BaseWebController
        if(!BaseWebController.class.isAssignableFrom(controllerClass)){
            return "";
        }
        String controllerRequestMappingName = "/";
        //获取继承的类
        Type superclassType = controllerClass.getGenericSuperclass();

        // 检查是否是一个参数化类型（即带有泛型参数的类型）
        if (superclassType instanceof ParameterizedType) {
            ParameterizedType parameterizedType = (ParameterizedType) superclassType;

            // 获取泛型参数数组
            Type[] typeArguments = parameterizedType.getActualTypeArguments();

            // 假设B<C>是A的唯一泛型超类，那么typeArguments[0]就是C的类型
            if (typeArguments.length > 0) {
                Type cType = typeArguments[0];

                // 如果C是一个具体的类类型（而不是另一个泛型类型或通配符类型），则可以通过Class对象获取
                if (cType instanceof Class) {
                    Class<?> entityClass = (Class<?>) cType;
                    String entityBeanName = StrUtil.lowerFirst(entityClass.getSimpleName());
                    controllerRequestMappingName += entityBeanName;
                } else {
                    // 如果C是一个泛型类型或通配符类型，则需要进一步处理
                    System.out.println("C is a generic type or wildcard type: " + cType);
                }
            }
        } else {
            System.out.println("A does not have a parameterized superclass or the information is not accessible.");
        }
        return controllerRequestMappingName;
    }

    /**
     * 替换掉原来的RequestMappingUrl
     * @param mappingInfo
     * @param controllerRequestMappingName
     * @return
     */
    private RequestMappingInfo replaceRequestMappingUrl(RequestMappingInfo mappingInfo, String controllerRequestMappingName) {
        // 方法的路径
        if(mappingInfo.getPatternsCondition() == null){
            return mappingInfo;
        }
        String originalPattern = mappingInfo.getPatternsCondition().getPatterns().iterator().next();
        //String originalPattern = mappingInfo.getPathPatternsCondition().getPatterns().iterator().next().getPatternString();
        System.out.println("==================进来了进来了=====================");
        System.out.println(controllerRequestMappingName + originalPattern);
        //return RequestMappingInfo.paths(controllerRequestMappingName + originalPattern).build().combine(mappingInfo);

        // 创建新的路径模式
        // 创建新的PatternsRequestCondition
        PatternsRequestCondition newPatternsCondition = new PatternsRequestCondition(controllerRequestMappingName + originalPattern);

        // 创建新的RequestMappingInfo，并复制其他条件
        RequestMappingInfo newMappingInfo = new RequestMappingInfo(
                newPatternsCondition,
                mappingInfo.getMethodsCondition(),
                mappingInfo.getParamsCondition(),
                mappingInfo.getHeadersCondition(),
                mappingInfo.getConsumesCondition(),
                mappingInfo.getProducesCondition(),
                mappingInfo.getCustomCondition()
        );
        // 返回新的RequestMappingInfo
        return newMappingInfo;
    }

}
```