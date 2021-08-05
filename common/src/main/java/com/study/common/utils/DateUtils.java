package com.study.common.utils;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.*;

public class DateUtils {
	
	/**
	 * 添加日期天数
	 * @param date
	 * @param addDay
	 * @return
	 */
	public static Date getDateAdd(Date date, int addDay){
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.DATE, addDay);
		return cal.getTime();
	}
	/**
	 * 添加日期小时
	 * @param date
	 * @param addHour
	 * @return
	 */
	public static Date getDateAddHour(Date date, int addHour){
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.HOUR_OF_DAY, addHour);
		return cal.getTime();
	}
	/**
	 * 添加日期天数
	 * @param addDay
	 * @return
	 */
	public static Date getDateAdd(int addDay){
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DATE, addDay);
		return cal.getTime();
	}
	
	/**
	 * 当天
	 * @return
	 */
	public static Date getCurrentDate(){
		return getDateAdd(0);
	}
	
	/**
	 * 本周开始日期
	 * @return
	 */
	public static Date getCurrentWeekFirst(){
		return getDateAdd(getThisWeekStartOff());
	}
	
	/**
	 * 本周结束日期
	 * @return
	 */
	public static Date getCurrentWeekLast(){
		return getDateAdd(getThisWeekStartOff()+6);
	}
	
	/**
	 * 获取日期所在月第一天
	 * @param date
	 * @return
	 */
	public static Date getMonthFirst(Date date){
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.set(Calendar.DATE, 1);
		return cal.getTime();
	}
	
	/**
	 * 获取日期所在月的最后一天
	 * @param date
	 * @return
	 */
	public static Date getMonthLast(Date date){
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.set(Calendar.DATE, 1);
		cal.add(Calendar.MONTH, 1);
		cal.add(Calendar.DATE, -1);
		return cal.getTime();
	}
	
	/**
	 * 获取周一
	 * @param date
	 * @return
	 */
	public static Date getWeekFirst(Date date){
		int dayOff = getWeekStartOff(date);
		return getDateAdd(date, dayOff);
	}
	
	/**
	 * 获取周日
	 * @param date
	 * @return
	 */
	public static Date getWeekLast(Date date){
		int dayOff = getWeekStartOff(date);
		return getDateAdd(date, dayOff+6);
	}
	
	/**
	 * 当前月第一天
	 * @return
	 */
	public static Date getCurrentMonthFirst(){
		return getMonthFirst(Calendar.getInstance().getTime());
	}
	
	/**
	 * 当前月最后一天
	 * @return
	 */
	public static Date getCurrentMonthLast(){
		return getMonthLast(Calendar.getInstance().getTime());
	}
	
	/**
	 * 日期格式化
	 * @param date
	 * @param pattern
	 * @return
	 */
	public static String dateFormat(Date date,String pattern){
		if(date == null) return null;
		SimpleDateFormat sdf = new SimpleDateFormat(pattern);
		return sdf.format(date);
	}
	
	/**
	 * 日期格式化 yyyy-MM-dd
	 * @param date
	 * @return
	 */
	public static String dateFormat(Date date){
		return dateFormat(date, "yyyy-MM-dd");
	}
	
	/**
	 * 获取本周一与当天的相差天数
	 * @return
	 */
	private static int getThisWeekStartOff(){
		Calendar cal = Calendar.getInstance();
		int dayOfWeek = cal.get(Calendar.DAY_OF_WEEK);
		if (dayOfWeek == 1) {
            return -6;
        } else {
            return 2 - dayOfWeek;
        }
	}
	
	/**
	 * 周一与当前日期的相差天数
	 * @param date
	 * @return
	 */
	private static int getWeekStartOff(Date date){
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		int dayOfWeek = cal.get(Calendar.DAY_OF_WEEK);
		if (dayOfWeek == 1) {
            return -6;
        } else {
            return 2 - dayOfWeek;
        }
	}
	
	 /**
     * @desc  取当前时间
     * @param
     * @return date
     */
    public static String getCurrentDate(String model)
    {
        String date = "";
        try
        {
            Date dt = new Date();
            SimpleDateFormat formatter = new SimpleDateFormat(model);
            formatter.setTimeZone(TimeZone.getTimeZone("Asia/Shanghai"));
            date = formatter.format(dt);
        }
        catch (Exception ex)
        {
            date = "";
        }
        return date;
    }
    /**
     * 获得两个日期之间的所有月份
     * @param minDate
     * @param maxDate
     * @return
     */
    public static List<String> getMonthBetween(String minDate, String maxDate) throws ParseException
    {
        List<String> result = new ArrayList<String>();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");// 格式化为年月
        Calendar min = Calendar.getInstance();
        Calendar max = Calendar.getInstance();
        min.setTime(sdf.parse(minDate));
        min.set(min.get(Calendar.YEAR), min.get(Calendar.MONTH), 1);
        max.setTime(sdf.parse(maxDate));
        max.set(max.get(Calendar.YEAR), max.get(Calendar.MONTH), 2);
        Calendar curr = min;
        while (curr.before(max))
        {
            result.add(sdf.format(curr.getTime()).replace("-", ""));
            curr.add(Calendar.MONTH, 1);
        }
        return result;
    }

	/**
	 *  计算俩个字符串日期之间的秒差
	 * @param beginDateStr 起始时间
	 * @param endDateStr 结束时间
	 * @return
	 */
	public static long getSecendSub(String beginDateStr,String endDateStr){
		long secend=0;
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date beginDate = null;
		Date endDate = null;

		try {
			beginDate = format.parse(beginDateStr);
			endDate= format.parse(endDateStr);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		secend=(endDate.getTime()-beginDate.getTime())/1000;

		return secend;
	}
	/**
	 * 指定日期后的n秒
	 * @param firstDay
	 * @param second
	 * @return
	 */
	public static String getAfterSecondDate(String firstDay,String second) {
		int daysInt = Integer.parseInt(second);
		SimpleDateFormat sdfd = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

		Date dateExecute = new Date();
		try {
			dateExecute = sdfd.parse(firstDay);
		} catch (ParseException e) {
			dateExecute = new Date();
		}

		Calendar canlendar = Calendar.getInstance(); // java.util包
		canlendar.setTime(dateExecute);

		canlendar.add(Calendar.SECOND, daysInt); // 日期减 如果不够减会将月变动
		Date date = canlendar.getTime();


		String dateStr = sdfd.format(date);

		return dateStr;
	}

	/**
	 * 得到n天之后的日期
	 * @param days
	 * @return
	 */
	public static String getAfterDayDate(String firstDay,String days) {
		int daysInt = Integer.parseInt(days);
		SimpleDateFormat sdfd = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

		Date dateExecute = new Date();
		try {
			dateExecute = sdfd.parse(firstDay);
		} catch (ParseException e) {
			dateExecute = new Date();
		}

		Calendar canlendar = Calendar.getInstance(); // java.util包
		canlendar.setTime(dateExecute);

		canlendar.add(Calendar.DATE, daysInt); // 日期减 如果不够减会将月变动
		Date date = canlendar.getTime();


		String dateStr = sdfd.format(date);

		return dateStr;
	}

	/**
	 * date转LocalDateTime
	 * @param date
	 * @return
	 */
	public static LocalDateTime getLocalDateByDate(Date date) {
		Instant instant = date.toInstant();
		ZoneId zoneId = ZoneId.systemDefault();
		LocalDateTime localDateTime = instant.atZone(zoneId).toLocalDateTime();
		return localDateTime;
	}
/**
	 * LocalDateTime转date
	 * @param localDateTime
	 * @return
	 */
	public static Date getDateByLocalDate(LocalDateTime localDateTime) {
		ZoneId zoneId = ZoneId.systemDefault();
		ZonedDateTime zdt = localDateTime.atZone(zoneId);
		Date date = Date.from(zdt.toInstant());
		return date;
	}
	/**
	 * 按照参数format的格式，日期转字符串
	 * @param date
	 * @param format
	 * @return
	 */
	public static String date2Str(Date date,String format){
		if(date!=null){
			SimpleDateFormat sdf = new SimpleDateFormat(format);
			return sdf.format(date);
		}else{
			return "";
		}
	}
	/**
	 * <li>功能描述：时间相减得到分钟数
	 * @param beginDateStr
	 * @param endDateStr
	 * @return
	 * long
	 * @author xiunenginfo
	 */
	public static long getMiniteSub(String beginDateStr,String endDateStr){
		long minite=0;
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date beginDate = null;
		Date endDate = null;

		try {
			beginDate = format.parse(beginDateStr);
			endDate= format.parse(endDateStr);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		minite=(endDate.getTime()-beginDate.getTime())/(60*1000);

		return minite;
	}

	public static Boolean conpareTwoDate(Date date1,Date date2,String model) throws ParseException {
		SimpleDateFormat dateFormat = new SimpleDateFormat(model);
		date1 =dateFormat.parse(dateFormat.format(date1));
		date2 =dateFormat.parse(dateFormat.format(date2));
		Boolean flag = !date1.before(date2)&&!date1.after(date2);
		return flag;
	}
}
