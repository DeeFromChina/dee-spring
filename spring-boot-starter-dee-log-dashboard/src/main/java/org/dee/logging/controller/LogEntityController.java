package org.dee.logging.controller;

import io.swagger.annotations.ApiOperation;
import org.dee.logging.entity.LogEntity;
import org.dee.logging.service.LogEntityService;
import org.dee.http.WebResponse;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Map;

@RestController
@RequestMapping("/logEntity")
public class LogEntityController extends org.dee.framework.controller.BaseWebController<LogEntity, LogEntityService> {

    @Resource
    private LogEntityService service;

    /**
     * 查询其他纬度的数据
     * 1、查询超时的请求-【输入超时阀值timeout，单位秒】
     * 2、查询报错的请求-【输入报错状态status】
     * 3、查询某个时间段的请求-【输入请求时间区间startTime-endTime】
     * @param param 查询实体
     * @return 所有数据
     */
    @ApiOperation("查询所有数据")
    @PostMapping("/query/list/byEtc")
    public WebResponse queryListByEtc(@RequestBody Map<String, String> param) {
        return result(service.queryListByEtc(param));
    }

    @GetMapping("/checkLogLevel")
    public WebResponse checkLogLevel(@RequestParam String id) {
        return success();
    }

}
