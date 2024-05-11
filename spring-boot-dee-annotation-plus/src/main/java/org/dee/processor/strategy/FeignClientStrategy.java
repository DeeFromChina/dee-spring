package org.dee.processor.strategy;

import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.Element;
import java.lang.annotation.Annotation;
import java.util.Set;

/**
 * FeignClient策略
 * 1、有使用@FeignClient注解
 * 2、@RequestMapping注解标记在类上
 * 3、@RequestMapping注解没有传入参数
 * 4、类继承了BaseWebController
 */
public class FeignClientStrategy {

    /**
     * 1、有使用@RequestMapping注解
     * @param roundEnv
     * @param annotationClass
     * @return
     */
    public static Set<? extends Element> getRequestMappingClasses(RoundEnvironment roundEnv, Class<? extends Annotation> annotationClass) {
        return roundEnv.getElementsAnnotatedWith(annotationClass);
    }

}
