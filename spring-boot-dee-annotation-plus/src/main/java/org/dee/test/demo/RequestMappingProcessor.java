//package org.dee.processor.test.demo;
//
//import javax.annotation.processing.AbstractProcessor;
//import javax.annotation.processing.RoundEnvironment;
//import javax.annotation.processing.SupportedAnnotationTypes;
//import javax.annotation.processing.SupportedSourceVersion;
//import javax.lang.model.SourceVersion;
//import javax.lang.model.element.Element;
//import javax.lang.model.element.TypeElement;
//import javax.tools.Diagnostic;
//import java.util.Set;
//
//@SupportedAnnotationTypes("org.springframework.web.bind.annotation.RequestMapping")
//@SupportedSourceVersion(SourceVersion.RELEASE_8)
//public class RequestMappingProcessor extends AbstractProcessor {
//
//    @Override
//    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
//        //遍历所有本注解处理器要求的注解
//        for(TypeElement annotationElement : annotations) {
//            //从roundEnv中获取持有注解的所有classElement
//            for (Element classElement : roundEnv.getElementsAnnotatedWith(annotationElement)) {
//                if (!classElement.getKind().isClass()) continue;
//
//                //注解处理器的日志都要使用Messager发送，最终会以编译结果的形式呈现出来；
//                this.processingEnv.getMessager().printMessage(Diagnostic.Kind.NOTE, "test get file:" + classElement.getSimpleName());
//
//                //try {
//                //    printFile();
//                //} catch (IOException e) {
//                //    //出现ERROR等级的日志时，相当于编译报了一个error
//                //    this.processingEnv.getMessager().printMessage(Diagnostic.Kind.ERROR, "error in open File : " + e.getMessage());
//                //}
//            }
//        }
//
//        return true;
//    }
//
//}
