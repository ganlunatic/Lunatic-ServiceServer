package cn.lunatic.base.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class TimeUtil {
	
	public final static String DEFAULT_DATE_FORMAR = "yyyy-MM-dd HH:mm:ss";

	/**
	 * 计算两个日期的时间差
	 * @param begin 开始时间字符串
	 * @param end 结束时间字符串
	 * @param format java日期格式
	 * @return
	 */
	public static long interval(String begin, String end, String format){
		return toDate(begin, format).getTime() - toDate(end, format).getTime();
	}
	
	/**
	 * 获取系统当前时间+/-N天后的日期
	 * @param day 需要+/-的天数
	 * @param toFormat 返回日期java格式,可为空,默认为 yyyy-MM-dd
	 * @return
	 */
	public static String plusDay(int day,String toFormat){
		return plusDay(getCurrDateString(DEFAULT_DATE_FORMAR), DEFAULT_DATE_FORMAR, day, toFormat);
	}
	
	/**
	 * 获取指定时间+/-N天后的日期
	 * @param date 			指定的日期
	 * @param dateFormat 	日期字符串格式,可为空,默认为 yyyy-MM-dd HH:mm:ss
	 * @param day 			需要+/-的天数
	 * @param toFormat 		日期字符串格式,可为空,默认为 yyyy-MM-dd HH:mm:ss
	 * @return 返回获取指定时间+/-N天后的日期；异常返回空字符串
	 */
	public static String plusDay(String date, String dateFormat, int day, String toFormat){
		Calendar calendar = Calendar.getInstance();
		try{
			calendar.setTime(toDate(date, dateFormat));
			calendar.add(Calendar.DAY_OF_YEAR, day);
			return toString(calendar.getTime(), toFormat);
		}catch(Exception e){
			return "";
		}
	}
	
	/**
	 * 返回指定日期到当前日期个间隔天数
	 * @param oldDate 
	 * @param format  	日期字符串格式,可为空,默认为 yyyy-MM-dd HH:mm:ss
	 * @return
	 */
	public static int daysToNow(String oldDate, String format){
		return hoursToNow(oldDate, format)/24;
	}
	
	/**
	 * 返回指定日期到当前日期个间隔小时数(绝对值)
	 * @param oldDate 
	 * @param format  	日期字符串格式,可为空,默认为 yyyy-MM-dd HH:mm:ss
	 * @return
	 */
	public static int hoursToNow(String oldDate, String format){
		long interval = interval(oldDate, getCurrDateString(format), format);
		return (int) (interval/1000/60/60/24);
	}
	
	/**
	 * 返回当前时间字符串
	 * @param format 	日期字符串格式,可为空,默认为 yyyy-MM-dd HH:mm:ss
	 * @return
	 */
	public static String now(String format){
		return getCurrDateString(format);
	}
	
	/**
	 * 当前时间字符串
	 * @param format	日期字符串格式,可为空,默认为 yyyy-MM-dd HH:mm:ss
	 * @return
	 */
	public static String getCurrDateString(String format){
		return toString(new Date(), format);
	}
	
	/**
	 * 转化格式化时间
	 * @param date		日期字符串
	 * @param srcFormat	日期字符串格式,可为空,默认为 yyyy-MM-dd HH:mm:ss
	 * @param toFormat	日期字符串格式,可为空,默认为 yyyy-MM-dd HH:mm:ss
	 * @return
	 */
	public static String convert(String date, String srcFormat, String toFormat){
		return toString(toDate(date, srcFormat), toFormat);
	}
	
	/**
	 * 日期字符串--->日期
	 * @param date 		日期字符串
	 * @param format 	日期字符串格式,可为空,默认为 yyyy-MM-dd HH:mm:ss
	 * @return 转化失败返回null
	 */
	public static Date toDate(String date, String format){
		SimpleDateFormat dateFormat = new SimpleDateFormat(StringUtils.isBlankDefault(format, DEFAULT_DATE_FORMAR));
		try {
			return dateFormat.parse(date);
		} catch (ParseException e) {
			return null;
		} 
	}
	
	/**
	 * 日期--->日期字符串
	 * @param date
	 * @param format	日期字符串格式,可为空,默认为 yyyy-MM-dd HH:mm:ss
	 * @return	 转化失败返回null
	 */
	public static String toString(Date date, String format){
		SimpleDateFormat dateFormat = new SimpleDateFormat(StringUtils.isBlankDefault(format, DEFAULT_DATE_FORMAR));
		try {
			return dateFormat.format(date);
		} catch (Exception e) {
			return null;
		} 
	}
}
