/**
 * 
 */
package com.hal.io;

import java.io.File;

/**
 * @author hal
 *
 */
public class FileTest {

	/**
	 *2016年4月14日
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
        String path = "D://tmp//out.txt";
        File file = new File(path);
        System.out.println("文件对象名："+file.getName());
        System.out.println("文件相对路径名："+file.getPath());
        System.out.println(""+file.getAbsolutePath());
        System.out.println("父文件对象路径名："+file.getParent()+"父文件名"+file.getParentFile().getParent());
        System.out.println("返回文件中的所有文件对象"+file.listFiles());
        File newpath = new File("d://tmp//ext.txt");
        newpath.mkdir();
	}

}
