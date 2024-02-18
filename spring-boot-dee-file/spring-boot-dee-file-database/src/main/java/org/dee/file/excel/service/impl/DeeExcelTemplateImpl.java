package org.dee.file.excel.service.impl;

import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.SneakyThrows;
import org.apache.poi.ss.usermodel.Workbook;
import org.dee.file.excel.entity.DeeExcelSheet;
import org.dee.file.excel.entity.DeeExcelTemplate;
import org.dee.file.excel.mapper.DeeExcelTemplateMapper;
import org.dee.file.excel.service.ExcelTemplateService;
import org.dee.file.excel.utils.ExcelExportUtil;
import org.dee.file.excel.utils.ExcelImportUtil;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
public class DeeExcelTemplateImpl extends ServiceImpl<DeeExcelTemplateMapper, DeeExcelTemplate> implements ExcelTemplateService {

    private String excelName;

    private void setExcelName(String excelName) {
        this.excelName = excelName;
    }

    @Override
    public String getExcelName() {
        return this.excelName;
    }

    /**
     * 根据excelCode获取DeeExcelTemplate
     * @param excelCode
     * @return
     */
    private DeeExcelTemplate getTempByTempCode(String excelCode) {
        DeeExcelTemplate temp = getById(excelCode);
        if(temp == null){
            throw new RuntimeException("can't find excel temp");
        }
        return analysisConfigContentJson(temp);
    }

    /**
     * 解析ConfigContent的json
     * @param temp
     * @return
     * @throws RuntimeException
     */
    private DeeExcelTemplate analysisConfigContentJson(DeeExcelTemplate temp) throws RuntimeException {
        return JSONUtil.toBean(temp.getConfigContent(), DeeExcelTemplate.class);
    }

    @Override
    public <T> List<T> importFile(MultipartFile file, String excelCode, Class<T> entityClass) {
        DeeExcelTemplate jsonTemp = getTempByTempCode(excelCode);
        return ExcelImportUtil.getEntity(file, jsonTemp.getSheets().get(0), entityClass);
    }

    @Override
    public List<List<?>> importFileMultipleSheet(MultipartFile file, String excelCode) {
        DeeExcelTemplate jsonTemp = getTempByTempCode(excelCode);
        DeeExcelSheet[] sheetParams = jsonTemp.getSheets().toArray(new DeeExcelSheet[0]);
        return ExcelImportUtil.getEntitys(file, sheetParams);
    }

    @Override
    @SneakyThrows
    public Workbook download(String excelCode){
        DeeExcelTemplate jsonTemp = getTempByTempCode(excelCode);
        setExcelName(jsonTemp.getExcelName());
        return ExcelExportUtil.download(jsonTemp.getSheets().get(0));
    }

    @Override
    @SneakyThrows
    public Workbook download(List<List<?>> dataList, String excelCode){
        DeeExcelTemplate jsonTemp = getTempByTempCode(excelCode);
        setExcelName(jsonTemp.getExcelName());
        DeeExcelSheet[] sheetParams = jsonTemp.getSheets().toArray(new DeeExcelSheet[0]);
        return ExcelExportUtil.download(dataList, sheetParams);
    }

    @Override
    @SneakyThrows
    public <T> Workbook getWorkbook(List<T> dataList, String excelCode, Class<T> entityClass){
        DeeExcelTemplate jsonTemp = getTempByTempCode(excelCode);
        setExcelName(jsonTemp.getExcelName());
        return ExcelExportUtil.getWorkbook(dataList, jsonTemp.getSheets().get(0), entityClass);
    }

    @Override
    @SneakyThrows
    public Workbook getWorkbook(List<List<?>> dataList, String excelCode){
        DeeExcelTemplate jsonTemp = getTempByTempCode(excelCode);
        setExcelName(jsonTemp.getExcelName());
        DeeExcelSheet[] sheetParams = jsonTemp.getSheets().toArray(new DeeExcelSheet[0]);
        return ExcelExportUtil.getWorkbook(dataList, sheetParams);
    }

}
