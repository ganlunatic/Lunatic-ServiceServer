package cn.lunatic.service.socket.client;

import java.io.IOException;
import java.net.Socket;

import cn.lunatic.service.socket.client.thead.AcceptThead;
import cn.lunatic.base.util.Log;
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
public class Client extends Thread{
	private boolean logInFlag;
	
	private boolean logOutFlag;
	/**
	 * 关闭标识,true:不在接收消息,将发送消息队列的消息发送完成后结束
	 */
	private boolean closeFlag;
	
	public Socket socket;
	
	public ClientPackQueue sendQueue;
	
	public ServerPackQueue acceptQueue;
	
	public Client(Socket socket, boolean logInFlag, boolean logOutFlag) throws IOException {
		super();
		this.sendQueue = new ClientPackQueue();
		this.acceptQueue = new ServerPackQueue();
		this.socket = socket;
		this.logInFlag = logInFlag;
		this.logOutFlag = logOutFlag;
		new SendThead(this);
		new AcceptThead(this);
		new AcceptPackThead(this);
		this.start();
	}
	
	public Client(Socket socket) throws IOException {
		super();
		this.sendQueue = new ClientPackQueue();
		this.acceptQueue = new ServerPackQueue();
		this.socket = socket;
		new SendThead(this);
		new AcceptThead(this);
		new AcceptPackThead(this);
		this.start();
	}
	
	@Override
	public void run() {
		super.run();
		for(;;){
			if(closeFlag && getSendQueueSize() <=0 && getAcceptQueueSize() <=0){
				break;
			}
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				Log.print("线程休眠错误", e);
			}
		}
		// 关闭连接
		try {
			socket.close();
		} catch (IOException e) {
			Log.print("关闭Socket连接失败", e);
		}
	}
	
	public void sendPack(PackageClient sendPack){
		sendQueue.put(sendPack);
	}

	public void putAcceptQueue(PackageServer acceptPack){
		acceptQueue.put(acceptPack);
	}
	
	/**
	 * 获取当前发送队列的长度
	 * @return
	 */
	public int getSendQueueSize(){
		return sendQueue.size();
	}

	/**
	 * 获取当前接收队列的长度
	 * @return
	 */
	public int getAcceptQueueSize(){
		return acceptQueue.size();
	}
	
	
	public boolean getCloseFlag() {
		return closeFlag;
	}

	public void setCloseFlag(boolean closeFlag) {
		this.closeFlag = closeFlag;
	}

	public boolean getLogInFlag() {
		return logInFlag;
	}

	public void setLogInFlag(boolean logInFlag) {
		this.logInFlag = logInFlag;
	}

	public boolean getLogOutFlag() {
		return logOutFlag;
	}

	public void setLogOutFlag(boolean logOutFlag) {
		this.logOutFlag = logOutFlag;
	}
	
	
}
