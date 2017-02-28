package cn.lunatic.service.socket.pack;

import java.io.Serializable;
import java.util.SortedMap;

import cn.lunatic.service.itf.LunaticServiceItf;

/**
 * 客户端请求数据包对象
 * 
 * @author gan.feng
 *
 */
public class PackageClient implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private String reqestNo;

	private String type;

	private String serviceName;

	private LunaticServiceItf service;

	private SortedMap<Object, Object> serviceParams;
	
	/**
	 * 注册服务
	 * @param reqestNo
	 * @param type
	 * @param serviceName
	 * @param service
	 */
	public PackageClient(String reqestNo, String type, String serviceName, LunaticServiceItf service) {
		super();
		this.reqestNo = reqestNo;
		this.type = type;
		this.serviceName = serviceName;
		this.service = service;
	}
	
	/**
	 * 调用服务
	 * @param reqestNo
	 * @param type
	 * @param serviceName
	 * @param serviceParams
	 */
	public PackageClient(String reqestNo, String type, String serviceName, SortedMap<Object, Object> serviceParams) {
		super();
		this.reqestNo = reqestNo;
		this.type = type;
		this.serviceName = serviceName;
		this.serviceParams = serviceParams;
	}


	public String getReqestNo() {
		return reqestNo;
	}

	public void setReqestNo(String reqestNo) {
		this.reqestNo = reqestNo;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getServiceName() {
		return serviceName;
	}

	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}

	public LunaticServiceItf getService() {
		return service;
	}

	public void setService(LunaticServiceItf service) {
		this.service = service;
	}

	public SortedMap<Object, Object> getServiceParams() {
		return serviceParams;
	}


	public void setServiceParams(SortedMap<Object, Object> serviceParams) {
		this.serviceParams = serviceParams;
	}


	@Override
	public String toString() {
		return "PackageClient [reqestNo=" + reqestNo + ", type=" + type + ", serviceName=" + serviceName + ", service="
				+ service + "]";
	}
}
