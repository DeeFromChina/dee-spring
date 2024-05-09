package org.dee.controller;

import org.dee.entity.TestEntity;
import org.dee.framework.controller.BaseWebController;
import org.dee.service.TestEntityWebService;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@RequestMapping
public class TestEntityController extends BaseWebController<TestEntity, TestEntityWebService> {
    @Override
    protected void vaildPage(TestEntity param) {

    }

    @Override
    protected void vaildList(TestEntity param) {

    }

    @Override
    protected void vaildAdd(TestEntity testEntity) {

    }

    @Override
    protected void vaildAddBatch(List<TestEntity> entities) {

    }

    @Override
    protected void vaildUpdate(TestEntity testEntity) {

    }

    @Override
    protected void vaildUpdateBatch(List<TestEntity> entities) {

    }
}
