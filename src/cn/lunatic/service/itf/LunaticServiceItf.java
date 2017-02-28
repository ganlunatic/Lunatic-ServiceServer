package cn.lunatic.service.itf;

import java.io.Serializable;
import java.util.Date;
import java.util.SortedMap;

import cn.lunatic.service.exception.LunaticException;

/**
 * Lunatic服务接口定义,注册的Service必须实现此接口
 * @author gan.feng
 *
 */
public abstract class LunaticServiceItf implements Serializable{
	private static final long serialVersionUID = 1L;
	/**
	 * 服务状态,当前服务状态
	 */
	private boolean live;
	/**
	 * 注册时间
	 */
	private Date registTime;
	/**
	 * 注册客户端IP
	 */
	private String registIp;
	
	/**
	 * Service执行体
	 * @param serviceParams
	 * @throws LunaticException
	 */
	public abstract void run(SortedMap<Object, Object> serviceParams) throws LunaticException; 
	
	public boolean isLive() {
		return live;
	}
	public void setLive(boolean live) {
		this.live = live;
	}
	public Date getRegistTime() {
		return registTime;
	}
	public void setRegistTime(Date registTime) {
		this.registTime = registTime;
	}
	public String getRegistIp() {
		return registIp;
	}
	public void setRegistIp(String registIp) {
		this.registIp = registIp;
	}
	
	
	
	
	
	
	
	
	
	
	
}
