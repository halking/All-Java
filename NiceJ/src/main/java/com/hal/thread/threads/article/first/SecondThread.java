package com.hal.thread.threads.article.first;

public class SecondThread extends Thread {
	private int count =5;
	public SecondThread() {
		// TODO Auto-generated constructor stub
	}
	public  SecondThread(String name) {
		super(name);
	}
	@Override
	synchronized public void run() {
	// TODO Auto-generated method stub
		super.run();
			 count--;
			 System.out.println(this.currentThread().getName()+" calculated by count="+count);
		}
}
