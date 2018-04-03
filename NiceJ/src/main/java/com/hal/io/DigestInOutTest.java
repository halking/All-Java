/**
 * 
 */
package com.hal.io;

import java.beans.Encoder;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.security.DigestOutputStream;
import java.security.MessageDigest;

import org.omg.IOP.Encoding;

/**
 * @author hal
 *
 */
public class DigestInOutTest {

	/**
	 *2016年4月14日
	 * @param args
	 */
	@SuppressWarnings("resource")
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String str = "sha";
		String  path = "D://tmp//ins.txt";
		try {
			MessageDigest  messageDigest = MessageDigest.getInstance(str);
			FileInputStream fis = new FileInputStream(path);
//			String fileencode = 
			FileOutputStream fos =  new FileOutputStream("d://tmp//digest.txt");
			DigestOutputStream  dos = new DigestOutputStream(fos, messageDigest);
			byte[] b = new byte[512];
			while ((fis.read(b)!=-1)) {
				dos.write(b);
			}
			fis.read(b);
			if (b[0]==-17&&b[1]==-69&&b[2]==-65) {
				System.out.println(path+"是UTF-8编码");
			}else {
				System.out.println("GBK编码");
			}
			dos.flush();
			dos.close();
			fis.close();
			byte [] digest = dos.getMessageDigest().digest();
			String uString  =  new String (digest.toString().getBytes("GBK"),"ISO-8859-1");
			System.out.println(uString );
		} catch (Exception e) {
			// TODO: handle exception
			System.err.println(e);
		}
	}
}
