package com.hal.test;

public class StringTest {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
/*		String  string = "huangalin";
		subStringa(string);*/
		Integer  i = 0;
				if ( (null != i)) {
					System.out.println("aaaa");
				}
	}
  public static String subStringa(String str){
		str = str.substring(0, 5);
		System.out.println(str);
		return str;
  }
}
