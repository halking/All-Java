package com.hal.thread.threads.article.second;

public class SynObject {
		synchronized public void methodA(){
			System.out.println("begin methodA,threadName="+Thread.currentThread().getName());
			try {
				Thread.sleep(5000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			System.out.println("end MethodA!");
		}
		public void methodB() {
			System.out.println("begin methodB,threadName="+Thread.currentThread().getName());
			try {
				Thread.sleep(5000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			System.out.println("end MethodB!");
		}
}
