/**
 * 
 */
package com.hal.thread.assume;

/**
 * @author hal
 *
 */
public class Product {
     int n;
     boolean     valueset = false;
     synchronized void get (){
    	 if (!valueset) {
			try {
				wait();
			} catch (Exception e) {
				// TODO: handle exception
			}
		}
    	 System.out.println(Thread.currentThread().getName()+"-get:"+n);
    	 valueset =false;
    	 notify();
     }
     synchronized void put(int n){
    	 if (valueset) {
			try {
				wait();
			} catch (Exception e) {
				// TODO: handle exception
			}
		}
    		this.n =n;
			valueset = true;
			System.out.println(Thread.currentThread().getName()+"-put:"+n);
			notify();
     }
}
