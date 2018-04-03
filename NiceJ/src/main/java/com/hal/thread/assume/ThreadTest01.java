/**
 * 
 */
package com.hal.thread.assume;

/**
 * @author hal
 *
 */
public class ThreadTest01  extends Thread{
      public static void main(String[] args) {
		 ThreadTest01 test01 = new ThreadTest01();
		 test01.start();
		 try {
			for (int i = 0; i < 5; i++) {
				 Thread.sleep(1000);
				 System.out.println("main"+i);
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
	 }
 	 public void run(){
		 try {
			for (int i = 0; i < 5; i++) {
				 Thread.sleep(1000);
				 System.out.println("Run:"+i);
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
}
