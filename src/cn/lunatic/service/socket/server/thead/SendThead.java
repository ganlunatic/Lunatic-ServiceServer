package cn.lunatic.service.socket.server.thead;

import java.io.DataOutputStream;
import java.io.IOException;

import cn.lunatic.base.util.Log;
import cn.lunatic.service.socket.pack.PackTools;
import cn.lunatic.service.socket.pack.PackageServer;
import cn.lunatic.service.socket.server.ClientSocket;

/**
 * 守护线程：处理发送队列中的消息
 * @author gan.feng
 *
 */
public class SendThead extends Thread {
	
	public ClientSocket clientSocket;

	private DataOutputStream outToServer;

	public SendThead(ClientSocket clientSocket) throws IOException {
		super();
		this.clientSocket = clientSocket;
		this.outToServer = new DataOutputStream(clientSocket.socket.getOutputStream());
		start();
	}

	public void run() {
		try {
			for(;;) {
				if(clientSocket.getCloseFlag() && clientSocket.getSendQueueSize() <= 0){
					Log.print("客户端[" + clientSocket.socket.getInetAddress() + "]连接已关闭&&待发送的消息已经发送完毕,退出守护");
					break;
				}
				threadProc();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	public void threadProc() throws Exception {
		PackageServer pack = clientSocket.sendQueue.take();
		if (pack == null) {
			return;
		}
		if(clientSocket.getLogOutFlag()){
			Log.print("Out-->" + pack.toString());
		}
		byte[] sendBytes = PackTools.packServerPackage(pack);
		outToServer.write(sendBytes);
		outToServer.flush();
	}
}
