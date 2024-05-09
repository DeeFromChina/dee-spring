package org.dee.test.utils;

import cn.hutool.core.util.StrUtil;
import com.sun.tools.javac.tree.JCTree;
import com.sun.tools.javac.util.List;
import lombok.SneakyThrows;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

public class ControllerUtil {

    /**
     * 根据泛型参数，创建新的Controller的RequestMapping值
     * @param parentClassPath
     * @param controllerClassPath
     * @return
     */
    @SneakyThrows
    public static String createControllerRequestMappingName(String parentClassPath, String controllerClassPath) {
        //判断是否继承BaseWebController
        if(!ClassUtil.isExtends(parentClassPath, controllerClassPath)){
            return "";
        }
        return createControllerRequestMappingNameImpl(Class.forName(controllerClassPath));
    }

    /**
     * 根据泛型参数，创建新的Controller的RequestMapping值
     * @param parentClassPath
     * @param controllerClass
     * @return
     */
    @SneakyThrows
    public static String createControllerRequestMappingName(String parentClassPath, Class<?> controllerClass) {
        //判断是否继承BaseWebController
        if(!ClassUtil.isExtends(parentClassPath, controllerClass)){
            return "";
        }
        return createControllerRequestMappingNameImpl(controllerClass);
    }

    /**
     * 根据泛型参数，创建新的Controller的RequestMapping值
     * @param parentClass
     * @param controllerClass
     * @return
     */
    public static String createControllerRequestMappingName(Class<?> parentClass, Class<?> controllerClass) {
        //判断是否继承BaseWebController
        if(!ClassUtil.isExtends(parentClass, controllerClass)){
            return "";
        }
        return createControllerRequestMappingNameImpl(controllerClass);
    }

    private static String createControllerRequestMappingNameImpl(Class<?> controllerClass) {
        String controllerRequestMappingName = "/";
        //获取继承的类
        Type superclassType = controllerClass.getGenericSuperclass();

        // 检查是否是一个参数化类型（即带有泛型参数的类型）
        if (superclassType instanceof ParameterizedType) {
            ParameterizedType parameterizedType = (ParameterizedType) superclassType;

            // 获取泛型参数数组
            Type[] typeArguments = parameterizedType.getActualTypeArguments();

            // 假设B<C>是A的唯一泛型超类，那么typeArguments[0]就是C的类型
            if (typeArguments.length > 0) {
                Type cType = typeArguments[0];

                // 如果C是一个具体的类类型（而不是另一个泛型类型或通配符类型），则可以通过Class对象获取
                if (cType instanceof Class) {
                    Class<?> entityClass = (Class<?>) cType;
                    String entityBeanName = StrUtil.lowerFirst(entityClass.getSimpleName());
                    controllerRequestMappingName += entityBeanName;
                } else {
                    // 如果C是一个泛型类型或通配符类型，则需要进一步处理
                    System.out.println("C is a generic type or wildcard type: " + cType);
                }
            }
        } else {
            System.out.println("A does not have a parameterized superclass or the information is not accessible.");
        }
        return controllerRequestMappingName;
    }

    /**
     * 根据泛型参数，创建新的Controller的RequestMapping值
     * @param jcClassDecl
     * @return
     */
    public static String createControllerRequestMappingName(JCTree.JCClassDecl jcClassDecl) {
        JCTree.JCExpression expression = jcClassDecl.getExtendsClause();
        if(expression == null){
            return "";
        }
        //继承的类
        String extendClassName = expression.getTree().type.toString();
        if(!extendClassName.startsWith("org.dee.framework.controller.BaseWebController")){
            return "";
        }
        JCTree.JCTypeApply typeApply = (JCTree.JCTypeApply) expression.getTree();
        List<JCTree.JCExpression> typeArguments = typeApply.getTypeArguments();
        String entityName = typeArguments.get(0).toString();
        //String entityServiceName = typeArguments.get(1).toString();
        return "/" + StrUtil.lowerFirst(entityName);
    }

}
