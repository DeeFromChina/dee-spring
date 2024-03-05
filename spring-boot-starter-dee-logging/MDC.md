## MDC

### 相关链接
https://blog.csdn.net/weixin_38117908/article/details/107285978?utm_medium=distribute.pc_relevant.none-task-blog-2~default~baidujs_baidulandingword~default-9-107285978-blog-122664268.235^v43^control&spm=1001.2101.3001.4242.6&utm_relevant_index=12

### 用法
在拦截器中生成traceId，该traceId就是一次请求的唯一id

```java
import cn.sunline.pcmc.utils.TraceUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class JwtAuthorizationTokenFilter extends OncePerRequestFilter {
    private static final Logger log = LoggerFactory.getLogger(JwtAuthorizationTokenFilter.class);

    private static final String TRACE_ID = "TRACE_ID";

    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {
        //将生成的traceId放入MDC中
        MDC.put(TRACE_ID, TraceUtil.getTraceId());
        //业务逻辑
        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {
        MDC.remove(TRACE_ID);
        super.destroy();
    }
}
```

log4j2.xml
```xml
<Configuration status="WARN">

    <Properties>
        <!--  获取MDC定义的TRACE_ID  -->
        <Property name="TRACE_ID">%X{TRACE_ID}</Property>
    </Properties>

    <Appenders>
        <LogCollectionAppender name="logCollectionAppender" localQueueSize="10000">
            <PatternLayout pattern="%d{yyyy-MM-dd HH:mm:ss.SSS} [${sys:TRACE_ID}] [%-5p] {%F:%L} - %m%n"/>
        </LogCollectionAppender>
    </Appenders>
    
</Configuration>
```