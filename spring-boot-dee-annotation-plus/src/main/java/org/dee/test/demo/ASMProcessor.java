//package org.dee.processor.test.demo;
//
//import com.google.auto.service.AutoService;
//import jdk.internal.org.objectweb.asm.ClassReader;
//import jdk.internal.org.objectweb.asm.ClassVisitor;
//import jdk.internal.org.objectweb.asm.ClassWriter;
//import jdk.internal.org.objectweb.asm.Opcodes;
//import org.dee.classtranformer.MyVisitor;
//
//import javax.annotation.processing.*;
//import javax.lang.model.SourceVersion;
//import javax.lang.model.element.Element;
//import javax.lang.model.element.TypeElement;
//import javax.tools.Diagnostic;
//import javax.tools.JavaFileObject;
//import java.io.IOException;
//import java.io.OutputStream;
//import java.util.Set;
//
//@SupportedAnnotationTypes("org.dee.processor.test.demo.HelloWorld" )
//@SupportedSourceVersion(SourceVersion.RELEASE_8)
//@AutoService(Processor.class)
//public class ASMProcessor extends AbstractProcessor {
//
//    //打印日志
//    private Messager messager;
//
//    //文件生成器，类/资源等最后需要生成的文件
//    private Filer filer;
//
//    /**
//     * 初始化
//     * @param processingEnv environment to access facilities the tool framework
//     * provides to the processor
//     */
//    @Override
//    public synchronized void init(ProcessingEnvironment processingEnv) {
//        super.init(processingEnv);
//
//        messager = processingEnv.getMessager();
//        filer = processingEnv.getFiler();
//    }
//
//    @Override
//    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
//        System.out.println("hello world");
//        for (Element element : roundEnv.getElementsAnnotatedWith(HelloWorld.class)) {
//            try {
//                // 获取原始类文件的字节码
//                byte[] bytecode = getBytecode(element);
//
//                // 使用 ASM 读取和修改字节码
//                ClassReader cr = new ClassReader(bytecode);
//                ClassWriter cw = new ClassWriter(cr, ClassWriter.COMPUTE_FRAMES);
//                ClassVisitor cv = new MyVisitor(Opcodes.ASM5, cw);
//
//                cr.accept(cv, 0);
//
//                // 将修改后的字节码写入新类文件
//                String className = element.toString();
//                JavaFileObject fileObject = filer.createClassFile(className);
//                try (OutputStream out = fileObject.openOutputStream()) {
//                    out.write(cw.toByteArray());
//                }
//            }
//            catch (IOException e) {
//                messager.printMessage(Diagnostic.Kind.ERROR, "shenme 6:" + e.getMessage(), element);
//            }
//            catch (Exception e){
//
//            }
//        }
//        return true;
//    }
//
//    private byte[] getBytecode(Element element) {
//        // 实现获取类字节码的逻辑，可能涉及到读取文件系统或类路径中的 `.class` 文件
//        return new byte[0]; // 仅示例，实际应返回字节码
//    }
//
//}
