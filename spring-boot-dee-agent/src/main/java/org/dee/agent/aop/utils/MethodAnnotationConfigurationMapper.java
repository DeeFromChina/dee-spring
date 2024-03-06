package org.dee.agent.aop.utils;


import org.dee.agent.aop.configuration.MethodAnnotationConfiguration;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class MethodAnnotationConfigurationMapper {

    public static MethodAnnotationConfiguration createConfiguration(Properties properties) {
        MethodAnnotationConfiguration configuration = new MethodAnnotationConfiguration();
        List<String> annotationClasses = new ArrayList<>();
        List<String> basePackages = new ArrayList<>();
        int i = 0;
        while (true) {
            boolean isBreak = false;
            Object annotationClass = properties.get("org.dee.agent.aop.method.annotationClasses["+i+"]");
            if(annotationClass != null){
                annotationClasses.add(annotationClass.toString());
            }
            else {
                isBreak = true;
            }

            Object basePackage = properties.get("org.dee.agent.aop.method.basePackages["+i+"]");
            if(basePackage != null){
                String packagePath = basePackage.toString().replace(".", "/");
                basePackages.add(packagePath);
                isBreak = false;
            }

            if(isBreak) break;
            i++;
        }
        configuration.setAnnotationClasses(annotationClasses.toArray(new String[]{}));
        configuration.setBasePackages(basePackages.toArray(new String[]{}));
        return configuration;
    }

}
