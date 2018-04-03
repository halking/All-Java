package com.hal.thread.threads.article.second;

public class SynService {
	private String name;
	private String pwd;
	private String any = new String();
	
	synchronized public void doAntherthing() {
		System.out.println("Thread "+Thread.currentThread().getName()+" is doing "+"anther something");
	}
	
	public void doSomething(String name,String pwd) {
		try {
//			String any = new String();
			synchronized (any) {
				System.out.println("Thread "+Thread.currentThread().getName()+" enter into "+"syn");
				this.name =name;
				Thread.sleep(3000);
				this.pwd = pwd;
				System.out.println("Thread "+Thread.currentThread().getName()+" leave"+" syn!"
				+" [ name="+this.name+",pwd="+this.pwd+"]");
			}
		} catch (InterruptedException e) {
			// TODO: handle exception
		}
	}

}
