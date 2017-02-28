package cn.lunatic.startup;

import java.net.ServerSocket;
import java.net.Socket;

import cn.lunatic.base.util.Log;
import cn.lunatic.service.socket.server.ClientSocket;

/**
 * 启动服务端
 * 
 * @author gan.feng
 */
public class StartUpServer {

	private static ServerSocket Server;
	
	public static void main(String[] args) {
		try {
			Log.print("服务器启动,等待客户端连接");
			Server = new ServerSocket(4000);
			Socket client = null;
			while (true) {
				client = Server.accept();
				new ClientSocket(client);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
