/**
 * 
 */
package com.hal.algorithm.sort;

/**
 * @author hal
 *
 */
public class QuickSort {
    
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
     int  a[] = {42,16,35,57,18,25,12,96,52};
     QuickSort  qSort = new QuickSort();
     System.out.println("排序前的数组：\n");
      StaticMethod.print(a);
     qSort.quick(a);
     System.out.println("\n排序后的数组：\n");
     StaticMethod.print(a);
	}
	public  void quickSort(int a[],int low,int high){
		   if (low<high) {
			   int middle = getMiddle(a, low, high);
			   quickSort(a, low, middle-1);
			   quickSort(a, middle+1, high);
		}
	}
	public int getMiddle(int a[],int low,int high){
		 int temp = a[low];
		 while (low<high) {
			  while(low<high && a[high] >=temp) {
				       high--; 
			}
			  a[low] = a[high];
//			  System.out.println("第一次交换的值：");
//			  StaticMethod.print(a);
			  while (low<high && a[low]<=temp) {
				   low++;
			}
			  a[high] =a[low];
//			  System.out.println("第一次交换的值：");
			  StaticMethod.print(a);
		}
		 a[low] = temp;
//		  System.out.println("第一次执行完毕的值：");
//		  StaticMethod.print(a);
		return low;
	}
	public  void  quick(int a[]){
		if (a.length>0) {
			   quickSort(a, 0, a.length-1);
		}
	}
}
