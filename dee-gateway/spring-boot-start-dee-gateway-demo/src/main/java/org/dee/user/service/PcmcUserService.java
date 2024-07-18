package org.dee.user.service;

import org.dee.user.entity.PcmcUser;

import java.util.List;

public interface PcmcUserService {
    /**
     *分页查询用户信息
     * @Author Chen Zhou
     * @param userCode:用户编码，模糊查询
     * @param userName:用户名称，模糊查询
     * @param userCodeList:用户编码集合，in条件查询，示例参数："userCodeList":["admin","etlAdmin"]
     * @return java.util.List<com.sunline.pcmc.modules.user.entity.PcmcUser>
     * @Date 2021/4/1 16:51
     **/
    List<PcmcUser> queryPcmcUserList(String userCode, String userName, List<String> userCodeList);

}
