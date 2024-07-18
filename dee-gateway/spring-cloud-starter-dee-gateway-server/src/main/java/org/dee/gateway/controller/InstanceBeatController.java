package org.dee.gateway.controller;

import cn.hutool.json.JSONUtil;
import lombok.extern.slf4j.Slf4j;
import org.dee.gateway.entity.BeatInfo;
import org.dee.gateway.service.InstanceService;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@Slf4j
@RestController
@RequestMapping("/instance")
@ConditionalOnProperty(
        name = "spring.cloud.dee.gateway.health.sender",
        havingValue = "client",
        matchIfMissing = false // 如果属性不存在，则不匹配（默认为false）
)
public class InstanceBeatController {

    @Resource
    private InstanceService instanceService;

    @PostMapping("/beat")
    public String beat(@RequestBody BeatInfo beatInfo){
        if(instanceService.setInstanceStateMap(beatInfo.getServiceName(), beatInfo)) {
            log.debug("===============心跳更新成功===============");
            log.debug(JSONUtil.toJsonStr(beatInfo));
            return "success";
        }
        return "error";
    }

}
