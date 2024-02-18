package org.dee.utils;

import cn.hutool.core.date.DateUtil;

import java.text.SimpleDateFormat;
import java.util.Date;

public class ConvertUtil {

    private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    private static SimpleDateFormat sdt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private static SimpleDateFormat ymd = new SimpleDateFormat("yyyy年MM月dd日");
    private static SimpleDateFormat ymdhms = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss");

    /**
     * 将对象转成string返回
     * @param object
     * @return
     */
    public static String returnString(Object object) {
        try {
            if(object instanceof Integer){
                return String.valueOf(object);
            }
            return object.toString();
        } catch (Exception e) {
            return "";
        }
    }

    /**
     * 将对象转成int返回
     * @param object
     * @return
     */
    public static Integer returnInt(Object object){
        try {
            if(object != null){
                return Integer.valueOf(object.toString());
            }
        } catch (Exception e) {}
        return 0;
    }

    /**
     * 将对象转成double返回
     * @param object
     * @return
     */
    public static Double returnDouble(Object object){
        try {
            if(object != null){
                return Double.valueOf(object.toString());
            }
        } catch (Exception e) {}
        return 0D;
    }

    /**
     * 将时间对象格式化，根据type来决定格式
     * @param object
     * @param type（yyyy-mm-dd或yyyy-MM-dd HH:mm:ss等）
     * @return
     */
    public static String returnDateType(Object object, String type){
        try {
            if(object == null){
                return null;
            }
            if(object instanceof Date){
                if("yyyy-MM-dd".equalsIgnoreCase(type)){
                    return sdf.format(object);
                }
                else if("yyyy-MM-dd HH:mm:ss".equalsIgnoreCase(type)){
                    return sdt.format(object);
                }
                else if("yyyy年MM月dd日".equalsIgnoreCase(type)){
                    return ymd.format(object);
                }
                else if("yyyy年MM月dd日 HH:mm:ss".equalsIgnoreCase(type)){
                    return ymdhms.format(object);
                }
                else {
                    SimpleDateFormat s = new SimpleDateFormat(type);
                    return s.format(object);
                }
            }
            else if(object instanceof String){
                Date time = DateUtil.parse(object.toString());
                return DateUtil.format(time, type);
            }
        } catch (Exception e) {
            return returnString(object);
        }
        return null;
    }

    /**
     * 返回对象数组
     * @param objects
     * @return
     */
    public static Object[] returnObjects(Object...objects){
        return objects;
    }
}
