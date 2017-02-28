package cn.lunatic.service.socket.pack;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;

import cn.lunatic.base.util.ObjectUtil;
import cn.lunatic.base.util.security.ByteUtil;

public class PackTools {
	
	public static final String PACK_TYPE_REGIST_SERVICE = "REGIST_SERVICE";
	
	public static final String PACK_TYPE_CALL_SERVICE = "CALL_SERVICE";

	public static final String PACK_TYPE_CLOSE_CONNECTION = "CLOSE_CONNECTION";
	
	private final static int BUFF_SIZE = 400;
	
	/**
	 * 从输入流中读取报文,报文前4位为报文长度
	 * @param inputStream
	 * @return
	 * @throws IOException
	 */
	public static byte[] getInputByte(InputStream inputStream) throws IOException{
		byte[] lenbuf = new byte[4];
		inputStream.read(lenbuf);
		int size = ByteUtil.byte4ToInt(lenbuf, 0);
		if (size == 0) {
			return null;
		}
		byte[] buf = new byte[size];
		if (inputStream.read(buf) != size) {
			return null;
		}
		byte[] bs = new byte[size];
		System.arraycopy(buf, 0, bs, 0, bs.length);
		return bs;
	}
	
	
	/**
	 * 客户端报文打包成字节数组,前4位为报文长度
	 * @param pack
	 * @return
	 */
	public static byte[] packClientPackage(PackageClient pack){
		ByteBuffer sendBuf = ByteBuffer.allocate(BUFF_SIZE);
		sendBuf.put(new byte[4]);
		byte[] sendBodyByte = ObjectUtil.serializObject(pack);
		sendBuf.put(sendBodyByte);
		int len = sendBuf.position();
		sendBuf = sendBuf.putInt(0, len);
		sendBuf.flip();
		return sendBuf.array();
	}
	

	/**
	 * 
	 * @param inputStream
	 * @return
	 * @throws IOException
	 */
	public static PackageClient getPackageClientFromInputSteam(InputStream inputStream) throws IOException{
		byte[] inputBytes = getInputByte(inputStream);
		if(inputBytes == null){
			return null;
		}
		return (PackageClient) ObjectUtil.toObject(inputBytes);
	}
	
	/**
	 * 服务端报文打成字节数组,前4位为报文长度
	 * @param pack
	 * @return
	 */
	public static byte[] packServerPackage(PackageServer pack){
		ByteBuffer sendBuf = ByteBuffer.allocate(BUFF_SIZE);
		sendBuf.put(new byte[4]);
		byte[] sendBodyByte = ObjectUtil.serializObject(pack);
		sendBuf.put(sendBodyByte);
		int len = sendBuf.position();
		sendBuf = sendBuf.putInt(0, len);
		sendBuf.flip();
		return sendBuf.array();
	}
	
	/**
	 * 
	 * @param inputStream
	 * @return
	 * @throws IOException
	 */
	public static PackageServer getPackageServerFromInputStream(InputStream inputStream) throws IOException{
		byte[] inputBytes = getInputByte(inputStream);
		if(inputBytes == null){
			return null;
		}
		return (PackageServer) ObjectUtil.toObject(inputBytes);
	}
	
}
