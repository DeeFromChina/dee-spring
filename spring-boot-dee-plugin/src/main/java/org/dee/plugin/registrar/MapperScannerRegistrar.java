package org.dee.plugin.registrar;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.override.MybatisMapperProxy;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.ApplicationContext;

import java.lang.reflect.InvocationTargetException;

public class MapperScannerRegistrar {

    private ApplicationContext applicationContext;

    public void setApplicationContext(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    public void registerKpi(Class<?> componentClass) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        DefaultListableBeanFactory factory = (DefaultListableBeanFactory) applicationContext.getAutowireCapableBeanFactory();

        ClassPathMapperScannerParam param = new ClassPathMapperScannerParam();
        param.setRegistry(factory);
        param.setLazyInitialization(false);

        //mybatis声明逻辑
        ClassPathMapperScanner scanner = new ClassPathMapperScanner(factory);
        scanner.doScan(param,"org.dee");

        injectComponentClass(componentClass);
    }

    /**
     * 注入需加载的class
     * @param componentClass
     */
    private void injectComponentClass(Class<?> componentClass) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        //Bean工厂
        DefaultListableBeanFactory factory = (DefaultListableBeanFactory) applicationContext.getAutowireCapableBeanFactory();
        //创建bean信息.
        BeanDefinitionBuilder beanDefinitionBuilder = BeanDefinitionBuilder.genericBeanDefinition(MybatisMapperProxy.class);
        BeanDefinition beanDefinition = beanDefinitionBuilder.getRawBeanDefinition();

        String beanName = StrUtil.lowerFirst(componentClass.getSimpleName());
        //设置当前bean定义对象是单例的
        beanDefinition.setScope("singleton");
        //将Bean注册到Bean工厂
        factory.registerBeanDefinition(beanName, beanDefinitionBuilder.getBeanDefinition());

    }

    public void test(Class<?> componentClass) {
        // 获取AutowireCapableBeanFactory实例
        AutowireCapableBeanFactory beanFactory = applicationContext.getAutowireCapableBeanFactory();

        //componentClass.

        // 手动触发依赖注入
        beanFactory.autowireBean(myComponent);
    }

}
