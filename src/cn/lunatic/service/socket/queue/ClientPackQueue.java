package cn.lunatic.service.socket.queue;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import cn.lunatic.service.socket.pack.PackageClient;

/**
 * 客户端报文队列
 * @author gan.feng
 */
public class ClientPackQueue {
	/**
	 * 阻塞队列
	 */
	public BlockingQueue<PackageClient> queue = new LinkedBlockingQueue<PackageClient>();
	
	public int queueMax;
	
	/**
	 * 添加到接收数据队列
	 * 
	 * @param smsMsg
	 * @return
	 */
	public boolean put(PackageClient pack) {
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
	
	public PackageClient take() throws Exception {
		return queue.take();
	}
}
