/**
 * 
 */
package com.hal.thread.assume;

/**
 * @author hal
 *
 */
public class WaitDemo {

	/**
	 *2016年4月15日
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
     Product product = new Product();
     Producer producer = new Producer(product);
     Consumer consumer = new Consumer(product);
	}

}
