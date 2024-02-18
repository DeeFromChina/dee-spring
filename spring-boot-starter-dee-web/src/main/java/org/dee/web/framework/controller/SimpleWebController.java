package org.dee.web.framework.controller;

import org.dee.web.framework.service.IWebService;

import java.util.List;

/**
 * 简单模式
 * 参数无需校验，无需加工
 * 返回无需加工
 * @param <T>
 * @param <S>
 */
public class SimpleWebController<T, S extends IWebService> extends BaseWebController<T, S> {

    @Override
    protected void vaildPage(T param) {

    }

    @Override
    protected void vaildList(T param) {

    }

    @Override
    protected void vaildAdd(T t) {

    }

    @Override
    protected void vaildAddBatch(List<T> entities) {

    }

    @Override
    protected void vaildUpdate(T t) {

    }

    @Override
    protected void vaildUpdateBatch(List<T> entities) {

    }
}
