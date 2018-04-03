package com.hal.algorithm.sort;

public class Shell{
	
			private static <T extends Comparable<? super T>> void ShellSort(T[] a) {
				int j;
				for (int d = a.length/2; d > 0; d/=2) {
					for (int i = d; i < a.length; i++) {
						T tmp = a[i];
						for (j = 0; j >=d && tmp.compareTo(a[j-d])<0; j-=d) {
							a[j] = a[j-d];
						}
						a[j] =tmp;
					}
				}
			}
			public static void main(String[] args) {
			}
}
