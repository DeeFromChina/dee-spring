package org.dee.user.service.impl;

import cn.hutool.core.util.StrUtil;
import com.alibaba.nacos.common.utils.CollectionUtils;
import com.google.common.collect.Maps;
import org.dee.common.service.impl.WebClientImpl;
import org.dee.rpc.RPCRequest;
import org.dee.rpc.RPCResult;
import org.dee.user.entity.PcmcUser;
import org.dee.user.service.PcmcUserService;
import org.dee.user.webclient.PcmcUserWebClient;
import org.dee.agent.utils.RpcRequestUtil;
import org.dee.agent.utils.RpcResultUtil;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@Service
public class PcmcUserServiceImpl extends WebClientImpl implements PcmcUserService {

    @Resource
    private PcmcUserWebClient userClient;

    /**
     * 用户编码集合
     */
    private static final String USER_CODES_KEY = "userCodeList";
    /**
     * 用户名
     */
    private static final String USER_NAME = "userName";
    /**
     * 用户编码
     */
    private static final String USER_CODE = "userCode";

    /**
     * @param userCode
     * @param userName
     * @param userCodeList 用户编码集合，in条件查询，示例参数："userCodeList":["admin","etlAdmin"]
     * @return * @return java.util.List<com.sunline.pcmc.modules.user.entity.PcmcUser>
     * 查询绩效系统表信息
     * @Author Hu Tao
     * @Date 2020/7/24
     * @Param userCode:用户编码，模糊查询
     * @Param userName:用户名称，模糊查询
     **/
    @Override
    public List<PcmcUser> queryPcmcUserList(String userCode, String userName, List<String> userCodeList) {
        Map<String, Object> paraMap = Maps.newHashMap();
        if (StrUtil.isNotBlank(userCode)) {
            paraMap.put(USER_CODE, userCode);
        }
        if (StrUtil.isNotBlank(userName)) {
            paraMap.put(USER_NAME, userName);
        }
        if (CollectionUtils.isNotEmpty(userCodeList)) {
            paraMap.put(USER_CODES_KEY, userCodeList);
        }
        RPCRequest rpcRequest = RpcRequestUtil.build(paraMap);
        RPCResult rpcResult = userClient.queryList(rpcRequest);
        if (!RpcResultUtil.restRequestIsSuccess(rpcResult)) {
            throw new RuntimeException(rpcResult.getMessage());
        }
        return rpcResult.getJavaList(PcmcUser.class);
    }

}
