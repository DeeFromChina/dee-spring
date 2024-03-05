package org.dee.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.Arrays;

@Component
@Aspect
public class AgentLogAspect {

    public AgentLogAspect() {
        System.out.println("DeeLogAspect");
    }

    @Pointcut("@annotation(com.sunline.erp.annotation.DeeLog)")
    public void logPointcut() {
    }

    @Around("logPointcut()")
    public Object before(ProceedingJoinPoint joinPoint) throws Throwable {
        System.out.printf("ThreadId", Thread.currentThread().getId());
        System.out.printf("ThreadName", Thread.currentThread().getName());
        // 获取方法签名
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();

        // 获取方法参数
        Object[] args = joinPoint.getArgs();
        System.out.println("Method name: " + method.getName());
        System.out.println("Arguments: " + Arrays.toString(args));

        return joinPoint.proceed();
    }

    // 在方法正常返回后执行的通知
    @AfterReturning(pointcut = "logPointcut()", returning = "result")
    public void afterReturningAdvice(JoinPoint joinPoint, Object result) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        //获取方法所在类的类名
        System.out.println("ClassName:"+signature.getDeclaringTypeName());
        //获取方法名
        System.out.println("MethodName:"+signature.getName());
        // result参数就是方法返回的值
        System.out.println("Method " + joinPoint.getSignature().getName() + " returned: " + result);
    }

}
