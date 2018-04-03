package com.hal.thread.threads.local.inheritable;

public class InheriThread extends Thread {
	@Override
	public void run() {
		// TODO Auto-generated method stub
		super.run();
		try {
			for (int i = 0; i < 10; i++) {
				System.out.println("在" + Thread.currentThread().getName() + "取得 ：" + Tools.in.get());
				Thread.sleep(1000);
			}
		} catch (InterruptedException e) {
			// TODO: handle exception
		}
	}
}
