//package org.dee.file.configuration.datasource;
//
//import cn.hutool.json.JSONUtil;
//import lombok.SneakyThrows;
//import org.apache.poi.ss.usermodel.Workbook;
//import org.dee.file.excel.entity.DeeExcelTemplate;
//import org.dee.file.excel.utils.ExcelExportUtil;
//import org.dee.file.excel.utils.ExcelImportUtil;
//import org.springframework.web.multipart.MultipartFile;
//
//import javax.annotation.Resource;
//import java.util.List;
//
//public class DeeExcelTemplateRPCImpl implements FileTemplate {
//
//    @Resource
//    private DeeExcelTemplateClient client;
//
//    private String excelName;
//
//    private void setExcelName(String excelName) {
//        this.excelName = excelName;
//    }
//
//    @Override
//    public String getExcelName() {
//        return this.excelName;
//    }
//
//    @Override
//    public <T> List<T> importFile(MultipartFile file, String tempCode, Class<T> entityClass) {
//        RPCResult<DeeExcelTemplate> rpcResult =  DeeExcelTemplateClient.getOneByCode(RpcRequestUtil.build(tempCode));
//        DeeExcelTemplateDTO temp = rpcResult.getBody();
//        DeeExcelTemplate jsonTemp = JSONUtil.toBean(temp.getContent(), DeeExcelTemplate.class);
//        return ExcelImportUtil.getEntity(file, jsonTemp.getSheets().get(0), entityClass);
//    }
//
//    @Override
//    public List<List<?>> importFileMultipleSheet(MultipartFile file, String tempCode) {
//        RPCResult<DeeExcelTemplateDTO> rpcResult =  DeeExcelTemplateClient.getOneByCode(RpcRequestUtil.build(tempCode));
//        DeeExcelTemplateDTO temp = rpcResult.getBody();
//        DeeExcelTemplate jsonTemp = JSONUtil.toBean(temp.getContent(), DeeExcelTemplate.class);
//        ExcelSheet[] sheets = new ExcelSheet[jsonTemp.getSheets().size()];
//        int i = 0;
//        for(ExcelSheet sheet : jsonTemp.getSheets()){
//            sheets[i] = sheet;
//            i++;
//        }
//        return ExcelImportUtil.getEntitys(file, sheets);
//    }
//
//    @Override
//    @SneakyThrows
//    public Workbook download(String tempCode){
//        RPCResult<DeeExcelTemplateDTO> tempRpcResult =  DeeExcelTemplateClient.getOneByCode(RpcRequestUtil.build(tempCode));
//        if (!ValidationUtil.validateRestIsSuccess(tempRpcResult)) {
//            throw new CommonException(tempRpcResult.getHeader().getMsg());
//        }
//
//        DeeExcelTemplateDTO temp = tempRpcResult.getBody();
//        if(temp == null){
//            throw new Exception("can't find excel temp");
//        }
//        DeeExcelTemplate jsonTemp = JSONUtil.toBean(temp.getContent(), DeeExcelTemplate.class);
//        setExcelName(jsonTemp.getTempName());
//        return ExcelExportUtil.download(jsonTemp.getSheets().get(0));
//    }
//
//    @Override
//    @SneakyThrows
//    public Workbook download(List<List<?>> dataList, String tempCode){
//        RPCResult<DeeExcelTemplateDTO> tempRpcResult =  DeeExcelTemplateClient.getOneByCode(RpcRequestUtil.build(tempCode));
//        if (!ValidationUtil.validateRestIsSuccess(tempRpcResult)) {
//            throw new CommonException(tempRpcResult.getHeader().getMsg());
//        }
//
//        DeeExcelTemplateDTO temp = tempRpcResult.getBody();
//        if(temp == null){
//            throw new Exception("can't find excel temp");
//        }
//        DeeExcelTemplate jsonTemp = JSONUtil.toBean(temp.getContent(), DeeExcelTemplate.class);
//        setExcelName(jsonTemp.getTempName());
//        int i = 0;
//        ExcelSheet[] sheetParams = new ExcelSheet[jsonTemp.getSheets().size()];
//        for(ExcelSheet sheet : jsonTemp.getSheets()){
//            sheetParams[i] = sheet;
//            i++;
//        }
//        return ExcelExportUtil.download(dataList, sheetParams);
//    }
//
//    @Override
//    @SneakyThrows
//    public <T> Workbook getWorkbook(List<T> dataList, String tempCode, Class<T> entityClass){
//        RPCResult<DeeExcelTemplateDTO> tempRpcResult =  DeeExcelTemplateClient.getOneByCode(RpcRequestUtil.build(tempCode));
//        if (!ValidationUtil.validateRestIsSuccess(tempRpcResult)) {
//            throw new CommonException(tempRpcResult.getHeader().getMsg());
//        }
//
//        DeeExcelTemplateDTO temp = tempRpcResult.getBody();
//        if(temp == null){
//            throw new Exception("can't find excel temp");
//        }
//        DeeExcelTemplate jsonTemp = JSONUtil.toBean(temp.getContent(), DeeExcelTemplate.class);
//        setExcelName(jsonTemp.getTempName());
//        return ExcelExportUtil.getWorkbook(dataList, jsonTemp.getSheets().get(0), entityClass);
//    }
//
//    @Override
//    @SneakyThrows
//    public Workbook getWorkbook(List<List<?>> dataList, String tempCode){
//        RPCResult<DeeExcelTemplateDTO> tempRpcResult =  DeeExcelTemplateClient.getOneByCode(RpcRequestUtil.build(tempCode));
//        if (!ValidationUtil.validateRestIsSuccess(tempRpcResult)) {
//            throw new CommonException(tempRpcResult.getHeader().getMsg());
//        }
//
//        DeeExcelTemplateDTO temp = tempRpcResult.getBody();
//        if(temp == null){
//            throw new Exception("can't find excel temp");
//        }
//        DeeExcelTemplate jsonTemp = JSONUtil.toBean(temp.getContent(), DeeExcelTemplate.class);
//        setExcelName(jsonTemp.getTempName());
//        int i = 0;
//        ExcelSheet[] sheetParams = new ExcelSheet[jsonTemp.getSheets().size()];
//        for(ExcelSheet sheet : jsonTemp.getSheets()){
//            sheetParams[i] = sheet;
//            i++;
//        }
//        return ExcelExportUtil.getWorkbook(dataList, sheetParams);
//    }
//
//}
