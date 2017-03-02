package cn.lunatic.service.socket.server.thead;

import java.io.BufferedInputStream;
import java.io.IOException;

import cn.lunatic.base.util.Log;
import cn.lunatic.service.socket.pack.PackTools;
import cn.lunatic.service.socket.pack.PackageClient;
import cn.lunatic.service.socket.server.ClientSocket;

/**
 * 守护线程：接收客户端的请求报文放置到请求队列中
 * @author gan.feng
 *
 */
public class AcceptPackThead extends Thread {

	private ClientSocket clientSocket;
	
	private BufferedInputStream inFromServer;
	
	public AcceptPackThead(ClientSocket clientConnBean) throws IOException {
		super();
		this.clientSocket = clientConnBean;
		this.inFromServer = new BufferedInputStream(this.clientSocket.socket.getInputStream());
		start();
	}

	public void run() {
		for (;;) {
			if (clientSocket.getCloseFlag()) {
				Log.print("连接已关闭,不再接收消息,退出守护！");
				break;
			}
			threadProc();
		}
	}
	
	
	public void threadProc(){
		PackageClient pack = null;
		try{
			pack = PackTools.getPackageClientFromInputSteam(inFromServer);
		}catch(Exception e){
			if("Connection reset".equals(e.getMessage())){
				clientSocket.setCloseFlag(true);
			}
			Log.print("[AcceptPackThead]接收消息失败:" + e.getMessage());
		}
		if (pack == null) {
			return;
		}
		if(clientSocket.getLogInFlag()){
			Log.print("In--->" + pack);
		}
		clientSocket.putAcceptQueue(pack);
	}
}
