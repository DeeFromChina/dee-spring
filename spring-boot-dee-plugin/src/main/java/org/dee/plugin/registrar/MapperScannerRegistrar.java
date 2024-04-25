package org.dee.plugin.registrar;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.override.MybatisMapperProxy;
import org.apache.ibatis.binding.MapperRegistry;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.mapper.MapperFactoryBean;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.ApplicationContext;

public class MapperScannerRegistrar {

    private ApplicationContext applicationContext;

    public void setApplicationContext(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    public void registerKpi(Class<?> componentClass) throws Exception {
        DefaultListableBeanFactory factory = (DefaultListableBeanFactory) applicationContext.getAutowireCapableBeanFactory();

        ClassPathMapperScannerParam param = new ClassPathMapperScannerParam();
        param.setRegistry(factory);
        param.setLazyInitialization(false);

        //mybatis声明逻辑
        ClassPathMapperScanner scanner = new ClassPathMapperScanner(factory);
        scanner.doScan(param,"com.sunline");

        injectComponentClass(componentClass);
    }

    /**
     * 注入需加载的class
     * @param componentClass
     */
    private void injectComponentClass(Class<?> componentClass) throws Exception {
        //Bean工厂
        DefaultListableBeanFactory factory = (DefaultListableBeanFactory) applicationContext.getAutowireCapableBeanFactory();
        //创建bean信息.
        BeanDefinitionBuilder beanDefinitionBuilder = BeanDefinitionBuilder.genericBeanDefinition(MybatisMapperProxy.class);
        BeanDefinition beanDefinition = beanDefinitionBuilder.getRawBeanDefinition();

        String beanName = StrUtil.lowerFirst(componentClass.getSimpleName());
        //设置当前bean定义对象是单例的
        beanDefinition.setScope("singleton");

        SqlSessionFactory sqlSessionFactory = applicationContext.getBean(SqlSessionFactory.class);

        MapperFactoryBean<?> mapperFactoryBean = new MapperFactoryBean<>(componentClass);
        mapperFactoryBean.setSqlSessionFactory(sqlSessionFactory);

        // 注入mybatis的MapperRegistry注册工厂
        Configuration configuration = sqlSessionFactory.getConfiguration();
        MapperRegistry mapperRegistry = configuration.getMapperRegistry();
        mapperRegistry.addMapper(componentClass);

        // 手动触发依赖注入
        factory.autowireBean(mapperFactoryBean.getObject());

        //将Bean注册到Bean工厂
        factory.registerBeanDefinition(beanName, beanDefinitionBuilder.getBeanDefinition());

    }

    public void test(Class<?> componentClass) throws Exception {
        // 获取AutowireCapableBeanFactory实例
        AutowireCapableBeanFactory beanFactory = applicationContext.getAutowireCapableBeanFactory();

        //componentClass.
        SqlSessionFactory sqlSessionFactory = applicationContext.getBean(SqlSessionFactory.class);

        MapperFactoryBean<?> mapperFactoryBean = new MapperFactoryBean<>(componentClass);
        mapperFactoryBean.setSqlSessionFactory(sqlSessionFactory);
        // 手动触发依赖注入
        beanFactory.autowireBean(mapperFactoryBean.getObject());
    }

}
