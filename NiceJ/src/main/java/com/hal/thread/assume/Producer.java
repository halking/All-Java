/**
 * 
 */
package com.hal.thread.assume;

/**
 * @author hal
 *
 */
public class Producer implements Runnable {
	Product product;
	/**
	 * 
	 */
	public Producer(Product product) {
		// TODO Auto-generated constructor stub
		this.product = product;
		new Thread(this,"Producer").start();
	}

	/* 
	 *2016年4月15日
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run() {
		// TODO Auto-generated method stub
		for (int i = 0; i < 5; i++) {
			product.put(i);
		}
	}

}
