package org.dee.framework.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import org.dee.framework.rpc.RpcRequest;

import java.io.Serializable;
import java.util.List;

public interface IMybatiesPlusService<T> extends IService<T> {

    /**
     * 查询分页数据
     * @param param 实体参数
     */
    Page<T> queryPage(RpcRequest<T> param);

    /**
     * 查询所有数据
     * @param param 实体参数
     */
    List<T> queryList(T param);

    /**
     * 查询单个数据
     * @param id 主键
     */
    T getById(Serializable id);

}
