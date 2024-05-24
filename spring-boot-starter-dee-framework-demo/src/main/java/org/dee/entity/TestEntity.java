package org.dee.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import org.dee.annotation.validation.NotEmpty;

@Data
@TableName("test")
public class TestEntity extends BaseEntity {

    @TableField("code")
    @NotEmpty(value = "code", message = "字段不能为空")
    private String code;

}
