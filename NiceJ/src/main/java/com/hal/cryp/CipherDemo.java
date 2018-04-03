package com.hal.cryp;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.nio.file.Path;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.SecretKeySpec;


public class CipherDemo {
	static String path = "D:\\tmp\\cry.key";

	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		 System.out.println(randomKey());
		// System.out.println(enCrypt());
	}

	// 生成随机密钥
	public static Key randomKey() throws NoSuchAlgorithmException,
			InvalidKeySpecException {
		KeyGenerator keygen = KeyGenerator.getInstance("AES");
		SecureRandom random = new SecureRandom();
		keygen.init(random);
		Key key = keygen.generateKey();
		try {
			ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(path));
			oos.writeObject(key);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		/*
		 * SecretKeyFactory factory = SecretKeyFactory.getInstance("AES"); byte
		 * [] date = new byte[16]; SecretKeySpec keySpec = new
		 * SecretKeySpec(date, "AES"); key = factory.generateSecret(keySpec);
		 */
		return key;
	}

	public static byte[] enCrypt(InputStream is, OutputStream os)
			throws NoSuchAlgorithmException, NoSuchPaddingException, Exception {
		Cipher cipher = Cipher.getInstance("AES");
		int mode = Cipher.ENCRYPT_MODE;
		Key key = randomKey();
		cipher.init(mode, key);
		int size = cipher.getBlockSize();
		System.out.println(size);
		byte[] inByte = new byte[size];
		int outSize = cipher.getOutputSize(size);
		byte[] outByte = new byte[outSize];

		int inLength = 0;
		boolean flag = true;
		while (flag) {
			inLength = is.read(inByte);
			if (inLength == size) {
				int outLength = cipher.update(inByte, 0, size, outByte);
				os.write(outByte, 0, outLength);
			} else
				flag = false;
		}
		if (inLength > 0)
			outByte = cipher.doFinal(inByte, 0, inLength);
		else
			outByte = cipher.doFinal();
		return outByte;
	}
}
