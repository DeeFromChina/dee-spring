package org.dee.file.excel.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;

import java.io.Serializable;

/**
 * DeeExcelTemplate
 */
@Data
@TableName(value = "dee_excel_template")
public class DeeExcelTemplate extends DeeExcelInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 配置内容
     */
    @TableField(value = "CONFIG_CONTENT")
    private String configContent;

}
