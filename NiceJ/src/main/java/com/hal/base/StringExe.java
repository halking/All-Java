package com.hal.base;

public class StringExe {
   	String  s1 = new String("abc");
	public static void main(String[] args) {
		// TODO Auto-generated method stub
    	StringExe sExe = new StringExe();
       System.out.println(sExe.s1);
//       sExe.s1="bcd";
       sExe.change(sExe.s1);
       System.out.println(sExe.s1);
       String zip = "103-0003";
       zip = zip.replaceAll("-", "");
     System.out.println(zip);
	} 
	public void change(String string) {
		 s1= "test";
	}
}
