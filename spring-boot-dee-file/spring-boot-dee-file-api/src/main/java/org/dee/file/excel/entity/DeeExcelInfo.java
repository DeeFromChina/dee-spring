package org.dee.file.excel.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;
import org.dee.entity.BaseEntity;

import java.io.Serializable;
import java.util.List;

/**
 * DeeExcelInfo
 */
@Data
@Builder
@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
@NoArgsConstructor
@TableName(value = "dee_excel_info")
public class DeeExcelInfo extends BaseEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
    * excel编码
    */
    @TableId(value = "EXCEL_CODE")
    private String excelCode;

    /**
    * excel名称
    */
    @TableField(value = "EXCEL_NAME")
    private String excelName;

    /**
     * sheet配置项
     */
    @TableField(exist = false)
    private List<DeeExcelSheet> sheets;

}
