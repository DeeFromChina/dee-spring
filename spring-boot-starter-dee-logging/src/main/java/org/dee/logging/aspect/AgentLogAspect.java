package org.dee.logging.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.Arrays;

/**
 * 用于输出注解标记的方法、入参和出参
 */
@Component
@Aspect
@Slf4j
public class AgentLogAspect {

    public AgentLogAspect() {
        log.info("AgentLogAspect");
    }

    @Pointcut("@annotation(org.dee.logging.annotation.AgentLog)")
    public void logPointcut() {
    }

    @Around("logPointcut()")
    public Object before(ProceedingJoinPoint joinPoint) throws Throwable {
        // 获取方法签名
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();

        StringBuffer s = new StringBuffer();
        s.append("【Start】: "+signature.getDeclaringTypeName() + "." + method.getName() + "()");
        log.info(s.toString());

        return joinPoint.proceed();
    }

    // 在方法正常返回后执行的通知
    @AfterReturning(pointcut = "logPointcut()", returning = "result")
    public void afterReturningAdvice(JoinPoint joinPoint, Object result) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();

        // 获取方法参数
        Object[] args = joinPoint.getArgs();

        StringBuffer s = new StringBuffer();
        s.append("【End】: "+signature.getDeclaringTypeName() + "." + method.getName() + "()\n");
        s.append("【Status】：success\n");
        s.append("【Parameters】: " + Arrays.toString(args) + "\n");
        // result参数就是方法返回的值
        s.append("【Returned】: " + result);
        log.info(s.toString());
    }

    // 当目标方法抛出异常时，执行此通知方法
    @AfterThrowing(pointcut = "logPointcut()", throwing = "e")
    public void logAfterThrowing(JoinPoint joinPoint, Throwable e) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();

        // 获取方法参数
        Object[] args = joinPoint.getArgs();

        // 可以在这里记录日志，或者执行其他操作

        StringBuffer s = new StringBuffer();
        s.append("【End】: "+signature.getDeclaringTypeName() + "." + method.getName() + "()\n");
        s.append("【Status】：fail\n");
        s.append("【Parameters】: " + Arrays.toString(args) + "\n");
        s.append("【Exception】：" + e.getMessage());
    }

}
