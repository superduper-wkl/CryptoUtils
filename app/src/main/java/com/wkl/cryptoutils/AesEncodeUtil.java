package com.wkl.cryptoutils;

import java.math.BigInteger;
import java.security.MessageDigest;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class AesEncodeUtil {
	
	/**
	 * 将byte[]转为各种进制的字符串
	 * @param bytes byte[]
	 * @param radix 可以转换进制的范围，从Character.MIN_RADIX到Character.MAX_RADIX，超出范围后变为10进制
	 * @return 转换后的字符串
	 */
	public static String binary(byte[] bytes, int radix){
		return new BigInteger(1, bytes).toString(radix);// 这里的1代表正数
	}
	
	/**
	 * 获取byte[]的md5值
	 * @param bytes byte[]
	 * @return md5
	 * @throws Exception
	 */
	public static byte[] md5(byte[] bytes) throws Exception {
		MessageDigest md = MessageDigest.getInstance("MD5");
		md.update(bytes);
		
		return md.digest();
	}
	
	
	/**
	 * AES加密
	 * @param content 待加密的内容
	 * @param encryptKey 加密密钥
	 * @return 加密后的byte[]
	 * @throws Exception
	 */
	public static byte[] aesEncrypt(String content, String encryptKey) throws Exception {
		byte[] raw = encryptKey.getBytes();
		SecretKeySpec sKeySpec = new SecretKeySpec(raw, "AES");
		Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
		IvParameterSpec iv = new IvParameterSpec(encryptKey.getBytes());
		cipher.init(Cipher.ENCRYPT_MODE, sKeySpec, iv);
		
		return cipher.doFinal(content.getBytes());
		
	}
	
	/**
	 * AES加密
	 * @param content 待加密的内容
	 * @param encryptKey 加密密钥
	 * @return 加密后的byte[]
	 * @throws Exception
	 */
	public static byte[] aesEncrypt(String content, byte[] encryptKey) throws Exception {
		SecretKeySpec sKeySpec = new SecretKeySpec(encryptKey, "AES");
		Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
		IvParameterSpec iv = new IvParameterSpec(encryptKey);
		cipher.init(Cipher.ENCRYPT_MODE, sKeySpec, iv);
		return cipher.doFinal(content.getBytes("utf-8"));
	}

	/**
	 * AES加密
	 * @param content 待加密的内容
	 * @param encryptKey 加密密钥
	 * @return 加密后的byte[]
	 * @throws Exception
	 */
	public static byte[] aesEncrypt(byte[] content, byte[] encryptKey) throws Exception {
		SecretKeySpec sKeySpec = new SecretKeySpec(encryptKey, "AES");
		Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
		IvParameterSpec iv = new IvParameterSpec(encryptKey);
		cipher.init(Cipher.ENCRYPT_MODE, sKeySpec, iv);
		return cipher.doFinal(content);
	}


	/**
	 * AES解密
	 * @param encryptBytes 待解密的byte[]
	 * @param decryptKey 解密密钥
	 * @return 解密后的String
	 * @throws Exception
	 */
	public static String aesDecryptByBytes(byte[] encryptBytes, String decryptKey) throws Exception {
		SecretKeySpec sKeySpec = new SecretKeySpec(decryptKey.getBytes("utf-8"), "AES");
		Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
		IvParameterSpec iv = new IvParameterSpec(decryptKey.getBytes("utf-8"));
		cipher.init(Cipher.DECRYPT_MODE, sKeySpec, iv);
		byte[] decryptBytes = cipher.doFinal(encryptBytes);
		
		
		return new String(decryptBytes, "utf-8");
	}
	
	/**
	 * AES解密
	 * @param encryptBytes 待解密的byte[]
	 * @param decryptKey 解密密钥
	 * @return 解密后的String
	 * @throws Exception
	 */
	public static byte[] aesDecryptByStr(byte[] encryptBytes, String decryptKey) throws Exception {
		SecretKeySpec sKeySpec = new SecretKeySpec(decryptKey.getBytes("utf-8"), "AES");
		Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
		IvParameterSpec iv = new IvParameterSpec(decryptKey.getBytes("utf-8"));
		cipher.init(Cipher.DECRYPT_MODE, sKeySpec, iv);
		byte[] decryptBytes = cipher.doFinal(encryptBytes);
		
		
		return decryptBytes;
	}
	
	/**
	 * AES解密
	 * @param encryptBytes 待解密的byte[]
	 * @param decryptKey 解密密钥
	 * @return 解密后的String
	 * @throws Exception
	 */
	public static String aesDecryptByBytes(byte[] encryptBytes, byte[] decryptKey) throws Exception {

		SecretKeySpec sKeySpec = new SecretKeySpec(decryptKey, "AES");
		Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
		IvParameterSpec iv = new IvParameterSpec(decryptKey);
		cipher.init(Cipher.DECRYPT_MODE, sKeySpec, iv);
		byte[] decryptBytes = cipher.doFinal(encryptBytes);
		
		
		return new String(decryptBytes, "utf-8");
	}
	
	/**
	 * 将二进制转换成16进制
	 * 
	 * @param buf
	 * @return
	 */
	public static String parseByte2HexStr(byte buf[]) {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < buf.length; i++) {
			String hex = Integer.toHexString(buf[i] & 0xFF);
			if (hex.length() == 1) {
				hex = '0' + hex;
			}
			sb.append(hex.toUpperCase());
		}
		return sb.toString();
	}

	/**
	 * 将16进制转换为二进制
	 * 
	 * @param hexStr
	 * @return
	 */
	public static byte[] parseHexStr2Byte(String hexStr) {
		if (hexStr.length() < 1)
			return null;
		byte[] result = new byte[hexStr.length() / 2];
		for (int i = 0; i < hexStr.length() / 2; i++) {
			int high = Integer.parseInt(hexStr.substring(i * 2, i * 2 + 1), 16);
			int low = Integer.parseInt(hexStr.substring(i * 2 + 1, i * 2 + 2),
					16);
			result[i] = (byte) (high * 16 + low);
		}
		return result;
	}

	/**
	 * 将16进制转换为字符串
	 * @param result
	 * @return
	 */
	public static String parseHex2String(String result) {
		if ("0x".equals(result.substring(0, 2))) {
			result = result.substring(2);
		}
		byte[] baKeyword = new byte[result.length() / 2];
		for (int i = 0; i < baKeyword.length; i++) {
			try {
				baKeyword[i] = (byte) (0xff & Integer.parseInt(result.substring(
						i * 2, i * 2 + 2), 16));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		try {
			result = new String(baKeyword, "utf-8");//UTF-16le:Not
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		return result;
	}

	/**
	 * 将32位整数转换成长度为4的byte数组
	 *
	 * @param s int
	 * @return byte[]
	 * */
	public static byte[] intToByteArray(int s) {
		byte[] targets = new byte[4];
		for (int i = 0; i < 4; i++) {
			int offset = (targets.length - 1 - i) * 8;
			targets[i] = (byte) ((s >>> offset) & 0xff);
		}
		return targets;
	}

	//java 合并两个byte数组
	public static byte[] byteMerger(byte[] byte_1, byte[] byte_2){
		byte[] byte_3 = new byte[byte_1.length+byte_2.length];
		System.arraycopy(byte_1, 0, byte_3, 0, byte_1.length);
		System.arraycopy(byte_2, 0, byte_3, byte_1.length, byte_2.length);
		return byte_3;
	}

}
