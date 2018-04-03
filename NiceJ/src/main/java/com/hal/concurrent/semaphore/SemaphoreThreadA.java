package com.hal.concurrent.semaphore;

import com.hal.thread.ThreadService;

public class SemaphoreThreadA extends Thread {
	private SemaphoreService service;
	
	public SemaphoreThreadA(ThreadService service) {
		super();
		this.service=(SemaphoreService) service;
	}
	
	@Override
	public void run() {
//		service.parallelProcess();
		service.produceItem();
	}
}
