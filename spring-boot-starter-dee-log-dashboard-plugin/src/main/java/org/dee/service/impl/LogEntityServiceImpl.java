package org.dee.service.impl;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.dee.entity.LogEntity;
import org.dee.service.LogEntityService;
import org.dee.utils.DateUtil;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class LogEntityServiceImpl implements LogEntityService {

    private List<LogEntity> logEntities = new ArrayList<>();

    @Override
    public void setLogEntities(List<LogEntity> logEntities) {
        this.logEntities = logEntities;
    }

    @Override
    public List<LogEntity> getLogEntities() {
        return logEntities;
    }

    @Override
    public LogEntity getById(Serializable traceId) {
        return logEntities.stream()
                .filter(item -> traceId.equals(item.getTraceId()))
                .findFirst().orElse(null);
    }

    @Override
    public List<LogEntity> queryList(LogEntity param) {
        return logEntities;
    }

    @Override
    public List<LogEntity> queryListByEtc(Map<String, String> param) {
        return logEntities.stream()
                .filter(item -> filterCondition(param, item))
                .collect(Collectors.toList());
    }

    private boolean filterCondition(Map<String, String> param, LogEntity logEntity) {
        return isTimeOut(param, logEntity) || equalsStatus(param, logEntity) || isInTime(param, logEntity);
    }

    /**
     * 判断是否超时
     * @param param
     * @param logEntity
     * @return
     */
    private boolean isTimeOut(Map<String, String> param, LogEntity logEntity) {
        if(StrUtil.isEmpty(param.get("timeout"))) {
            return false;
        }
        return logEntity.getTimeDuration() >= Long.valueOf(param.get("timeout"));
    }

    /**
     * status是否相等
     * @return
     */
    private boolean equalsStatus(Map<String, String> param, LogEntity logEntity) {
        return StrUtil.isNotEmpty(param.get("status")) && param.get("status").equals(logEntity.getStatus());
    }

    /**
     * 是否在时间区间内
     * @param param
     * @param logEntity
     * @return
     */
    private boolean isInTime(Map<String, String> param, LogEntity logEntity) {
        String startTime = param.get("startTime");
        String endTime = param.get("endTime");
        if(StrUtil.isEmpty(startTime) || StrUtil.isEmpty(endTime)){
            return false;
        }
        DateTime paramStartDateTime = DateUtil.parse(startTime);
        DateTime paramEndDateTime = DateUtil.parse(endTime);

        DateTime entityStartDateTime = DateUtil.parse(logEntity.getStartTime());
        DateTime entityEndDateTime = DateUtil.parse(logEntity.getEndTime());

        Double days = DateUtil.betweenDay(paramStartDateTime, paramEndDateTime, entityStartDateTime, entityEndDateTime);
        return days > 0;
    }

    @Override
    public Page<LogEntity> queryPage(LogEntity param) {
        return null;
    }

    @Override
    public void add(LogEntity logEntity) {

    }

    @Override
    public void addBatch(List<LogEntity> entities) {

    }

    @Override
    public void update(LogEntity logEntity) {

    }

    @Override
    public void updateBatch(List<LogEntity> entities) {

    }

    @Override
    public void delete(Serializable id) {

    }

    @Override
    public void deleteBatch(List<Serializable> ids) {

    }

    @Override
    public void downloadTemplate(String tempCode, HttpServletResponse response) {

    }

    @Override
    public void importExcel(String tempCode, MultipartFile file, LogEntity param) {

    }

    @Override
    public void exportExcel(String tempCode, LogEntity param, HttpServletResponse response) {

    }

}
