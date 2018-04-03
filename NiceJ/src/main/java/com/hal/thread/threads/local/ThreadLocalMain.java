package com.hal.thread.threads.local;

public class ThreadLocalMain {
	
	public static ThreadLocal<String> local = new ThreadLocal<String>();
	public static void main(String[] args) {
	/*	if (local.get()==null) {
			System.out.println("local is null now!");
			local.set("local");
		}
		System.out.println(local.get());
		System.out.println(local.get());*/
		
		try {
			localA localA = new localA();
			LocalB localB = new LocalB();
			localA.start();
			localB.start();
			for (int i = 1; i <=100; i++) {
				ThreadLocalUtil.local.set("Main_"+i);
				System.out.println("Main get Value"+ThreadLocalUtil.local.get());
				Thread.sleep(200);
			}
		} catch (InterruptedException e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}
	
}
