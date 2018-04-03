/**
 * 
 */
package com.hal.io;

import java.io.File;
import java.io.FilenameFilter;
import java.util.Scanner;

/**
 * @author hal
 *
 */
public class FileFilterText  implements FilenameFilter {
	private String pre="",suf="";
	private String str="";
    
	/**
	 * 
	 */
	public FileFilterText(String string) {
		// TODO Auto-generated constructor stub
		str = string;
		int i = string.indexOf(".");
		if (i>0) {
			pre = string.substring(0, i);
			suf = string.substring(i+1);
		}
	}
	public static void main(String[] args) {
		System.out.println("请输入你要查询的文件名过滤符：");
		@SuppressWarnings("resource")
		Scanner sca =new Scanner(System.in);
		String str = sca.next();
		FilenameFilter filter = new FileFilterText(str);
		String path = "d://images";
		File file = new File(path);
		File cdir = new File(file.getAbsoluteFile(),"");
		System.out.println(cdir.getAbsolutePath());
		String [] strfile = cdir.list(filter);
		for (int i = 0; i < strfile.length; i++) {
			 System.out.println(strfile[i]);
		}
	}
	/* 
	 *2016年4月14日
	 * @see java.io.FilenameFilter#accept(java.io.File, java.lang.String)
	 *@param dir
	 *@param name
	 *@return
	 */
	@Override
	public boolean accept(File dir, String name) {
		// TODO Auto-generated method stub
		boolean flag = false;
		try {
			name = name.toLowerCase();
			if ((name.contains(str))|| str.equals("*")) {
				flag=true;
			}else if ((pre.contains("*"))&&(name.endsWith(suf))){
				flag=true;
			}
		} catch (NullPointerException e) {
			// TODO: handle exception
			System.out.println(e.getMessage());
		}
		return flag;
	}
}
