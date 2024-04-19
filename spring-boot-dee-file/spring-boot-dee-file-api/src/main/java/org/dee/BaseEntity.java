package org.dee;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

import java.util.Date;

@Data
public class BaseEntity {

    /**
     * 标准-说明
     */
    @TableField(value = "DESCRIPTION")
    private String description;

    /**
     * 标准-是否启用
     */
    @TableField(value = "IS_ENABLED")
    private String isEnabled;

    /**
     * 标准-自
     */
    @TableField(value = "START_DATE_ACTIVE")
    private Date startDateActive;

    /**
     * 标准-至
     */
    @TableField(value = "END_DATE_ACTIVE")
    private Date endDateActive;

    /**
     * 标准-创建时间
     */
    @TableField(value = "CREATE_DATE")
    private Date createDate;

    /**
     * 标准-创建用户
     */
    @TableField(value = "CREATE_BY")
    private String  createBy;

    /**
     * 标准-最后更新时间
     */
    @TableField(value = "LAST_UPDATE_DATE")
    private Date lastUpdateDate;

    /**
     * 标准-最后更新用户
     */
    @TableField(value = "LAST_UPDATE_BY")
    private String lastUpdateBy;

}
