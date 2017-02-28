package cn.lunatic.service.socket.queue;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import cn.lunatic.service.socket.pack.PackageServer;

/**
 * 服务端报文队列
 * @author gan.feng
 */
public class ServerPackQueue {
	/**
	 * 阻塞队列
	 */
	public BlockingQueue<PackageServer> queue = new LinkedBlockingQueue<PackageServer>();
	
	public int queueMax;
	
	/**
	 * 添加到接收数据队列
	 * 
	 * @param smsMsg
	 * @return
	 */
	public boolean put(PackageServer pack) {
		boolean ret = true;
		try {
			queue.put(pack);
			int count = queue.size();
			if (count > queueMax) {
				queueMax = count;
			}
		} catch (Exception e) {
			ret = false;
			e.printStackTrace();
		}
		return ret;
	}
	
	public PackageServer take() throws Exception {
		return queue.take();
	}
	
	public int size(){
		return queue.size();
	}
}
