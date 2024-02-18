package org.dee.file.excel.service;

import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ExcelTemplateService {

    String getExcelName();

    <T> List<T> importFile(MultipartFile file, String tempCode, Class<T> entityClass);

    List<List<?>> importFileMultipleSheet(MultipartFile file, String tempCode);

    Workbook download(String tempCode);

    Workbook download(List<List<?>> dataList, String tempCode);

    <T> Workbook getWorkbook(List<T> dataList, String tempCode, Class<T> entityClass);

    Workbook getWorkbook(List<List<?>> dataList, String tempCode);

}
