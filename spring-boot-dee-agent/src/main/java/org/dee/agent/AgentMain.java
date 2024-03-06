package org.dee.agent;


import org.dee.agent.aop.MethodAOPTransformer;
import org.dee.agent.aop.utils.ResourceUtil;

import java.lang.instrument.Instrumentation;

public class AgentMain {

    /**
     * jvm方式启动，运行此方法
     * @deprecated 通过premain进行字节码增强，可以通过添加注解方式处理程序，也可以通过修改方法内容处理程序
     * @param agentArgs
     * @param inst
     */
    public static void premain(String agentArgs, Instrumentation inst) {
        System.out.println("premain");
        //修改字节码-改方法代码
//        inst.addTransformer(new MethodTimingTransformer(), true);
        //修改字节码-aop
        inst.addTransformer(new MethodAOPTransformer(ResourceUtil.getMethodAnnotationConfiguration()), true);
    }

    /**
     * 动态 attach 方式启动，运行此方法
     * @param agentArgs
     * @param inst
     */
    public static void agentmain(String agentArgs, Instrumentation inst) {
        System.out.println("agentmain");
    }

}