package com.hal.utils;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class ReadWriterUtils{
	 public final  static  String FILE_PATH = "D://tmp//net.txt";
	 public   static  File file;
      public  static  InputStream  INS;
      public  static  OutputStream   OPS;
      public   static  byte[]  b = new byte[1024];
	  public  static void WriterContentToFile(String str) throws IOException{
		  file = new File(FILE_PATH);
		  try {
			OPS = new  FileOutputStream(FILE_PATH);
			OPS = new BufferedOutputStream(OPS);
			b = (str+"\n").getBytes();
			OPS.write(b);
			OPS.flush();
			OPS.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	  }
}
