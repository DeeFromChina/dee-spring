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

    //@Autowired
    //protected SpringContext context;
    //
    //public IMybatiesPlusServiceImpl() {
    //    try {
    //        SqlSessionFactory sqlSessionFactory = context.getBean(SqlSessionFactory.class);
    //
    //        MapperFactoryBean<?> mapperFactoryBean = new MapperFactoryBean<>(getM());
    //        mapperFactoryBean.setSqlSessionFactory(sqlSessionFactory);
    //        baseMapper = (M) mapperFactoryBean.getObject();
    //    } catch (Exception e) {
    //        e.printStackTrace();
    //    }
    //}

    protected Class<M> getM() {
        return (Class)this.getResolvableType().as(IMybatiesPlusServiceImpl.class).getGeneric(new int[]{0}).getType();
    }

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

    protected LambdaQueryWrapper<T> getPageQueryWrapper(T t) {
        return new LambdaQueryWrapper<T>();
    }

    /**
     * 查询所有数据
     * @param param 实体参数
     */
    @Override
    public List<T> queryList(T param) {
        return list(getListQueryWrapper(param));
    }

    protected LambdaQueryWrapper<T> getListQueryWrapper(T param) {
        return new LambdaQueryWrapper<T>();
    }

    /**
     * 查询单个数据
     * @param id 主键
     */
    @Override
    public T getById(Serializable id) {
        return super.getById(id);
    }

}
