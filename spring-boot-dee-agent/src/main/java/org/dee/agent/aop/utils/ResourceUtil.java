package org.dee.agent.aop.utils;

import lombok.extern.slf4j.Slf4j;
import org.dee.agent.AgentMain;
import org.dee.agent.aop.configuration.MethodAnnotationConfiguration;
import org.springframework.beans.factory.config.YamlPropertiesFactoryBean;
import org.springframework.core.io.InputStreamResource;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Properties;

@Slf4j
public class ResourceUtil {

    private static final String resourceFile = "agent.yml";

    public static Properties loadYamlIntoProperties(InputStream yamlInputStream) {
        YamlPropertiesFactoryBean yamlFactory = new YamlPropertiesFactoryBean();
        yamlFactory.setResources(new InputStreamResource(yamlInputStream));
        yamlFactory.afterPropertiesSet();
        return yamlFactory.getObject();
    }

    public static MethodAnnotationConfiguration getMethodAnnotationConfiguration() {
        // 获取 MyAgent 类的类加载器
        ClassLoader classLoader = AgentMain.class.getClassLoader();

        // 使用类加载器加载资源
        URL resourceUrl = classLoader.getResource(resourceFile);
        if (resourceUrl != null) {
            try (InputStream inputStream = resourceUrl.openStream()) {
                Properties properties = loadYamlIntoProperties(inputStream);
                return MethodAnnotationConfigurationMapper.createConfiguration(properties);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            log.error("Resource not found.");
        }
        return null;
    }

}
