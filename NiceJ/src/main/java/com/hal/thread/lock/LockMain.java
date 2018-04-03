package com.hal.thread.lock;

public class LockMain {

	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		MyLock lock = new MyLock();
		System.out.println("--------start main-----");
		LockThreadA threadA = new LockThreadA(lock);
		LockThreadB threadB = new LockThreadB(lock);
		threadA.start();
		Thread.sleep(1000);
		threadB.start();
	}

}
