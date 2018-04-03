/**
 *
 */
package com.hal.algorithm.sort;

/**
 * @author hal
 */
public class BubbleSort {


    public static void main(String[] args) {
        // TODO Auto-generated method stub
        BubbleSort bSort = new BubbleSort();
        System.out.println("排序前的数组:");
        StaticMethod.print(StaticMethod.a);
        bSort.bublesort(StaticMethod.a);
        System.out.println("\n排序后的数组:");
        StaticMethod.print(StaticMethod.a);
    }

    public void bublesort(int a[]) {
        for (int i = 0; i < a.length; i++) {
//			System.out.println("a["+i+"]="+a[i]);
            for (int j = i + 1; j < a.length; j++) {
//				System.out.println("a[i]"+a[i]);
				System.out.println("a[j]="+a[j]);
                if (a[i] > a[j]) {
                    StaticMethod.swap(a, i, j);
                }
            }
        }
    }
}
