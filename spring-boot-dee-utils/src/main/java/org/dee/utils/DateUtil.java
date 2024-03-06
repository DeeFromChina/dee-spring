package org.dee.utils;

import cn.hutool.core.date.DateTime;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * hutao 2020/6/17
 * @author hepenghu
 */
public class DateUtil extends cn.hutool.core.date.DateUtil {

    /**
     * 时间格式转换
     */
    public static final String DATE_FORMAT_YMD_LONG = "yyyy-MM-dd";
    public static final String DATE_FORMAT_YMD = "yyyyMMdd";
    public static final String DATE_FORMAT_YMDHMS_LONG = "yyyy-MM-dd hh:mm:ss";
    public static final String DATE_FORMAT_YM = "yyyy-MM";
    private DateUtil() {
    }

    /**
     * 将字符串日期转换为日期对象
     *
     * @param dateStr 字符串日期
     * @param pattern 转换的格式
     * @throws ParseException
     */
    public static Date stringToDate(String dateStr, String pattern) {
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        Date date;
        try {
            date = sdf.parse(dateStr);
        } catch (ParseException e) {
            throw new RuntimeException("不能按照格式" + pattern + ",将字符串" + dateStr + ",转为日期");
        }
        return date;
    }

    /**
     * 判断两个日期的大小 ，yyyyMMdd格式
     *
     * @param date1 日期1
     * @param date2 日期2
     * @return boolean 当date1>=date2 为ture，反之false
     * @throws ParseException
     */
    public static boolean compareDate(String date1, String date2) {
        boolean flag;
        Date d1 = parse(date1);
        Date d2 = parse(date2);
        int cNum = d1.compareTo(d2);
        flag = cNum >= 0;
        return flag;
    }

    /**
     * 得到mongo时间
     * @param date
     * @return
     * @throws ParseException
     */
    public static Date getMongoDate(Date date)  {
        Date d = null;
        try{
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Calendar ca = Calendar.getInstance();
            ca.setTime(date);
            ca.add(Calendar.HOUR_OF_DAY, 8);
            d = sdf.parse(sdf.format(ca.getTime()));
        }catch (ParseException e){
            e.getMessage();
        }
        return d;
    }

    /**
     * 格式化日期
     */
    public static String getDateFormat(Date date, String format) {
        if (null == format || "".equals(format)) {
            format = "yyyy-MM-dd HH:mm:ss";
        }
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(date);
    }

    public static String convertDate(Date date) {
        SimpleDateFormat formatter = new SimpleDateFormat(DATE_FORMAT_YMD_LONG);
        return formatter.format(date);
    }

    /**
     * @param year:  年份
     * @param month: 月份
     * @Description: 获取某月的第一天
     * @Author: Zhou Chen
     * @Date: 2020/8/7 12:10
     * @return: java.lang.String
     **/
    public static Date getFirstDayOfMonth(int year, int month) {
        Calendar cal = Calendar.getInstance();
        //设置年份
        cal.set(Calendar.YEAR, year);
        //设置月份
        cal.set(Calendar.MONTH, month - 1);
        //设置日历中月份的最大天数
        cal.set(Calendar.DAY_OF_MONTH, 1);
        // 时
        cal.set(Calendar.HOUR_OF_DAY, 0);
        // 分
        cal.set(Calendar.MINUTE, 0);
        // 秒
        cal.set(Calendar.SECOND, 0);
        // 毫秒
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTime();
    }

    /**
     * @param year:  年份
     * @param month: 月份
     * @Description: 获取某月的最后一天
     * @Author: Zhou Chen
     * @Date: 2020/8/7 12:10
     * @return: java.lang.String
     **/
    public static Date getLastDayOfMonth(int year, int month) {
        Calendar cal = Calendar.getInstance();
        //设置年份
        cal.set(Calendar.YEAR, year);
        //设置月份
        cal.set(Calendar.MONTH, month - 1);
        //获取某月最大天数
        int lastDay = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
        //设置日历中月份的最大天数
        cal.set(Calendar.DAY_OF_MONTH, lastDay);
        // 时
        cal.set(Calendar.HOUR_OF_DAY, 0);
        // 分
        cal.set(Calendar.MINUTE, 0);
        // 秒
        cal.set(Calendar.SECOND, 0);
        // 毫秒
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTime();
    }

    /**
     * 获取当前日期的前后时间
     *
     * @param date 当前日期
     * @param day  天数(如果day数为负数,说明是此日期前的天数)
     * @return java.util.Date
     * @Author JiHong Duan
     * @Date 2021/4/19 0019 15:12
     **/
    public static Date beforNumDay(Date date, int day) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(Calendar.DAY_OF_YEAR, day);
        return c.getTime();
    }

    /**
     * 获取输入时间的上年末
     *
     * @return
     */
    public static Date getLastYearEnd(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.set(Calendar.DAY_OF_YEAR, 1);
        c.add(Calendar.DAY_OF_MONTH, -1);
        return c.getTime();// 上年末;
    }

    /**
     * 判断一段时间在另一段时间交集的天数，带上下午
     * @param sourceStartDate
     * @param sourceEndDate
     * @param targetStartDate
     * @param targetEndDate
     * @return
     */
    public static Double betweenDay(Date sourceStartDate, Integer sourceStartApm, Date sourceEndDate, Integer sourceEndApm, Date targetStartDate, Date targetEndDate) {
        //开始时间vsRecord结束时间
        int oSvRE = sourceStartDate.compareTo(targetEndDate);
        //开始时间vsRecord开始时间
        int oSvRS = sourceStartDate.compareTo(targetStartDate);
        //结束时间vsRecord结束时间
        int oEvRE = sourceEndDate.compareTo(targetEndDate);
        //结束时间vsRecord开始时间
        int oEvRS = sourceEndDate.compareTo(targetStartDate);
        double days = 0D;
        if(oSvRE < 1 && oSvRS > -1 && oEvRS > -1 && oEvRE < 1){
            //开始日期与结束日期相等
            if(sourceStartDate.compareTo(sourceEndDate) == 0){
                if(sourceStartApm == sourceEndApm){
                    return 0.5;
                }else{
                    return 1D;
                }
            }else{
                days = cn.hutool.core.date.DateUtil.betweenDay(sourceStartDate, sourceEndDate, true)+1;
                if(sourceStartApm == 2){
                    days = Double.valueOf(days) - 0.5;
                }
                if(sourceEndApm == 1){
                    days = Double.valueOf(days) - 0.5;
                }
            }
        }
        //开始时间《=start && 结束时间》=end
        else if(oSvRS < 1 && oEvRE > -1){
            days = cn.hutool.core.date.DateUtil.betweenDay(targetStartDate, targetEndDate, true)+1;
            if(oSvRE == 0){
                if(sourceStartApm == 2){
                    days = Double.valueOf(days) - 0.5;
                }
            }
            if(oEvRE == 0){
                if(sourceEndApm == 1){
                    days = Double.valueOf(days) - 0.5;
                }
            }
        }
        //开始时间《end && 开始时间》start
        else if(oSvRE < 1 && oSvRS > -1){
            days = cn.hutool.core.date.DateUtil.betweenDay(sourceStartDate, targetEndDate, true)+1;
            if(sourceStartApm == 2){
                days = Double.valueOf(days) - 0.5;
            }
        }
        //结束时间 》 start &&结束时间《 end
        else if(oEvRS > -1 && oEvRE < 1){
            //考虑相等日期
            days = cn.hutool.core.date.DateUtil.betweenDay(targetStartDate, sourceEndDate, true)+1;
            if(sourceEndApm == 1){
                days = Double.valueOf(days) - 0.5;
            }
        }
        return days;
    }

    /**
     * 判断一段时间在另一段时间交集的天数，带上下午
     * @param sourceStartDate
     * @param sourceEndDate
     * @param targetStartDate
     * @param targetEndDate
     * @return
     */
    public static Double betweenDay(Date sourceStartDate, Date sourceEndDate, Date targetStartDate, Date targetEndDate) {
        //开始时间vsRecord结束时间
        int oSvRE = sourceStartDate.compareTo(targetEndDate);
        //开始时间vsRecord开始时间
        int oSvRS = sourceStartDate.compareTo(targetStartDate);
        //结束时间vsRecord结束时间
        int oEvRE = sourceEndDate.compareTo(targetEndDate);
        //结束时间vsRecord开始时间
        int oEvRS = sourceEndDate.compareTo(targetStartDate);
        double days = 0D;
        if(oSvRE < 1 && oSvRS > -1 && oEvRS > -1 && oEvRE < 1){
            //开始日期与结束日期相等
            if(sourceStartDate.compareTo(sourceEndDate) == 0){
                return 1D;
            }else{
                days = cn.hutool.core.date.DateUtil.betweenDay(sourceStartDate, sourceEndDate, true)+1;
            }
        }
        //开始时间《=start && 结束时间》=end
        else if(oSvRS < 1 && oEvRE > -1){
            days = cn.hutool.core.date.DateUtil.betweenDay(targetStartDate, targetEndDate, true)+1;
        }
        //开始时间《end && 开始时间》start
        else if(oSvRE < 1 && oSvRS > -1){
            days = cn.hutool.core.date.DateUtil.betweenDay(sourceStartDate, targetEndDate, true)+1;
        }
        //结束时间 》 start &&结束时间《 end
        else if(oEvRS > -1 && oEvRE < 1){
            //考虑相等日期
            days = cn.hutool.core.date.DateUtil.betweenDay(targetStartDate, sourceEndDate, true)+1;
        }
        return days;
    }

    /**
     * 获取上一个月1号的时间
     * @return
     */
    public static  Date getBeforeFirstMonthDate(){
        SimpleDateFormat format=new SimpleDateFormat(DATE_FORMAT_YMDHMS_LONG);
        Calendar calendar=Calendar.getInstance();
        calendar.add(Calendar.MONTH, -1);
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        Date date = calendar.getTime();
        return date;
    }

    /**
     * 根据日期和月得到累加的时间
     * @return
     */
    public static  Date getDateByMonth(Date date,int month){
        SimpleDateFormat format=new SimpleDateFormat(DATE_FORMAT_YMDHMS_LONG);
        Calendar calendar=Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.MONTH, month);
        Date returnDate = calendar.getTime();
        return returnDate;
    }

    /**
     *
     * @param nowTime   当前时间
     * @param startTime    开始时间
     * @param endTime   结束时间
     * @return
     * @author sunran   判断当前时间在时间区间内
     */
    public static boolean isEffectiveDate(Date startTime, Date nowTime, Date endTime) {
        if (nowTime.getTime() == startTime.getTime()
                || nowTime.getTime() == endTime.getTime()) {
            return true;
        }

        Calendar date = Calendar.getInstance();
        date.setTime(nowTime);

        Calendar begin = Calendar.getInstance();
        begin.setTime(startTime);

        Calendar end = Calendar.getInstance();
        end.setTime(endTime);

        if (date.after(begin) && date.before(end)) {
            return true;
        } else {
            return false;
        }
    }
    /**
     * 获取一个月的第一天或者最后一天
     *
     * @param belongDate
     * @param type
     * @return
     */

    public static Date getFirstOrLastDayOfMonth(Date belongDate, String type) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(belongDate);
        if (type.equals("first")) {
            calendar.set(Calendar.DAY_OF_MONTH, 1);
            calendar.set(Calendar.HOUR_OF_DAY, 0);
            calendar.set(Calendar.MINUTE, 0);
        } else {
            int month = calendar.get(Calendar.MONTH);
            calendar.set(Calendar.MONTH, month);
            calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
        }
        return calendar.getTime();
    }

    /**
     * 将时间作为一个线段，月初和月末作为线段的两头，如果线段中间的点大于1个，则为超过两段
     * @param time
     */
    public static void putDatePot(Date time, Map<String, Map<String, Date>> map) {
        if(time == null){
            return;
        }
        DateTime monthStart = DateUtil.beginOfMonth(time);
        DateTime monthEnd = DateUtil.endOfMonth(time);
        String monthStr = DateUtil.getDateFormat(monthStart, DateUtil.DATE_FORMAT_YM);
        //如果不是月初或月末
        if(monthStart.compareTo(time) != 0 && monthEnd.compareTo(time) != 0){
            Map<String, Date> monthPot = map.get(monthStr);
            if(monthPot == null){
                monthPot = new HashMap<>();
                map.put(monthStr, monthPot);
            }
            String key = DateUtil.getDateFormat(time, DateUtil.DATE_FORMAT_YMD_LONG);
            monthPot.put(key, time);
        }
    }

}
