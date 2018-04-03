package com.hal.concurrent.semaphore;

import java.util.Iterator;

import com.hal.common.utils.Global;
import com.hal.thread.ThreadService;

public class SemaphoreMain {
	private 	static ThreadService service = new SemaphoreService();
	public static void main(String[] args) {
//		parallel();
//		serial();
		producerAndConsume();
	}
	public static void parallel() {
		SemaphoreThreadA[] threadA = new SemaphoreThreadA[10];
		for (int i = 0; i <Global.TEN_COUNT; i++) {
			threadA[i] = new SemaphoreThreadA(service);
			threadA[i].start();
		}
	}
	public static void serial() {
		SemaphoreThreadB[] threadB = new  SemaphoreThreadB[10];
		for (int i = 0; i <Global.FIVE_COUNT; i++) {
			threadB[i] = new SemaphoreThreadB(service);
			threadB[i].start();
		}
	}
	public static void producerAndConsume() {
		SemaphoreThreadA[] producer= new SemaphoreThreadA[60];
		SemaphoreThreadB[] consumer= new SemaphoreThreadB[60];
		for (int i = 0; i < consumer.length; i++) {
			producer[i] = new SemaphoreThreadA(service);
			consumer[i] = new SemaphoreThreadB(service);
		}
		try {
			Thread.sleep(2000);
			for (int i = 0; i < consumer.length; i++) {
				producer[i].start();
				consumer[i].start();
			}
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
