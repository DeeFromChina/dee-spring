package org.dee.processor.strategy;

import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.Element;
import java.lang.annotation.Annotation;
import java.util.Set;

/**
 * FeignClient策略
 * 1、有使用@FeignClient注解
 */
public class FeignClientStrategy {

    /**
     * 1、有使用@FeignClient注解
     * @param roundEnv
     * @param annotationClass
     * @return
     */
    public static Set<? extends Element> getFeignClientClasses(RoundEnvironment roundEnv, Class<? extends Annotation> annotationClass) {
        return roundEnv.getElementsAnnotatedWith(annotationClass);
    }

}
