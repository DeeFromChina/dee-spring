package org.dee.plugin.registrar;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.*;
import org.springframework.core.annotation.AnnotationAwareOrderComparator;
import org.springframework.core.env.Environment;
import org.springframework.core.env.EnvironmentCapable;
import org.springframework.core.env.StandardEnvironment;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.type.AnnotatedTypeMetadata;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.lang.Nullable;
import org.springframework.util.Assert;
import org.springframework.util.ClassUtils;
import org.springframework.util.MultiValueMap;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class ConditionEvaluator {
    private final ConditionContextImpl context;

    public ConditionEvaluator(@Nullable BeanDefinitionRegistry registry, @Nullable Environment environment, @Nullable ResourceLoader resourceLoader) {
        this.context = new ConditionContextImpl(registry, environment, resourceLoader);
    }

    public boolean shouldSkip(AnnotatedTypeMetadata metadata) {
        return this.shouldSkip(metadata, (ConfigurationCondition.ConfigurationPhase)null);
    }

    public boolean shouldSkip(@Nullable AnnotatedTypeMetadata metadata, @Nullable ConfigurationCondition.ConfigurationPhase phase) {
        if (metadata != null && metadata.isAnnotated(Conditional.class.getName())) {
            if (phase == null) {
                return metadata instanceof AnnotationMetadata && ConfigurationClassUtils.isConfigurationCandidate((AnnotationMetadata)metadata) ? this.shouldSkip(metadata, ConfigurationCondition.ConfigurationPhase.PARSE_CONFIGURATION) : this.shouldSkip(metadata, ConfigurationCondition.ConfigurationPhase.REGISTER_BEAN);
            } else {
                List<Condition> conditions = new ArrayList();
                Iterator var4 = this.getConditionClasses(metadata).iterator();

                while(var4.hasNext()) {
                    String[] conditionClasses = (String[])var4.next();
                    String[] var6 = conditionClasses;
                    int var7 = conditionClasses.length;

                    for(int var8 = 0; var8 < var7; ++var8) {
                        String conditionClass = var6[var8];
                        Condition condition = this.getCondition(conditionClass, this.context.getClassLoader());
                        conditions.add(condition);
                    }
                }

                AnnotationAwareOrderComparator.sort(conditions);
                var4 = conditions.iterator();

                Condition condition;
                ConfigurationCondition.ConfigurationPhase requiredPhase;
                do {
                    do {
                        if (!var4.hasNext()) {
                            return false;
                        }

                        condition = (Condition)var4.next();
                        requiredPhase = null;
                        if (condition instanceof ConfigurationCondition) {
                            requiredPhase = ((ConfigurationCondition)condition).getConfigurationPhase();
                        }
                    } while(requiredPhase != null && requiredPhase != phase);
                } while(condition.matches(this.context, metadata));

                return true;
            }
        } else {
            return false;
        }
    }

    private List<String[]> getConditionClasses(AnnotatedTypeMetadata metadata) {
        MultiValueMap<String, Object> attributes = metadata.getAllAnnotationAttributes(Conditional.class.getName(), true);
        Object values = attributes != null ? attributes.get("value") : null;
        return (List)((List)(values != null ? values : Collections.emptyList()));
    }

    private Condition getCondition(String conditionClassName, @Nullable ClassLoader classloader) {
        Class<?> conditionClass = ClassUtils.resolveClassName(conditionClassName, classloader);
        return (Condition) BeanUtils.instantiateClass(conditionClass);
    }

    private static class ConditionContextImpl implements ConditionContext {
        @Nullable
        private final BeanDefinitionRegistry registry;
        @Nullable
        private final ConfigurableListableBeanFactory beanFactory;
        private final Environment environment;
        private final ResourceLoader resourceLoader;
        @Nullable
        private final ClassLoader classLoader;

        public ConditionContextImpl(@Nullable BeanDefinitionRegistry registry, @Nullable Environment environment, @Nullable ResourceLoader resourceLoader) {
            this.registry = registry;
            this.beanFactory = this.deduceBeanFactory(registry);
            this.environment = environment != null ? environment : this.deduceEnvironment(registry);
            this.resourceLoader = resourceLoader != null ? resourceLoader : this.deduceResourceLoader(registry);
            this.classLoader = this.deduceClassLoader(resourceLoader, this.beanFactory);
        }

        @Nullable
        private ConfigurableListableBeanFactory deduceBeanFactory(@Nullable BeanDefinitionRegistry source) {
            if (source instanceof ConfigurableListableBeanFactory) {
                return (ConfigurableListableBeanFactory)source;
            } else {
                return source instanceof ConfigurableApplicationContext ? ((ConfigurableApplicationContext)source).getBeanFactory() : null;
            }
        }

        private Environment deduceEnvironment(@Nullable BeanDefinitionRegistry source) {
            return (Environment)(source instanceof EnvironmentCapable ? ((EnvironmentCapable)source).getEnvironment() : new StandardEnvironment());
        }

        private ResourceLoader deduceResourceLoader(@Nullable BeanDefinitionRegistry source) {
            return (ResourceLoader)(source instanceof ResourceLoader ? (ResourceLoader)source : new DefaultResourceLoader());
        }

        @Nullable
        private ClassLoader deduceClassLoader(@Nullable ResourceLoader resourceLoader, @Nullable ConfigurableListableBeanFactory beanFactory) {
            if (resourceLoader != null) {
                ClassLoader classLoader = resourceLoader.getClassLoader();
                if (classLoader != null) {
                    return classLoader;
                }
            }

            return beanFactory != null ? beanFactory.getBeanClassLoader() : ClassUtils.getDefaultClassLoader();
        }

        public BeanDefinitionRegistry getRegistry() {
            Assert.state(this.registry != null, "No BeanDefinitionRegistry available");
            return this.registry;
        }

        @Nullable
        public ConfigurableListableBeanFactory getBeanFactory() {
            return this.beanFactory;
        }

        public Environment getEnvironment() {
            return this.environment;
        }

        public ResourceLoader getResourceLoader() {
            return this.resourceLoader;
        }

        @Nullable
        public ClassLoader getClassLoader() {
            return this.classLoader;
        }
    }
}
