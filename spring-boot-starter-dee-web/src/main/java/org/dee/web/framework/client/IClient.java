package org.dee.web.framework.client;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.dee.web.framework.rpc.RPCRequest;
import org.dee.web.framework.rpc.RPCResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;

import java.io.Serializable;
import java.util.List;

public interface IClient<T> {

    @PostMapping("/query/page")
    RPCResult<Page<T>> queryPage(RPCRequest<T> rpcRequest);

    @PostMapping("/query/list")
    RPCResult<List<T>> queryList(RPCRequest<T> rpcRequest);

    @GetMapping("/query/unique")
    RPCResult<T> getById(RPCRequest<Serializable> rpcRequest);

    @PostMapping("/add")
    RPCResult<Void> add(RPCRequest<T> rpcRequest);

    @PostMapping("/add/batch")
    RPCResult<Void> addBatch(RPCRequest<List<T>> rpcRequest);

    @PutMapping("/update")
    RPCResult<Void> update(RPCRequest<T> rpcRequest);

    @PutMapping("/update/batch")
    RPCResult<Void> updateBatch(RPCRequest<List<T>> rpcRequest);

    @DeleteMapping("/delete")
    RPCResult<Void> delete(RPCRequest<List<Serializable>> rpcRequest);

    @PostMapping("/import")
    RPCResult<Void> importExcel(RPCRequest<List<T>> rpcRequest);

}
