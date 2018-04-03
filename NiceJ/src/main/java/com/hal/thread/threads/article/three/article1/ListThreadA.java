package com.hal.thread.threads.article.three.article1;

public class ListThreadA extends Thread {
	private Object lock;

	public ListThreadA(Object lock) {
		// TODO Auto-generated constructor stub
		super();
		this.lock = lock;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		try {
			synchronized (lock) {
				if (MyList.size() != 5) {
					System.out.println("wait begin :" + System.currentTimeMillis());
					lock.wait();
					System.out.println("wait end :" + System.currentTimeMillis());
				}
			}
		} catch (InterruptedException e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}
}
