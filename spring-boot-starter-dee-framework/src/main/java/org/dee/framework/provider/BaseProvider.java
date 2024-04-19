package org.dee.framework.provider;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.ApiOperation;
import org.dee.framework.rpc.RpcRequest;
import org.dee.framework.rpc.RpcResult;
import org.dee.framework.service.IMybatiesPlusService;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;

import javax.annotation.Resource;
import java.io.Serializable;
import java.util.List;

public class BaseProvider<T, S extends IMybatiesPlusService<T>> {

    @Resource
    protected S service;

    protected RpcResult success() {
        return RpcResult.success();
    }

    protected RpcResult<T> success(T t) {
        return RpcResult.success(t);
    }

    protected RpcResult<List<T>> success(List<T> list) {
        return RpcResult.success(list);
    }

    protected RpcResult<Page<T>> success(Page<T> page) {
        return RpcResult.success(page);
    }

    /**
     * 新增数据
     * @param request
     */
    @ApiOperation("新增数据")
    @PostMapping("/add")
    public RpcResult<Void> add(RpcRequest<T> request) {
        service.save(request.getBody());
        return success();
    }

    /**
     * 批量新增数据
     * @param request
     */
    @ApiOperation("批量新增数据")
    @PostMapping("/add/batch")
    public RpcResult<Void> addBatch(RpcRequest<List<T>> request) {
        service.saveBatch(request.getBody());
        return success();
    }

    /**
     * 修改数据
     * @param request
     */
    @ApiOperation("修改数据")
    @PutMapping("/update")
    public RpcResult<Void> update(RpcRequest<T> request) {
        service.updateById(request.getBody());
        return success();
    }

    /**
     * 批量修改数据
     * @param request
     */
    @ApiOperation("批量修改数据")
    @PutMapping("/update/batch")
    public RpcResult<Void> updateBatch(RpcRequest<List<T>> request) {
        service.updateBatchById(request.getBody());
        return success();
    }

    /**
     * 删除数据
     * @param request
     */
    @ApiOperation("删除数据")
    @DeleteMapping("/delete")
    public RpcResult<Void> delete(RpcRequest<Serializable> request) {
        service.removeById(request.getBody());
        return success();
    }

    /**
     * 批量删除数据
     * @param request
     */
    @ApiOperation("批量删除数据")
    @DeleteMapping("/delete/batch")
    public RpcResult<Void> deleteBatch(RpcRequest<List<Serializable>> request) {
        service.removeByIds(request.getBody());
        return success();
    }

    /**
     * 通过主键查询单条数据
     * @param request
     */
    @ApiOperation("查询单个信息")
    @PostMapping("/query/unique")
    public RpcResult<T> queryUnique(RpcRequest<Serializable> request) {
        return success(service.getById(request.getBody()));
    }

    /**
     * 分页查询所有数据
     * @param request
     */
    @ApiOperation("分页查询所有数据")
    @PostMapping("/query/page")
    public RpcResult<Page<T>> queryPage(RpcRequest<T> request) {
        return success(service.queryPage(request));
    }

    /**
     * 查询所有数据
     * @param request
     */
    @ApiOperation("查询所有数据")
    @PostMapping("/query/list")
    public RpcResult<List<T>> queryList(RpcRequest<T> request) {
        return success(service.queryList(request.getBody()));
    }


}
