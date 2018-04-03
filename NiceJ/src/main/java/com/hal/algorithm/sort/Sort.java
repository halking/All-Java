package com.hal.algorithm.sort;

public class Sort {
	
	
	public static void main(String[] args) {
//		selectSort(StaticMethod.a);
//		shellSort(StaticMethod.a);
		selectSortTwo(StaticMethod.a);
		StaticMethod.print(StaticMethod.a);
	}

	/**
	 * 
	 * @description:冒泡排序
	 * @param a
	 *            void
	 * @author d1m
	 */
	public void bublesort(int a[]) {
		for (int i = 0; i < a.length; i++) {
			// System.out.println("a["+i+"]="+a[i]);
			for (int j = i + 1; j < a.length; j++) {
				// System.out.println("a[i]"+a[i]);
				// System.out.println("a[j]="+a[j]);
				if (a[i] > a[j]) {
					StaticMethod.swap(a, i, j);
				}
			}
		}
	}

	public void quickSort(int a[], int low, int high) {
		if (low < high) {
			int middle = getMiddle(a, low, high);
			quickSort(a, low, middle - 1);
			quickSort(a, middle + 1, high);
		}
	}

	public int getMiddle(int a[], int low, int high) {
		int temp = a[low];
		while (low < high) {
			while (low < high && a[high] >= temp) {
				high--;
			}
			a[low] = a[high];
			// System.out.println("第一次交换的值：");
			// StaticMethod.print(a);
			while (low < high && a[low] <= temp) {
				low++;
			}
			a[high] = a[low];
			// System.out.println("第一次交换的值：");
			StaticMethod.print(a);
		}
		a[low] = temp;
		// System.out.println("第一次执行完毕的值：");
		// StaticMethod.print(a);
		return low;
	}

	public void quick(int a[]) {
		if (a.length > 0) {
			quickSort(a, 0, a.length - 1);
		}
	}

	/**
	 * 
	 * @description:一元选择排序
	 *@param arry
	 *void
	 *@author d1m
	 */
	public static void selectSort(int[] arry) {
		for (int i = 0; i < arry.length-1; i++) {
			int min = i;
			for (int j = i + 1; j < arry.length; j++) {
				if (arry[j] < arry[min])
					min = j;
			}
			int tmp = arry[i];
			arry[i] = arry[min];
			arry[min] = tmp;
		}
	}
	/**
	 * 
	 * @description:二元选择排序
	 *@param arry
	 *void
	 *@author d1m
	 */
	public static void selectSortTwo(int[] arry) {
		int len = arry.length;
		int tmp;
		for (int i = 0; i <= len/2; i++) {
			int min = i;
			int max=i;
			for (int j = i + 1; j <len-i; j++) {
				if (arry[j]>arry[max]) 
					max=j;	
				if (arry[j] < arry[min])
					min = j;
			}
			tmp = arry[i];
			arry[i] = arry[min];
			arry[min] = tmp;
			
			tmp = arry[len-i-1];
			arry[len-i-1]=arry[max];
			arry[max]=tmp;
		}
	}

	/**
	 * 插入排序（交换数据）
	 */
	public  static void insertSortChange(int[] arry) {
		for (int i = 1; i < arry.length; i++) {
			for (int j = i ; j > 0; j--) {
				if (arry[j] < arry[j - 1]) {
					int tmp = arry[j];
					arry[j] = arry[j - 1];
					arry[j - 1] = tmp;
				}
			}
		}
	}
/**
 * 
 * @description:插入排序（平移数据）
 *@param arry
 *void
 *@author d1m
 */
	public static void insertSortMove(int[] arry) {
		for (int i = 1; i < arry.length; i++) {
			int tmp = arry[i];
			int j = i - 1;
			while (j >= 0 && arry[j] > tmp) {
				arry[j +1] = arry[j];
				j--;
			}
			arry[j+1] = tmp;
		}
	}
	/**
	 * 
	 * @description:希尔排序 
	 *@param arry
	 *void
	 *@author d1m
	 */
	public static void shellSort(int[] arry) {
		int len = arry.length;
		int d=1;
		while (d<len/2) d=2*d+1;
		while (d>=1) {
			for (int i = d; i < len; i++) {
				for (int j = i; j >=d&&(arry[j]<arry[j-d]); j-=d) {
					int tmp = arry[j-d];
					arry[j-d]=arry[j];
					arry[j]=tmp;
				}
			}
			d/=2;
		}
	}
}
