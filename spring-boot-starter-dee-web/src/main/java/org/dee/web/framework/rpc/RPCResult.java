package org.dee.web.framework.rpc;

import lombok.Data;

import java.io.Serializable;

@Data
public class RPCResult<T> implements Serializable {

    private Integer code;

    private String message;

    private T body;

}
