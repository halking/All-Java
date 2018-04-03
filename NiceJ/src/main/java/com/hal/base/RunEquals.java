package com.hal.base;

public class RunEquals {
   
	@SuppressWarnings("unused")
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String  s1 = "abc";  //存在方法区的常量池中
		String  s2 = new String("abc");   //存在堆中
		String  s22 = new String("abc");
		s1="efg";
		s2="def";
		System.out.println(s1);
		System.out.println(s2);
		String s3 = s1 ;     
		String s4 = s2 ;
		StringBuffer  sb1 = new StringBuffer("abc"); 
		StringBuffer  sb2 = new StringBuffer("abc");
		String  s8 = sb1.toString();
		String s5 = sb2.toString();
	    String  s6 = "abcde";
	    String  s7 = new String("abcde");
	System.out.println(s1==s2);  // false
		System.out.println(s1==s3); // true
		System.out.println(s1==s4);//false
		System.out.println(s2==s4);//true
		System.out.println(s2==s3);//false
		System.out.println(s1==s6); //false
		System.out.println(s2==s7);//false
		System.out.println(s1==s8);//false
		System.out.println(s2==s8);//false
		System.out.println(s2==s22); //flase
		System.out.println(sb1==sb2);//false
		System.out.println(s5==s8);  //false   
		//System.out.println(s6 ==s2); //true
		/*System.out.println(s1.equals(s2)); //true
		System.out.println(s1.equals(s6));//false
		System.out.println(s2.equals(s7)); //false
		System.out.println(s1.equals(s3));//true*/
	    System.out.println(s2.equals(s22));//true
	    System.out.println(s1.equals(s3)); //true
	    System.out.println(s1.equals(s5));
	    
	}

}
