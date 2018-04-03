/**
 * 
 */
package com.hal.thread.assume;

/**
 * @author hal
 *
 */
public class YieldTest {

	/**
	 *2016年4月15日
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
     Thread t1 = new MyThread01(false);
     Thread t2 = new MyThread01(true);
     Thread t3 = new MyThread01(false);
     t1.start();
     t2.start();
     t3.start();
	}

}
