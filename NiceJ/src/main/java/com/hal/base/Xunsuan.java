/**
 * 
 */
package com.hal.base;

/**
 * @author hal
 *
 */
public class Xunsuan {
        public static void main(String[] args) {
			Xunsuan  xs = new Xunsuan();
			int  x=   xs.f(729, 271);
			System.out.println(x);
//			不用中间变量交换
			int  a=10, b=5;
//			a=a+b;   
			a= a^b;
			System.out.println("a="+a);
		 //	b=a-b;
			b=a^b;
			System.out.println("b="+b);
			a= a^b;
			System.out.println("a="+a);
		}
        public  int  f(int x,int y){
        	return  (x&y)+((x^y) >>1);
        }
}
