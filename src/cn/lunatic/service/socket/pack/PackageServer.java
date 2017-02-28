package cn.lunatic.service.socket.pack;

import java.io.Serializable;

import cn.lunatic.base.util.TimeUtil;
import cn.lunatic.service.result.Result;

/**
 * 服务端返回数据包对象
 * 
 * @author gan.feng
 *
 */
public class PackageServer implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private String requstNo;
	
	private String responseNo;
	
	private String type;


	private String errCode;

	private String errMsg;

	private String errType;

	public PackageServer() {
		super();
	}

	public PackageServer(String requstNo, String responseNo, String type, String errCode,
			String errMsg, String errType) {
		super();
		this.requstNo = requstNo;
		this.responseNo = responseNo;
		this.type = type;
		this.errCode = errCode;
		this.errMsg = errMsg;
		this.errType = errType;
	}
	
	public static PackageServer getServerPackage(PackageClient clientPackage, Result result) {
		PackageServer pack = new PackageServer();
		pack.requstNo = clientPackage.getReqestNo();
		pack.responseNo = TimeUtil.now("yyyyMMddHHmmss");
		pack.type = clientPackage.getType();
		pack.errCode = result.getErrCode();
		pack.errMsg = result.getErrMsg();
		pack.errType = result.getErrType();
		return pack;
	}
	

	public String getRequstNo() {
		return requstNo;
	}

	public void setRequstNo(String requstNo) {
		this.requstNo = requstNo;
	}

	public String getResponseNo() {
		return responseNo;
	}

	public void setResponseNo(String responseNo) {
		this.responseNo = responseNo;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getErrCode() {
		return errCode;
	}

	public void setErrCode(String errCode) {
		this.errCode = errCode;
	}

	public String getErrMsg() {
		return errMsg;
	}

	public void setErrMsg(String errMsg) {
		this.errMsg = errMsg;
	}

	public String getErrType() {
		return errType;
	}

	public void setErrType(String errType) {
		this.errType = errType;
	}

	@Override
	public String toString() {
		return "PackageServer [requstNo=" + requstNo + ", responseNo=" + responseNo + ", type=" + type + ", errCode="
				+ errCode + ", errMsg=" + errMsg + ", errType=" + errType + "]";
	}
}
