package cn.lunatic.service.socket.pack;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;

import cn.lunatic.base.util.ObjectUtil;
import cn.lunatic.base.util.security.ByteUtil;

public class PackTools {
	
	public static final String PACK_TYPE_REGIST_SERVICE = "REGIST_SERVICE";
	
	public static final String PACK_TYPE_CALL_SERVICE = "CALL_SERVICE";

	public static final String PACK_TYPE_CLOSE_CONNECTION = "CLOSE_CONNECTION";
	
	/**
	 * 从输入流中读取报文,报文前4位为报文长度
	 * @param inputStream
	 * @return
	 * @throws IOException
	 */
	public static byte[] getInputByte(BufferedInputStream inputStream) throws IOException{
		byte[] lenbuf = readBytes(inputStream, 4);
		int packLen = ByteUtil.byte4ToInt(lenbuf, 0);
		byte[] buffByte = new byte[packLen];
		byte[] tempByte = readBytes(inputStream, packLen);
		/*字节数组中包含前4位长度*/
		/*
		System.arraycopy(lenbuf, 0, buffByte, 0, 4);
		System.arraycopy(tempByte, 0, buffByte, 4, tempByte.length);
		*/
		/*字节数组中不包含前4位长度*/
		System.arraycopy(tempByte, 0, buffByte, 0, tempByte.length);
		return buffByte;
	}
	
	
	/**
	 * 客户端报文打包成字节数组,前4位为报文长度
	 * @param pack
	 * @return
	 */
	public static byte[] packClientPackage(PackageClient pack){
		byte[] sendBodyByte = ObjectUtil.serializObject(pack);
		ByteArrayOutputStream aos = new ByteArrayOutputStream();
		try {
			ByteBuffer sendBuf = ByteBuffer.allocate(4);
			sendBuf.putInt(sendBodyByte.length);
			aos.write(sendBuf.array());
			aos.write(sendBodyByte);
			aos.flush();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				aos.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return aos.toByteArray();
	}
	

	/**
	 * 
	 * @param inputStream
	 * @return
	 * @throws IOException
	 */
	public static PackageClient getPackageClientFromInputSteam(BufferedInputStream inputStream) throws IOException{
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
		byte[] sendBodyByte = ObjectUtil.serializObject(pack);
		ByteArrayOutputStream aos = new ByteArrayOutputStream();
		try {
			ByteBuffer sendBuf = ByteBuffer.allocate(4);
			sendBuf.putInt(sendBodyByte.length);
			aos.write(sendBuf.array());
			aos.write(sendBodyByte);
			aos.flush();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				aos.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return aos.toByteArray();
	}
	
	/**
	 * 
	 * @param inputStream
	 * @return
	 * @throws IOException
	 */
	public static PackageServer getPackageServerFromInputStream(BufferedInputStream inputStream) throws IOException{
		byte[] inputBytes = getInputByte(inputStream);
		if(inputBytes == null){
			return null;
		}
		return (PackageServer) ObjectUtil.toObject(inputBytes);
	}
	
	
	private static byte[] readBytes(BufferedInputStream sockedInputStream, int readLen) throws IOException{
		byte[] buffByte = new byte[readLen];
		int lastLen = 0;
		for(;;){
			byte[] tempByte = new byte[readLen-lastLen];
			int currlen = sockedInputStream.read(tempByte);
			if (currlen == 0) {
				continue;
			}
			System.arraycopy(tempByte, 0, buffByte, lastLen, currlen);
			lastLen += currlen;
			if (lastLen >= readLen) {
				break;
			}
		}
		return buffByte;
	}
	
}
