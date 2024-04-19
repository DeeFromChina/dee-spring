package org.dee.framework.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.dee.framework.rpc.RpcRequest;
import org.dee.framework.service.IMybatiesPlusService;

import java.io.Serializable;
import java.util.List;

public abstract class IMybatiesPlusServiceImpl<M extends BaseMapper<T>, T> extends ServiceImpl<M, T> implements IMybatiesPlusService<T> {

    protected Class<T> currentMapperClass() {
        return (Class)this.getResolvableType().as(IMybatiesPlusServiceImpl.class).getGeneric(new int[]{0}).getType();
    }

    protected Class<T> currentModelClass() {
        return (Class)this.getResolvableType().as(IMybatiesPlusServiceImpl.class).getGeneric(new int[]{1}).getType();
    }

    /**
     * 查询分页数据
     * @param param 实体参数
     */
    @Override
    public Page<T> queryPage(RpcRequest<T> param) {
        Page<T> page = new Page();
        page.setCurrent(param.getPageParam().getCurrent());
        page.setSize(param.getPageParam().getSize());
        page.setTotal(param.getPageParam().getTotal());
        page.setPages(param.getPageParam().getPages());
        LambdaQueryWrapper<T> wrapper = getPageQueryWrapper(param.getBody());
        return page(page, wrapper);
    }

    protected abstract LambdaQueryWrapper<T> getPageQueryWrapper(T t);

    /**
     * 查询所有数据
     * @param param 实体参数
     */
    @Override
    public List<T> queryList(T param) {
        return list(getListQueryWrapper(param));
    }

    protected abstract LambdaQueryWrapper<T> getListQueryWrapper(T param);

    /**
     * 查询单个数据
     * @param id 主键
     */
    @Override
    public T getById(Serializable id) {
        return super.getById(id);
    }

}
