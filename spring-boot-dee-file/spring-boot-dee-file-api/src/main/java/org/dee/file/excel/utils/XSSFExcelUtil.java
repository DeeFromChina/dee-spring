package org.dee.file.excel.utils;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import lombok.SneakyThrows;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.dee.file.excel.entity.DeeExcelCell;
import org.dee.file.excel.entity.DeeExcelSheet;
import org.dee.file.excel.enums.ExcelOperationType;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 07版excel
 */
public class XSSFExcelUtil extends ExcelUtil {

    /**
     * 获取excel内容，返回String[]数组
     * 根据参数读取内容
     * @param fis excel文件流
     * @param needReadSheetNums 需要读取的excel的sheet列表
     * @param startReadRowNums  每个sheet开始读取的行数
     * @return
     */
    @SneakyThrows
    public static List<List<String[]>> getXSSFWorkBook(InputStream fis, Integer[] needReadSheetNums, Integer[] startReadRowNums) {
        try{
            //根据路径获取这个操作xls的实例  创建一个工作簿HSSFWorkbook
            XSSFWorkbook wb = new XSSFWorkbook(fis);
            //比对sheet数有没有问题
            for(int i = 0; i < needReadSheetNums.length; i++){
                if(needReadSheetNums[i] > wb.getNumberOfSheets()){
                    throw new RuntimeException("can't not find sheet; { seqNo: " + needReadSheetNums[i] + " } ");
                }
            }

            List<List<String[]>> list = new ArrayList<>();
            for(int i = 0; i < needReadSheetNums.length; i++){
                XSSFSheet xssfSheet = wb.getSheetAt(needReadSheetNums[i]);
                //获取内容
                List<String[]> dataList = getEntityContent(xssfSheet, startReadRowNums[i]);
                list.add(dataList);
            }
            return list;
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取excel内容，返回对象数组
     * 根据参数读取内容
     * @param fis excel文件流
     * @param sheetParams sheet配置项
     * @return
     */
    @SneakyThrows
    public static List<List<?>> getXSSFWorkBook(InputStream fis, DeeExcelSheet[] sheetParams) {
        //根据路径获取这个操作xls的实例  创建一个工作簿HSSFWorkbook
        XSSFWorkbook wb = new XSSFWorkbook(fis);
        //比对sheet数有没有问题
        checkSheetNums(wb, sheetParams);

        List<List<?>> list = new ArrayList<>();
        for(int i = 0; i < sheetParams.length; i++){
            DeeExcelSheet excelSheet = sheetParams[i];
            if(ObjectUtils.isNull(excelSheet.getEntityClass()) && StrUtil.isNotEmpty(excelSheet.getMapperClassPath())) {
                excelSheet.setEntityClass(Class.forName(excelSheet.getMapperClassPath()));
            }
            int seqNo = excelSheet.getSheetNo();
            XSSFSheet xssfSheet = wb.getSheetAt(seqNo);

            //查询sheet名有没有问题
            checkSheetNames(xssfSheet, excelSheet);

            //检查sheet的表头有没有问题
            checkSheetTitle(xssfSheet, excelSheet);

            //获取内容
            List<?> dataList = getEntityContent(xssfSheet, excelSheet);
            list.add(dataList);
        }
        return list;
    }

    /**
     * 比对sheet数有没有问题
     * @param wb workbook对象
     * @param sheetParams sheet配置项
     */
    @SneakyThrows
    private static void checkSheetNums(XSSFWorkbook wb, DeeExcelSheet[] sheetParams) {
        //比对sheet数有没有问题
        int sheetNums = wb.getNumberOfSheets();
        for(int i = 0; i < sheetParams.length; i++){
            int seqNo = sheetParams[i].getSheetNo();
            if(seqNo > sheetNums){
                throw new RuntimeException("can't find sheet; { seqNo: " + seqNo + ", sheetName: " + sheetParams[i].getSheetName() + " }");
            }
        }
    }

    /**
     * 检查sheet名有没有问题
     * @param xssfSheet sheet对象
     * @param sheetParam sheet配置项
     */
    @SneakyThrows
    private static void checkSheetNames(XSSFSheet xssfSheet, DeeExcelSheet sheetParam) {
        //查询sheet名有没有问题
        if(!xssfSheet.getSheetName().equals(sheetParam.getSheetName())){
            throw new RuntimeException(
                    "sheet not same name; { seqNo: " + sheetParam.getSheetNo() + ", sheetName: " + sheetParam.getSheetName() + " }; " +
                            "can't find sheet: " + xssfSheet.getSheetName());
        }
    }

    /**
     * 检查sheet的表头有没有问题
     * @param xssfSheet sheet对象
     * @param sheetParam sheet配置项
     */
    @SneakyThrows
    private static void checkSheetTitle(XSSFSheet xssfSheet, DeeExcelSheet sheetParam) {
        List<DeeExcelCell> cells = sheetParam.getCells();
        XSSFRow row = xssfSheet.getRow(0);
        int cellNums = row.getLastCellNum();
        //比对表头数量有没有问题
        if(cellNums != cells.size()){
            throw new RuntimeException("import sheet title not same to config");
        }

        if("byName".equals(sheetParam.getImportCellType())){
            for(int j = 0; j < cellNums; j++){
                XSSFCell cell = row.getCell(j);
                String title = getCell(cell);
                boolean isPass = false;
                for(DeeExcelCell excelCell : cells){
                    if(title.equals(excelCell.getFieldName())){
                        //设置数据字典
                        ExcelCellUtil.setKeyValue(excelCell, ExcelOperationType.EXPORT);

                        isPass = true;
                        break;
                    }
                }
                if(!isPass){
                    throw new RuntimeException("can't find title; title: " + title);
                }
            }
        }
    }

    /**
     * 获取单个sheet内容，返回String[]数组
     * @param xssfSheet sheet对象
     * @param startReadRowNum 开始读取行数
     * @return
     */
    public static List<String[]> getEntityContent(XSSFSheet xssfSheet, Integer startReadRowNum){
        List<String[]> dataList = new ArrayList<>();
        //处理导入数据
        for(int n = startReadRowNum; n <= xssfSheet.getLastRowNum(); n++){
            XSSFRow row = xssfSheet.getRow(n);
            String[] data = new String[row.getLastCellNum()];
            for(int m = 0; m < row.getLastCellNum(); m++){
                String value = getCell(row.getCell(m));
                data[m] = value;
            }
            dataList.add(data);
        }
        return dataList;
    }

    /**
     * 获取单个sheet内容，返回对象数组
     * @param xssfSheet sheet对象
     * @param sheetParam sheet配置项
     * @return
     */
    private static List<?> getEntityContent(XSSFSheet xssfSheet, DeeExcelSheet sheetParam){
        List<Map<String, Object>> dataList = null;
        if("byName".equals(sheetParam.getImportCellType())){
            dataList = getContentByName(xssfSheet, sheetParam);
        }else if("byCol".equals(sheetParam.getImportCellType())){
            dataList = getContentByCol(xssfSheet, sheetParam);
        }
        return BeanUtil.copyToList(dataList, sheetParam.getEntityClass());
    }

    /**
     * 根据表头字段获取内容
     * @param xssfSheet
     * @param sheetParam
     * @return
     */
    private static List<Map<String, Object>> getContentByName(XSSFSheet xssfSheet, DeeExcelSheet sheetParam) {
        List<DeeExcelCell> cells = sheetParam.getCells();
        XSSFRow rowTitle = xssfSheet.getRow(0);
        int cellNums = rowTitle.getLastCellNum();

        Map<String, DeeExcelCell> nameCell = new HashMap<>();
        cells.forEach(cell -> {
            nameCell.put(cell.getFieldName(), cell);
        });

        String[] nameSeq = new String[cellNums];
        for(int m = 0; m < cellNums; m++){
            nameSeq[m] = getCell(rowTitle.getCell(m));
        }

        //处理导入数据
        List<Map<String, Object>> dataList = new ArrayList<>();
        for(int n = 1; n <= xssfSheet.getLastRowNum(); n++){
            XSSFRow row = xssfSheet.getRow(n);
            Map<String, Object> map = new HashMap<>();
            for(int m = 0; m < cellNums; m++){
                DeeExcelCell cell = nameCell.get(nameSeq[m]);
                String value = getCell(row.getCell(m));
                //处理keyValue替换
                value = getKey(cell, value);
                map.put(cell.getFieldCode(), value);
            }
            dataList.add(map);
        }
        return dataList;
    }

    /**
     * 根据表头字段列序获取内容
     * @param xssfSheet
     * @param sheetParam
     * @return
     */
    private static List<Map<String, Object>> getContentByCol(XSSFSheet xssfSheet, DeeExcelSheet sheetParam) {
        List<DeeExcelCell> cells = sheetParam.getCells();
        List<Map<String, Object>> dataList = new ArrayList<>();

        //处理导入数据
        for(int n = 1; n <= xssfSheet.getLastRowNum(); n++){
            XSSFRow row = xssfSheet.getRow(n);
            Map<String, Object> map = new HashMap<>();
            for(int m = 0; m < row.getLastCellNum(); m++){
                DeeExcelCell cell = cells.get(m);
                String value = getCell(row.getCell(m));
                //处理keyValue替换
                value = getKey(cell, value);
                map.put(cell.getFieldCode(), value);
            }
            dataList.add(map);
        }
        return dataList;
    }

}
