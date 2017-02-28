package cn.lunatic.base.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;

import cn.lunatic.base.util.Log;

/**
 * 字符串工具类
 * 
 * @author gan.feng
 */
public class StringUtils {

	/**
	 * 功能：不定长参数,其中一个参数为null或空则返回true,负责返回false
	 * 
	 * @param str
	 * @return boolean
	 */
	public static boolean isEmpty(String... str) {
		for (String s : str) {
			if (org.apache.commons.lang.StringUtils.isEmpty(s)) {
				return true;
			}
		}
		return false;
	}
	/**
	 * 功能：不定长参数,其中一个参数为null或空则返回true,负责返回false
	 * 
	 * @param str
	 * @return boolean
	 */
	public static String isBlankDefault(String str ,String def) {
		if(isBlank(str)){
			return def;
		}else{
			return str;
		}
	}

	/**
	 * 功能：不定长参数,其中一个参数为null或空或为空格字符串则返回true,负责返回false
	 * 
	 * @param str
	 * @return boolean
	 */
	public static boolean isBlank(String... str) {
		for (String s : str) {
			if (org.apache.commons.lang.StringUtils.isBlank(s))
				return true;
		}
		return false;
	}

	/**
	 * 功能：不定长参数,其中一个参数为null或空则返回false,负责返回true
	 * 
	 * @param str
	 * @return
	 */
	public static boolean isNotEmpty(String... str) {
		return !isEmpty(str);
	}

	/**
	 * 功能：不定长参数,其中一个参数为null或空或为空格字符串则返回true,负责返回false
	 * 
	 * @param str
	 * @return boolean
	 */
	public static boolean isNotBlank(String... str) {
		return !isBlank(str);
	}

	/**
	 * 功能：让字符串首字母大写
	 * 
	 * @param input
	 * @return
	 */
	public static String toUpperFristCase(String input) {
		if (!isEmpty(input)){
			input = input.replaceAll("^[a-z]", input.substring(0, 1).toUpperCase());
		}
		return input;
	}

	/**
	 * 功能：让字符串首字母小写
	 * 
	 * @param input
	 * @return
	 */
	public static String toLowerFristCase(String input) {
		if (!isEmpty(input)){
			input = input.replaceAll("^[A-Z]", input.substring(0, 1).toLowerCase());
		}
		return input;
	}
	
	/**
	 * 去除字符串前后空格和换行
	 * @param input
	 * @return
	 */
	public static String trimLine(String input){
		BufferedReader reader = new BufferedReader(new StringReader(input));
		String line = "";
		StringBuffer result = new StringBuffer();
		try {
			while((line = reader.readLine()) != null){
				result.append(line.trim());
			}
		} catch (IOException e) {
			Log.print("读取字符串失败.", e);;
		}
		return result.toString();
	}
	
	/**
	 * 功能：取最后的split分割字符串,取最右边部分
	 * 		例 D:\a\bb.txt,"\" ==> bb.txt
	 * @param input
	 * @param split
	 * @return
	 */
	public static String subRight(String input, String split) {
		if (isEmpty(input, split)){
			return input;
		}
		int right = input.lastIndexOf(split);
		if (right == -1){
			return null;
		} 
		right = right + split.length();
		String retString = input.substring(right);
		return retString;
	}
	
	/**
	 * 功能：取最后的split分割字符串,取左边部分
	 * 	例D:\a\bb.txt,"\" ==> D:\a
	 * @param input
	 * @param split
	 * @return
	 */
	public static String subLeft(String input, String split) {
		if (isEmpty(input, split)){
			return input;
		}
		int left = input.lastIndexOf(split);
		if (left == -1){
			return null;
		}
		String retString = input.substring(0, left);
		return retString;
	}
	
	/**
	 * 功能：字符串是否是数字
	 * @param input
	 * @return
	 */
	public static boolean isInt(String input) {
		try {
			Integer.parseInt(input);
			return true;
		} catch (Exception e) {
			return false;
		}
	}
	
	/**
	 * 功能：字符串转int类型,非int返回默认值
	 * @param input		字符串
	 * @param def		默认值
	 * @return
	 */
	public static int toInt(String input, int def) {
		try {
			return (int) Float.parseFloat(input);
		} catch (Exception e) {
			return def;
		}
	}

	/**
	 * 字符串转int类型,非int返回0
	 * @param intString
	 * @return
	 */
	public static int toInt(String intString) {
		return toInt(intString, 0);
	}
	
	/**
	 * 骆驼化字符串“_"之后的字母大写 
	 * @param str
	 * @param delStr
	 * @return
	 */
	public static String toClassNameFormat(String str){
		return toClassNameFormat("_");
	}

	/**
	 * 骆驼化字符串“_"之后的字母大写 
	 * @param str
	 * @param delStr
	 * @return
	 */
	public static String toClassNameFormat(String str , String splitStr){
		if(str==null||"".equals(str)){
			return "";
		}
		StringBuffer returnStr = new StringBuffer();
		String[] subStrs = str.toLowerCase().split(splitStr);
		for(int i = 0;i<subStrs.length;i++){
			returnStr.append(toUpperFristCase(subStrs[i]));
		}
		return returnStr.toString();
	}

	/**
	 * 去除字符串开始的重复字符串
	 * @param str
	 * @param oldStr
	 * @param newStr
	 * @return
	 */
	public static String replaceFirst(String str, String oldStr, String newStr){
		while (str.startsWith(oldStr)) {
			str = str.substring(oldStr.length());
		}
		return str;
	}
}
