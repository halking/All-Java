package com.hal.thread.threads.article.second.article3;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PrintString implements  Runnable{
	
	public static final Logger logger = LoggerFactory.getLogger(PrintString.class);
	
	private volatile boolean isPrint = true;

	public boolean isPrint() {
		return isPrint;
	}

	public void setPrint(boolean isPrint) {
		this.isPrint = isPrint;
	}
	
	public void printStringMethod() {
		try {
//			System.out.println(Thread.currentThread().getName()+" is running printStringMethod");
			while (isPrint) {
				System.out.println(Thread.currentThread().getName()+" is running printStringMethod");
//				logger.info("stopping it now! stop thread >>>"+Thread.currentThread().getName());
				Thread.sleep(1000);
			}
//			logger.info(Thread.currentThread().getName()+" is stopping");
		} catch (InterruptedException e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}
	public void run() {
		printStringMethod();
	}
}
