package org.dee.plugin.factory;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.JarURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

@Component
public class ControllerFactory implements ApplicationContextAware {

    private ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    /**
     * 注册Controller
     * @param controllerBeanName
     * @throws Exception
     */
    public void registerController(String controllerBeanName) throws Exception{
        final RequestMappingHandlerMapping requestMappingHandlerMapping=
                applicationContext.getBean(RequestMappingHandlerMapping.class);
        if(requestMappingHandlerMapping!=null){
            String handler=controllerBeanName;
            Object controller= applicationContext.getBean(handler);
            if(controller==null){
                return;
            }

            //注册Controller
            Method method=requestMappingHandlerMapping.getClass().getSuperclass().getSuperclass().
                    getDeclaredMethod("detectHandlerMethods",Object.class);
            //将private改为可使用
            method.setAccessible(true);
            method.invoke(requestMappingHandlerMapping,handler);
        }
    }

    /**
     * @param
     * @method 创建通知
     */
    public void bulidController(String jarPath) {
        try {
            Boolean isLoad = false;
            //获取jar包url路径
            URL targetUrl = new URL("jar:file:" + jarPath + "!/");
            //获取系统类加载器
            URLClassLoader jarclassLoader = (URLClassLoader) ClassLoader.getSystemClassLoader();
            //遍历所有jar包url比较是否已经加载过插件
            URL[] urLs = jarclassLoader.getURLs();
            for (URL url : urLs) {
                if (targetUrl.equals(url)) {
                    System.out.println("jar包已经加载过了");
                    isLoad = true;
                    break;
                }
            }
            //没有加载过插件时
            if (!isLoad) {
                //加载jar包
                Method addurl = URLClassLoader.class.getDeclaredMethod("addURL", URL.class);
                addurl.setAccessible(true);
                addurl.invoke(jarclassLoader, targetUrl);
            }else{
                return;
            }
            //扫描Class并注入
            scanComponentClass(targetUrl, jarclassLoader);

        } catch (MalformedURLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 扫描class并注入
     * @param targetUrl
     * @param classLoader
     * @throws IOException
     * @throws ClassNotFoundException
     * @throws InvocationTargetException
     * @throws NoSuchMethodException
     * @throws IllegalAccessException
     */
    private void scanComponentClass(URL targetUrl, URLClassLoader classLoader) throws IOException, ClassNotFoundException, InvocationTargetException, NoSuchMethodException, IllegalAccessException {
        JarURLConnection jarConnection = (JarURLConnection) targetUrl.openConnection();
        JarFile jarFile = jarConnection.getJarFile();
        // 列出JAR中的所有条目（包括类和其他资源）
        Enumeration<JarEntry> entries = jarFile.entries();
        while (entries.hasMoreElements()) {
            JarEntry entry = entries.nextElement();
            String name = entry.getName();

            // 如果条目是类文件，打印类名
            if (name.endsWith(".class")) {
                String className = name.substring(0, name.length() - 6).replace('/', '.');
                System.out.println(className);

                // 使用URLClassLoader加载类
                Class<?> clazz = classLoader.loadClass(className);

                // 检查类是否有Spring的组件注解
                if (isComponent(clazz)) {
                    // 创建bean定义并注册到Spring容器中
                    injectComponentClass(classLoader, className);
                }
            }
        }

        // 关闭JAR文件连接
        jarFile.close();
    }

    /**
     * 判断是否需要扫描
     * @param clazz
     * @return
     */
    private static boolean isComponent(Class<?> clazz) {
        // 检查类是否有Spring的组件注解，如@Component, @Service, @Repository, @Controller等
        return clazz.isAnnotationPresent(org.springframework.stereotype.Component.class) ||
                clazz.isAnnotationPresent(org.springframework.stereotype.Service.class) ||
                clazz.isAnnotationPresent(org.springframework.stereotype.Repository.class) ||
                clazz.isAnnotationPresent(org.springframework.stereotype.Controller.class) ||
                clazz.isAnnotationPresent(org.springframework.web.bind.annotation.RestController.class);
    }

    /**
     * 注入需加载的class
     * @param jarclassLoader
     * @param classPath
     */
    private void injectComponentClass(URLClassLoader jarclassLoader, String classPath) throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Class<?> pluginClass = jarclassLoader.loadClass(classPath);
        //Bean工厂
        DefaultListableBeanFactory factory = (DefaultListableBeanFactory) applicationContext.getAutowireCapableBeanFactory();
        //创建bean信息.
        BeanDefinitionBuilder beanDefinitionBuilder = BeanDefinitionBuilder.genericBeanDefinition(pluginClass);
        BeanDefinition beanDefinition = beanDefinitionBuilder.getRawBeanDefinition();

        //设置当前bean定义对象是单利的
        beanDefinition.setScope("singleton");
        //将Bean注册到Bean工厂
        factory.registerBeanDefinition("testController", beanDefinitionBuilder.getBeanDefinition());
        //Mapping关系注入
        final RequestMappingHandlerMapping requestMappingHandlerMapping=applicationContext.getBean(RequestMappingHandlerMapping.class);
        //注册Controller
        Method method=RequestMappingHandlerMapping.class.
                getSuperclass().getSuperclass().
                getDeclaredMethod("detectHandlerMethods",Object.class);
        //将private改为可使用
        method.setAccessible(true);
        method.invoke(requestMappingHandlerMapping,"testController");
    }

    /**
     * 往ClassLoad中注入bean
     * @param jarclassLoader
     * @param path
     * @throws NoSuchMethodException
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     * @throws ClassNotFoundException
     */
    private void addBean(URLClassLoader jarclassLoader, String path) throws NoSuchMethodException,IllegalAccessException,InvocationTargetException,ClassNotFoundException {
        //将变量首字母置小写
        String beanName = StringUtils.uncapitalize(path);

        beanName =  beanName.substring(beanName.lastIndexOf(".")+1);
        beanName = StringUtils.uncapitalize(beanName);

        Class<?> pluginClass = jarclassLoader.loadClass(path);
        //Bean工厂
        DefaultListableBeanFactory factory = (DefaultListableBeanFactory) applicationContext.getAutowireCapableBeanFactory();
        //创建bean信息.
        BeanDefinitionBuilder beanDefinitionBuilder = BeanDefinitionBuilder.genericBeanDefinition(pluginClass);
        BeanDefinition beanDefinition = beanDefinitionBuilder.getRawBeanDefinition();

        //设置当前bean定义对象是单例的
        beanDefinition.setScope("singleton");
        //将Bean注册到Bean工厂
        factory.registerBeanDefinition(beanName, beanDefinitionBuilder.getBeanDefinition());
        //Mapping关系注入
        final RequestMappingHandlerMapping requestMappingHandlerMapping = applicationContext.getBean(RequestMappingHandlerMapping.class);
        //注册Controller
        Method method=RequestMappingHandlerMapping.class.
                getSuperclass().getSuperclass().
                getDeclaredMethod("detectHandlerMethods",Object.class);
        //将private改为可使用
        method.setAccessible(true);
        method.invoke(requestMappingHandlerMapping,beanName);
    }

}
