package com.hal.thread.threads.article.first;

public class SleepThread extends Thread {
		@Override
		public void run() {
			// TODO Auto-generated method stub
			System.out.println("strating thread "+this.currentThread().getName());
			try {
				Thread.sleep(2000);
				System.out.println("ending thread "+this.currentThread().getName());
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
}	
