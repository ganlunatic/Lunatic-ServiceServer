package cn.lunatic.service.service;

import java.util.Date;
import java.util.SortedMap;
import java.util.TreeMap;

import cn.lunatic.base.util.Log;
import cn.lunatic.service.exception.LunaticException;
import cn.lunatic.service.itf.LunaticServiceItf;
import cn.lunatic.service.result.Result;

/**
 * 服务管理类:注册、调用、查询、删除
 * @author gan.feng
 */
public class ServiceManager {
	/**
	 * 当前注册的服务集合,key:服务名;value:服务对象
	 */
	private static SortedMap<String, LunaticServiceItf> services = new TreeMap<String, LunaticServiceItf>();
	
	/**
	 * 注册Service
	 * @param serviceName
	 * @param service
	 * @return
	 */
	public synchronized static Result registService(String serviceName, LunaticServiceItf service){
		if(getService(serviceName) == null){
			service.setLive(true);
			service.setRegistTime(new Date());
			services.put(serviceName, service);
			return Result.getSuccessResult();
		}else{
			return Result.getFailResult("E001", "["+ serviceName +"]已存在.不允许重复注册.", Result.ERR_TYPE_BUSINESS);
		}
	}
	
	/**
	 * 调用Service
	 * @param serviceName
	 * @param serviceParams
	 * @return
	 */
	public static Result callService(String serviceName, SortedMap<Object, Object> serviceParams){
		LunaticServiceItf service = getService(serviceName);
		if(service == null){
			return Result.getFailResult("E002", "["+serviceName+"]未找到有效的服务", Result.ERR_TYPE_BUSINESS);
		}
		try{
			service.run(serviceParams);
			return Result.getSuccessResult();
		}catch(LunaticException e){
			Log.print("执行失败", e);
			return Result.getFailResult("A001", e.getMessage(), Result.ERR_TYPE_SYSTEM);
		}
	}
	/**
	 * 根据Service名称获取Service对象
	 * @param serviceName
	 * @return
	 * 	根据ServiceName找到有效的Serice返回,没有找到返回null
	 */
	public static LunaticServiceItf getService(String serviceName){
		LunaticServiceItf service = services.get(serviceName);
		if(service == null){
			return null;
		}
		if(service.isLive()){
			return service;
		}else{
			return null;
		}
	}
	
	/**
	 * 获取所有集合
	 * @return
	 */
	public static SortedMap<String, LunaticServiceItf> getAllService(){
		return services;
	}
	
	
	
	
	
	
	
	
	
}
