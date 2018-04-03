/**
 * 
 */
package com.hal.io;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.Cipher;
import javax.crypto.CipherOutputStream;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;

/**
 * @author hal
 *
 */
public class CipherInOutTest {

	/**
	 * 2016年4月14日
	 * 
	 * @param args
	 * @throws InvalidKeyException
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String str = "huangalin is cool";
		byte[] date = str.getBytes();
		try {
			
			FileInputStream fis = new FileInputStream("D://tmp//out.txt");
			FileOutputStream  fos = new FileOutputStream("D://tmp//cryp.txt");
			//注册算法的实现者
//			Provider  provider = 
			//创建密钥
			DESKeySpec desKeySpec = new DESKeySpec(date);
			SecretKeyFactory secretKeyFactory = SecretKeyFactory.getInstance("DES");
			SecretKey secretKey = secretKeyFactory.generateSecret(desKeySpec);	
			//使用数据加密标准
			Cipher cipher = Cipher.getInstance("DES");
			cipher.init(Cipher.DECRYPT_MODE, secretKey);
			CipherOutputStream  cos  = new CipherOutputStream(fos, cipher);
			
			byte[] b = new byte[512];
			while ((fis.read(b))!=-1) {
				cos.write(b);
			}
			cos.flush();
			cos.close();
			fis.close();
		} catch (InvalidKeyException ein) {
			// TODO Auto-generated catch block
			ein.printStackTrace();
		} catch (NoSuchAlgorithmException en) {
			// TODO: handle exception
			en.printStackTrace();
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}

	}

}
