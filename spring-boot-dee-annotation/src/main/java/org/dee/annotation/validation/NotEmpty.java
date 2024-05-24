package org.dee.annotation.validation;

import org.springframework.core.annotation.AliasFor;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD, ElementType.PARAMETER}) // 可以用于字段或方法参数
@Retention(RetentionPolicy.RUNTIME) // 在运行时保留注解，以便可以通过反射读取
@Constraint(validatedBy = {NotEmptyValidator.class})
public @interface NotEmpty {

    @AliasFor("field")
    String value() default "";

    String fieldValue() default "";

    String message() default "字段验证失败"; // 默认的验证失败消息

    Class<?>[] groups() default {}; // 默认的groups是空的，但你应该定义它
    Class<? extends Payload>[] payload() default {}; // 默认的payload也是空的

}
