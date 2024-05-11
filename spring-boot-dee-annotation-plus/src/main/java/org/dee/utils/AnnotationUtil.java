//package org.dee.processor.test.utils;
//
//import lombok.SneakyThrows;
//
//import java.lang.annotation.Annotation;
//import java.lang.reflect.Field;
//import java.lang.reflect.InvocationHandler;
//import java.lang.reflect.Proxy;
//import java.util.Map;
//import java.util.Objects;
//
//public class AnnotationUtil {
//
//    @SneakyThrows
//    public static <T extends Annotation> void setValueToAnnotation(T annotation, String valueName, Object value) {
//        InvocationHandler invocationHandler = null;
//        if(Objects.nonNull(annotation)){
//            invocationHandler = Proxy.getInvocationHandler(annotation);
//        }
//        if(Objects.isNull(invocationHandler)){
//            return;
//        }
//        Field nameField = invocationHandler.getClass().getDeclaredField("memberValues");
//        nameField.setAccessible(true);
//        Map<String, Object> memberValues = (Map<String, Object>) nameField.get(invocationHandler);
//        memberValues.put(valueName, value);
//    }
//
//}
