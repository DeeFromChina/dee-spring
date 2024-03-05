package org.dee.aop;

import cn.hutool.core.util.ArrayUtil;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.LoaderClassPath;
import javassist.bytecode.AnnotationsAttribute;
import javassist.bytecode.AttributeInfo;
import javassist.bytecode.ClassFile;
import javassist.bytecode.ConstPool;
import javassist.bytecode.annotation.Annotation;
import org.dee.aop.configuration.MethodAnnotationConfiguration;

import java.io.ByteArrayInputStream;
import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.security.ProtectionDomain;

public class MethodAOPTransformer implements ClassFileTransformer {

    private MethodAnnotationConfiguration configuration;

    public MethodAOPTransformer() {

    }

    public MethodAOPTransformer(MethodAnnotationConfiguration configuration) {
        this.configuration = configuration;
    }

    /**
     * 将原有的class文件，经过加工生成新的class文件，在新class文件中植入埋点
     * @param loader                the defining loader of the class to be transformed,
     *                              may be <code>null</code> if the bootstrap loader
     * @param className             the name of the class in the internal form of fully
     *                              qualified class and interface names as defined in
     *                              <i>The Java Virtual Machine Specification</i>.
     *                              For example, <code>"java/util/List"</code>.
     * @param classBeingRedefined   if this is triggered by a redefine or retransform,
     *                              the class being redefined or retransformed;
     *                              if this is a class load, <code>null</code>
     * @param protectionDomain      the protection domain of the class being defined or redefined
     * @param classfileBuffer       the input byte buffer in class file format - must not be modified
     *
     * @return
     * @throws IllegalClassFormatException
     */
    @Override
    public byte[] transform(ClassLoader loader,
                            String className, Class<?> classBeingRedefined,
                            ProtectionDomain protectionDomain,
                            byte[] classfileBuffer) throws IllegalClassFormatException {
        if(configuration == null || ArrayUtil.isEmpty(configuration.getBasePackages())){
            return null;
        }
        //判断前缀
        for (String startsWithString : configuration.getBasePackages()) {
            //内部类，不处理
            if(className.indexOf('$')!=-1) return null;
            if (className.startsWith(startsWithString)) {
                //生成新的class文件
                return transformNewClass(loader, classfileBuffer, className);
            }
        }
        return null;
    }

    /**
     * 添加埋点，生成新的class文件
     * @param loader
     * @param classfileBuffer
     * @param className
     * @return
     */
    private byte[] transformNewClass(ClassLoader loader, byte[] classfileBuffer, String className) {
        try {
            //获取class池
            ClassPool classPool = ClassPool.getDefault();
            classPool.appendClassPath(new LoaderClassPath(loader));
            classPool.get(className.replaceAll("/", ".")).defrost();

            //创建新class,注入当前class字节码
            CtClass ctClass = null;
            ctClass = classPool.makeClass(new ByteArrayInputStream(classfileBuffer));
            ctClass.defrost();

            //添加方法注解
            ctClass = addMethodAnnotation(ctClass);

            System.out.println("log-埋点成功:"+className+":"+loader);
            return ctClass.toBytecode();
        } catch (Exception e) {
            System.out.println("log-埋点失败:"+className+":"+loader);
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 添加方法注解
     * @param ctClass
     */
    private CtClass addMethodAnnotation(CtClass ctClass) {
        ClassFile classFile = ctClass.getClassFile();
        ConstPool constPool = classFile.getConstPool();
        CtMethod[] methods = ctClass.getDeclaredMethods();

        for (CtMethod ctMethod : methods) {
            System.out.println(ctMethod.getName());
            // 添加方法注解
            // 添加方法注解
            AnnotationsAttribute methodAttr = null;
            if (!ctMethod.getMethodInfo().getAttributes().isEmpty()) {
                for (AttributeInfo attribute : ctMethod.getMethodInfo().getAttributes()) {
                    if (attribute instanceof AnnotationsAttribute) {
                        methodAttr = (AnnotationsAttribute) attribute;
                        break;
                    }
                }
                if (methodAttr == null)
                    methodAttr = new AnnotationsAttribute(constPool, AnnotationsAttribute.visibleTag);
            } else {
                methodAttr = new AnnotationsAttribute(constPool, AnnotationsAttribute.visibleTag);
            }
            for(String annotation : configuration.getAnnotationClasses()) {
                //注解
                Annotation logAnnotation = new Annotation(annotation, constPool);
                methodAttr.addAnnotation(logAnnotation);
            }
            ctMethod.getMethodInfo().addAttribute(methodAttr);
            ctMethod.getMethodInfo();
        }
        return ctClass;
    }

}
