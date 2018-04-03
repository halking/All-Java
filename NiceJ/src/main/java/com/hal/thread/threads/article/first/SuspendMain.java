package com.hal.thread.threads.article.first;

public class SuspendMain {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		try {
/*			final SusResObject object = new SusResObject();
			Thread thread1 = new Thread() {
				public void run() {
					object.printString();
				}
			};
			thread1.setName("a");
			thread1.start();
			Thread.sleep(2000);
			
			Thread thread2 = new Thread(){
				public void run() {
					System.out.println("thread started, but it don't come into printstring() mehod");
					System.out.println("it's reason for a thread locked it forever.");
					object.printString();
				}
			};
			thread2.start();*/
			
			SusResObject object2 = new SusResObject();
			object2.start();
			Thread.sleep(2000);
			object2.suspend();
			System.out.println("end main");
		} catch (InterruptedException e) {
			// TODO: handle exception
		}

	}

}
