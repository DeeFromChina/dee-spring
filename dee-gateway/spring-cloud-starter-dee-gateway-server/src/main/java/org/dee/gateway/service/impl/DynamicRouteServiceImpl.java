package org.dee.gateway.service.impl;


import org.apache.http.conn.routing.RouteInfo;
import org.dee.gateway.entity.CustomRouteDefinition;
import org.dee.gateway.service.DynamicRouteService;
import org.dee.gateway.service.RouteService;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.cloud.gateway.event.RefreshRoutesEvent;
import org.springframework.cloud.gateway.filter.FilterDefinition;
import org.springframework.cloud.gateway.handler.predicate.PredicateDefinition;
import org.springframework.cloud.gateway.route.RouteDefinition;
import org.springframework.cloud.gateway.route.RouteDefinitionWriter;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Mono;

import javax.annotation.Resource;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class DynamicRouteServiceImpl implements DynamicRouteService,ApplicationEventPublisherAware, ApplicationRunner {

    private final RouteDefinitionWriter routeDefinitionWriter;

    //通过构造方法进行注入，此处通过跟踪代码，RouteDefinitionWriter的实现类是基于内存的，非redis的
    private ApplicationEventPublisher publisher;

    @Resource
    private RouteService routeService;

    public DynamicRouteServiceImpl(RouteDefinitionWriter routeDefinitionWriter) {
        this.routeDefinitionWriter = routeDefinitionWriter;
    }

    @Override
    public void setApplicationEventPublisher(ApplicationEventPublisher publisher) {
        this.publisher = publisher;
    }

    /**
     * 增加路由
     * @param routeForm
     * @return
     */
    @Override
    public boolean add(CustomRouteDefinition routeForm) {
        try{
            RouteDefinition definition = convert(routeForm);
            routeDefinitionWriter.save(Mono.just(definition)).subscribe();
            publishRouteEvent();

            routeForm.setUrl(definition.getUri().toString());
            routeService.addRegisterServerAddr(definition.getId(), routeForm);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     * 更新路由
     * @param routeForm
     * @return
     */
    @Override
    public boolean update(CustomRouteDefinition routeForm) {
        RouteDefinition definition = convert(routeForm);
        try {
            this.routeDefinitionWriter.delete(Mono.just(definition.getId())).subscribe();
            routeService.removeRegisterServerAddr(definition.getId());
        } catch (Exception e) {
            //未知路由信息
            e.printStackTrace();
            return false;
        }
        try {
            routeDefinitionWriter.save(Mono.just(definition)).subscribe();
            routeForm.setUrl(definition.getUri().getPath());
            routeService.addRegisterServerAddr(definition.getId(), routeForm);
            publishRouteEvent();
        } catch (Exception e) {
            //路由信息修改失败!
            e.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     * 删除路由
     * @param id
     * @return
     */
    @Override
    public boolean delete(String id) {
        try{
            this.routeDefinitionWriter.delete(Mono.just(id)).subscribe();
            routeService.removeRegisterServerAddr(id);
            publishRouteEvent();
        }catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    private void publishRouteEvent() {
        this.publisher.publishEvent(new RefreshRoutesEvent(this));
    }

    /**
     * 刷新路由信息
     **/
    @Override
    public boolean flushRoute() {
        try{
            publishRouteEvent();
        } catch (Exception e){
            e.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     * 获取路由信息列表
     * @return
     */
    @Override
    public List<CustomRouteDefinition> getRouteList(){
        Map<String, CustomRouteDefinition> routeDefinitionMap = routeService.getRegisterServerAddr();
        List<CustomRouteDefinition> routeList = new ArrayList<>();
        routeDefinitionMap.forEach((k,v) -> {
            routeList.add(v);
        });
        return routeList;
    }

    @Override
    public CustomRouteDefinition getRouteById(String routeId) {
        Map<String, CustomRouteDefinition> routeDefinitionMap = routeService.getRegisterServerAddr();
        return routeDefinitionMap.get(routeId);
    }

    /**
     * 转换为自定义路由
     * @param info 路由持久化对象
     * @return 自定义路由
     */
    private CustomRouteDefinition convertCustomRouteDefinition(RouteInfo info){
        CustomRouteDefinition routeDefinition = new CustomRouteDefinition();
        return routeDefinition;
    }

    /**
     * 把自定义请求模型转换为RouteDefinition
     *
     * @param form
     * @return
     */
    private RouteDefinition convert(CustomRouteDefinition form) {
        RouteDefinition definition = new RouteDefinition();
        definition.setId(form.getId());
        definition.setOrder(form.getOrder());
        //设置断言
        List<PredicateDefinition> predicateDefinitions = form.getPredicateDefinitions().stream()
                .distinct().map(predicateInfo -> {
                    PredicateDefinition predicate = new PredicateDefinition();
                    predicate.setArgs(predicateInfo.getArgs());
                    predicate.setName(predicateInfo.getName());
                    return predicate;
                }).collect(Collectors.toList());
        definition.setPredicates(predicateDefinitions);

        if(form.getFilters() != null) {
            // 设置过滤
            List<FilterDefinition> filterList = form.getFilters().stream().distinct().map(x -> {
                FilterDefinition filter = new FilterDefinition();
                filter.setName(x.getName());
                filter.setArgs(x.getArgs());
                return filter;
            }).collect(Collectors.toList());
            definition.setFilters(filterList);
        }
        // 设置URI,判断是否进行负载均衡
        URI uri;
        if (form.getUrl().startsWith("http")) {
            uri = UriComponentsBuilder.fromHttpUrl(form.getUrl()).build().toUri();
        } else {
            uri = URI.create(form.getUrl());
        }
        definition.setUri(uri);
        definition.setMetadata(form.getMetadata());
        return definition;
    }

    /**
     * 该方法会在网关启动时，从数据库读取路由信息，并加载至内存中。
     */
    @Override
    public void run(ApplicationArguments args) {
    }

}
