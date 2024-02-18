package org.dee.file.excel.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.*;
import org.dee.BaseEntity;

import java.io.Serializable;
import java.util.LinkedHashMap;

/**
 * DeeExcelCell
 */
@Data
@Builder
@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
@NoArgsConstructor
@TableName(value = "dee_excel_cell")
public class DeeExcelCell extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
    * 主键
    */
    @TableId(value = "ID", type = IdType.AUTO)
    private Integer id;

    /**
     * 关联sheet编码
     */
    @TableField(value = "SHEET_CODE")
    private String sheetCode;

    /**
     * 字段编码
     */
    @TableField(value = "FIELD_CODE")
    private String fieldCode;

    /**
     * 字段名称
     */
    @TableField(value = "FIELD_NAME")
    private String fieldName;

    /**
     * 开始行数
     */
    @TableField(value = "ROW_NUM")
    private Integer rowNum;

    /**
     * 开始列数
     */
    @TableField(value = "COL_NUM")
    private String colNum;

    /**
     * 结束行数
     */
    @TableField(value = "END_ROW_NUM")
    private Integer endRowNum;

    /**
     * 结束列数
     */
    @TableField(value = "END_COL_NUM")
    private String endColNum;

    /**
     * 单元格日期格式
     */
    @TableField(value = "CELL_FORMAT", updateStrategy = FieldStrategy.IGNORED)
    private String cellFormat;

    /**
    * 单元格类型：表头/表内容
    */
    @TableField(value = "CELL_TYPE")
    private String cellType;

    /**
     * 单元格类型<br/>
     * 数字:number<br/>
     * 文本:String<br/>
     */
    @TableField(value = "CELL_DATA_TYPE")
    private String cellDataType;

    /**
     * 有3种情况<br/>
     * 1、使用值集，这里是值集类型(keyValueDataSource=valueSet)<br/>
     * 2、使用枚举(keyValueDataSource=enum)<br/>
     * 3、使用自定义的key-value(keyValueDataSource=self)
     */
    @TableField(exist = false)
    private String keyValueDataSource;

    /**
     * 单元格下拉框类型
     */
    @TableField(value = "REPLACE_TYPE")
    private String replaceType;

    /**
    * 单元格下拉框类型编码
    */
    @TableField(value = "REPLACE_CODE", updateStrategy = FieldStrategy.IGNORED)
    private String replaceCode;

    /**
     * 业务字典
     */
    @TableField(exist = false)
    private LinkedHashMap<String, String> keyValue;

    /**
     * 下拉框
     */
    @TableField(exist = false)
    private String[] valuesArrs;

    /**
     * 规则编码<br/>
     * notnull：必填<br/>
     * null：非必填<br/>
     * uppercase：大写
     */
    @TableField(value = "RULE_CODE")
    private String ruleCode;

    /**
     * 规则名称
     */
    @TableField(value = "RULE_NAME")
    private String ruleName;

    /**
     * 列宽(最大只246)
     */
    @TableField(value = "WIDTH")
    private Integer width;

    /**
     * 填入字段类型
     * list:多条，按行插入
     * alone:单个，按单元格插入
     * 如果是list的话rowNum和colNum是开始的行列数,如果是alone的话rowNum和colNum是单元格的位置
     */
    @TableField(value = "FILL_IN_FIELD_TYPE")
    private String fillInFieldType;

    /**
     * 是否需要合并单元格
     */
    @TableField(value = "IS_MERGE")
    private Integer isMerge;

    /**
     * 数值
     */
    @TableField(exist = false)
    private String cellValue;

    /**
     * 单元格颜色
     */
    @TableField(value = "CELL_COLOR")
    private short cellColor;

}
