/**
 * 
 */
package com.hal.algorithm.sort;

/**
 * @author hal
 *
 */
public class StaticMethod {
	
	public  static  int  a[] = {42,2,10,6,8,7,3,96,1,5,53,41,1};
	public  static  int  b[] ={5,10,2,8,6,0,4,0,11,0,8,0,0,2};
	public  static void print(int a[]){
		for (int i=0 ;i<a.length ;i++) {
			System.out.print(a[i]+" ");
		}
	}
	public static void   swap(int arr[],int i,int j) {
		int temp;
		temp = arr[i];
		arr[i] = arr[j];
		arr[j] = temp;
	}
} 
