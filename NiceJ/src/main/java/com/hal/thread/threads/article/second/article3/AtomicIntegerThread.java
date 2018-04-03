package com.hal.thread.threads.article.second.article3;

import java.util.concurrent.atomic.AtomicInteger;

public class AtomicIntegerThread extends Thread {
	private  AtomicInteger atomic = new AtomicInteger(0);
			@Override
			public void run() {
				for (int i = 0; i < 10000; i++) {
					System.out.println(atomic.incrementAndGet());
				}
			}
			
}
