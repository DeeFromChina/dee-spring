package org.dee.file.excel.enums;

import org.apache.commons.lang3.StringUtils;
import org.dee.annotations.GetKey;
import org.dee.annotations.GetValue;

/**
 * 导入导出类型
 */
public enum ExcelOperationType {

    /**
     * 导入
     */
    IMPORT("IMPORT", "导入"),

    /**
     * 导出
     */
    EXPORT("EXPORT", "导出"),

    /**
     * 下载模版
     */
    DOWNLOAD("DOWNLOAD", "下载模版");

    @GetKey
    private String value;

    @GetValue
    private String description;

    public String getValue() {
        return value;
    }

    public String getDescription() {
        return this.description;
    }

    ExcelOperationType(String value, String description) {
        this.value = value;
        this.description = description;
    }

    public static String get(String value) {
        if (StringUtils.isNotBlank(value)) {
            for (ExcelOperationType type : values()) {
                if (type.value.equals(value)) {
                    return type.description;
                }
            }
        }
        throw new RuntimeException("未知类型");
    }

    public boolean is(String isEnabled) {
        return this.value.equalsIgnoreCase(isEnabled);
    }
}
