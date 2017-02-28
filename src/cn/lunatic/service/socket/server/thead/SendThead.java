package cn.lunatic.service.socket.server.thead;

import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Arrays;

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
			boolean goon = true;
			while (goon) {
				threadProc();
				if(clientSocket.getSendQueueSize() <= 0 && clientSocket.closeFlag){
					break;
				}
			}
			clientSocket.closeConn();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	public void threadProc() throws Exception {
		PackageServer pack = clientSocket.sendQueue.take();
		if (pack == null) {
			return;
		}
		Log.print("发送报文:" + pack.toString());
		byte[] sendBytes = PackTools.packServerPackage(pack);
		Log.print("发送字节数组:" + Arrays.toString(sendBytes));
		outToServer.write(sendBytes);
		outToServer.flush();
	}
}
