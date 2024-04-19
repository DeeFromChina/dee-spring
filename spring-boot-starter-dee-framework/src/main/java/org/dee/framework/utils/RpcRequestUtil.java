package org.dee.framework.utils;

import org.dee.framework.rpc.RpcRequest;

public class RpcRequestUtil {

    public static <T> RpcRequest<T> build(T body) {
        return new RpcRequest(body);
    }

}
