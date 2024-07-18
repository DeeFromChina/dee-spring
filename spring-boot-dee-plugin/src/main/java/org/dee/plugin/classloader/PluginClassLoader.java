package org.dee.plugin.classloader;

import java.io.*;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;

public class PluginClassLoader extends ClassLoader {

    private String classPath;

    public PluginClassLoader(String classPath, ClassLoader parent) {
        super(parent);
        this.classPath = classPath;
    }

    public PluginClassLoader(String classPath) {
        this(classPath, PluginClassLoader.class.getClassLoader());
    }

    @Override
    public Class<?> findClass(String name) throws ClassNotFoundException {
        byte[] b = loadClassData(name);
        return defineClass(name, b, 0, b.length);
    }

    private byte[] loadClassData(String name) throws ClassNotFoundException {
        // 这里只是一个简单的示例，实际中你可能需要从文件系统、网络或其他地方加载类数据
        String fileName = name.replace('.', '/') + ".class";
        try (InputStream inputStream = new FileInputStream(new File(classPath + File.separator + fileName))) {
            ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
            int ch;
            while ((ch = inputStream.read()) != -1) {
                byteStream.write(ch);
            }
            return byteStream.toByteArray();
        } catch (IOException e) {
            throw new ClassNotFoundException("Class " + name + " not found", e);
        }
    }

    public void addURL(URL url) throws Exception {
        // 获取URLClassLoader类的addURL方法
        Method addurl = URLClassLoader.class.getDeclaredMethod("addURL", URL.class);
        // 设置为可访问，因为addURL是受保护的
        addurl.setAccessible(true);
        // 调用addURL方法
        addurl.invoke(this, url);
    }

    public void loadJar(String jarPath) {
        PluginClassLoader classLoader = new PluginClassLoader(jarPath);

    }

}
