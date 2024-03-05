package org.dee.file.excel.producer;

import cn.hutool.core.util.StrUtil;
import org.dee.file.excel.entity.DeeExcelCell;
import org.dee.file.excel.factory.AbstractDeeExcelDistFactory;
import org.dee.file.excel.factory.impl.EnumFactory;
import org.dee.web.context.SpringContext;

public class ExcelDistProducer {

    public static AbstractDeeExcelDistFactory getFactory(DeeExcelCell cell) {
        String dataSource = cell.getKeyValueDataSource();
        if(StrUtil.isEmpty(dataSource)){
            return null;
        }
        switch (dataSource) {
            case "enum" :
                return (EnumFactory) SpringContext.getBean("enumFactory");
            default :
                return null;
        }
    }

}
