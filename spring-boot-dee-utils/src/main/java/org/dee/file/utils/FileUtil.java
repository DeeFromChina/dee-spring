package org.dee.file.utils;

import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.ZipUtil;
import org.apache.commons.io.FileUtils;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.util.StringUtils;
import org.springframework.util.unit.DataSize;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.UUID;
import java.util.zip.ZipFile;

public class FileUtil {
    private static final Logger log = LoggerFactory.getLogger(FileUtil.class);
    private static final String[] IMAGE_PATTERN = new String[]{"png", "jpg"};

    private FileUtil() {
    }

    public static InputStream getInputStream(String filePath) {
        String basedir = FilePathUtil.getBasedir();

        try {
            return (InputStream)(StringUtils.isEmpty(basedir) ? (new ClassPathResource(filePath)).getInputStream() : new FileInputStream(basedir.concat(File.separator).concat(filePath)));
        } catch (IOException var3) {
            log.error("Get system path error", var3);
            return null;
        }
    }

    public static File getFile(String filePath) {
        String basedir = FilePathUtil.getBasedir();

        try {
            return StringUtils.isEmpty(basedir) ? (new ClassPathResource(filePath)).getFile() : new File(basedir.concat(File.separator).concat(filePath));
        } catch (IOException var3) {
            log.error("Get system path error", var3);
            return null;
        }
    }

    public static ZipFile getZipFileInputStreamMap(InputStream inputStream) throws IOException {
        File destFile = new File(FilePathUtil.getTempPath() + File.separator + UUID.randomUUID().toString() + ".zip");
        FileUtils.copyInputStreamToFile(inputStream, destFile);
        return new ZipFile(destFile, StandardCharsets.UTF_8);
    }

    public static void download(HttpServletResponse response, Workbook wb, String fileName) {
        if (null == wb) {
            throw new IllegalArgumentException("param workbook can not be null");
        } else {
            try {
                OutputStream outputStream = response.getOutputStream();
                Throwable var4 = null;

                try {
                    response.setContentType("application/octet-stream");
                    fileName = URLEncoder.encode(fileName, StandardCharsets.UTF_8.toString());
                    response.addHeader("Content-Disposition", "attachment;filename=" + fileName);
                    wb.write(outputStream);
                    outputStream.flush();
                    if(wb instanceof SXSSFWorkbook){
                        ((SXSSFWorkbook) wb).dispose();
                    }
                    wb.close();
                } catch (Throwable var14) {
                    var4 = var14;
                    throw var14;
                } finally {
                    if (outputStream != null) {
                        if (var4 != null) {
                            try {
                                outputStream.close();
                            } catch (Throwable var13) {
                                var4.addSuppressed(var13);
                            }
                        } else {
                            outputStream.close();
                        }
                    }

                }
            } catch (Exception var16) {
                log.error("download error", var16);
            }

        }
    }

    public static int compare(DataSize compareDataSize, long fileBytes) {
        return compareDataSize.compareTo(DataSize.ofBytes(fileBytes));
    }

    public static boolean isImage(String fileSuffix) {
        Assert.notNull(fileSuffix, "param fileSuffix can not be null", new Object[0]);
        String[] var1 = IMAGE_PATTERN;
        int var2 = var1.length;

        for(int var3 = 0; var3 < var2; ++var3) {
            String pattern = var1[var3];
            if (pattern.equalsIgnoreCase(fileSuffix)) {
                return true;
            }
        }

        return false;
    }

    public static String encodeFileToBase64(byte[] bytes, String contentType) {
        return "data:" + contentType + ";base64," + Base64.getEncoder().encodeToString(bytes);
    }

    public static String encodeFileToBase64(MultipartFile file) throws IOException {
        return encodeFileToBase64(file.getBytes(), file.getContentType());
    }

    public static void uploadToLocale(InputStream inputStream, String filePath) {
        Assert.notNull(inputStream);
        Assert.notBlank(filePath);
        filePath = filePath.replace("/", File.separator);
        if (filePath.startsWith(File.separator)) {
            filePath = filePath.substring(1);
        }

        String resourceFilePath = FilePathUtil.getResourcesPath().concat(filePath);
        cn.hutool.core.io.FileUtil.writeFromStream(inputStream, new File(resourceFilePath));
    }

    public static File unzipMultipartFile(MultipartFile file) throws IOException {
        File toFile = new File(FilePathUtil.getTempPath() + File.separator + UUID.randomUUID().toString() + ".zip");
        FileUtils.copyInputStreamToFile(file.getInputStream(), toFile);
        File unzipFile = ZipUtil.unzip(toFile);
        toFile.delete();
        return unzipFile;
    }
}
