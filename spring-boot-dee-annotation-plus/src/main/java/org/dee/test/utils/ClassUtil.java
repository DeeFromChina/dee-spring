package org.dee.test.utils;

import lombok.SneakyThrows;

public class ClassUtil {

    @SneakyThrows
    public static boolean isExtends(Class parentClass, Class<?> target) {
        return parentClass.isAssignableFrom(target);
    }

    @SneakyThrows
    public static boolean isExtends(String parentClassPath, Class<?> target) {
        return Class.forName(parentClassPath).isAssignableFrom(target);
    }

    @SneakyThrows
    public static boolean isExtends(String parentClassPath, String targetClassPath) {
        return Class.forName(parentClassPath).isAssignableFrom(Class.forName(targetClassPath));
    }

}
