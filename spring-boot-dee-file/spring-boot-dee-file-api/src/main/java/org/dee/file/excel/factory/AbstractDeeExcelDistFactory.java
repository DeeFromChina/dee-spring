package org.dee.file.excel.factory;

import org.dee.file.excel.entity.DeeExcelCell;
import org.dee.file.excel.entity.DeeExcelDist;
import org.dee.file.excel.enums.ExcelOperationType;

import java.util.LinkedHashMap;
import java.util.List;

public abstract class AbstractDeeExcelDistFactory {

    public abstract List<DeeExcelDist> queryExcelDists(String keyValueType);

    /**
     *
     * @param cell
     * @param type 导入还是导出，import or export
     * @return
     */
    public LinkedHashMap<String, String> getKeyValue(DeeExcelCell cell, ExcelOperationType type) {
        List<DeeExcelDist> dists = this.queryExcelDists(cell.getReplaceType());
        LinkedHashMap<String, String> map = new LinkedHashMap<>();
        if(ExcelOperationType.IMPORT.equals(type)){
            for(DeeExcelDist dist : dists){
                map.put(dist.getValue(), dist.getKey());
            }
        } else if (ExcelOperationType.EXPORT.equals(type)) {
            for(DeeExcelDist dist : dists){
                map.put(dist.getKey(), dist.getValue());
            }
        }

        return map;
    }

}
