/**
 * 
 */
package com.hal.io;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * @author hal
 *
 */
public class ReadFileTest {

	/**
	 * 2016年4月14日
	 * 
	 * @param args
	 */
	public static void main(String[] args)  {
		// TODO Auto-generated method stub
		try {
			String path = "D:"+File.separator+"tmp"+File.separator+"text.txt";
			File file = new File(path);
			System.out.println(file);
			FileInputStream fis = new FileInputStream(file);
			FileOutputStream fos = new FileOutputStream("D://tmp//out.txt");
			int i = fis.available();
			byte[] b = new byte[i];
			String c = b.toString();
			while ((fis.read(b, 0, i)) != -1) {
				System.out.println(c);
			}
			fos.write(b);
			fis.close();
			fos.close();
			System.out.println("写入文件成功");
		} catch (IOException ioe) {
			ioe.printStackTrace();
		} catch (Exception e) {
			// TODO: handle exception
			 System.out.println(e.getMessage());
		}
	}

}
