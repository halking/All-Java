package com.hal.thread.lock;

public class LockThreadB extends Thread {
	private MyLock myLock;

	public LockThreadB(MyLock myLock) {
		this.myLock = myLock;
	}
	@Override
	public void run() {
//		myLock.TestLock();
		for (int i = 0; i < 100; i++) {
			myLock.put("put-"+i);;
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
