package org.dee.file.excel.utils;

import org.apache.commons.lang3.StringUtils;
import org.dee.file.excel.entity.DeeExcelCell;
import org.dee.file.excel.enums.ExcelOperationType;
import org.dee.file.excel.factory.AbstractDeeExcelDistFactory;
import org.dee.file.excel.producer.ExcelDistProducer;

import java.util.List;

public class ExcelCellUtil {

    public static void setCellsKeyValue(List<DeeExcelCell> cells, ExcelOperationType type) {
        for(DeeExcelCell cell : cells){
            if(!StringUtils.isEmpty(cell.getKeyValueDataSource())){
                setKeyValue(cell, type);
            }
        }
    }

    /**
     *
     * @param cell
     * @param type 导入还是导出，import or export
     */
    public static void setKeyValue(DeeExcelCell cell, ExcelOperationType type) {
        //由工厂提供固定格式数据，再由公共方法进行数据重新封装及转换
        AbstractDeeExcelDistFactory factory = ExcelDistProducer.getFactory(cell);
        if(factory != null){
            cell.setKeyValue(factory.getKeyValue(cell, type));
        }
    }

}
