//package org.dee.processor.test.demo;
//
//import javax.annotation.processing.AbstractProcessor;
//import javax.annotation.processing.RoundEnvironment;
//import javax.annotation.processing.SupportedAnnotationTypes;
//import javax.annotation.processing.SupportedSourceVersion;
//import javax.lang.model.SourceVersion;
//import javax.lang.model.element.Element;
//import javax.lang.model.element.ElementKind;
//import javax.lang.model.element.TypeElement;
//import java.lang.annotation.Annotation;
//import java.util.Set;
//
//
//@SupportedAnnotationTypes("org.springframework.web.bind.annotation.RequestMapping")
//@SupportedSourceVersion(SourceVersion.RELEASE_8)
//public class HelloWorldProcessor extends AbstractProcessor {
//
//    @Override
//    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
//        for (Element element : roundEnv.getElementsAnnotatedWith(HelloWorld.class)) {
//            if (element.getKind() == ElementKind.CLASS) {
//                TypeElement typeElement = (TypeElement) element;
//                String className = typeElement.getQualifiedName().toString();
//                HelloWorld helloWorld = element.getAnnotation(HelloWorld.class);
//                System.out.println("className:" + className);
//                System.out.println("requestMappingValue:" + helloWorld.value());
//                // 检查并修改注解参数
//                // 创建一个新的 HelloWorld 实例
//                HelloWorld newHelloWorld = new HelloWorld() {
//                    @Override
//                    public Class<? extends Annotation> annotationType() {
//                        return HelloWorld.class;
//                    }
//
//                    @Override
//                    public String value() {
//                        return "/b";
//                    }
//                    // 重写其他方法...
//                };
//                // 使用JavacTrees和TreeMaker来修改AST
//
//            }
//        }
//        return true;
//    }
//}
