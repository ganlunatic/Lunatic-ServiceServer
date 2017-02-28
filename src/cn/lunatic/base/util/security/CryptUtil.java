package cn.lunatic.base.util.security;

import java.io.FileInputStream;
import java.security.KeyStore;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;

import cn.lunatic.base.util.StringUtils;

/**
 * 加解密工具类
 * 
 * @author gan.feng
 *
 */
public class CryptUtil {

	/**
	 * SHA1签名
	 * 
	 * @param content
	 *            签名原串
	 * @return 16进制的签名串,长度为40
	 */
	public static String SHA1(String content) {
		try {
			MessageDigest digest = MessageDigest.getInstance("SHA-1");
			digest.update(content.getBytes());
			return ByteUtil.bytes2HexStr(digest.digest());
		} catch (NoSuchAlgorithmException e) {
			System.out.println(e.getMessage());
			return null;
		}
	}

	/**
	 * MD5签名16位
	 * 
	 * @param content
	 *            签名原串
	 * @return 16进制的签名串,长度为16
	 */
	public static String Md5_16(String content) {
		try {
			MessageDigest digest = MessageDigest.getInstance("MD5");
			digest.update(content.getBytes());
			return ByteUtil.bytes2HexStr(digest.digest()).substring(8, 24);
		} catch (Exception e) {
			System.out.println(e.getMessage());
			return null;
		}
	}

	/**
	 * MD5签名32位
	 * 
	 * @param content
	 *            签名原串
	 * @return 16进制的签名串,长度为32
	 */
	public static String Md5_32(String content) {
		try {
			MessageDigest digest = MessageDigest.getInstance("MD5");
			digest.update(content.getBytes());
			return ByteUtil.bytes2HexStr(digest.digest());
		} catch (Exception e) {
			System.out.println(e.getMessage());
			return null;
		}
	}
	
	
	/**
	 * 从P12文件中导出私钥,base64处理
	 * @param p12FilePath
	 * @return
	 * @throws Exception 
	 */
	public static String getPeivateKeyFromP12(String p12FilePath, String keyalias, String keypasswd) throws Exception{
		KeyStore ks = KeyStore.getInstance("PKCS12");
		FileInputStream fis = new FileInputStream(p12FilePath);
		char[] Password = null;  
        if (StringUtils.isNotBlank(keypasswd)) {  
        	Password = keypasswd.toCharArray();  
        }  
		ks.load(fis, Password);
		fis.close();
		PrivateKey prikey = (PrivateKey)ks.getKey(keyalias, keypasswd.toCharArray());
//		Certificate cert = ks.getCertificate(keyalias);
//		PublicKey pubkey = cert.getPublicKey();
		return Base64.encodeBytes(prikey.getEncoded());
	}
	
}
