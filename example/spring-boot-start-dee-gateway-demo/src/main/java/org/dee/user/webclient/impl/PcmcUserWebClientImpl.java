package org.dee.user.webclient.impl;

import org.dee.common.service.impl.WebClientImpl;
import org.dee.rpc.RPCRequest;
import org.dee.rpc.RPCResult;
import org.dee.user.entity.PcmcUser;
import org.dee.user.webclient.PcmcUserWebClient;
import org.springframework.stereotype.Service;

@Service
public class PcmcUserWebClientImpl extends WebClientImpl implements PcmcUserWebClient {

    private static final String baseUrl = "http://pcmc-service/rest/users";

    /**
     * 获取所有用户
     *
     * @param rpcRequest
     * @return
     */
    @Override
    public RPCResult queryList(RPCRequest rpcRequest) {
        return doPostRequestReturnList(baseUrl, "/query/list", rpcRequest, PcmcUser.class);
    }

    /**
     * @param rpcRequest :
     * @Description: 分页获取用户信息
     * @Author: Zhou Chen
     * @Date: 2020/8/26 15:38
     * @return: com.sunline.jraf.rpc.model.RPCResult
     **/
    @Override
    public RPCResult queryUserPage(RPCRequest rpcRequest) {
        return doPostRequestReturnList(baseUrl, "/query/page", rpcRequest, PcmcUser.class);
    }

    /**
     * 根据用户编码获取用户信息
     *
     * @param rpcRequest
     * @return cn.sunline.edsp.msdp.rpc.model.RPCResult
     * @Author Xutong Li @Date 2021/10/9
     */
    @Override
    public RPCResult getPcmcUser(RPCRequest rpcRequest) {
        return doPostRequestReturnEntity(baseUrl, "/query/unique", rpcRequest, PcmcUser.class);
    }

}
