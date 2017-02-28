package cn.lunatic.service.socket.client.thead;

import java.io.DataInputStream;
import java.io.IOException;

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
	
	private DataInputStream inFromServer;
	
	public AcceptPackThead(Client client) throws IOException {
		super();
		this.client = client;
		this.inFromServer = new DataInputStream(client.socket.getInputStream());
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
		PackageServer pack = PackTools.getPackageServerFromInputStream(inFromServer);
		if (pack == null) {
			return;
		}
		client.putAcceptQueue(pack);
	}
}
