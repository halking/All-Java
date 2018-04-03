package com.hal.cryp;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import com.hal.utils.FileUtils;


public class DigestDemo {
   
	 static byte[] digest ;
	public static void main(String[] args)  throws IOException, NoSuchAlgorithmException{
		// TODO Auto-generated method stub
      System.out.println(getDigest(FileUtils.PATH).toString());
      String  path = "D:\\tmp\\digest.txt";
      writerDigestToFile(getDigest(FileUtils.PATH),path );
	}
   
  public  static  byte[]  getDigest(String path) throws IOException, NoSuchAlgorithmException{
	   InputStream  is  = new FileInputStream(path);
	   MessageDigest messageDigest = MessageDigest.getInstance("SHA-1");
	   int c;
	   while ((c=is.read())!=-1) {
		  messageDigest.update((byte)c);
	}
	   digest = messageDigest.digest();
	   return digest;
  }
  public  static void writerDigestToFile(byte[] b,String path) throws IOException{
	  OutputStream  os  = new FileOutputStream(path);
	  os.write(b);
	  os.flush();
	  os.close();
  }
  public static  void  msgEncryp() {
	  
  }
}
