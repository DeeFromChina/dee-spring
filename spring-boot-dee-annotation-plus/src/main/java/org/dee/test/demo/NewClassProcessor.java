//package org.dee.processor.test.demo;
//
//import com.google.auto.service.AutoService;
//import com.squareup.javapoet.AnnotationSpec;
//import com.squareup.javapoet.JavaFile;
//import com.squareup.javapoet.TypeSpec;
//import org.springframework.web.bind.annotation.RequestMapping;
//
//import javax.annotation.processing.*;
//import javax.lang.model.SourceVersion;
//import javax.lang.model.element.*;
//import javax.tools.Diagnostic;
//import java.io.IOException;
//import java.util.Set;
//
//@SupportedAnnotationTypes("org.dee.processor.test.demo.HelloWorld" )
//@SupportedSourceVersion(SourceVersion.RELEASE_8)
//@AutoService(Processor.class)
//public class NewClassProcessor extends AbstractProcessor {
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
//        for (Element element : roundEnv.getElementsAnnotatedWith(HelloWorld.class)) {
//            if(element.getKind() == ElementKind.METHOD){
//                ExecutableElement method = (ExecutableElement) element;
//
//                //获取方法所在类的信息
//                TypeElement classElement = (TypeElement) method.getEnclosingElement();
//                String packageName = processingEnv.getElementUtils().getPackageOf(classElement).toString();
//                String className = classElement.getSimpleName().toString();
//
//                TypeSpec typeSpecClass = TypeSpec.classBuilder(className + "1")
//                        .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
//                        .addAnnotation(AnnotationSpec.builder(RequestMapping.class).build())
//                        .build();
//                        //.addField(FieldSpec.builder(String.class, "name")
//                        //                .addModifiers(Modifier.PRIVATE)
//                        //                .build();
//
//                // 生成一个新的Java类，该类位于被注解的包中
//                JavaFile javaFile = JavaFile.builder(packageName, typeSpecClass).build();
//
//                try{
//                    javaFile.writeTo(filer);
//                } catch (IOException e) {
//                    messager.printMessage(Diagnostic.Kind.NOTE, "代码生成失败:" + className);
//                    throw new RuntimeException();
//                }
//            }
//        }
//        return false;
//    }
//}
