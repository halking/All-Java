/**
 * @date:2017年1月18日
 * @author:D1M
 */
package com.hal.base;

import org.apache.activemq.filter.function.splitFunction;

import com.hal.thread.threads.article.first.ThreeThread;


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
		// TODO Auto-generated method stub
	/*	MyThread A = new MyThread("A");
		MyThread B = new MyThread("B");
		MyThread C = new MyThread("C");
		A.start();
		B.start();
		C.start();*/
/*		SecondThread secondThread = new SecondThread();
		Thread A = new Thread(secondThread,"A");
		Thread B = new Thread(secondThread,"B");
		Thread C = new Thread(secondThread,"C");
		Thread D = new Thread(secondThread,"D");
		Thread E = new Thread(secondThread,"E");
		A.start();
		B.start();
		C.start();
		D.start();
		E.start();*/
		ThreeThread secondThread = new ThreeThread();
		Thread A = new Thread(secondThread,"A");
		Thread B = new Thread(secondThread,"B");
		Thread C = new Thread(secondThread,"C");
		Thread D = new Thread(secondThread,"D");
		Thread E = new Thread(secondThread,"E");
		A.start();
		B.start();
		C.start();
		D.start();
		E.start();
	}

}
