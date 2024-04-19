package org.dee.plugin.registrar;

import org.mybatis.logging.Logger;
import org.mybatis.logging.LoggerFactory;
import org.springframework.aop.scope.ScopedProxyFactoryBean;
import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.config.RuntimeBeanReference;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.util.StringUtils;

import java.util.Arrays;
import java.util.Iterator;
import java.util.Optional;
import java.util.Set;

public class ClassPathMapperScanner extends ClassPathBeanDefinitionScanner {

    private static final Logger LOGGER = LoggerFactory.getLogger(ClassPathMapperScanner.class);

    public ClassPathMapperScanner(BeanDefinitionRegistry registry) {
        super(registry, false);
    }

    public Set<BeanDefinitionHolder> doScan(ClassPathMapperScannerParam param, String... basePackages) {
        Set<BeanDefinitionHolder> beanDefinitions = super.doScan(basePackages);
        if (beanDefinitions.isEmpty()) {
            LOGGER.warn(() -> {
                return "No MyBatis mapper was found in '" + Arrays.toString(basePackages) + "' package. Please check your configuration.";
            });
        } else {
            this.processBeanDefinitions(param, beanDefinitions);
        }

        return beanDefinitions;
    }

    public void processBeanDefinitions(ClassPathMapperScannerParam param, Set<BeanDefinitionHolder> beanDefinitions) {
        DefaultListableBeanFactory registry = param.getRegistry();
        Iterator var4 = beanDefinitions.iterator();

        while(var4.hasNext()) {
            BeanDefinitionHolder holder = (BeanDefinitionHolder)var4.next();
            AbstractBeanDefinition definition = (AbstractBeanDefinition)holder.getBeanDefinition();
            boolean scopedProxy = false;
            if (ScopedProxyFactoryBean.class.getName().equals(definition.getBeanClassName())) {
                definition = (AbstractBeanDefinition) Optional.ofNullable(((RootBeanDefinition)definition).getDecoratedDefinition()).map(BeanDefinitionHolder::getBeanDefinition).orElseThrow(() -> {
                    return new IllegalStateException("The target bean definition of scoped proxy bean not found. Root bean definition[" + holder + "]");
                });
                scopedProxy = true;
            }

            String beanClassName = definition.getBeanClassName();
            System.out.println(beanClassName);
            LOGGER.debug(() -> {
                return "Creating MapperFactoryBean with name '" + holder.getBeanName() + "' and '" + beanClassName + "' mapperInterface";
            });
            definition.getConstructorArgumentValues().addGenericArgumentValue(beanClassName);
            definition.setBeanClass(param.getMapperFactoryBeanClass());
            definition.getPropertyValues().add("addToConfig", param.getAddToConfig());
            definition.setAttribute("factoryBeanObjectType", beanClassName);
            boolean explicitFactoryUsed = false;
            if (StringUtils.hasText(param.getSqlSessionFactoryBeanName())) {
                definition.getPropertyValues().add("sqlSessionFactory", new RuntimeBeanReference(param.getSqlSessionFactoryBeanName()));
                explicitFactoryUsed = true;
            }
            else if (param.getSqlSessionFactory() != null) {
                definition.getPropertyValues().add("sqlSessionFactory", param.getSqlSessionFactory());
                explicitFactoryUsed = true;
            }

            if (StringUtils.hasText(param.getSqlSessionTemplateBeanName())) {
                if (explicitFactoryUsed) {
                    LOGGER.warn(() -> {
                        return "Cannot use both: sqlSessionTemplate and sqlSessionFactory together. sqlSessionFactory is ignored.";
                    });
                }

                definition.getPropertyValues().add("sqlSessionTemplate", new RuntimeBeanReference(param.getSqlSessionTemplateBeanName()));
                explicitFactoryUsed = true;
            }
            else if (param.getSqlSessionTemplate() != null) {
                if (explicitFactoryUsed) {
                    LOGGER.warn(() -> {
                        return "Cannot use both: sqlSessionTemplate and sqlSessionFactory together. sqlSessionFactory is ignored.";
                    });
                }

                definition.getPropertyValues().add("sqlSessionTemplate", param.getSqlSessionTemplate());
                explicitFactoryUsed = true;
            }

            if (!explicitFactoryUsed) {
                LOGGER.debug(() -> {
                    return "Enabling autowire by type for MapperFactoryBean with name '" + holder.getBeanName() + "'.";
                });
                definition.setAutowireMode(2);
            }

            definition.setLazyInit(param.getLazyInitialization());
            if (!scopedProxy) {
                if ("singleton".equals(definition.getScope()) && param.getDefaultScope() != null) {
                    definition.setScope(param.getDefaultScope());
                }

                if (!definition.isSingleton()) {
                    BeanDefinitionHolder proxyHolder = ScopedProxyUtils.createScopedProxy(holder, registry, true);
                    if (registry.containsBeanDefinition(proxyHolder.getBeanName())) {
                        registry.removeBeanDefinition(proxyHolder.getBeanName());
                    }

                    registry.registerBeanDefinition(proxyHolder.getBeanName(), proxyHolder.getBeanDefinition());
                }
            }
        }

    }

}
