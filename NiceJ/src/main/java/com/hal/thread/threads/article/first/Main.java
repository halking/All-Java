package com.hal.thread.threads.article.first;

public class Main {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
/*		FourThread thread = new FourThread();
		Thread thread2 = new Thread(thread,"steven");
		System.out.println("thread2 is alive:"+thread2.isAlive());
		thread2.setName("huang");
		thread2.start();
		System.out.println("main end,thread2 is alive:"+thread2.isAlive());
		thread.run();
*/		
		// sleep test
		/*SleepThread sleep = new SleepThread();
		System.out.println("start time:"+System.currentTimeMillis());
		sleep.run();
		System.out.println("end time:"+System.currentTimeMillis());*/
		
		//interrupted test
		try {
			
			InterruptedThread inte =  new InterruptedThread();
			inte.start();
			Thread.sleep(2000);
			inte.interrupt();
//			System.out.println(Thread.currentThread().getName()+" was interrupted "+Thread.currentThread().interrupted());
			System.out.println(inte.getName()+" was intrrrupted "+inte.isInterrupted());
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
