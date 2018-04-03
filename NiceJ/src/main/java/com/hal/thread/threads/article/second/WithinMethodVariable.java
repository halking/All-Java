package com.hal.thread.threads.article.second;

public class WithinMethodVariable {
	
	private int num = 0;

	synchronized public void add(String name) {
		try {
			if (name.equals("huang")) {
				num = 100;
				Thread.sleep(2000);
				System.out.println("huang set over!");
			} else {
				num = 200;
				System.out.println("b set over!");
			}
			System.out.println(name + " num=" + num);
		} catch (InterruptedException e) {
			// TODO: handle exception
		}
	}
}
