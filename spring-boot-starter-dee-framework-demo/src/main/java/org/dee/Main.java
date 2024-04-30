package org.dee;

import org.dee.processor.HelloWorld;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Main {

    @HelloWorld
    public static void main(String[] args) {
        try{
            SpringApplication.run(Main.class, args);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}