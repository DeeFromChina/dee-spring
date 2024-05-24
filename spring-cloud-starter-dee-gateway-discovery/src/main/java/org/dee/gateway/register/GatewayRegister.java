package org.dee.gateway.register;

import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.json.JSONUtil;
import lombok.Data;
import lombok.SneakyThrows;
import org.dee.gateway.entity.FilterDefinition;
import org.dee.gateway.entity.GatewayRouteDefinition;
import org.dee.gateway.entity.PredicateDefinition;
import org.dee.gateway.properties.GatewayRegisterConfigurationProperties;
import org.dee.gateway.properties.GatewayRegisterRouteConfigurationProperties;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;
import java.util.*;

@Configuration
public class GatewayRegister {

    @Resource
    private GatewayRegisterConfigurationProperties properties;

    @Value("${spring.application.name}")
    private String serverName;

    @Bean
    public void registerGateway() throws Exception {
        if(!properties.getEnabled()){
            return;
        }
        String serverAddr = properties.getServerAddr();
        if(StrUtil.isEmpty(serverAddr)){
            throw new Exception("serverAddr is empty");
        }

        //创建RouteDefinition，将配置数据封装至对象
        RouteDefinition routeDefinition = createRouteDefinition();
        //创建GatewayRouteDefinition，为调用gateway注册接口做准备
        GatewayRouteDefinition definition = createGatewayRouteDefinition(routeDefinition);
        //注册至gateway
        String result = HttpRequest.post("http://"+serverAddr+"/route/add")
                .header("Content-Type", "application/json")
                .body(JSONUtil.toJsonStr(definition))
                .execute().body();
        //WebResponse webResponse = JSONUtil.toBean(result, WebResponse.class);
        //if(!HttpStatusCode.OK.is(webResponse.getCode())) {
        //    throw new Exception(webResponse.getMessage());
        //}
    }

    private boolean validPredicateName(String predicateKey) {
        switch (predicateKey) {
            //时间点后匹配（After=2024-01-01T17:00:00.000-07:00[America/Denver]）
            case "After": return true;
            //时间点前匹配（Before=2024-01-01T17:00:00.000+08:00[Asia/Shanghai]）
            case "Before": return true;
            //时间区间匹配（Between=2024-01-01T17:00:00.000+08:00[Asia/Shanghai]，2024-01-02T17:00:00.000+08:00[Asia/Shanghai]）
            case "Between": return true;
            //指定Cookie正则匹配指定值（Cookie=sessionId, \d+）
            case "Cookie": return true;
            //指定Header正则匹配指定值（Header=requestInfo, \d+）
            case "Header": return true;
            //Host匹配（Host=**.server1.**)
            case "Host": return true;
            //请求Method指定请求方式（Method=GET,POST）
            case "Method": return true;
            //请求路径匹配（Path=/api/**）
            case "Path": return true;
            //请求包含某参数（Query=name）
            case "Query": return true;
            //远程地址匹配（RemoteAddr=192.168.0.1）
            case "RemoteAddr": return true;
            default: return false;
        }
    }

    private boolean validFilterName(String filterKey) {
        switch (filterKey) {
            //添加请求头（AddRequestHeader=X-Request-Id, {request_id}）
            case "AddRequestHeader": return true;
            //添加响应头（AddResponseHeader=X-Response-Foo, bar）
            case "AddResponseHeader": return true;
            //从请求路径中去除指定数量的前缀段（StripPrefix=1）
            case "StripPrefix": return true;
            //向请求路径添加前缀（PrefixPath=/myprefix）
            case "PrefixPath": return true;
            //限制请求速率
            //- name: RequestRateLimiter
            //  args:
            //  redis-rate-limiter.replenishRate: 10
            //  redis-rate-limiter.burstCapacity: 20
            //  key-resolver: "#{@myKeyResolver}"
            case "RequestRateLimiter": return true;
            //（注意：Hystrix 在较新版本的 Spring Cloud 中可能已被弃用，取而代之的是 resilience4j）
            //使用 Hystrix 进行容错处理。
            case "Hystrix": return true;
            //替换请求路径（SetPath=/{segment}）
            case "SetPath": return true;
            //根据正则表达式重写请求路径（RewritePath=/foo/(?<segment>.*), /$\{segment}）
            case "RewritePath": return true;
            //在请求转发之前保存会话信息
            case "SaveSession": return true;
            //设置请求头（SetRequestHeader=X-Custom-Header, custom_value）
            case "SetRequestHeader": return true;
            //设置响应头（虽然 AddResponseHeader 更常用）。
            case "SetResponseHeader": return true;
            //限制请求体的大小。
            case "RequestSize": return true;
            //重定向到指定的 URI。
            case "RedirectTo": return true;
            //移除请求头。
            case "RemoveRequestHeader": return true;
            //移除响应头。
            case "RemoveResponseHeader": return true;
            default: return false;
        }
    }

    @SneakyThrows
    private RouteDefinition createRouteDefinition() {
        GatewayRegisterRouteConfigurationProperties route = properties.getRoute();
        long currentTimeStamp = new Date().getTime();
        String id = StrUtil.isEmpty(route.getId()) ? serverName + "_" + currentTimeStamp : route.getId();
        String name = serverName;
        String description = serverName;
        //如果没有特殊设置，使用服务名调用
        String uri = StrUtil.isEmpty(route.getUri()) ? "lb://" + serverName : route.getUri();

        String[] predicates = route.getPredicates();
        String[] filters = route.getFilters();

        String predicateName = "";
        String predicateValue = "";
        if(predicates != null){
            for(String predicate : predicates) {
                predicateName = predicate.substring(0, predicate.indexOf("="));
                predicateValue = predicate.substring(predicate.indexOf("=") + 1);
                if(!validPredicateName(predicateName)) {
                    throw new Exception("predicateName is error");
                }
            }
        }

        String filterName = "";
        String filterValue = "";
        if(filters != null){
            for(String filter : filters) {
                filterName = filter.substring(0, filter.indexOf("="));
                filterValue = filter.substring(filter.indexOf("=") + 1);
            }
        }

        RouteDefinition routeDefinition = new RouteDefinition();
        routeDefinition.setId(id);
        routeDefinition.setName(name);
        routeDefinition.setDescription(description);
        routeDefinition.setUri(uri);
        routeDefinition.setPredicateName(predicateName);
        routeDefinition.setPredicateValue(predicateValue);
        routeDefinition.setFilterName(filterName);
        routeDefinition.setFilterValue(filterValue);
        return routeDefinition;
    }

    private GatewayRouteDefinition createGatewayRouteDefinition(RouteDefinition routeDefinition) {
        GatewayRouteDefinition definition = new GatewayRouteDefinition();
        definition.setId(routeDefinition.getId());
        definition.setName(routeDefinition.getName());
        definition.setDescription(routeDefinition.getDescription());
        definition.setOrder(1);

        List<PredicateDefinition> predicateDefinitions = new ArrayList<>();
        PredicateDefinition predicateDefinition = new PredicateDefinition();
        predicateDefinition.setName(routeDefinition.getPredicateName());
        Map<String, String> args1 = new HashMap<>();
        if("Header".equals(routeDefinition.getPredicateName()) && routeDefinition.getPredicateValue().indexOf(",") > 0){
            String[] values = routeDefinition.getPredicateValue().split(",");
            args1.put("header", values[0].trim());
            args1.put("value", values[1].trim());
        }else {
            args1.put("_genkey_0", routeDefinition.getPredicateValue());
        }

        predicateDefinition.setArgs(args1);
        predicateDefinitions.add(predicateDefinition);

        definition.setPredicateDefinitions(predicateDefinitions);

        if(StrUtil.isNotEmpty(routeDefinition.getFilterName())){
            List<FilterDefinition> filters = new ArrayList<>();
            FilterDefinition filterDefinition = new FilterDefinition();
            filterDefinition.setName(routeDefinition.getFilterName());
            Map<String, String> args2 = new HashMap<>();
            args2.put("_genkey_0", routeDefinition.getFilterValue());
            filterDefinition.setArgs(args2);
            filters.add(filterDefinition);

            definition.setFilters(filters);
        }

        definition.setUrl(routeDefinition.getUri());

        return definition;
    }

    @Data
    private class RouteDefinition {

        private String id;

        private String name;

        private String description;

        private String uri;

        private String predicateName;

        private String predicateValue;

        private String filterName;

        private String filterValue;

    }

}
