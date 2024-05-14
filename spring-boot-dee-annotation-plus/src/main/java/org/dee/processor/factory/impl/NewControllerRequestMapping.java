package org.dee.processor.factory.impl;

import cn.hutool.core.util.StrUtil;
import com.sun.tools.javac.tree.JCTree;
import org.dee.processor.factory.NewRequestMappingFactory;
import org.dee.utils.JCTreeUtil;

public class NewControllerRequestMapping implements NewRequestMappingFactory {

    public final static String extendClass = "org.dee.framework.controller.BaseWebController";

    /**
     * 根据泛型参数，创建新的Controller的RequestMapping值
     * @param expression
     * @return
     */
    @Override
    public String createNewRequestMappingName(JCTree.JCExpression expression) {
        String entityName = JCTreeUtil.getExtendClassArgument(expression, extendClass, 0);
        return StrUtil.isNotEmpty(entityName) ? "/" + StrUtil.lowerFirst(entityName) : entityName;
    }

}
