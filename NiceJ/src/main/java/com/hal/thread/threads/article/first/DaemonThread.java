package com.hal.thread.threads.article.first;

public class DaemonThread extends Thread {
	private int i=0;
	@Override
	public void run() {
		// TODO Auto-generated method stub
		while(true){
			i++;
			System.out.println("i="+i);
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
