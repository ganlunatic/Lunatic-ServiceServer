package cn.lunatic.service.socket.server;

import java.io.IOException;
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
public class ClientSocket {
	/**
	 * 当发送队列为空时,是否关闭连接
	 */
	public boolean closeFlag = false;
	
	public Socket socket;
	
	public ClientPackQueue acceptQueue;
	
	public ServerPackQueue sendQueue;

	public ClientSocket(Socket socket) throws IOException {
		super();
		Log.print(socket.getLocalAddress() + ":" + socket.getPort() + "建立连接");
		this.sendQueue = new ServerPackQueue();
		this.acceptQueue = new ClientPackQueue();
		this.socket = socket;
		new SendThead(this);
		new AcceptThead(this);
		new AcceptPackThead(this);
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
	 * 关闭连接(先关闭输入流,如果输出队列还有数据,设置关闭标识为关闭,处理完了关闭)
	 * @throws IOException
	 */
	public void closeAll() throws IOException{
		socket.getInputStream().close();
		if(getSendQueueSize() <= 0){
			socket.getOutputStream().close();
			socket.close();
		}else{
			closeFlag = true;
		}
	}
	
	/**
	 * 关闭连接
	 * @throws IOException
	 */
	public void closeConn() throws IOException{
		Log.print(socket.getLocalAddress() + ":" + socket.getPort() + "关闭连接");
		this.socket.close();
	}
	
	/**
	 * 获取当前发送队列的长度
	 * @return
	 */
	public int getSendQueueSize(){
		return sendQueue.size();
	}
}
