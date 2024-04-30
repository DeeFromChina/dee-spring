package org.dee.processor;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.TypeElement;
import java.lang.annotation.Annotation;
import java.util.Set;


@SupportedAnnotationTypes("org.springframework.web.bind.annotation.RequestMapping")
@SupportedSourceVersion(SourceVersion.RELEASE_8)
public class RequestMappingProcessor2 extends AbstractProcessor {

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        for (Element element : roundEnv.getElementsAnnotatedWith(RequestMapping.class)) {
            if (element.getKind() == ElementKind.CLASS) {
                TypeElement typeElement = (TypeElement) element;
                String className = typeElement.getQualifiedName().toString();
                RequestMapping requestMapping = element.getAnnotation(RequestMapping.class);
                System.out.println("className:" + className);
                System.out.println("requestMappingValue:" + requestMapping.value());
                // 检查并修改注解参数
                if ("/kpiStageConfig".equals(requestMapping.value())) {
                    // 创建一个新的 RequestMapping 实例
                    RequestMapping newRequestMapping = new RequestMapping() {
                        @Override
                        public Class<? extends Annotation> annotationType() {
                            return RequestMapping.class;
                        }

                        @Override
                        public String name() {
                            return "";
                        }

                        @Override
                        public String[] value() {
                            return new String[]{"/kpiStageConfig1"};
                        }

                        @Override
                        public String[] path() {
                            return new String[0];
                        }

                        @Override
                        public RequestMethod[] method() {
                            return new RequestMethod[0];
                        }

                        @Override
                        public String[] params() {
                            return new String[0];
                        }

                        @Override
                        public String[] headers() {
                            return new String[0];
                        }

                        @Override
                        public String[] consumes() {
                            return new String[0];
                        }

                        @Override
                        public String[] produces() {
                            return new String[0];
                        }
                        // 重写其他方法...
                    };
                    // 使用JavacTrees和TreeMaker来修改AST
                }
            }
        }
        return true;
    }
}
