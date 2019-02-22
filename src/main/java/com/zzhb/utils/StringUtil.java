package com.zzhb.utils;

import java.util.regex.Pattern;

public class StringUtil {

	/**
	 * 判断是否为正整数
	 * 
	 * @param str
	 * @return
	 */
	public static boolean isZInteger(String str) {
		Pattern pattern = Pattern.compile("^[1-9]\\d*$");
		return pattern.matcher(str).matches();
	}
	
}
