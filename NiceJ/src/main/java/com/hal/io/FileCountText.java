/**
 * 
 */
package com.hal.io;

import java.io.File;
import java.util.ArrayList;

/**
 * @author hal
 *
 */
public class FileCountText {
	String path = "d://web";
	 File file = new File(path);
	 String[] listfile = file.list();
	ArrayList<File> list = new ArrayList<File>();
	/**
	 * 2016年4月14日
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
	FileCountText fct = new FileCountText();
	for (int i=0;i<fct.listfile.length;i++) {
		System.out.println("列表："+fct.listfile.toString());
	}
	ArrayList<File>  all =    fct.getFile(fct.file);
	for (File file : all) {
		System.out.println(file);
	}
	}
  public ArrayList<File> getFile(File file){
	
	  File[] files = file.listFiles();
	  for (File file2 : files) {
		   if (file2.isDirectory()) {
			   getFile(file2);
		}else
		    list.add(file2);
	}
	  return list;
  }
}
