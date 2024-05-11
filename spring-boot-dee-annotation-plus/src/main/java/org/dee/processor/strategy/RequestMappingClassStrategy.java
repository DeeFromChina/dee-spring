package org.dee.processor.strategy;

import cn.hutool.core.util.StrUtil;
import com.sun.tools.javac.code.Attribute;
import com.sun.tools.javac.code.Symbol;
import com.sun.tools.javac.tree.JCTree;
import com.sun.tools.javac.util.List;
import com.sun.tools.javac.util.Pair;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.processing.Messager;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.Element;
import java.lang.annotation.Annotation;
import java.util.Set;

/**
 * RequestMappingClass策略
 * 1、有使用@RequestMapping注解
 * 2、@RequestMapping注解标记在类上
 * 3、@RequestMapping注解没有传入参数
 * 4、类继承了BaseWebController
 */
public class RequestMappingClassStrategy {

    /**
     * 1、有使用@RequestMapping注解
     * @param roundEnv
     * @param annotationClass
     * @return
     */
    public static Set<? extends Element> getRequestMappingClasses(RoundEnvironment roundEnv, Class<? extends Annotation> annotationClass) {
        return roundEnv.getElementsAnnotatedWith(RequestMapping.class);
    }

    /**
     * 2、@RequestMapping注解标记在类上
     * @param element
     * @return
     */
    public static boolean isClass(Element element) {
        return element.getKind().isClass();
    }

    /**
     * 3、@RequestMapping注解没有传入参数
     * @param anno
     * @return false：没有传入参数，true：有传入参数
     */
    public static boolean annotationHasParam(JCTree.JCAnnotation anno) {
        boolean hasParam = true;
        List<Pair<Symbol.MethodSymbol, Attribute>> pairs = anno.attribute.values;
        if(pairs == null || pairs.size() == 0) {
            return false;
        }
        for(Pair<Symbol.MethodSymbol, Attribute> pair : pairs){
            if("value".equals(pair.fst.name.toString())){
                hasParam = StrUtil.isNotEmpty(pair.snd.getValue().toString());
            }
            else if("path".equals(pair.fst.name.toString())){
                hasParam = StrUtil.isNotEmpty(pair.snd.getValue().toString());
            }
        }
        return hasParam;
    }

    /**
     * 4、类继承了BaseWebController
     * @param expression
     * @return
     */
    @Deprecated
    public static boolean isExtendBaseWebController(JCTree.JCExpression expression) {
        String extendClassName = expression.getTree().type.toString();
        return extendClassName.startsWith("org.dee.framework.controller.BaseWebController");
    }

}
