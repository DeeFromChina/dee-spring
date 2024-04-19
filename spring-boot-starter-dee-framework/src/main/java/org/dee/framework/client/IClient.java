package org.dee.framework.client;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.dee.framework.rpc.RpcRequest;
import org.dee.framework.rpc.RpcResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;

import java.io.Serializable;
import java.util.List;

public interface IClient<T> {

    @PostMapping("/query/page")
    RpcResult<Page<T>> queryPage(RpcRequest<T> rpcRequest);

    @PostMapping("/query/list")
    RpcResult<List<T>> queryList(RpcRequest<T> rpcRequest);

    @GetMapping("/query/unique")
    RpcResult<T> getById(RpcRequest<Serializable> rpcRequest);

    @PostMapping("/add")
    RpcResult<Void> add(RpcRequest<T> rpcRequest);

    @PostMapping("/add/batch")
    RpcResult<Void> addBatch(RpcRequest<List<T>> rpcRequest);

    @PutMapping("/update")
    RpcResult<Void> update(RpcRequest<T> rpcRequest);

    @PutMapping("/update/batch")
    RpcResult<Void> updateBatch(RpcRequest<List<T>> rpcRequest);

    @DeleteMapping("/delete")
    RpcResult<Void> delete(RpcRequest<Serializable> rpcRequest);

    @DeleteMapping("/delete/batch")
    RpcResult<Void> deleteBatch(RpcRequest<List<Serializable>> rpcRequest);

    @PostMapping("/import")
    RpcResult<Void> importExcel(RpcRequest<List<T>> rpcRequest);

}
