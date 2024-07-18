package org.dee.gateway.controller;

import cn.hutool.json.JSONUtil;
import org.dee.gateway.entity.CustomRouteDefinition;
import org.dee.gateway.service.DynamicRouteService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/route")
public class RouteController {

    @Resource
    private DynamicRouteService service;

    @PostMapping("/add")
    public String addRoute(@RequestBody CustomRouteDefinition routeDefinition){
        if(service.add(routeDefinition)) {
            System.out.println("===============注册成功===============");
            System.out.println(JSONUtil.toJsonStr(routeDefinition));
            return "success";
        }
        return "error";
    }

    @PutMapping
    public String updateRoute(@RequestBody CustomRouteDefinition routeDefinition){
        if(service.update(routeDefinition)){
            System.out.println("===============修改成功===============");
            System.out.println(JSONUtil.toJsonStr(routeDefinition));
            return "success";
        }
        return "error";
    }

    @DeleteMapping("/{routeId}")
    public String deleteRoute(@PathVariable String routeId){
        if(service.delete(routeId)){
            System.out.println("===============删除成功===============");
            System.out.println(routeId);
            return "success";
        }
        return "error";
    }

    /**
     * 刷新路由
     * @return
     */
    @GetMapping("/flushRoute")
    public String flushRoute(){
        if(service.flushRoute()){
            System.out.println("===============刷新成功===============");
            return "success";
        }
        return "error";
    }

    @GetMapping
    public List<CustomRouteDefinition> getList(){
        return service.getRouteList();
    }

    @GetMapping("/routeId/{routeId}")
    public CustomRouteDefinition getByRouteId(@PathVariable String routeId){
        return service.getRouteById(routeId);
    }

}
