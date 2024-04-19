package org.dee.agent.aop.utils;

import lombok.extern.slf4j.Slf4j;
import org.dee.agent.AgentMain;
import org.dee.agent.aop.properties.MethodAnnotationConfigurationProperties;
import org.springframework.beans.factory.config.YamlPropertiesFactoryBean;
import org.springframework.core.io.InputStreamResource;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
public class ResourceUtil {

    private static final String resourceFile = "agent.yml";

    public static Properties loadYamlIntoProperties(InputStream yamlInputStream) {
        YamlPropertiesFactoryBean yamlFactory = new YamlPropertiesFactoryBean();
        yamlFactory.setResources(new InputStreamResource(yamlInputStream));
        yamlFactory.afterPropertiesSet();
        return yamlFactory.getObject();
    }

    public static MethodAnnotationConfigurationProperties getMethodAnnotationConfiguration() {
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

    /**
     * 通配符匹配
     * @param pattern
     * @param target
     * @return
     */
    public static boolean patternMatch(String pattern, String target) {
        Pattern r = Pattern.compile(pattern);
        Matcher m = r.matcher(target);
        if (m.find()) {
            return true;
        } else {
            return false;
        }
    }

}
