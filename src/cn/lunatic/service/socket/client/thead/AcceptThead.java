package cn.lunatic.service.socket.client.thead;

import java.io.IOException;

import cn.lunatic.base.util.Log;
import cn.lunatic.service.socket.client.Client;
import cn.lunatic.service.socket.pack.PackTools;
import cn.lunatic.service.socket.pack.PackageServer;

/**
 * 客户端：服务端报文处理线程
 * 处理服务端报文对接中的报文
 * @author gan.feng
 *
 */
public class AcceptThead extends Thread {
	public Client client;
	
	public AcceptThead(Client client) throws IOException {
		super();
		this.client = client;
		start();
	}

	public void run() {
		try {
			boolean goon = true;
			while (goon) {
				threadProc();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	public void threadProc() throws Exception {
		PackageServer pack = client.acceptQueue.take();
		if (pack == null) {
			return;
		}
		Log.print("客户端接收报文：" + pack.toString());
		if (PackTools.PACK_TYPE_CLOSE_CONNECTION.equals(pack.getType())){
			Log.print("[关闭连接]请求处理结果:" + pack.getErrCode() + "(" + pack.getErrMsg() + ")");
		}else if (PackTools.PACK_TYPE_REGIST_SERVICE.equals(pack.getType())){
			Log.print("[注册服务]请求处理结果:" + pack.getErrCode() + "(" + pack.getErrMsg() + ")");
		}else if (PackTools.PACK_TYPE_CALL_SERVICE.equals(pack.getType())){
			Log.print("[调用服务]请求处理结果:" + pack.getErrCode() + "(" + pack.getErrMsg() + ")");
		}else{
			Log.print("[" + pack.getType() + "]请求处理结果:" + pack.getErrCode() + "(" + pack.getErrMsg() + ")");
		}
	}
}
