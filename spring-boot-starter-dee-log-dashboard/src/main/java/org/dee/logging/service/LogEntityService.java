package org.dee.logging.service;

import org.dee.logging.entity.LogEntity;
import org.dee.web.framework.service.IWebService;

import java.util.List;
import java.util.Map;

public interface LogEntityService extends IWebService<LogEntity> {

    void setLogEntities(List<LogEntity> logEntities);

    List<LogEntity> getLogEntities();

    List<LogEntity> queryListByEtc(Map<String, String> param);

}
