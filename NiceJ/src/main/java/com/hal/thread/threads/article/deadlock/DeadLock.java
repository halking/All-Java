package com.hal.thread.threads.article.deadlock;

public class DeadLock implements Runnable {
	private String name;
	private Object obj1 = new Object();
	private Object obj2 = new Object();

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		if (name.equals("lock1")) {
			synchronized (obj1) {
				try {
					System.out.println("name1=" + name);
					Thread.sleep(3000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				synchronized (obj2) {
					System.out.println("it is running lock2  in lock sequence!");
				}
			}
		}
		if (name.equals("lock2")) {
			synchronized (obj2) {
				try {
					System.out.println("name2=" + name);
					Thread.sleep(3000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				synchronized (obj1) {
					System.out.println("it is running lock1  in lock sequence!");
				}
			}
		}
	}

}
