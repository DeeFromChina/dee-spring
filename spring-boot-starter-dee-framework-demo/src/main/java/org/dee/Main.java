package org.dee;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients(basePackages = {"org.dee"})
public class Main {

    public static void main(String[] args) {
        try{
            SpringApplication.run(Main.class, args);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}