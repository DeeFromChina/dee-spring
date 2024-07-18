package org.dee.rpc;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Data
public class RPCResult<T> implements Serializable {

    private Integer code;

    private String message;

    private T body;

    public <V> List<V> getJavaList(Class<V> clazz) {
        if (this.body instanceof JSONArray) {
            JSONArray jsonArray = (JSONArray)this.body;
            return JSONUtil.toList(jsonArray, clazz);
        } else if (this.body instanceof List) {
            return (List) this.body;
        } else {
            return new ArrayList<>();
        }
    }

    public <V> V getJavaObject(Class<V> clazz) {
        if (null == this.body) {
            return null;
        } else if (this.body instanceof JSONObject) {
            JSONObject jsonObject = (JSONObject)this.body;
            return JSONUtil.toBean(jsonObject, clazz);
        } else if (clazz.isInstance(this.body)) {
            return (V) this.body;
        } else {
            return BeanUtil.toBean(this.body, clazz);
        }
    }

}
