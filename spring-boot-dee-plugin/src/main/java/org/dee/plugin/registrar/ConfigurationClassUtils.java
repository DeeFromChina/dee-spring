package org.dee.plugin.registrar;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.aop.framework.AopInfrastructureBean;
import org.springframework.beans.factory.annotation.AnnotatedBeanDefinition;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.context.annotation.*;
import org.springframework.context.event.EventListenerFactory;
import org.springframework.core.Conventions;
import org.springframework.core.annotation.Order;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.core.type.classreading.MetadataReaderFactory;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class ConfigurationClassUtils {
    public static final String CONFIGURATION_CLASS_FULL = "full";
    public static final String CONFIGURATION_CLASS_LITE = "lite";
    public static final String CONFIGURATION_CLASS_ATTRIBUTE = Conventions.getQualifiedAttributeName(ConfigurationClassPostProcessor.class, "configurationClass");
    private static final String ORDER_ATTRIBUTE = Conventions.getQualifiedAttributeName(ConfigurationClassPostProcessor.class, "order");
    private static final Log logger = LogFactory.getLog(ConfigurationClassUtils.class);
    private static final Set<String> candidateIndicators = new HashSet(8);

    ConfigurationClassUtils() {
    }

    public static boolean checkConfigurationClassCandidate(BeanDefinition beanDef, MetadataReaderFactory metadataReaderFactory) {
        String className = beanDef.getBeanClassName();
        if (className != null && beanDef.getFactoryMethodName() == null) {
            AnnotationMetadata metadata;
            if (beanDef instanceof AnnotatedBeanDefinition && className.equals(((AnnotatedBeanDefinition)beanDef).getMetadata().getClassName())) {
                metadata = ((AnnotatedBeanDefinition)beanDef).getMetadata();
            } else if (beanDef instanceof AbstractBeanDefinition && ((AbstractBeanDefinition)beanDef).hasBeanClass()) {
                Class<?> beanClass = ((AbstractBeanDefinition)beanDef).getBeanClass();
                if (BeanFactoryPostProcessor.class.isAssignableFrom(beanClass) || BeanPostProcessor.class.isAssignableFrom(beanClass) || AopInfrastructureBean.class.isAssignableFrom(beanClass) || EventListenerFactory.class.isAssignableFrom(beanClass)) {
                    return false;
                }

                metadata = AnnotationMetadata.introspect(beanClass);
            } else {
                try {
                    MetadataReader metadataReader = metadataReaderFactory.getMetadataReader(className);
                    metadata = metadataReader.getAnnotationMetadata();
                } catch (IOException var6) {
                    if (logger.isDebugEnabled()) {
                        logger.debug("Could not find class file for introspecting configuration annotations: " + className, var6);
                    }

                    return false;
                }
            }

            Map<String, Object> config = metadata.getAnnotationAttributes(Configuration.class.getName());
            if (config != null && !Boolean.FALSE.equals(config.get("proxyBeanMethods"))) {
                beanDef.setAttribute(CONFIGURATION_CLASS_ATTRIBUTE, "full");
            } else {
                if (config == null && !isConfigurationCandidate(metadata)) {
                    return false;
                }

                beanDef.setAttribute(CONFIGURATION_CLASS_ATTRIBUTE, "lite");
            }

            Integer order = getOrder(metadata);
            if (order != null) {
                beanDef.setAttribute(ORDER_ATTRIBUTE, order);
            }

            return true;
        } else {
            return false;
        }
    }

    public static boolean isConfigurationCandidate(AnnotationMetadata metadata) {
        if (metadata.isInterface()) {
            return false;
        } else {
            Iterator var1 = candidateIndicators.iterator();

            String indicator;
            do {
                if (!var1.hasNext()) {
                    return hasBeanMethods(metadata);
                }

                indicator = (String)var1.next();
            } while(!metadata.isAnnotated(indicator));

            return true;
        }
    }

    static boolean hasBeanMethods(AnnotationMetadata metadata) {
        try {
            return metadata.hasAnnotatedMethods(Bean.class.getName());
        } catch (Throwable var2) {
            if (logger.isDebugEnabled()) {
                logger.debug("Failed to introspect @Bean methods on class [" + metadata.getClassName() + "]: " + var2);
            }

            return false;
        }
    }

    @Nullable
    public static Integer getOrder(AnnotationMetadata metadata) {
        Map<String, Object> orderAttributes = metadata.getAnnotationAttributes(Order.class.getName());
        return orderAttributes != null ? (Integer)orderAttributes.get("value") : null;
    }

    public static int getOrder(BeanDefinition beanDef) {
        Integer order = (Integer)beanDef.getAttribute(ORDER_ATTRIBUTE);
        return order != null ? order : Integer.MAX_VALUE;
    }

    static {
        candidateIndicators.add(Component.class.getName());
        candidateIndicators.add(ComponentScan.class.getName());
        candidateIndicators.add(Import.class.getName());
        candidateIndicators.add(ImportResource.class.getName());
    }
}
