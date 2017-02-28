package cn.lunatic.service.result;

/**
 * 服务执行结果
 * @author gan.feng
 *
 */
public class Result {

	public static final String SUCCESS_ERR_CODE = "0000"; 
	public static final String SUCCESS_ERR_MSG = "执行成功"; 

	public static final String ERR_TYPE_COMMON = "00"; 
	
	public static final String ERR_TYPE_SYSTEM = "01"; 

	public static final String ERR_TYPE_BUSINESS = "02"; 
	
	
	private String errCode;
	
	private String errMsg;
	
	private String errType;

	public Result() {
		super();
	}

	public Result(String errCode, String errMsg, String errType) {
		super();
		this.errCode = errCode;
		this.errMsg = errMsg;
		this.errType = errType;
	}
	
	public static Result getSuccessResult(){
		return new Result(SUCCESS_ERR_CODE, SUCCESS_ERR_MSG, ERR_TYPE_COMMON);
	}
	public static Result getFailResult(String errCode, String errMsg, String errType){
		return new Result(errCode, errMsg, errType);
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
}
