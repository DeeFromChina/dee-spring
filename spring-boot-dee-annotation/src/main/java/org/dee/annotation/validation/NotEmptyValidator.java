package org.dee.annotation.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class NotEmptyValidator implements ConstraintValidator<NotEmpty, String> {

    private String field;

    @Override
    public void initialize(NotEmpty constraintAnnotation) {
        // 初始化方法，如果需要的话可以在这里读取注解上的元数据
        this.field = constraintAnnotation.value();
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        System.out.println(value);
        // 实现你的校验逻辑
        // 例如，检查value是否为null或满足其他条件
        return true;
        //return value != null && !value.isEmpty();
    }

}
