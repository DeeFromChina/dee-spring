package org.dee.file.excel.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;
import org.dee.BaseEntity;

import java.io.Serializable;
import java.util.List;

/**
 * DeeExcelSheet
 */
@Data
@Builder
@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
@NoArgsConstructor
@TableName(value = "dee_excel_sheet")
public class DeeExcelSheet extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
    * 主键
    */
    @TableId(value = "ID", type = IdType.AUTO)
    private Integer id;

    /**
    * excel编码
    */
    @TableField(value = "EXCEL_CODE")
    private String excelCode;

    /**
    * sheet编码
    */
    @TableField(value = "SHEET_CODE")
    private String sheetCode;

    /**
    * sheet名称
    */
    @TableField(value = "SHEET_NAME")
    private String sheetName;

    /**
     * sheet对应实体类路径
     */
    @TableField(value = "MAPPER_CLASS_PATH")
    private String mapperClassPath;

    /**
     * sheet对应实体类
     */
    @TableField(exist = false)
    private Class<?> entityClass;

    /**
    * sheet顺序
    */
    @TableField(value = "SHEET_NO")
    private Integer sheetNo;

    /**
    * sheet类型：表格/表单
    */
    @TableField(value = "SHEET_TYPE")
    private String sheetType;

    /**
     * 导入类型：
     * byName、按表头名称获取值
     * byCol、按列数获取值
     */
    @TableField(exist = false)
    private String importCellType;

    /**
     * 单元格内容
     */
    @TableField(exist = false)
    private List<DeeExcelCell> cells;

}
