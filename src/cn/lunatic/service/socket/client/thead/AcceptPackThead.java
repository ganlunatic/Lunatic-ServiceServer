package cn.lunatic.service.socket.client.thead;

import java.io.BufferedInputStream;
import java.io.IOException;

import cn.lunatic.base.util.Log;
import cn.lunatic.service.socket.client.Client;
import cn.lunatic.service.socket.pack.PackTools;
import cn.lunatic.service.socket.pack.PackageServer;

/**
 * 客户端：监控接受报文线程
 * 监控接受服务端发送的报文,放到服务端报文队列中
 * @author gan.feng
 *
 */
public class AcceptPackThead extends Thread {

	private Client client;
	
	private BufferedInputStream inFromServer;
	
	public AcceptPackThead(Client client) throws IOException {
		super();
		this.client = client;
		this.inFromServer = new BufferedInputStream(client.socket.getInputStream());
		start();
	}

	public void run() {
		for (;;) {
			if (client.getCloseFlag()) {
				Log.print("[AcceptPackThead]连接已关闭,不再接收消息,退出守护！");
				break;
			}
			try {
				threadProc();
			}catch (Exception e) {
				Log.print("接收消息失败", e);
			}
		}
	}
	
	public void threadProc() throws Exception {
		PackageServer pack = null;
		try{
			pack = PackTools.getPackageServerFromInputStream(inFromServer);
		}catch(Exception e){
			if("Connection reset".equals(e.getMessage())){
				client.setCloseFlag(true);
			}
			Log.print("[AcceptPackThead]接收消息失败:" + e.getMessage());
		}
		if (pack == null) {
			return;
		}
		if(client.getLogInFlag()){
			Log.print("In--->" + pack.toString());
		}
		client.putAcceptQueue(pack);
	}
}
