package org.dee.plugin.factory;

import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.type.AnnotationMetadata;

import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;

public class JarBeanDefinitionRegistrar implements ImportBeanDefinitionRegistrar {

    private static final String JAR_PATH = "path/to/your/external.jar"; // 外部JAR文件的路径

    /**
     * 注册bean
     * @param importingClassMetadata1
     * @param registry
     */
    @Override
    public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata1, BeanDefinitionRegistry registry) {
        try {
            // 使用URLClassLoader加载外部JAR文件
            URLClassLoader classLoader = new URLClassLoader(new URL[]{new File(JAR_PATH).toURI().toURL()}, getClass().getClassLoader());

            // 使用PathMatchingResourcePatternResolver来查找JAR中的bean定义文件（例如：*.class）
            PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver(classLoader);
            Resource[] resources = resolver.getResources("classpath*:org/dee/*.class"); // 假设bean定义在com.example.myapp包下

            // 遍历资源并注册bean定义
            for (Resource resource : resources) {
                String beanName = resource.getFilename().replace(".class", ""); // 假设类名即bean名
                RootBeanDefinition beanDefinition = new RootBeanDefinition(classLoader.loadClass(beanName));
                registry.registerBeanDefinition(beanName, beanDefinition);
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to register beans from external JAR", e);
        }
    }

//    public void registerBeansFromJar(URLClassLoader classLoader) {
//        // 获取Bean定义注册器
//        BeanDefinitionRegistry registry = (BeanDefinitionRegistry)
//                ApplicationContextProvider.getApplicationContext().getBeanFactory();
//
//        // 扫描JAR文件中的类
//        try {
//            Enumeration<URL> urls = classLoader.getResources("com/example/myapp/"); // 假设你的类在com.example.myapp包下
//            while (urls.hasMoreElements()) {
//                URL url = urls.nextElement();
//                URLConnection connection = url.openConnection();
//                connection.connect();
//                Enumeration<String> entries = ((ZipFile) connection.getContent()).entries();
//                while (entries.hasMoreElements()) {
//                    String entryName = entries.nextElement();
//                    if (entryName.endsWith(".class")) {
//                        String className = entryName.substring(0, entryName.length() - 6).replace('/', '.');
//                        Class<?> clazz = classLoader.loadClass(className);
//
//                        // 注册Bean定义，这里假设所有类都应该被注册为单例Bean
//                        RootBeanDefinition beanDefinition = new RootBeanDefinition(clazz);
//                        beanDefinition.setScope(BeanDefinition.SCOPE_SINGLETON);
//                        registry.registerBeanDefinition(className, beanDefinition);
//                    }
//                }
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }

}