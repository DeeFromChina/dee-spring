package org.dee.web.framework.http;

import lombok.Data;
import org.dee.web.framework.enums.HttpStatusCode;

import java.io.Serializable;

@Data
public class WebResponse<T> implements Serializable {

    private Integer code;

    private String message;

    private Object data;

    private PageParam pageParam;

    public WebResponse() {

    }

    public WebResponse(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    public static <T> WebResponse<T> newInstance() {
        return new WebResponse();
    }

    public static <T> WebResponse<T> success(Object obj) {
        WebResponse<T> result = new WebResponse();
        result.setCode(HttpStatusCode.OK.getKey());
        result.setMessage(HttpStatusCode.OK.getValue());
        result.setData(obj);
        return result;
    }

    public static <T> WebResponse<T> success() {
        return success(null);
    }

    public static <T> WebResponse<T> error(String msg, Object... args) {
        WebResponse<T> result = new WebResponse();
        result.setCode(HttpStatusCode.INTERNAL_SERVER_ERROR.getKey());
        result.setMessage(HttpStatusCode.INTERNAL_SERVER_ERROR.getValue());
        return result;
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
