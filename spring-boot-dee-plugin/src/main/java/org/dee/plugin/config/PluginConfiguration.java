package org.dee.plugin.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PluginConfiguration implements CommandLineRunner {

    private final ApplicationContext applicationContext;

    @Autowired
    public PluginConfiguration(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    @Override
    public void run(String... args) throws Exception {
        if(!checkLicense()){
            //让spring启动失败
            SpringApplication.exit(applicationContext, () -> 1); // 1 表示启动失败
        }
    }

    public boolean checkLicense() throws Exception {
        return true;
    }

}
