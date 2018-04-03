package com.hal.thread.threads.local;

public class localA extends Thread {
	@Override
	public void run() {
		
			try {
				for (int i = 0; i < 100; i++) {
					ThreadLocalUtil.local.set("localA_"+i);
					System.out.println("localA get Value:"+ThreadLocalUtil.local.get());
					Thread.sleep(200);
				}
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
}
