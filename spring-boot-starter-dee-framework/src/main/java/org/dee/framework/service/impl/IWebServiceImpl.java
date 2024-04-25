package org.dee.framework.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.SneakyThrows;
import org.apache.poi.ss.usermodel.Workbook;
import org.dee.entity.BaseEntity;
import org.dee.file.excel.service.ExcelTemplateService;
import org.dee.framework.client.IClient;
import org.dee.framework.rpc.RpcRequest;
import org.dee.framework.rpc.RpcResult;
import org.dee.framework.service.IWebService;
import org.dee.framework.utils.RpcRequestUtil;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.util.List;

public abstract class IWebServiceImpl<T extends BaseEntity, C extends IClient> extends IWebServiceHandler implements IWebService<T> {

    @Resource
    protected C client;

    @Resource
    protected ExcelTemplateService excelTemplateService;

    @SneakyThrows
    private Class<T> getTClass(){
        return (Class<T>)((ParameterizedType)getClass().getGenericSuperclass()).getActualTypeArguments()[0];
    }

    /**
     * 预处理参数
     * @param param
     */
    protected abstract void beforeQueryPage(T param);

    /**
     * 处理查询后的数据
     * @param msdp
     * @return
     */
    protected abstract Page<T> afterQueryPage(Page<T> msdp);

    @Override
    public Page<T> queryPage(T param) {
        beforeQueryPage(param);
        RpcRequest rpcRequest = new RpcRequest(param);
        rpcRequest.setPageParam(param.getPageParam());
        RpcResult<Page<T>> rpcResult = client.queryPage(rpcRequest);
        vaildFeginResult(rpcResult);
        return afterQueryPage(rpcResult.getBody());
    }

    /**
     * 预处理参数
     * @param param
     */
    protected abstract void beforeQueryList(T param);

    /**
     * 处理查询后的数据
     * @param list
     * @return
     */
    protected abstract List<T> afterQueryList(List<T> list);

    @Override
    public List<T> queryList(T param) {
        beforeQueryList(param);
        RpcResult<List<T>> rpcResult = client.queryList(new RpcRequest(param));
        vaildFeginResult(rpcResult);
        return afterQueryList(rpcResult.getBody());
    }

    protected abstract void beforeGetById(Serializable id);

    protected abstract T afterGetById(T t);

    @Override
    public T getById(Serializable id) {
        beforeGetById(id);
        RpcResult<T> rpcResult = client.getById(new RpcRequest(id));
        vaildFeginResult(rpcResult);
        return afterGetById(rpcResult.getBody());
    }

    protected abstract T beforeAdd(T t);

    @Override
    public void add(T t) {
        RpcResult<Void> rpcResult = client.add(new RpcRequest(beforeAdd(t)));
        vaildFeginResult(rpcResult);
        RpcResult.success(rpcResult);
    }

    protected abstract List<T> beforeAddBatch(List<T> entities);

    @Override
    public void addBatch(List<T> entities){
        RpcResult<Void> rpcResult = client.addBatch(new RpcRequest(beforeAddBatch(entities)));
        vaildFeginResult(rpcResult);
        RpcResult.success(rpcResult);
    }

    protected abstract T beforeUpdate(T t);

    @Override
    public void update(T t) {
        RpcResult<Void> rpcResult = client.update(new RpcRequest(beforeUpdate(t)));
        vaildFeginResult(rpcResult);
        RpcResult.success(rpcResult);
    }

    protected abstract List<T> beforeUpdateBatch(List<T> entities);

    @Override
    public void updateBatch(List<T> entities){
        RpcResult<Void> rpcResult = client.updateBatch(new RpcRequest(beforeUpdateBatch(entities)));
        vaildFeginResult(rpcResult);
        RpcResult.success(rpcResult);
    }

    protected abstract Serializable beforeDelete(Serializable id);

    @Override
    public void delete(Serializable id) {
        RpcResult<Void> rpcResult = client.delete(new RpcRequest(beforeDelete(id)));
        vaildFeginResult(rpcResult);
        RpcResult.success(rpcResult);
    }

    protected abstract List<Serializable> beforeDeleteBatch(List<Serializable> ids);

    @Override
    public void deleteBatch(List<Serializable> ids) {
        RpcResult<Void> rpcResult = client.deleteBatch(new RpcRequest(beforeDeleteBatch(ids)));
        vaildFeginResult(rpcResult);
        RpcResult.success(rpcResult);
    }

    @Override
    public void downloadTemplate(String tempCode, HttpServletResponse response) {
        Workbook workbook = excelTemplateService.download(tempCode);
        //下载文件
        downloadFile(response, workbook, excelTemplateService.getExcelName());
    }

    /**
     * 处理excel数据
     * @return
     */
    protected abstract List<T> afterReadExcel(List<T> list);

    /**
     * 导入数据
     * @param tempCode 模版编码
     * @param file 导入文件
     * @param param 实体参数
     * 不确定导入策略（增量/覆盖），所以自定义import方法
     */
    @Override
    public void importExcel(String tempCode, MultipartFile file, T param) {
        List<T> list = excelTemplateService.importFile(file, tempCode, getTClass());
        if(list == null || list.isEmpty()){
            throw new RuntimeException("不能上传空文件！");
        }
        RpcResult<Void> rpcResult = client.importExcel(RpcRequestUtil.build(afterReadExcel(list)));
        vaildFeginResult(rpcResult);
    }

    protected abstract List<T> afterQueryExcel(List<T> list);

    @Override
    public void exportExcel(String tempCode, T param, HttpServletResponse response) {
        RpcResult<List<T>> rpcResult = client.queryList(RpcRequestUtil.build(param));
        vaildFeginResult(rpcResult);
        Workbook workbook = excelTemplateService.getWorkbook(afterQueryExcel(rpcResult.getBody()), tempCode, getTClass());
        //下载文件
        downloadFile(response, workbook, excelTemplateService.getExcelName());
    }

}
