package cn.lunatic.service.socket.client.thead;

import java.io.DataOutputStream;
import java.io.IOException;

import cn.lunatic.base.util.Log;
import cn.lunatic.service.socket.client.Client;
import cn.lunatic.service.socket.pack.PackTools;
import cn.lunatic.service.socket.pack.PackageClient;

/**
 * 客户端：发送报文队列处理线程
 * 从发送队列中获取报文发送到服务端
 * @author gan.feng
 *
 */
public class SendThead extends Thread {
	
	public Client client;
	
	private DataOutputStream outToServer;

	public SendThead(Client client) throws IOException {
		super();
		this.client = client;
		outToServer = new DataOutputStream(client.socket.getOutputStream());
		start();
	}

	public void run() {
		for(;;) {
			if(client.getCloseFlag() && client.getSendQueueSize() <= 0){
				Log.print("[SendThead]连接已关闭,不再发送消息,退出守护！");
				break;
			}
			try {
				threadProc();
			} catch (Exception e) {
				Log.print("发送消息出错", e);
			}
		}
	}
	
	
	public void threadProc() throws Exception {
		PackageClient pack = client.sendQueue.take();
		if (pack == null) {
			return;
		}
		if(client.getLogOutFlag()){
			Log.print("Out-->" + pack.toString());
		}
		byte[] sendBytes = PackTools.packClientPackage(pack);
		outToServer.write(sendBytes);
		outToServer.flush();
	}
}
