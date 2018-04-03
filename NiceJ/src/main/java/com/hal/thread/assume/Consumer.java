/**
 * 
 */
package com.hal.thread.assume;

/**
 * @author hal
 *
 */
public class Consumer implements Runnable {
   Product  product;
   /**
 * 
 */
public Consumer(Product product) {
	// TODO Auto-generated constructor stub
	this.product = product;
	new Thread(this,"Consumer").start();
}
	/* 
	 *2016年4月15日
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run() {
		// TODO Auto-generated method stub
    for (int i = 0; i < 5; i++) {
		product.get();
	}
	}

}
