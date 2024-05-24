package org.dee.controller;

import io.swagger.annotations.ApiOperation;
import org.dee.annotation.validation.EnableValid;
import org.dee.entity.TestEntity;
import org.dee.framework.controller.BaseWebController;
import org.dee.http.WebResponse;
import org.dee.service.TestEntityWebService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequestMapping
@RestController
@EnableValid
public class TestEntityController extends BaseWebController<TestEntity, TestEntityWebService> {

    @ApiOperation("分页信息")
    @PostMapping("/query/page")
    public WebResponse<TestEntity> queryPage(@RequestBody @Validated TestEntity param) {
        return result(param);
    }

    @Override
    protected void vaildPage(
            TestEntity param
    ) {

    }

    @Override
    protected void vaildList(TestEntity param) {

    }

    @Override
    protected void vaildAdd(TestEntity testEntity) {
        super.vaildAdd(testEntity);
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
