package org.dee.init;

import cn.hutool.core.collection.CollUtil;
import org.dee.entity.LogEntity;
import org.dee.entity.ThreadLog;
import org.dee.service.LogEntityService;
import org.dee.utils.LogFileUtil;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@Component
public class ReadLogFileConfiguration {

    @Resource
    private LogEntityService logEntityService;

    @Bean
    public void init() {
        String logFilePath = "/Users/frieda.li/Desktop/code/sunline/SunERP/Application_Server/oa-kpi/${sys:system.basedir}/logs/default.log";
        Map<String, ThreadLog> map = LogFileUtil.parseLogFileNio(logFilePath);
        List<LogEntity> list = LogFileUtil.convertLogEntity(map);
        if(CollUtil.isNotEmpty(list)){
            logEntityService.setLogEntities(list);
        }
    }

}
