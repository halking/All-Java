package com.hal.thread.threads.article.first;

public class SusResObject extends Thread{
	private int i =0;
	synchronized public void printString(){
		System.out.println("start");
		if (Thread.currentThread().getName().equals("a")) {
			System.out.println("a thread is suspended forever");
			Thread.currentThread().suspend();
		}
		System.out.println("end!");
	} 
	@Override
	public void run() {
		// TODO Auto-generated method stub
		while(true){
			i++;
			System.out.println(i++);
			
		}
	}
}
