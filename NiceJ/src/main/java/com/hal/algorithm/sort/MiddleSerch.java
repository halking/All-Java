/**
 * 
 */
package com.hal.algorithm.sort;


/**
 * @author hal
 *
 */
public class MiddleSerch {
    
	private  static  int  count = 0;
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
    MiddleSerch  ms = new MiddleSerch();
    BubbleSort bSort = new BubbleSort();
    bSort.bublesort(StaticMethod.a);
    StaticMethod.print(StaticMethod.a);
    ms.middleserch(StaticMethod.a, 53);
    System.out.println("共查找了"+count);
	}
  
	public   boolean  middleserch(  int a[],int key) {
		int left =0,right =a.length-1,middle;
		System.out.println("");
		if (a.length>0) {
		    while (left<=right) {
				 middle = (left+right)/2;
				 System.out.println(middle);
				 if (key<a[middle]) {
					     right=middle-1;
				} else  if (key > a[middle]) {
					  left = middle+1;
				} else {
                       System.out.println("key="+a[middle]);
                       return true;
				}
				 count++;
			}
		}
		return false;
	}
}
