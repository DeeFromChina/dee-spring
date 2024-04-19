package org.dee.framework.rpc;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.Data;
import org.dee.framework.enums.HttpStatusCode;
import org.dee.framework.http.PageParam;

import java.io.Serializable;
import java.util.List;

@Data
public class RpcResult<T> implements Serializable {

    private Integer code;

    private String message;

    private T body;

    private PageParam pageParam;

    public static <T> RpcResult<T> success() {
        RpcResult<T> rpcResult = new RpcResult<>();
        rpcResult.setCode(HttpStatusCode.OK.key);
        rpcResult.setMessage(HttpStatusCode.OK.value);
        return rpcResult;
    }

    public static RpcResult success(RpcResult rpcResult) {
        return rpcResult;
    }

    public static <T> RpcResult<T> success(T t) {
        RpcResult<T> rpcResult = new RpcResult<>();
        rpcResult.setCode(HttpStatusCode.OK.key);
        rpcResult.setMessage(HttpStatusCode.OK.value);
        rpcResult.setBody(t);
        return rpcResult;
    }

    public static <T> RpcResult<List<T>> success(List<T> list) {
        RpcResult<List<T>> rpcResult = new RpcResult<>();
        rpcResult.setCode(HttpStatusCode.OK.key);
        rpcResult.setMessage(HttpStatusCode.OK.value);
        rpcResult.setBody(list);
        return rpcResult;
    }

    public static <T> RpcResult<Page<T>> success(Page<T> page) {
        RpcResult<Page<T>> rpcResult = new RpcResult<>();
        rpcResult.setCode(HttpStatusCode.OK.key);
        rpcResult.setMessage(HttpStatusCode.OK.value);
        rpcResult.setBody(page);
        rpcResult.setPageParam(initPageParam(page.getCurrent(), page.getSize(), page.getTotal(), page.getPages()));
        return rpcResult;
    }

    /**
     * 初始化页面信息
     * @param current 当前页
     * @param size
     * @param total
     * @param pages
     * @return
     */
    public static PageParam initPageParam(long current, long size, long total, long pages) {
        PageParam pageParam = new PageParam();
        pageParam.setCurrent(current);
        pageParam.setSize(size);
        pageParam.setTotal(total);
        pageParam.setPages(pages);
        return pageParam;
    }

}
