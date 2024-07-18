package org.dee.rpc;

import lombok.Data;

import java.io.Serializable;

@Data
public class RPCRequest<T> implements Serializable {

    private Integer code;

    private String message;

    private T body;

    public RPCRequest() {

    }

    public RPCRequest(T body) {
        this.body = body;
    }

}
