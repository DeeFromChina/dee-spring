package org.dee.file.excel.utils;

import cn.hutool.core.util.StrUtil;
import lombok.SneakyThrows;
import org.dee.file.excel.entity.DeeExcelSheet;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public class ExcelImportUtil {

    /**
     * 获取单个sheet导入数据，返回string[]数组
     * 默认第一个sheet页
     * 从指定行数开始读取
     * @param file 文件对象
     * @param startReadRowNum 从...行开始读取
     * @return
     */
    public static List<String[]> getEntity(MultipartFile file, Integer startReadRowNum) {
        List<List<String[]>> list = getEntitys(file, new Integer[]{0}, new Integer[]{startReadRowNum});
        return list != null && list.size() > 0 ? list.get(0) : null;
    }

    /**
     * 获取单个sheet导入数据
     * @param file 文件对象
     * @param sheetParam sheet参数
     * @return
     */
    public static <T> List<T> getEntity(MultipartFile file, DeeExcelSheet sheetParam, Class<T> entityClass) {
        return (List<T>) getEntitys(file, sheetParam).get(0);
    }

    /**
     * 获取excel内容，返回string[]数组
     * @param file 文件对象
     * @param needReadSheetNums 需要读取的excel的sheet列表
     * @param startReadRowNums  每个sheet开始读取的行数
     * @return
     */
    @SneakyThrows
    public static List<List<String[]>> getEntitys(MultipartFile file, Integer[] needReadSheetNums, Integer[] startReadRowNums) {
        if(file.isEmpty()){
            throw new Exception("file is empty");
        }
        String fileName = file.getOriginalFilename().toLowerCase();
        if (!StrUtil.isEmpty(fileName)) {
            String suffix = fileName.substring(fileName.lastIndexOf(".") + 1);
            if ("xls".equals(suffix)) {//读取03
                return HSSFExcelUtil.getHSSFWorkBook(file.getInputStream(), needReadSheetNums, startReadRowNums);
            } else if ("xlsx".equals(suffix)) {//读取07
                return XSSFExcelUtil.getXSSFWorkBook(file.getInputStream(), needReadSheetNums, startReadRowNums);
            } else {
                return null;
            }
        }
        return null;
    }

    /**
     * 获取excel内容，返回对象数组
     * @param file 文件对象
     * @param sheetParams sheet参数
     * @return
     */
    @SneakyThrows
    public static List<List<?>> getEntitys(MultipartFile file, DeeExcelSheet... sheetParams) {
        if(file.isEmpty()){
            throw new Exception("file is empty");
        }
        String fileName = file.getOriginalFilename().toLowerCase();
        if (!StrUtil.isEmpty(fileName)) {
            String suffix = fileName.substring(fileName.lastIndexOf(".") + 1);
            if ("xls".equals(suffix)) {//读取03
                return HSSFExcelUtil.getHSSFWorkBook(file.getInputStream(), sheetParams);
            } else if ("xlsx".equals(suffix)) {//读取07
                return XSSFExcelUtil.getXSSFWorkBook(file.getInputStream(), sheetParams);
            } else {
                return null;
            }
        }
        return null;
    }

}
