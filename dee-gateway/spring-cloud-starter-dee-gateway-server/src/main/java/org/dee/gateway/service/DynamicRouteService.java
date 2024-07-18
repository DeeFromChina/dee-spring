package org.dee.gateway.service;


import org.dee.gateway.entity.CustomRouteDefinition;

import java.util.List;

public interface DynamicRouteService {

    /**
     * 增加路由
     * @param routeForm
     * @return
     */
    boolean add(CustomRouteDefinition routeForm);

    /**
     * 更新路由
     * @param routeForm
     * @return
     */
    boolean update(CustomRouteDefinition routeForm);

    /**
     * 删除路由
     * @param id
     * @return
     */
    boolean delete(String id);

    /**
     * 刷新路由信息
     **/
    boolean flushRoute();

    /**
     * 获取路由信息列表
     * @return
     */
    List<CustomRouteDefinition> getRouteList();

    /**
     * 根据id获取单个路由信息
     * @return
     */
    CustomRouteDefinition getRouteById(String routeId);

}
