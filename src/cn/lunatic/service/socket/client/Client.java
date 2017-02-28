package cn.lunatic.service.socket.client;

import java.io.IOException;
import java.net.Socket;

import cn.lunatic.service.socket.client.thead.AcceptThead;
import cn.lunatic.service.socket.client.thead.AcceptPackThead;
import cn.lunatic.service.socket.client.thead.SendThead;
import cn.lunatic.service.socket.pack.PackageClient;
import cn.lunatic.service.socket.pack.PackageServer;
import cn.lunatic.service.socket.queue.ClientPackQueue;
import cn.lunatic.service.socket.queue.ServerPackQueue;

/**
 * 客户端对象
 * @author gan.feng
 */
public class Client {

	public Socket socket;
	
	public ClientPackQueue sendQueue;
	
	public ServerPackQueue acceptQueue;
	
	public Client(Socket socket) throws IOException {
		super();
		this.sendQueue = new ClientPackQueue();
		this.acceptQueue = new ServerPackQueue();
		this.socket = socket;
		new SendThead(this);
		new AcceptThead(this);
		new AcceptPackThead(this);
	}
	
	public void sendPack(PackageClient sendPack){
		sendQueue.put(sendPack);
	}

	public void putAcceptQueue(PackageServer acceptPack){
		acceptQueue.put(acceptPack);
	}
}
