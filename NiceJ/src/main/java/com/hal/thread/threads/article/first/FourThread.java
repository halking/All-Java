package com.hal.thread.threads.article.first;

public class FourThread extends Thread{
		
	public FourThread() {
		// TODO Auto-generated constructor stub
		System.out.println(Thread.currentThread().getName()+" invoke the "+this.getClass().getConstructors()[0]);
		System.out.println("current thread isAlive():"+Thread.currentThread().isAlive());
		System.out.println("thread getName():"+this.getName());
		System.out.println("current thread isAlive():"+this.isAlive());
	}
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		super.run();
		System.out.println(Thread.currentThread().getName()+" invoke the method run()");
		System.out.println("current thread isAlive():"+Thread.currentThread().isAlive());
		System.out.println("thread getName():"+this.getName());
		System.out.println("current thread isAlive():"+this.isAlive());
	}
}
