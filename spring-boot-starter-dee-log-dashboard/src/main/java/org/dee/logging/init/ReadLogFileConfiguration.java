package org.dee.logging.init;

import cn.hutool.core.collection.CollUtil;
import org.dee.logging.entity.LogEntity;
import org.dee.logging.entity.ThreadLog;
import org.dee.logging.service.LogEntityService;
import org.dee.logging.utils.LogFileUtil;
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
