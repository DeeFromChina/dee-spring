package org.dee.plugin.registrar;

import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.env.Environment;
import org.springframework.core.env.EnvironmentCapable;
import org.springframework.core.env.StandardEnvironment;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.ResourceLoader;
import org.springframework.lang.Nullable;
import org.springframework.util.Assert;
import org.springframework.util.ClassUtils;

public class ConditionContextImpl implements ConditionContext {
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
