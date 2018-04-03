/**
 * 
 */
package com.hal.algorithm.sort;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @author hal
 *
 */
public class WipeZero {
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
//		WipeZero wp = new WipeZero();
       System.out.println("处理前的数组：");
       StaticMethod.print(StaticMethod.b);

       System.out.println("处理后的数组：");
       wipe(StaticMethod.b);
	}
	
	public   static void wipe(int[] a){
		List<Integer>  last = new  ArrayList<Integer>();
		for (int i = 0; i < a.length; i++) {
			 if (a[i]!=0) {
				 last.add(a[i]);
			}
		}
	 	int m[] = new int[last.size()];
		for (int i = 0; i < last.size(); i++) {
			m[i] = last.get(i);
		}
		for (int i : m) {
			  System.out.print(i+" "); 
		}
	}

}
