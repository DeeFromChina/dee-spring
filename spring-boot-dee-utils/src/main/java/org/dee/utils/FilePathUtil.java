package org.dee.utils;

import cn.hutool.core.util.StrUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.FileNotFoundException;

public class FilePathUtil {
    private static final Logger log = LoggerFactory.getLogger(FilePathUtil.class);

    private FilePathUtil() {
    }

    public static String getBasedir() {
        return System.getProperty("system.basedir");
    }

    public static String getTempPath() {
        return getResourcesPath("temp");
    }

    public static String getStaticResourcesPath() {
        return getResourcesPath("static").concat(File.separator);
    }

    private static String getResourcesPath(String tempPath) {
        String basedir = getBasedir();
        if (StrUtil.isEmpty(basedir)) {
            try {
                return StrUtil.isBlank(tempPath) ? ResourceUtils.getURL("classpath:").getPath() : ResourceUtils.getURL("classpath:").getPath().concat(tempPath).concat(File.separator);
            } catch (FileNotFoundException var3) {
                log.error("", var3);
                return "";
            }
        } else {
            return StrUtil.isBlank(tempPath) ? basedir.concat(File.separator) : basedir.concat(File.separator).concat(tempPath).concat(File.separator);
        }
    }

    public static String getResourcesPath() {
        return getResourcesPath((String)null);
    }
}
