package org.dee.file.excel.utils;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.ss.usermodel.CellType;
import org.dee.utils.DateUtil;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.dee.file.excel.entity.DeeExcelCell;
import org.dee.utils.ConvertUtil;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

public class ExcelUtil {

    /**
     * 处理keyValue替换
     * @param cell
     * @param value
     * @return
     */
    protected static String getKey(DeeExcelCell cell, Object value) {
        if(ObjectUtil.isEmpty(value)){
            return ConvertUtil.returnString(value);
        }
        //keyValue替换
        if(StrUtil.isNotEmpty(cell.getKeyValueDataSource())){
            Map<String, String> dicts = cell.getKeyValue();
            for(Map.Entry<String, String> entry : dicts.entrySet()){
                if(entry.getValue().equals(value)){
                    return entry.getKey();
                }
            }
            return ConvertUtil.returnString(value);
        }
        //format
        else if(StrUtil.isNotEmpty(cell.getCellFormat())){
            if(value instanceof Date){
                return DateUtil.format((Date) value, cell.getCellFormat());
            }
            DateTime date = DateUtil.parse(ConvertUtil.returnString(value), cell.getCellFormat());
            return DateUtil.format(date, DatePattern.NORM_DATE_PATTERN);
        }
        else{
            return ConvertUtil.returnString(value);
        }
    }

    /**
     * 处理keyValue替换
     * @param cell
     * @param key
     * @return
     */
    protected static String getValue(DeeExcelCell cell, String key) {
        //keyValue替换
        if(!StrUtil.isEmpty(cell.getKeyValueDataSource())){
            Map<String, String> dicts = cell.getKeyValue();
            String value = dicts.get(key);
            return value;
        }
        else{
            return key;
        }
    }

    public static <T> String getCell(T t) {
        if (t == null) {
            return null;
        }
        DecimalFormat df = new DecimalFormat("0.00");
        String value = null;
        if (t instanceof HSSFCell) {
            HSSFCell cell = (HSSFCell) t;
            CellType celltype = cell.getCellType();
            switch (celltype) {
                case BLANK:
                    break;
                case BOOLEAN:
                    value = cell.getBooleanCellValue() + "";
                    break;
                case ERROR:
                    value = cell.getErrorCellValue() + "";
                    break;
                case FORMULA:
                    double dou = cell.getNumericCellValue();
                    value = String.valueOf(dou); // cell.getCellFormula();
                    break;
                case NUMERIC:
                    // 判断是否为日期类型
                    if (org.apache.poi.ss.usermodel.DateUtil.isCellDateFormatted(cell)) {
                        // 用于转化为日期格式
                        Date d = cell.getDateCellValue();
                        SimpleDateFormat formater = new SimpleDateFormat(
                                "yyyy-MM-dd HH:mm:ss");
                        value = formater.format(d);
                    } else {
                        String a = cell.getStringCellValue();
                        if(a.endsWith(".0")){
                            value = a.replace(".0", "");
                        }else{
                            value = df.format(cell.getNumericCellValue());
                        }
                    }

                    break;
                case STRING:
                    value = cell.getStringCellValue();
                    break;
                default:
                    value = cell.getRichStringCellValue().getString();
                    break;
            }
        }
        if (t instanceof XSSFCell) {
            XSSFCell cell = (XSSFCell) t;
            CellType celltype = cell.getCellType();
            switch (celltype) {
                case BLANK:
                    break;
                case BOOLEAN:
                    value = cell.getBooleanCellValue() + "";
                    break;
                case ERROR:
                    value = cell.getErrorCellValue() + "";
                    break;
                case FORMULA:// 获取公式
                    // double dou = cell.getNumericCellValue();
                    // value=String.valueOf(dou); //cell.getCellFormula();
                    break;
                case NUMERIC:
                    // 判断是否为日期类型
                    if (org.apache.poi.ss.usermodel.DateUtil.isCellDateFormatted(cell)) {
                        // 用于转化为日期格式
                        Date d = cell.getDateCellValue();
                        SimpleDateFormat formater = new SimpleDateFormat(
                                "yyyy-MM-dd HH:mm:ss");
                        value = formater.format(d);
                    } else {
                        cell.setCellType(CellType.STRING);
                        value = cell.getStringCellValue();
//                        cell.setCellType(CellType.NUMERIC);
//                        value = df.format(cell.getNumericCellValue());
                    }
                    break;
                case STRING:
                    value = cell.getStringCellValue();
                    break;
                default:
                    value = cell.getRichStringCellValue().getString();
                    break;
            }
        }

        return value;
    }

}
