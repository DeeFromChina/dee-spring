package org.dee.web.framework.utils;

import org.dee.web.framework.rpc.RPCRequest;

public class RpcRequestUtil {

    public static <T> RPCRequest<T> build(T body) {
        return new RPCRequest(body);
    }

}
