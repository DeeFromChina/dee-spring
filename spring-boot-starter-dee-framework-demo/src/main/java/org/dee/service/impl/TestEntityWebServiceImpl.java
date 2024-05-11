package org.dee.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.dee.client.TestEntityClient;
import org.dee.entity.TestEntity;
import org.dee.framework.service.impl.IWebServiceImpl;
import org.dee.service.TestEntityWebService;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.List;

@Service
public class TestEntityWebServiceImpl extends IWebServiceImpl<TestEntity, TestEntityClient> implements TestEntityWebService {

    @Override
    protected void beforeQueryPage(TestEntity param) {

    }

    @Override
    protected Page<TestEntity> afterQueryPage(Page<TestEntity> msdp) {
        return null;
    }

    @Override
    protected void beforeQueryList(TestEntity param) {

    }

    @Override
    protected List<TestEntity> afterQueryList(List<TestEntity> list) {
        return null;
    }

    @Override
    protected void beforeGetById(Serializable id) {

    }

    @Override
    protected TestEntity afterGetById(TestEntity testEntity) {
        return null;
    }

    @Override
    protected TestEntity beforeAdd(TestEntity testEntity) {
        return null;
    }

    @Override
    protected List<TestEntity> beforeAddBatch(List<TestEntity> entities) {
        return null;
    }

    @Override
    protected TestEntity beforeUpdate(TestEntity testEntity) {
        return null;
    }

    @Override
    protected List<TestEntity> beforeUpdateBatch(List<TestEntity> entities) {
        return null;
    }

    @Override
    protected Serializable beforeDelete(Serializable id) {
        return null;
    }

    @Override
    protected List<Serializable> beforeDeleteBatch(List<Serializable> ids) {
        return null;
    }

    @Override
    protected List<TestEntity> afterReadExcel(List<TestEntity> list) {
        return null;
    }

    @Override
    protected List<TestEntity> afterQueryExcel(List<TestEntity> list) {
        return null;
    }
}
