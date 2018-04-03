package com.hal.thread.threads.article.three.article1;

public class ListThreadB extends Thread {
	private Object lock;

	public ListThreadB(Object lock) {
		// TODO Auto-generated constructor stub
		super();
		this.lock = lock;
	}
	@Override
	public void run() {
		// TODO Auto-generated method stub
		try {
			synchronized (lock) {
				for (int i = 0; i < 10; i++) {
					MyList.add();
					if (MyList.size()==5) {
						lock.notify();
						System.out.println("notified thread is wating to get lock!");
					}
					System.out.println("add "+(i+1)+" element");
					Thread.sleep(1000);
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
}
