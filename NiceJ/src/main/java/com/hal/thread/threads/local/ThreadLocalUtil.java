package com.hal.thread.threads.local;

import java.util.concurrent.atomic.AtomicInteger;

public class ThreadLocalUtil {
	
	 private static final int HASH_INCREMENT = 0x61c88647;
	 
	public static ThreadLocal<String> local = new ThreadLocal<String>();

	public static ThreadLocal<String> getLocal() {
		return local;
	}

	public static void setLocal(ThreadLocal<String> local) {
		ThreadLocalUtil.local = local;
	}
	public static void main(String[] args) {
		AtomicInteger aInteger = new AtomicInteger();
		int Atohash = aInteger.getAndAdd(HASH_INCREMENT);
		int intilSize = Atohash & 15;
		System.out.println("atohash:"+Atohash+"___intiSize:"+intilSize+"_sig:"+10/4);
		String[] arr = new String[16];
		for (int i = 0; i < arr.length; i++) {
			arr[i] = "arr_"+i;
		}
		arr[5]=null;
		int size = arr.length;
		size--;
		for (int i = 0; i < arr.length; i++) {
			System.out.println(arr[i]);
		}
	}
}
