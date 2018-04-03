package com.hal.concurrent.pool;

public class CachePoolRunnable implements Runnable {
	
	private String username;
	
	public CachePoolRunnable(String username) {
		// TODO Auto-generated constructor stub
		super();
		this.username=username;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		try {
			System.out.println(Thread.currentThread().getName()+" \t username="+username+"--start--"+System.currentTimeMillis());
			Thread.sleep(1000);
			System.out.println(Thread.currentThread().getName()+" \t username="+username+"--end---"+System.currentTimeMillis());
		} catch (InterruptedException e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}

}
