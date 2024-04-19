package org.dee.agent.aop.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "org.dee.agent.aop.method")
public class MethodAnnotationConfigurationProperties {

    private String[] basePackages;

    private String[] annotationClasses;

}
