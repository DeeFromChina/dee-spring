package org.dee.processor.factory;

import com.sun.tools.javac.tree.JCTree;

public interface NewRequestMappingFactory {

    /**
     * 根据泛型参数，创建新的Controller的RequestMapping值
     * @param expression
     * @return
     */
    String createNewRequestMappingName(JCTree.JCExpression expression);

}
