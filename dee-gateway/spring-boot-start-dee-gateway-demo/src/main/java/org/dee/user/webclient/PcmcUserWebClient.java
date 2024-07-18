package org.dee.user.webclient;

import org.dee.rpc.RPCRequest;
import org.dee.rpc.RPCResult;

public interface PcmcUserWebClient {

    /**
     * 获取所有用户
     * @param rpcRequest
     * @return
     */
    RPCResult queryList(RPCRequest rpcRequest);

    /**
     * @Description: 分页获取用户信息
     * @Author: Zhou Chen
     * @Date: 2020/8/26 15:38
     * @param rpcRequest:
     * @return: com.sunline.jraf.rpc.model.RPCResult
     **/
    RPCResult queryUserPage(RPCRequest rpcRequest);

    /**
     * 根据用户编码获取用户信息
     * @Author Xutong Li @Date 2021/10/9
     * @param rpcRequest
     * @return cn.sunline.edsp.msdp.rpc.model.RPCResult
     */
    RPCResult getPcmcUser(RPCRequest rpcRequest);

}
