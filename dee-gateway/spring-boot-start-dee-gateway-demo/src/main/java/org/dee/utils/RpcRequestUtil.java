package org.dee.agent.utils;

import org.dee.rpc.RPCRequest;

public class RpcRequestUtil {

    public static <T> RPCRequest<T> build(T body) {
        return new RPCRequest(body);
    }

}
