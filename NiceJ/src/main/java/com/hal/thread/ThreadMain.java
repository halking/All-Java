/**
 * @date:2017年1月18日
 * @author:D1M
 */
package com.hal.thread;


/**
 * @author D1M
 *
 */
public class ThreadMain {

	/**
	 * @description:
	 *@param args
	 *void
	 *@author D1M
	 */
	public static void main(String[] args) {
//		Thread thread = new Thread(new MyThread("mythread"),"mythread");
		MyThread thread = new MyThread("mythread");
		thread.start();
		System.out.println(Thread.currentThread().getName()+" thread is running!" );
	}
	
}
