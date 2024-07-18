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

RequestBodyAdvice
`RequestBodyAdvice` 是 Spring Web MVC 提供的一个接口，它允许开发者在请求体（Request Body）被绑定到控制器方法参数之前或之后进行拦截和处理。这可以用于日志记录、请求体验证、请求体修改等多种场景。通过实现 `RequestBodyAdvice` 接口，你可以自定义请求体的处理逻辑。

`RequestBodyAdvice` 接口中定义了几个关键的方法：

- `supports(MethodParameter methodParameter, Type targetType, Class<? extends HttpMessageConverter<?>> converterType)`: 用于判断当前实现的 `RequestBodyAdvice` 是否应该应用于给定的请求体。返回 `true` 表示适用。
- `beforeBodyRead(HttpInputMessage inputMessage, MethodParameter methodParameter, Type targetType, Class<? extends HttpMessageConverter<?>> converterType)`: 在请求体被读取并绑定到控制器方法参数之前调用。可以修改 `HttpInputMessage` 或者进行其他前置处理。
- `afterBodyRead(Object body, HttpInputMessage inputMessage, MethodParameter methodParameter, Type targetType, Class<? extends HttpMessageConverter<?>> converterType)`: 在请求体被读取并绑定到控制器方法参数之后调用。可以修改或检查已绑定的对象。

### 举例

假设我们想要在所有通过 `@RequestBody` 注解接收的请求体中自动添加一些公共信息，比如时间戳，我们可以通过实现 `RequestBodyAdvice` 来实现。

首先，我们定义一个简单的 `RequestBodyAdvice` 实现，用于在请求体中添加时间戳：

```java
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.bind.annotation.RequestBodyAdvice;
import org.springframework.web.servlet.mvc.method.annotation.RequestBodyAdviceAdapter;

import java.io.IOException;

@RequestBodyAdvice
public class TimestampRequestBodyAdvice extends RequestBodyAdviceAdapter {

    @Override
    public boolean supports(MethodParameter methodParameter, Type targetType,
                            Class<? extends HttpMessageConverter<?>> converterType) {
        // 假设我们只对某些类型的请求体感兴趣，比如这里以 MyRequest 为例
        return MyRequest.class.isAssignableFrom(targetType);
    }

    @Override
    public Object afterBodyRead(Object body, HttpInputMessage inputMessage,
                                MethodParameter methodParameter, Type targetType,
                                Class<? extends HttpMessageConverter<?>> converterType) {
        // 假设 MyRequest 有一个 setTimestamp 方法
        if (body instanceof MyRequest) {
            MyRequest myRequest = (MyRequest) body;
            myRequest.setTimestamp(System.currentTimeMillis());
        }
        return body;
    }
}

// 假设的 MyRequest 类
class MyRequest {
    private Long timestamp;

    // getters and setters
    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }
}
```

然后，确保 Spring 能够扫描到并注册你的 `RequestBodyAdvice` 实现。通常，如果你将 `TimestampRequestBodyAdvice` 放在一个被 Spring 管理的组件扫描路径下，并且它被标记为 `@RequestBodyAdvice`，那么 Spring 就会自动注册它。

### 注意事项

- 确保你的 `RequestBodyAdvice` 实现是线程安全的，尤其是如果你计划在 `afterBodyRead` 方法中修改请求体对象时。
- `supports` 方法的实现非常关键，因为它决定了哪些请求体会被你的 `RequestBodyAdvice` 处理。
- 在处理请求体时，需要小心处理可能的异常和错误情况，以避免影响整个请求处理流程。


ResponseBodyAdvice

`ResponseBodyAdvice` 是 Spring 框架提供的一个接口，它允许开发者在控制器（Controller）方法返回的响应体（Response Body）被写入到 HTTP 响应之前进行拦截和处理。这使得开发者可以对所有通过 `@ResponseBody` 或 `@RestController` 注解返回的响应体进行全局的、统一的修改或增强操作，如添加统一的响应头、对返回结果进行包装、数据加密或压缩等。

### ResponseBodyAdvice 的主要方法

- `supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType)`: 用于判断当前实现类是否支持对指定返回类型的响应体进行处理。如果返回 `true`，则会执行 `beforeBodyWrite` 方法；如果返回 `false`，则不会执行。
- `beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType, Class<? extends HttpMessageConverter<?>> selectedConverterType, ServerHttpRequest request, ServerHttpResponse response)`: 在响应体写入之前调用，可以对响应体进行修改或增强。该方法的返回值将作为最终的响应体返回给客户端。
- `handleEmptyBody(Object body, HttpOutputMessage outputMessage)`: 当控制器方法返回 `null` 时，会调用此方法来处理空响应体的情况。默认实现会返回一个空的 `HttpHeaders` 对象，但开发者可以重写此方法以生成非空的响应体。

### 举例

假设我们有一个 Spring Boot 应用，其中有一个控制器方法返回用户信息，现在我们想要在所有返回的用户信息中添加一个时间戳字段。我们可以使用 `ResponseBodyAdvice` 来实现这一需求。

首先，定义一个 `UserResponse` 类来封装用户信息和时间戳：

```java
public class UserResponse {
    private User user;
    private long timestamp;

    // 省略getter和setter方法
}
```

然后，实现 `ResponseBodyAdvice` 接口，并重写 `supports` 和 `beforeBodyWrite` 方法：

```java
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

@ControllerAdvice
public class MyResponseBodyAdvice implements ResponseBodyAdvice<Object> {

    @Override
    public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
        // 假设我们对所有返回Object类型的响应体都进行处理
        return true;
    }

    @Override
    public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType,
                                  Class<? extends HttpMessageConverter<?>> selectedConverterType, ServerHttpRequest request,
                                  ServerHttpResponse response) {
        if (body instanceof User) {
            User user = (User) body;
            UserResponse userResponse = new UserResponse();
            userResponse.setUser(user);
            userResponse.setTimestamp(System.currentTimeMillis());
            return userResponse;
        }
        // 如果不是User类型，则直接返回原响应体
        return body;
    }

    // handleEmptyBody 方法在这里可以保持默认实现，因为我们不处理空响应体的情况
}
```

在这个例子中，`MyResponseBodyAdvice` 类通过 `@ControllerAdvice` 注解被标记为一个全局的控制器增强器。它实现了 `ResponseBodyAdvice` 接口，并在 `beforeBodyWrite` 方法中检查响应体是否为 `User` 类型。如果是，就将其封装到 `UserResponse` 对象中，并添加一个时间戳字段。最后，将封装后的 `UserResponse` 对象作为新的响应体返回给客户端。

通过这种方式，我们可以对所有通过 `@ResponseBody` 或 `@RestController` 注解返回的 `User` 类型响应体进行统一的处理，而无需在每个控制器方法中重复相同的逻辑。
