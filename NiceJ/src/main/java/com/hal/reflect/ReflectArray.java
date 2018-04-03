package com.hal.reflect;

import java.lang.reflect.Array;

public class ReflectArray {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		int[] arr = (int[])intitalArray();
		print(arr);
		int[] newArr =(int[]) changeArrayLength(arr, 10);
		print(newArr);
	}
	private static Object intitalArray(){
		int[] arr = {1,2,3,4,5};
		return arr;
	}
	private static Object changeArrayLength(Object object,int len){
		Class<?> clazz = object.getClass();
		System.out.println(clazz);
		Object newArr = Array.newInstance(clazz, len);
		int newlen = Array.getLength(object);
		System.arraycopy(object, 0, newArr,5, newlen);
		return newArr;
	}
	private static void print(Object object){
		Class<?> clazz = object.getClass();
		if (!clazz.isArray()) {
			return;
		}
		int len =Array.getLength(object) ;
		System.out.println("array length: "+len);
		for (int i = 0; i < len; i++) {
			System.out.println(Array.get(object, i));
		}
	}
}
