package cn.lunatic.startup;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.SortedMap;
import java.util.TreeMap;

import cn.lunatic.base.util.Log;
import cn.lunatic.base.util.TimeUtil;
import cn.lunatic.service.socket.server.ClientSocket;

/**
 * 启动服务端
 * 
 * @author gan.feng
 */
public class StartUpServer {

	private static ServerSocket Server;
	
	public static SortedMap<String, ClientSocket> clients = new TreeMap<String, ClientSocket>();
	
	public static void main(String[] args) {
		try {
			Log.print("服务器启动,等待客户端连接");
			Server = new ServerSocket(4000);
			Socket client = null;
			while (true) {
				client = Server.accept();
				String serialNo = "C" + TimeUtil.now("yyyyMMddHHmmssSSS");
				clients.put(serialNo, new ClientSocket(client, serialNo));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
