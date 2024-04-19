package org.dee.plugin.registrar;

import lombok.Data;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.mapper.MapperFactoryBean;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;

import java.lang.annotation.Annotation;

@Data
public class ClassPathMapperScannerParam {

    private DefaultListableBeanFactory registry;

    static final String FACTORY_BEAN_OBJECT_TYPE = "factoryBeanObjectType";

    private Boolean addToConfig = true;

    private Boolean lazyInitialization;

    private SqlSessionFactory sqlSessionFactory;

    private SqlSessionTemplate sqlSessionTemplate;

    private String sqlSessionTemplateBeanName;

    private String sqlSessionFactoryBeanName;

    private Class<? extends Annotation> annotationClass;

    private Class<?> markerInterface;

    //private Class<? extends MapperFactoryBean> mapperFactoryBeanClass = MapperFactoryBean.class;
    private Class<MapperFactoryBean> mapperFactoryBeanClass = MapperFactoryBean.class;

    private String defaultScope;

}
