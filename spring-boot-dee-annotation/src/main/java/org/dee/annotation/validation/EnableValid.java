package org.dee.annotation.validation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.TYPE}) // 可以用于字段或方法参数
@Retention(RetentionPolicy.RUNTIME) // 在运行时保留注解，以便可以通过反射读取
public @interface EnableValid {

    String[] methods() default {};

}
