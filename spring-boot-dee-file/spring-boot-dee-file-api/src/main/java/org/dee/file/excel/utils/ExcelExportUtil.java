package org.dee.file.excel.utils;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import lombok.SneakyThrows;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddressList;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFDataValidation;
import org.dee.file.excel.entity.DeeExcelCell;
import org.dee.file.excel.entity.DeeExcelSheet;
import org.dee.file.excel.enums.ExcelOperationType;
import org.dee.utils.ConvertUtil;

import java.util.*;

public class ExcelExportUtil {

    //设置下拉框个数
    private static final int LIST_SIZE = 50;

    //excel列和数字对照关系(key列数,value是列名)
    public static Map<Integer, String> excelColMap = new HashMap<Integer, String>();

    //excel列和数字对照关系(key列名,value是列数)
    private static Map<String, Integer> excelColMap2 = new HashMap<String, Integer>();

    /**
     * 初始化excel列
     */
    private static void initExcelCol() {
        if (excelColMap == null || excelColMap.size() == 0) {
            StringBuffer az = new StringBuffer();
            az.append("A,B,C,D,E,F,G,H,I,J,K,L,M,N,O,P,Q,R,S,T,U,V,W,X,Y,Z,");
            az.append("AA,AB,AC,AD,AE,AF,AG,AH,AI,AJ,AK,AL,AM,AN,AO,AP,AQ,AR,AS,AT,AU,AV,AW,AX,AY,AZ,");
            az.append("BA,BB,BC,BD,BE,BF,BG,BH,BI,BJ,BK,BL,BM,BN,BO,BP,BQ,BR,BS,BT,BU,BV,BW,BX,BY,BZ,");
            az.append("CA,CB,CC,CD,CE,CF,CG,CH,CI,CJ,CK,CL,CM,CN,CO,CP,CQ,CR,CS,CT,CU,CV,CW,CX,CY,CZ,");
            az.append("DA,DB,DC,DD,DE,DF,DG,DH,DI,DJ,DK,DL,DM,DN,DO,DP,DQ,DR,DS,DT,DU,DV,DW,DX,DY,DZ,");
            az.append("EA,EB,EC,ED,EE,EF,EG,EH,EI,EJ,EK,EL,EM,EN,EO,EP,EQ,ER,ES,ET,EU,EV,EW,EX,EY,EZ,");
            az.append("FA,FB,FC,FD,FE,FF,FG,FH,FI,FJ,FK,FL,FM,FN,FO,FP,FQ,FR,FS,FT,FU,FV,FW,FX,FY,FZ,");
            az.append("GA,GB,GC,GD,GE,GF,GG,GH,GI,GJ,GK,GL,GM,GN,GO,GP,GQ,GR,GS,GT,GU,GV,GW,GX,GY,GZ");
            String[] azs = az.toString().split(",");
            for (int j = 0; j < azs.length; j++) {
                excelColMap.put(j, azs[j]);
                excelColMap2.put(azs[j], j);
            }
        }
    }

    /**
     * 下载模版，封装excel，返回workbook(单sheet页)
     * @param sheetParam
     * @return
     */
    @SneakyThrows
    public static Workbook download(DeeExcelSheet sheetParam) {
        List<List<?>> list = new ArrayList<>();
        list.add(null);
        DeeExcelSheet[] sheetParams = new DeeExcelSheet[1];
        sheetParams[0] = sheetParam;
        return download(list, sheetParams);
    }

    /**
     * 下载模版，封装excel，返回workbook
     * @param dataList 数据结果集
     * @param sheetParams
     * @return
     */
    @SneakyThrows
    public static Workbook download(List<List<?>> dataList, DeeExcelSheet... sheetParams) {
        return createWorkbook(ExcelOperationType.DOWNLOAD, dataList, sheetParams);
    }

    /**
     * 导出，封装excel，返回workbook(单sheet页)
     * @param dataList
     * @param sheetParam
     * @return
     */
    @SneakyThrows
    public static <T> Workbook getWorkbook(List<T> dataList, DeeExcelSheet sheetParam, Class<T> entityClass) {
        List<List<?>> list = new ArrayList<>();
        list.add(dataList);
        DeeExcelSheet[] sheetParams = new DeeExcelSheet[1];
        sheetParams[0] = sheetParam;
        return getWorkbook(list, sheetParams);
    }

    /**
     * 导出，封装excel，返回workbook
     * @param dataList
     * @param sheetParams
     * @return
     */
    @SneakyThrows
    public static Workbook getWorkbook(List<List<?>> dataList, DeeExcelSheet... sheetParams) {
        return createWorkbook(ExcelOperationType.EXPORT, dataList, sheetParams);
    }

    /**
     * 封装excel，返回workbook
     * @param portType
     * @param dataList
     * @param sheetParams
     * @return
     */
    @SneakyThrows
    private static Workbook createWorkbook(ExcelOperationType portType, List<List<?>> dataList, DeeExcelSheet... sheetParams) {
        //初始化excel列
        initExcelCol();
        //创建workbook
        SXSSFWorkbook sxssfWorkbook = new SXSSFWorkbook(1000);
        for(int i = 0; i < sheetParams.length; i++){
            DeeExcelSheet sheetConfig = sheetParams[i];
            // 生成一个表格
            SXSSFSheet sheet = (SXSSFSheet) sxssfWorkbook.createSheet(sheetConfig.getSheetName());
            Map<Integer, Row> rowMap = new HashMap<>();

            // 创建表格标题行 第一行
            Row titleRow = createTitle(sheet, sheetConfig);
            rowMap.put(0, titleRow);

            List<?> datas = dataList.get(i);
            //判断是下载导入模板还是导出
            if(datas == null){
                //下载导入模板，内容允许为空
                if(ExcelOperationType.DOWNLOAD.equals(portType)){
                    continue;
                }
                //导出，内容不允许为空
                else if(ExcelOperationType.EXPORT.equals(portType)){
                    throw new Exception("dataList[" + i + "] is null");
                }
            }

            int num = 1;
            for(Object o : datas){
                Map<String, Object> data = BeanUtil.beanToMap(o);
                for(DeeExcelCell cellConfig : sheetConfig.getCells()){
                    Object value = createCellValue(data, cellConfig);
                    //获取行对象
                    Row row = getRow(sheet, num, rowMap);
                    //其他行
                    createRow(sxssfWorkbook, sheet, row, num, cellConfig, value);
                }
                num++;
            }
        }
        return sxssfWorkbook;
    }

    /**
     * 生成单元格数值
     * @param data
     * @param cellConfig
     * @return
     */
    private static Object createCellValue(Map<String, Object> data, DeeExcelCell cellConfig) {
        if(!cellConfig.getFieldCode().contains(".")){
            return data.get(cellConfig.getFieldCode());
        }
        String subKey = cellConfig.getFieldCode().split("\\.")[0];
        String subValue = cellConfig.getFieldCode().split("\\.")[1];
        Object subEntity = data.get(subKey);
        if(subEntity != null){
            if(subEntity instanceof HashMap){
                return ((HashMap)subEntity).get(subValue);
            }
            //else if(subEntity.getClass().getName().startsWith("com.sunline")){
            //    Map<String, Object> subMap = BeanUtil.beanToMap(subEntity);
            //    return subMap.get(subValue);
            //}
        }
        return null;
    }

    /**
     * 创建表格标题行 第一行
     * @param sheet sheet页
     * @param sheetConfig sheet配置项
     */
    private static Row createTitle(SXSSFSheet sheet, DeeExcelSheet sheetConfig) {
        // 创建表格标题行 第一行
        Row titleRow = sheet.createRow(0);
        for(DeeExcelCell cellConfig : sheetConfig.getCells()){
            titleRow.createCell(excelColMap2.get(cellConfig.getColNum())).setCellValue(cellConfig.getFieldName());
            //设置每列列宽
            sheet.setColumnWidth(excelColMap2.get(cellConfig.getColNum()), (cellConfig.getWidth() == null ? 16 : cellConfig.getWidth()) * 256);
            //设置数据字典
            ExcelCellUtil.setKeyValue(cellConfig, ExcelOperationType.EXPORT);
            //设置下拉框
            if(cellConfig.getKeyValue() != null){
                //超过下拉个数，不设置下拉框（excel特性，超过255个字符下拉框无效）
                if(cellConfig.getKeyValue().size() <= LIST_SIZE){
                    Collection<String> valueArrs = cellConfig.getKeyValue().values();
                    cellConfig.setValuesArrs(valueArrs.toArray(new String[valueArrs.size()]));
                }
            }
            //设置单元格下拉框
            if (cellConfig.getValuesArrs() != null) {
                setSelectList(sheet, cellConfig.getValuesArrs(), 1, 1000, excelColMap2.get(cellConfig.getColNum()), excelColMap2.get(cellConfig.getColNum()));
            }
        }
        return titleRow;
    }

    /**
     * 创建内容行
     * @param sxssfWorkbook
     * @param sheet
     * @param num
     * @param cellConfig
     * @param cellValue
     * @return
     */
    @SneakyThrows
    private static Row createRow(SXSSFWorkbook sxssfWorkbook, SXSSFSheet sheet, Row row, int num, DeeExcelCell cellConfig, Object cellValue) {
        //创建单元格
        Cell cell = row.createCell(excelColMap2.get(cellConfig.getColNum()));
        //设置单元格内容
        setCellValue(cellConfig, cell, cellValue);
        //设置单元格下拉框
        if (cellConfig.getValuesArrs() != null && num > 1000) {
            setSelectList(sheet, cellConfig.getValuesArrs(), 1, num, excelColMap2.get(cellConfig.getColNum()), excelColMap2.get(cellConfig.getColNum()));
        }
        //设置单元格颜色
        if (cellConfig.getCellColor() != 0) {
            setCellColor(cell, sxssfWorkbook, cellConfig.getCellColor());
        }
        //是否合并单元格
        if (cellConfig.getIsMerge() != null && cellConfig.getIsMerge() == 1) {
            //合并单元格
            setSumCell(cellConfig, sheet);
        }
        return row;
    }

    /**
     * 获取行对象
     * @param sheet
     * @param num
     * @return
     */
    private static Row getRow(SXSSFSheet sheet, int num, Map<Integer, Row> rowMap) {
        Row row = null;
        if (rowMap.get(num) == null) {
            row = sheet.createRow(num);
            rowMap.put(num, row);
        } else {
            row = rowMap.get(num);
        }
        return row;
    }

    /**
     * 设置单元格内容
     * @param cellConfig
     * @param cell
     * @param cellValue
     */
    private static void setCellValue(DeeExcelCell cellConfig, Cell cell, Object cellValue) {
        String type = cellConfig.getCellDataType() == null ? "String" : cellConfig.getCellDataType();

        //数据字典替换
        if(!StrUtil.isEmpty(cellConfig.getKeyValueDataSource())){
            Map<String, String> keyValueMap = cellConfig.getKeyValue();
            cellValue = keyValueMap.get(ConvertUtil.returnString(cellValue));
            cell.setCellValue(ConvertUtil.returnString(cellValue));
        }
        else if ("number".equals(type)) {
            cell.setCellValue(ConvertUtil.returnDouble(cellValue));
        }
        else if (!StrUtil.isEmpty(cellConfig.getCellFormat())) {
            cell.setCellValue(ConvertUtil.returnDateType(cellValue, cellConfig.getCellFormat()));
        }else {
            cell.setCellValue(ConvertUtil.returnString(cellValue));
        }
    }


    /**
     * 设置下拉框
     *
     * @param sheet
     * @param arrs
     */
    @SneakyThrows
    private static void setSelectList(SXSSFSheet sheet, String[] arrs, Integer... valuesArrsInt) {
        DataValidationHelper helper = sheet.getDataValidationHelper();
        CellRangeAddressList addressList = new CellRangeAddressList(valuesArrsInt[0], valuesArrsInt[1], valuesArrsInt[2], valuesArrsInt[3]);
        //如果带双引号超过30个, 打开excel的时候就会提示错误 而且下拉框不生效,
        //如果不带双引号就没有问题(测试心得)
        //设置下拉框数据
        DataValidationConstraint constraint = helper.createExplicitListConstraint(arrs);
        DataValidation dataValidation = helper.createValidation(constraint, addressList);
        if (dataValidation instanceof XSSFDataValidation) {
            dataValidation.setSuppressDropDownArrow(true);
            dataValidation.setShowErrorBox(true);
        } else {
            dataValidation.setSuppressDropDownArrow(false);
        }
        sheet.addValidationData(dataValidation);
    }

    /**
     * 设置单元格颜色
     * @param cell
     * @param sxssfWorkbook
     * @param cellColor
     * @throws Exception
     */
    private static void setCellColor(Cell cell, SXSSFWorkbook sxssfWorkbook, short cellColor) throws Exception {
        CellStyle style = sxssfWorkbook.createCellStyle();
        style.setFillForegroundColor(cellColor);//IndexedColors.YELLOW.getIndex()
        cell.setCellStyle(style);
    }

    /**
     * 合并单元格
     */
    public static void setSumCell(DeeExcelCell cellConfig, SXSSFSheet sheet) throws Exception {
        //起始行，结束行 , 起始列, 结束列
//        CellRangeAddress cra = new CellRangeAddress(cell.getRowNum(), cell.getEndRowNum(), cell.getColNum(), cell.getEndColNum());
//        sheet.addMergedRegion(cra);
    }

}
