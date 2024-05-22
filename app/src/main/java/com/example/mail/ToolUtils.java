package com.example.mail;


import android.annotation.SuppressLint;
import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


/**
 * 其他方法工具类
 */
public final class ToolUtils {

    private static final Calendar calendar = Calendar.getInstance();

    //获取年
    public static int getYear() {

        return calendar.get(Calendar.YEAR);
    }

    //获取月
    public static int getMonth() {

        return calendar.get(Calendar.MONTH) + 1;
    }

    //获取日
    public static int getDay() {

        return calendar.get(Calendar.DAY_OF_MONTH);
    }

    //手机号验证 （大小写英文字母、汉字、数字、下划线 . 组成的长度4-12个字节）
    public static boolean isPhoneNo(String account) {

        //半角中文：[\u4e00-\u9fa5] 全角：[ufe30-uffa0]
        return account.matches("^([\u4e00-\u9fa5]|[ufe30-uffa0]|[0-9]){11}$");
    }

    /**
     * 获取当前日期是星期几
     *
     * @param dt
     * @return 当前日期是星期几
     */
    public static String getWeekByCalendar(Date dt) {
        String[] weekDays = {"星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六"};
        Calendar cal = Calendar.getInstance();
        cal.setTime(dt);
        int w = cal.get(Calendar.DAY_OF_WEEK) - 1;
        if (w < 0)
            w = 0;
        return weekDays[w];
    }

    //获取当前日期是星期几
    public static String getWeekByDate() {
        Date date = new Date();
        SimpleDateFormat dateFm = new SimpleDateFormat("EEEE");
        return dateFm.format(date);
    }

    //获取随机数
    public static int getRandom(int num) {
        java.util.Random random = new java.util.Random();// 定义随机类
        int result = random.nextInt(num);// 返回[0,num)集合中的整数，注意不包括num
        return result + 1;
    }


    /**
     * 弹出输入框
     *
     * @param context
     * @param view
     * @param show_type true:显示输入框；false：隐藏
     */
    public static void showInputMethod(Context context, View view, boolean show_type) {
        InputMethodManager im = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (show_type) {
            im.showSoftInput(view, 0);
        } else {
            im.hideSoftInputFromWindow(view.getWindowToken(), 0); // 强制隐藏键盘
        }
    }


    public static String getCurrentTime() {

        SimpleDateFormat mDateFormat = new SimpleDateFormat("MM-dd HH:mm");
        return mDateFormat.format(new Date(System.currentTimeMillis()));
    }

    /**
     * 判断NULL或空字符串
     */
    public static boolean isNullOrEmpty(String param) {
        return null == param || param.trim().length() == 0 || param.equals("null");
    }


    public static String getTime() {
        SimpleDateFormat mDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return mDateFormat.format(new Date(System.currentTimeMillis()));
    }

    /**
     * 将yyyy-MM-dd HH:mm:ss字符串转换成日期<br/>
     *
     * @param dateStr    时间字符串
     * @param dataFormat 当前时间字符串的格式。
     * @return Date 日期 ,转换异常时返回null。
     */
    public static long parseTime(String dateStr, String dataFormat) {
        try {
            @SuppressLint("SimpleDateFormat")
            SimpleDateFormat dateFormat = new SimpleDateFormat(dataFormat);
            Date date = dateFormat.parse(dateStr);
            return date.getTime();
        } catch (Exception e) {
            //LogUtils.warn(e);
            return 0;
        }
    }


}
