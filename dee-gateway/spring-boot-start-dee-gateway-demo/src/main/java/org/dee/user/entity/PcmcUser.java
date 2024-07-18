package org.dee.user.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class PcmcUser implements Serializable {

    private static final long serialVersionUID = -5721055334139184713L;

    private String userCode;

    private String userName;

    private String userPassword;

    private String userType;

    private String employeeCode;

    private String email;

    private Integer pagesize;

    private String menuType;

    private String skinCode;

    private String lastRole;

    private String defaultRole;

    private Date updatePasswordDate;

    private Integer updatePasswordDays;

    private String createEmployee;

    private String isEnabled;

    private String image;

    private String updatePasswordFlag;
    
    private String description;
    
    private Date startDateActive;
    
    private Date endDateActive;
    
    private Date createDate;
    
    private String createBy;
    
    private Date lastUpdateDate;
    
    private String lastUpdateBy;

}
