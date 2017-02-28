package cn.lunatic.base.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

import cn.lunatic.base.util.Log;
import cn.lunatic.base.util.io.FileUtil;
import cn.lunatic.base.util.security.Base64;
import cn.lunatic.base.util.security.ByteUtil;

/**
 * 对象工具
 * 
 * @author gan.feng
 *
 */
public class ObjectUtil implements Serializable {

	private static final long serialVersionUID = -6366480960069132912L;

	/**
	 * 序列化对象为byte数组
	 * 
	 * @param obj
	 * @return
	 */
	public static byte[] serializObject(Object obj) {
		ObjectOutputStream objectOutputStream = null;
		ByteArrayOutputStream byteArrayOutputStream = null;
		try {
			byteArrayOutputStream = new ByteArrayOutputStream();
			objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
			objectOutputStream.writeObject(obj);
			return byteArrayOutputStream.toByteArray();
		} catch (Exception e) {
			Log.print("序列化对象失败", e);
			return null;
		} finally {
			try {
				if (byteArrayOutputStream != null) {
					byteArrayOutputStream.close();
				}
				if (objectOutputStream != null) {
					objectOutputStream.close();
				}
			} catch (Exception e) {
			}
		}
	}
	
	public static Object toObject(byte[] bytes) {
		ObjectInputStream objectInputStream = null;
		ByteArrayInputStream byteArrayInputStream = null;
		try {
			byteArrayInputStream = new ByteArrayInputStream(bytes);
			objectInputStream = new ObjectInputStream(byteArrayInputStream);
			return objectInputStream.readObject();
		} catch (Exception e) {
			Log.print("恢复java对象失败",e);
			return null;
		}finally{
			try {
				if (byteArrayInputStream != null) {
					byteArrayInputStream.close();
				}
				if (objectInputStream != null) {
					objectInputStream.close();
				}
			} catch (Exception e) {
			}
		}
	}

	/**
	 * 序列化对象为16进制串
	 * 
	 * @param obj
	 * @return
	 */
	public static String serializObject2Hex(Object obj) {
		return ByteUtil.bytes2HexStr(serializObject(obj));
	}
	
	public static Object toObjectByHex(String hex){
		return toObject(ByteUtil.hexStr2Bytes(hex));
	}

	/**
	 * 序列化对象为base64串
	 * 
	 * @param obj
	 * @return
	 */
	public static String serializObject2Base64(Object obj) {
		return Base64.encodeBytes(serializObject(obj));
	}
	
	public static Object toObjectByBase64(String base64){
		try {
			return toObject(Base64.decode(base64));
		} catch (IOException e) {
			Log.print("Base64解码错误", e);
			return null;
		}
	}
	
	
	/**
	 * 序列化对象到文件
	 * @param filePath			保存文件路径,文件名格式：类名_变量名.byte
	 * @param obj
	 * @return	true:成功,false:失败
	 */
	public static boolean serializObject2File(String filePath, Object obj){
		try {
			FileUtil.byteArrayToFile(serializObject(obj), filePath);
			return true;
		} catch (IOException e) {
			Log.print("将对象序列化到文件失败",e);
			return false;
		}
	}
	
	public static Object toObjectByFile(String filePath){
		try {
			return toObject(FileUtil.fileToByte(filePath));
		} catch (IOException e) {
			Log.print("Base64解码错误", e);
			return null;
		}
	}
	
	
	@SuppressWarnings("unchecked")
	public static <T> T deepCopy(T obj){
		byte[] bytes = serializObject(obj);
		return (T) toObject(bytes);
	}
	
}
