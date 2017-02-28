package cn.lunatic.service.socket.server.thead;

import java.io.DataInputStream;
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
	
	private DataInputStream inFromServer;
	
	public AcceptPackThead(ClientSocket clientConnBean) throws IOException {
		super();
		this.clientSocket = clientConnBean;
		this.inFromServer = new DataInputStream(this.clientSocket.socket.getInputStream());
		start();
	}

	public void run() {
		try {
			for(;;) {
				threadProc();
				if(clientSocket.closeFlag){
					Log.print("连接已关闭.退出守护！");
					return;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	public void threadProc() throws Exception {
		PackageClient pack = PackTools.getPackageClientFromInputSteam(inFromServer);
		if (pack == null) {
			return;
		}
		clientSocket.putAcceptQueue(pack);
	}
}
