package cn.lunatic.base.util;

import java.io.FileInputStream;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

public class Log {
	
	public static String log4jInfo = "resources/Log4jInfo.properties";
	private static boolean configure = false;
	
	private static boolean logFlag = true;
	
	private Log(){
	}
	
	/**
	 * 初始化log4j
	 */
	public static void init(){
		init(log4jInfo);
	}
	
	/**
	 * 初始化log4j
	 * @param log4jProperties
	 */
	public static void init(String log4jProperties){
		Properties props = new Properties();
		try {
			FileInputStream is = new FileInputStream(log4jProperties);
			props.load(is);
			is.close();
			PropertyConfigurator.configure(props);
		} catch (Throwable e) {
			e.printStackTrace();
		}
		log4jInfo = log4jProperties;
	}
	
	/**
	 * 获取日志log4J对象
	 * @param clazz
	 * @return
	 */
	public static Logger getLogger(Class<?> clazz){
		if(!configure){
			init();
		}
		return Logger.getLogger(clazz);
	}
	
	
	private static StackTraceElement getCurrStackTraces(){
		// 获取当前线程的堆栈数据,0:当前线程,1：当前方法的执行堆栈,2：上级方法的执行堆栈,以此类推
		StackTraceElement[] stackTraces=Thread.currentThread().getStackTrace();
		StackTraceElement preStackTrace = stackTraces[3];
		return preStackTrace;
	}
	
	public static void print(String msg){
		if(logFlag){
			StackTraceElement stackTrace = getCurrStackTraces();
			String fix = " <----[" + getUsedMemory(MEMORY_UNIT_K) + "K/" + getTotalMemory(MEMORY_UNIT_K) + "K] [" + stackTrace.getClassName()+ "." + stackTrace.getMethodName() + ":" + stackTrace.getLineNumber() + "]";;
			try {
				getLogger(Class.forName(stackTrace.getClassName())).info(msg + fix);
			} catch (ClassNotFoundException e) {
			}
		}
	}

	public static void print(String msg, Throwable e){
		if(logFlag){
			StackTraceElement stackTrace = getCurrStackTraces();
			String fix = " <----[" + getUsedMemory(MEMORY_UNIT_K) + "K/" + getTotalMemory(MEMORY_UNIT_K) + "K] [" + stackTrace.getClassName()+ "." + stackTrace.getMethodName() + ":" + stackTrace.getLineNumber() + "]";;
			try {
				getLogger(Class.forName(stackTrace.getClassName())).info(msg + fix, e);
			} catch (ClassNotFoundException e1) {
			}
		}
	}
	
	public static final int MEMORY_UNIT_B = 1;
	public static final int MEMORY_UNIT_K = 2;
	public static final int MEMORY_UNIT_M = 3;
	public static final int MEMORY_UNIT_G = 4;
	public static final int MEMORY_UNIT_T = 5;
	/**
	 * 获取当前虚拟机总内存
	 * @param unit  T G M K B
	 * @return
	 */
	public static long getTotalMemory(int unit){
		long bytes = Runtime.getRuntime().totalMemory();
		return (long) (bytes/(Math.pow(1024, unit)));
	}

	/**
	 * 获取当前虚拟机使用中的内存
	 * @param unit  T G M K B
	 * @return
	 */
	public static long getUsedMemory(int unit){
		long bytes = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
		return (long) (bytes/(Math.pow(1024, unit)));
	}

	public static boolean isLogFlag() {
		return logFlag;
	}

	public static void setLogFlag(boolean logFlag) {
		Log.logFlag = logFlag;
	}
	
}
