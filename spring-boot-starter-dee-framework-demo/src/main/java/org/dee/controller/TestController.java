package org.dee.controller;

import org.dee.service.TestService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("/test1")
public class TestController {

    //@Resource
    //private TestMapper testMapper;

    @Resource
    private TestService testService;

    @GetMapping("/mapper")
    public String testMethod() {
        //testMapper.selectById("1");
        testService.test();
        return "1";
    }

}
