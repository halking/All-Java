package com.hal.thread.threads.article.first;

public class ThreeThread extends Thread {
	private int count =5;
	@Override
	 public void run() {
	// TODO Auto-generated method stub
		super.run();
			 System.out.println(this.currentThread().getName()+" calculated by count="+(count--));
		}
}
