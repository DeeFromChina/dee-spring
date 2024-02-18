package org.dee.web.framework.controller;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.json.JSONUtil;
import io.swagger.annotations.ApiOperation;
import lombok.SneakyThrows;
import org.dee.web.framework.utils.ValidationUtil;
import org.dee.web.framework.service.IWebService;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.util.List;

public abstract class BaseWebController<T, S extends IWebService> {

    protected String tempCode;

    @Resource
    private S service;

    @SneakyThrows
    private Class<T> getTClass(){
        return (Class<T>)((ParameterizedType)getClass().getGenericSuperclass()).getActualTypeArguments()[0];
    }

    protected abstract void vaildPage(T param);

    /**
     * 分页查询所有数据
     * @param param 查询实体
     * @return 所有数据
     */
    @ApiOperation("分页信息")
    @PostMapping("/query/page")
    public void queryPage(@RequestBody T param) {
        vaildPage(param);
//        MsdpPage<T> page = service.queryPage(param);
//        return result(page);
    }

    protected abstract void vaildList(T param);

    /**
     * 查询所有数据
     * @param param 查询实体
     * @return 所有数据
     */
    @ApiOperation("查询所有数据")
    @PostMapping("/query/list")
    public void queryList(@RequestBody T param) {
        vaildList(param);
//        return result(service.queryList(param));
    }

    /**
     * 通过主键查询单条数据
     * @param id 主键
     * @return 单条数据
     */
    @ApiOperation("通过主键查询单条数据")
    @GetMapping("/query/unique")
    public void getById(@RequestParam Serializable id) {
        Assert.isNull(id, "id can't empty");
//        return result((T) service.getById(id));
    }

    protected abstract void vaildAdd(T t);

    /**
     * 新增数据
     * @param t 实体对象
     * @return 新增结果
     */
    @ApiOperation("新增数据")
    @PostMapping("/add")
    public void add(@RequestBody T t) {
        vaildAdd(t);
        service.add(t);
//        return result();
    }

    protected abstract void vaildAddBatch(List<T> entities);

    /**
     * 批量新增数据
     * @param entities 实体对象数组
     * @return 新增结果
     */
    @ApiOperation("批量新增数据")
    @PostMapping("/add/batch")
    public void addBatch(@RequestBody List<T> entities) {
        vaildAddBatch(entities);
        service.addBatch(entities);
//        return result();
    }

    protected abstract void vaildUpdate(T t);

    /**
     * 修改数据
     * @param t 实体对象
     * @return 修改结果
     */
    @ApiOperation("修改数据")
    @PutMapping("/update")
    public void update(@RequestBody T t) {
        vaildUpdate(t);
        service.update(t);
//        return result();
    }

    protected abstract void vaildUpdateBatch(List<T> entities);

    /**
     * 批量修改数据
     * @param entities 实体对象
     * @return 修改结果
     */
    @ApiOperation("批量修改数据")
    @PutMapping("/update/batch")
    public void updateBatch(@RequestBody List<T> entities) {
        vaildUpdateBatch(entities);
        service.updateBatch(entities);
//        return result();
    }

    /**
     * 删除数据
     * @param ids 主键列表
     * @return 删除结果
     */
    @ApiOperation("删除数据")
    @DeleteMapping("/delete")
    public void delete(@RequestBody List<Serializable> ids) {
        Assert.isTrue(ObjectUtil.isNull(ids) || ids.isEmpty(), "ids is empty");
        service.delete(ids);
//        return result();
    }

    /**
     * 下载导入模版
     * @return 下载导入模版
     */
    @ApiOperation("下载导入模版")
    @PostMapping("/template/download")
    public void downloadTemplate(HttpServletResponse response) {
        service.downloadTemplate(tempCode, response);
    }

    /**
     * 导入数据
     * @param file 导入文件
     * @param json 实体参数json字符串
     */
    @ApiOperation("导出数据")
    @PostMapping("/import")
    public void importExcel(MultipartFile file, @RequestPart String json) {
        ValidationUtil.validateExcelFile(file);
        T param = JSONUtil.toBean(json, getTClass());
        service.importExcel(tempCode, file, param);
    }

    /**
     * 导出数据
     * @param param 实体参数
     * @return 导出结果
     */
    @ApiOperation("导出数据")
    @PostMapping("/export")
    public void exportExcel(@RequestBody T param, HttpServletResponse response) {
        service.exportExcel(tempCode, param, response);
    }

}
