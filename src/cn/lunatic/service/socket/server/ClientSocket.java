package cn.lunatic.service.socket.server;

import java.io.IOException;
import java.io.Serializable;
import java.net.Socket;

import cn.lunatic.base.util.Log;
import cn.lunatic.service.socket.pack.PackageClient;
import cn.lunatic.service.socket.pack.PackageServer;
import cn.lunatic.service.socket.queue.ClientPackQueue;
import cn.lunatic.service.socket.queue.ServerPackQueue;
import cn.lunatic.service.socket.server.thead.AcceptThead;
import cn.lunatic.service.socket.server.thead.AcceptPackThead;
import cn.lunatic.service.socket.server.thead.SendThead;

/**
 * 客户端连接服务对象
 * 
 * 每个客户端对应一个此对象
 * 
 * @author gan.feng
 *
 */
public class ClientSocket extends Thread implements Serializable{
	private static final long serialVersionUID = 1L;
	
	private String serialNo;
	
	private boolean logInFlag;
	
	private boolean logOutFlag;
	/**
	 * true:不在接收消息,只处理未处理完的消息
	 */
	public boolean closeFlag = false;
	
	public Socket socket;
	
	public ClientPackQueue acceptQueue;
	
	public ServerPackQueue sendQueue;

	public ClientSocket(Socket socket, String serialNo, boolean logInFlag, boolean logOutFlag) throws IOException {
		super();
		Log.print(socket.getLocalAddress() + ":" + socket.getPort() + "建立连接");
		this.serialNo = serialNo;
		this.sendQueue = new ServerPackQueue();
		this.acceptQueue = new ClientPackQueue();
		this.socket = socket;
		this.logInFlag = logInFlag;
		this.logOutFlag = logOutFlag;
		new SendThead(this);
		new AcceptThead(this);
		new AcceptPackThead(this);
		this.start();
	}
	
	public ClientSocket(Socket socket, String serialNo) throws IOException {
		super();
		Log.print(socket.getLocalAddress() + ":" + socket.getPort() + "建立连接");
		this.serialNo = serialNo;
		this.sendQueue = new ServerPackQueue();
		this.acceptQueue = new ClientPackQueue();
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
	
	/**
	 * 发送消息给客户端
	 * @param sendPack
	 */
	public void sendPack(PackageServer sendPack){
		sendQueue.put(sendPack);
	}

	/**
	 * 接受消息
	 * @param acceptPack
	 */
	public void putAcceptQueue(PackageClient acceptPack){
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

	public String getSerialNo() {
		return serialNo;
	}

	public void setSerialNo(String serialNo) {
		this.serialNo = serialNo;
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
