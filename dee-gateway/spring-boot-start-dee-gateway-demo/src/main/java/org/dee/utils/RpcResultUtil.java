package org.dee.agent.utils;

import lombok.extern.slf4j.Slf4j;
import org.dee.enums.HttpStateCode;
import org.dee.rpc.RPCResult;

/**
 * 校验rpc接口类
 */
@Slf4j
public class RpcResultUtil {
    private RpcResultUtil() {
        throw new IllegalStateException("RpcResultUtil class");
    }

    public static <T> RPCResult<T> returnSuccess(RPCResult<T> rpcResult) {
        return rpcResult;
    }

    public static boolean restRequestIsSuccess(RPCResult rpcResult) {
        if (rpcResult == null || rpcResult.getCode() == null) {
            throw new RuntimeException("远程请求错误");
        }
        if (!HttpStateCode.OK.is(rpcResult.getCode())) {
            log.error(rpcResult.getCode().toString());
            throw new RuntimeException(rpcResult.getMessage());
        }
        return true;
    }
}
