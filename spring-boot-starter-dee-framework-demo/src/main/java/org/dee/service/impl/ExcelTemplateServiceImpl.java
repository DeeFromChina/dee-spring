package org.dee.service.impl;

import org.apache.poi.ss.usermodel.Workbook;
import org.dee.file.excel.service.ExcelTemplateService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
public class ExcelTemplateServiceImpl implements ExcelTemplateService {

    @Override
    public String getExcelName() {
        return null;
    }

    @Override
    public <T> List<T> importFile(MultipartFile multipartFile, String s, Class<T> aClass) {
        return null;
    }

    @Override
    public List<List<?>> importFileMultipleSheet(MultipartFile multipartFile, String s) {
        return null;
    }

    @Override
    public Workbook download(String s) {
        return null;
    }

    @Override
    public Workbook download(List<List<?>> list, String s) {
        return null;
    }

    @Override
    public <T> Workbook getWorkbook(List<T> list, String s, Class<T> aClass) {
        return null;
    }

    @Override
    public Workbook getWorkbook(List<List<?>> list, String s) {
        return null;
    }
}
