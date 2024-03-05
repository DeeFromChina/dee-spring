package org.dee.classtranformer;

import jdk.internal.org.objectweb.asm.ClassReader;
import jdk.internal.org.objectweb.asm.ClassVisitor;
import jdk.internal.org.objectweb.asm.ClassWriter;
import jdk.internal.org.objectweb.asm.Opcodes;

import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.security.ProtectionDomain;

public class MethodTimingTransformer implements ClassFileTransformer {

    @Override
    public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined,
                            ProtectionDomain protectionDomain, byte[] classfileBuffer) throws IllegalClassFormatException {
        if("com/sunline/erp/modules/pcmc/user/controller/LoginWebController".equals(className)){
//            try {
//                ClassPool classPool = ClassPool.getDefault();
//                CtClass ctClass = classPool.get(className.replace("/", "."));
//
//                // 添加一个新的方法，用于打印方法参数
//                CtMethod printArgumentsMethod = CtNewMethod.make(
//                        "public static void printArguments(java.lang.Object... args) { System.out.println(java.util.Arrays.toString(args)); }",
//                        ctClass
//                );
//                ctClass.addMethod(printArgumentsMethod);
//
//                // 遍历目标类的所有方法，并在每个方法前插入打印参数的方法调用
//                for (CtMethod method : ctClass.getDeclaredMethods()) {
//                    if (!method.getName().equals("<init>")) { // 忽略构造函数
//                        // 在方法开始处插入打印参数的方法调用
//                        method.insertBefore("{ com.sunline.erp.modules.wecom.service.impl.WeComWebServiceImpl.printArguments($args); }");
//                    }
//                }
//
//                return ctClass.toBytecode();
//            } catch (Exception e) {
//                e.printStackTrace();
//            }


            // 使用ASM框架进行字节码转换
            ClassReader cr = new ClassReader(classfileBuffer);
            ClassWriter cw = new ClassWriter(cr, ClassWriter.COMPUTE_FRAMES);
            ClassVisitor cv = new TimeStatisticsVisitor(Opcodes.ASM5, cw);
            cr.accept(cv, ClassReader.SKIP_FRAMES | ClassReader.SKIP_DEBUG);
            return cw.toByteArray();



        }
        return classfileBuffer;
    }

}
