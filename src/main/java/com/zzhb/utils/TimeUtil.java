package com.zzhb.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class TimeUtil {

	private static final String SHOWFORMAT3 = "yyyyMMddHHmmss";

	public static String getTimestamp() {
		SimpleDateFormat sdf = new SimpleDateFormat(SHOWFORMAT3);
		return sdf.format(new Date());
	}

	public static long getLongFromTime(String dataFormatStr, String time) {
		SimpleDateFormat sdf = new SimpleDateFormat(dataFormatStr);
		try {
			return sdf.parse(time).getTime();
		} catch (ParseException e) {
			e.printStackTrace();
			return 0l;
		}
	}

	public static String getTimeByCustom(String dataFormatStr) {
		SimpleDateFormat format = new SimpleDateFormat(dataFormatStr);
		return format.format(new Date());
	}

	public static String getTimeByCustom(String dataFormatStr, Date date) {
		SimpleDateFormat format = new SimpleDateFormat(dataFormatStr);
		return format.format(date);
	}

	public static String getTimeByCustom(String dateFormatStr, int calendar, int size) {
		SimpleDateFormat format = new SimpleDateFormat(dateFormatStr);
		Calendar c = Calendar.getInstance();
		c.setTime(new Date());
		c.add(calendar, size);
		Date d = c.getTime();
		return format.format(d);
	}

	public static Date getDateByCustom(String dateFormatStr, String dateStr) {
		SimpleDateFormat format = new SimpleDateFormat(dateFormatStr);
		Date parse = null;
		try {
			parse = format.parse(dateStr);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return parse;
	}

	public static String getTimeFromDate(String dateFormatFromStr, String dateStr, String dateFormatToStr) {
		SimpleDateFormat format = new SimpleDateFormat(dateFormatToStr);
		return format.format(getDateByCustom(dateFormatFromStr, dateStr));
	}

	public static String getTimeFromDate(String dateFormatFromStr, Date date) {
		if (date == null) {
			return "";
		}
		SimpleDateFormat format = new SimpleDateFormat(dateFormatFromStr);
		return format.format(date);
	}

	public static int getDaysOfMonth(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		return calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
	}

	public static int getTimeByCalendar(int calendar) {
		Calendar cal = Calendar.getInstance();
		int num = cal.get(calendar);
		return num;
	}

	public static void main(String[] args) {
		// 1544220000000
		System.out.println(getTimeFromDate("yyyyMMddHHmmss", "20181212130000", "yyyy-MM-dd HH:mm:ss"));
	}
}
