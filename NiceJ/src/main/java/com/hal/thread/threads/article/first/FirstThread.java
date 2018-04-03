package com.hal.thread.threads.article.first;

public class FirstThread extends Thread {
		private int count =5;
		
		public FirstThread(String name) {
			super(name);
		}
		@Override
		public void run() {
		// TODO Auto-generated method stub
			super.run();
			while (count>0) {
				 count--;
				 System.out.println(this.currentThread().getName()+" calculated by count="+count);
			}
		}
}
