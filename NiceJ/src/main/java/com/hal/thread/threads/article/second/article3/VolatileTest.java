package com.hal.thread.threads.article.second.article3;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class VolatileTest {
	
	public static final Logger logger = LoggerFactory.getLogger(VolatileTest.class);
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
/*		PrintString printString = new PrintString();
		new Thread(printString).start();
//		printString.printStringMethod();
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		printString.setPrint(false);
		logger.info("stopping it now! stop thread >>>"+Thread.currentThread().getName());
	}*/
		VolatileThread[] threadArr = new VolatileThread[100];
		for (int i = 0; i < 100; i++) {
			threadArr[i] = new VolatileThread();
		}
		for (int i = 0; i < 100; i++) {
			threadArr[i].start();
		}
	}

}
