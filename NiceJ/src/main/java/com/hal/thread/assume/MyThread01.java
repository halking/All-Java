/**
 * 
 */
package com.hal.thread.assume;

import java.util.Date;

/**
 * @author hal
 *
 */
public class MyThread01 extends Thread{
     private  boolean flag;
     /**
	 * 
	 */
	public MyThread01(boolean flag) {
		// TODO Auto-generated constructor stub
		this.flag = flag;
	}
	public void setFlag(boolean flag) {
		this.flag = flag;
	}
	/* 
	 *2016年4月15日
	 * @see java.lang.Thread#run()
	 */
	@Override
	public void run() {
		// TODO Auto-generated method stub
		long start = new Date().getTime();
		for (int i = 0; i < 10000; i++) {
			if (flag) 
				   Thread.yield();
				   for (int j = 0; j <3000; j++) {
					;
				}
		}
		long end = new Date().getTime();
		System.out.println("\n"+this.getName()+"执行时间："+(end-start)+"毫秒");
	}
}
