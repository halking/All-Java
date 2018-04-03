package com.d1m.wechat.util;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import com.d1m.wechat.common.Constant;
import org.apache.commons.codec.binary.Base64;

public class Security {

	private static final String AES = "AES";
	
	/**
	 * 数据加密
	 * @param input
	 * @param key
	 * @return
	 */
	public static String encrypt(String input, String key) {
		byte[] crypted = null;
		try {
			DESKeySpec desKeySpec = new DESKeySpec(key.getBytes("UTF-8"));
			SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
			SecretKey secretKey = keyFactory.generateSecret(desKeySpec);
			IvParameterSpec iv = new IvParameterSpec(key.getBytes("UTF-8"));
			Cipher cipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
			cipher.init(Cipher.ENCRYPT_MODE, secretKey, iv);
			crypted = cipher.doFinal(input.getBytes());
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		return new String(Base64.encodeBase64(crypted));
	}

	/**
	 * 数据解密
	 * @param input
	 * @param key
	 * @return
	 */
	public static String decrypt(String input, String key) {
		byte[] output = null;
		try {
			Cipher cipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
			DESKeySpec desKeySpec = new DESKeySpec(key.getBytes("UTF-8"));
			SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
			SecretKey secretKey = keyFactory.generateSecret(desKeySpec);
			IvParameterSpec iv = new IvParameterSpec(key.getBytes("UTF-8"));
			cipher.init(Cipher.DECRYPT_MODE, secretKey, iv);
			output = cipher.doFinal(Base64.decodeBase64(input));
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		return new String(output);
	}

	/* Encrypt message with AES*/
	public static String encryptMsgWithAES(String msg, String aesKey)  {
		String encrptyresult = null;
		try {
			Cipher cipher = Cipher.getInstance(AES);
			SecretKeySpec keySpec = new SecretKeySpec(Base64.decodeBase64(aesKey), AES);
			cipher.init(Cipher.ENCRYPT_MODE, keySpec);
			byte[] encrypted = cipher.doFinal(msg.getBytes(Constant.CHARSET_UTF_8));
			// Encode the encryption message by BASE64
			Base64 base64 = new Base64();
			encrptyresult = base64.encodeToString(encrypted);
		} catch (Exception e) {
			throw new RuntimeException("Encrypt with AES failed.");
		}
		return encrptyresult;
	}

	/* Decrypt message with AES*/
	public static String decryptMsgWithAES(String encryptMsg, String aesKey)  {
		String decrptyresult = null;
		try {
			Cipher cipher = Cipher.getInstance(AES);
			SecretKeySpec keySpec = new SecretKeySpec(Base64.decodeBase64(aesKey), AES);
			cipher.init(Cipher.DECRYPT_MODE, keySpec);
			byte[] encrypted = Base64.decodeBase64(encryptMsg);
			decrptyresult = new String(cipher.doFinal(encrypted), Constant.CHARSET_UTF_8);
		} catch (Exception e) {
			throw new RuntimeException("Encrypt with AES failed.");
		}
		return decrptyresult;
	}
	
	public static void main(String[] args) {
		String key = "national";
		String data = "{\"instno\":\"1469159232347\"}";
		long currentTime = System.currentTimeMillis();
		System.out.println(currentTime);
		System.out.println(Security.encrypt(data,key));
		System.out.println(Security.decrypt("Qrb7tbmaC+R0J4Aqnd+5j61BqU+TSsLKRe6vXt+VMC2rCwrmj3Kld+WxnNb5FKvhlNB9lhsZ6hnb916MNTVxM2uPGRym11Jb3c5CGhwVwZ9hlTo/77bePz68sLKwMbnTTyO+w1d12elj7rU4JF0i7ht3ChohWjqJHY0KIRxXiYTyKcMAyzOG6Nz3s8GEsjc0x9zkXm7c17rQDEY0P8sfYqLdCCt+kIMCD1CiUi/LtyyJWnxKr7d+82UmEY7FLRLemVteXFdyiWbdIeVYgesu9FT9MC9Us9pmJgioMjzcw4r0q2oCgMPekDTFZ/CgMy5Hm7jO78o/cVv2yj839RlyoJTohIegfk/TJL9iG5YJZA0+mzpXNYABmE+8yXjuPmVcKURl1yJ3wvt+AkZSYUaKWgl5cM6ChYrpXWoP0tyQZK2HQol8I83Il44glelW0hH2WLJ8GIbLDofdI8SQ0UrqKJn+KbtWCY3rGRJJHjFUKJ9VNN4lfmvqQE87XnSkAZtD",key));
		
	}
}
