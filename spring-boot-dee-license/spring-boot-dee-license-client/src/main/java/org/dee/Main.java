package org.dee;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;

@Slf4j
@SpringBootApplication
@ServletComponentScan
public class Main{

    public static void main(String[] args) {
        log.info("启动开始");
        SpringApplication.run(Main.class, args);
        log.info("启动成功");
    }
}