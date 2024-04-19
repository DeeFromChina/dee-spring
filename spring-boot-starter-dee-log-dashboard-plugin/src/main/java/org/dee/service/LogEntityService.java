package org.dee.service;

import org.dee.entity.LogEntity;
import org.dee.framework.service.IWebService;

import java.util.List;
import java.util.Map;

public interface LogEntityService extends IWebService<LogEntity> {

    void setLogEntities(List<LogEntity> logEntities);

    List<LogEntity> getLogEntities();

    List<LogEntity> queryListByEtc(Map<String, String> param);

}
