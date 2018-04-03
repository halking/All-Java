package com.hal.thread.lock;

public class LockThreadA extends Thread {
	private MyLock myLock;

	public LockThreadA(MyLock myLock) {
		this.myLock = myLock;
	}
	@Override
	public void run() {
//		myLock.TestLock();
		for (int i = 0; i < 100; i++) {
			myLock.take();
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
