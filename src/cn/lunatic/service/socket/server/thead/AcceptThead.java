package cn.lunatic.service.socket.server.thead;

import java.io.IOException;

import cn.lunatic.base.util.Log;
import cn.lunatic.service.result.Result;
import cn.lunatic.service.service.ServiceManager;
import cn.lunatic.service.socket.pack.PackTools;
import cn.lunatic.service.socket.pack.PackageClient;
import cn.lunatic.service.socket.pack.PackageServer;
import cn.lunatic.service.socket.server.ClientSocket;

/**
 * 守护线程：处理请求队列中的报文
 * @author gan.feng
 */
public class AcceptThead extends Thread {
	
	public ClientSocket clientSocket;
	
	public AcceptThead(ClientSocket clientSocket) throws IOException {
		super();
		this.clientSocket = clientSocket;
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
		PackageClient pack = clientSocket.acceptQueue.take();
		if (pack == null) {
			return;
		}
		Log.print("处理报文:" + pack.toString());
		String requestType = pack.getType();
		if(PackTools.PACK_TYPE_REGIST_SERVICE.equals(requestType )){
			Result result = ServiceManager.registService(pack.getServiceName(), pack.getService());
			// 处理结束返回报文
			PackageServer sendPack = PackageServer.getServerPackage(pack, result);
			clientSocket.sendPack(sendPack);
		}else if(PackTools.PACK_TYPE_CALL_SERVICE.equals(requestType)){
			Result result = ServiceManager.callService(pack.getServiceName(), pack.getServiceParams());
			// 处理结束返回报文
			PackageServer sendPack = PackageServer.getServerPackage(pack, result);
			clientSocket.sendPack(sendPack);
		}else if(PackTools.PACK_TYPE_CLOSE_CONNECTION.equals(requestType)){
			clientSocket.closeAll();
		}
	}
}
