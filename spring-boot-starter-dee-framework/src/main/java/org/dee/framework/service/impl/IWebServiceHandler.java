package org.dee.framework.service.impl;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import org.apache.poi.ss.usermodel.Workbook;
import org.dee.file.utils.FileUtil;
import org.dee.framework.utils.RpcResultUtil;
import org.dee.framework.rpc.RpcResult;

import javax.servlet.http.HttpServletResponse;
import java.util.Date;

public class IWebServiceHandler {

    protected void vaildFeginResult(RpcResult rpcResult) {
        if (!RpcResultUtil.restRequestIsSuccess(rpcResult)) {
            throw new RuntimeException(rpcResult.getMessage());
        }
    }

    protected void downloadFile(HttpServletResponse response, Workbook workbook, String fileName) {
        String nowDate = DateUtil.format(new Date(), DatePattern.PURE_DATE_PATTERN);
        FileUtil.download(response, workbook, fileName + nowDate + ".xlsx");
    }

}
