package org.dee.plugin.controller;

import org.dee.plugin.entity.Dto;
import org.dee.plugin.factory.ControllerFactory;
import org.dee.plugin.factory.PluginFactory;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.util.Map;

/**
 * 使用controller的方式，通过调用接口，加载plugin
 */
@RestController
public class PluginController {

    @Resource
    private PluginFactory pluginFactory;

    @Resource
    private ControllerFactory controllerFactory;

    @RequestMapping("/test/{id}")
    public String test(@PathVariable("id") String id) {
        return "testsuccess";
    }

    /**
     * 插件激活
     * @param id
     * @return
     */
    @RequestMapping("/plugin/active/{id}")
    public String active(@PathVariable("id") String id) {
        pluginFactory.activePlugin(id);
        return "active success";
    }

    @RequestMapping("/plugin/remove/{id}")
    public String remove(@PathVariable("id") String id) {
        pluginFactory.removePlugin(id);
        return "remove success";
    }

    @PostMapping("/controller/add")
    public String add(Map<String, String> param) {
//        String jarPath = param.get("jarPath");
        String jarPath = "/Users/frieda.li/Desktop/code/dee-spring/dee-spring/spring-boot-starter-dee-plugin-demo/target/spring-boot-starter-dee-plugin-demo-0.0.1-SNAPSHOT.jar";
        controllerFactory.bulidController(jarPath);
        return "add success";
    }

    @PostMapping(value = "/uploadFile", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public void uploadFile(Dto dto, MultipartFile[] files){
        System.out.println(dto.getName());
        for(MultipartFile file : files){
            System.out.println(file.getOriginalFilename());
        }
    }

}
