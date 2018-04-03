package com.hal.thread.threads.article.first;

public class DaemonMain {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		DaemonThread daemon = new DaemonThread();
		daemon.setDaemon(true);
		daemon.start();
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("when it is destroied, the thread of daemon stop!");
	}

}
