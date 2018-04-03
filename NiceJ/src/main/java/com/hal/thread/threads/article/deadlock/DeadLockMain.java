package com.hal.thread.threads.article.deadlock;

public class DeadLockMain {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		try {
			DeadLock lock = new DeadLock();
			lock.setName("lock1");
			Thread thread1 = new Thread(lock);
			thread1.start();
			Thread.sleep(100);
			lock.setName("lock2");
			Thread thread2 = new Thread(lock);
			thread2.start();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}
