package com.hal.thread.threads.local.inheritable;

public class InheriMain {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		try {
			for (int i = 0; i < 10; i++) {
				System.out.println("在"+Thread.currentThread().getName()+"取得："+Tools.in.get());
				Thread.sleep(1000);
			}
			InheriThread inheriThread = new InheriThread();
			inheriThread.start();
		} catch (InterruptedException e) {
			// TODO: handle exception
		}
	}

}
