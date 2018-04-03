package com.hal.thread.threads.article.second.article3;

public class VolatileThread extends Thread {
		public volatile static int count;
		private synchronized static void addCount() {
			for (int i = 0; i < 100; i++) {
				count++;
			}
			System.out.println("count="+count);
		}
		@Override
		public void run() {
		// TODO Auto-generated method stub
			addCount();
		}
}
