package org.dee.framework.utils;

import lombok.extern.slf4j.Slf4j;
import org.dee.framework.enums.HttpStatusCode;
import org.dee.framework.rpc.RpcResult;

/**
 * 校验rpc接口类
 */
@Slf4j
public class RpcResultUtil {
    private RpcResultUtil() {
        throw new IllegalStateException("RpcResultUtil class");
    }

    public static boolean restRequestIsSuccess(RpcResult rpcResult) {
        if (rpcResult == null || rpcResult.getCode() == null) {
            throw new RuntimeException("远程请求错误");
        }
        if (!HttpStatusCode.OK.is(rpcResult.getCode())) {
            log.error(rpcResult.getCode().toString());
            throw new RuntimeException(rpcResult.getMessage());
        }
        return true;
    }
}
