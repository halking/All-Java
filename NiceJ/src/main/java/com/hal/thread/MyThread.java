package com.hal.thread;

public class MyThread extends Thread {
	private String some;
	public MyThread(String some) {
		this.some = some;
	}
	@Override
	public void run() {
		System.out.println(Thread.currentThread().getName()+" is running!"+"--"+getSome());
	}
	public String getSome() {
		return some;
	}
	public void setSome(String some) {
		this.some = some;
	}
}
