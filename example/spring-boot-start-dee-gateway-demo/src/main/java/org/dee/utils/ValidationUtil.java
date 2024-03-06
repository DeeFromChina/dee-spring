package org.dee.agent.utils;

import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.ObjectUtil;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @Description 校验工具类
 */
public class ValidationUtil {

    private ValidationUtil() {

    }

    private static final String PATTERN_CODE = "^([a-zA-Z]([a-zA-Z0-9]|(-)|(_))*)$";
    public static final  String EXCEL_2003 = ".xls";
    public static final  String EXCEL_2007 = ".xlsx";
    public static final  char SPLIT = '.';


    /**
     * 校验开始时间和结束时间范围
     *
     * @param startDate
     * @param endDate
     * @return startDate或endDate为空，返回true;startDate在endDate之后，返回false，否则返回true。
     */
    public static boolean validateDateRange(Date startDate, Date endDate) {
        if (ObjectUtil.isNull(startDate) || ObjectUtil.isNull(endDate)) {
            return true;
        }
        return !startDate.after(endDate);
    }

    /**
     * 校验日期是否合法
     *
     * @param str
     * @return
     */
    public static boolean isDate(String str) {
        boolean result = true;
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        try {
            format.setLenient(false);
            format.parse(str);
        } catch (Exception e) {
            result = false;
        }
        return result;
    }

    /**
     * 编码只能包含英文字母，数字,下滑线“_”\n和横杠“-”,且只能以字母开头！
     *
     * @param str
     * @return
     */
    public static boolean isCode(String str) {
        Pattern r = Pattern.compile(PATTERN_CODE);
        Matcher m = r.matcher(str);
        return m.matches();
    }



    /**
     * 校验是否为excel文件，如果不是则报错
     *
     * @param file
     */
    public static void validateExcelFile(MultipartFile file) {
        boolean bool = EXCEL_2003.equals(getSuffix(file.getOriginalFilename()))
                || EXCEL_2007.equals(getSuffix(file.getOriginalFilename()));
        Assert.isTrue(bool, "仅支持上传xls、xlsx的文件");
    }

    /**
     * 文件后缀名（以 . 开始）
     *
     * @return
     * @throws
     */
    public static String getSuffix(File file) {
        String suffix = null;
        if (file != null) {
            suffix = getSuffix(file.getName());
        }
        return suffix;
    }

    /**
     * 文件后缀名（以 . 开始）
     *
     * @return
     */
    public static String getSuffix(String path) {
        String suffix = null;
        if (path != null && path.indexOf(SPLIT) > -1) {
            suffix = path.substring(path.lastIndexOf(SPLIT));
        }
        return suffix;
    }
}
