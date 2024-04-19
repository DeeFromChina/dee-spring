package org.dee.framework.rpc;

import lombok.Data;
import org.dee.framework.http.PageParam;

import java.io.Serializable;

@Data
public class RpcRequest<T> implements Serializable {

    private Integer code;

    private String message;

    private T body;

    private PageParam pageParam;

    public RpcRequest() {

    }

    public RpcRequest(T body) {
        this.body = body;
    }

}
