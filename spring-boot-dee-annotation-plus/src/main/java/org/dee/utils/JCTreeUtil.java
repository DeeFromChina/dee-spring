package org.dee.utils;

import com.sun.tools.javac.tree.JCTree;
import com.sun.tools.javac.util.List;

public class JCTreeUtil {

    /**
     * 是否继承extendClass
     * @param expression
     * @return
     */
    public static boolean isExtendClass(JCTree.JCExpression expression, String extendClass) {
        String extendClassName = expression.getTree().type.toString();
        return extendClassName.startsWith(extendClass);
    }

    /**
     * 获取继承类的泛型参数
     * @param jcClassDecl
     * @param extendClass
     * @param index 继承类的第几个泛型参数，从0开始
     * @return
     */
    public static String getExtendClassArgument(JCTree.JCClassDecl jcClassDecl, String extendClass, int index) {
        JCTree.JCExpression expression = jcClassDecl.getExtendsClause();
        if(expression == null){
            return null;
        }
        //类是否继承了extendClass
        if(!isExtendClass(expression, extendClass)){
            return null;
        }
        JCTree.JCTypeApply typeApply = (JCTree.JCTypeApply) expression.getTree();
        List<JCTree.JCExpression> typeArguments = typeApply.getTypeArguments();
        return typeArguments.get(index).toString();
    }

}
