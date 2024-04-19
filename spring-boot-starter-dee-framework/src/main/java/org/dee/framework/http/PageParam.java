package org.dee.framework.http;

import lombok.Data;

import java.io.Serializable;

@Data
public class PageParam implements Serializable {

    private static final long serialVersionUID = 1L;

    protected long total;

    protected long size;

    protected long current;

    protected long pages;

    public static PageParam newInstance() {
        return new PageParam();
    }

}
