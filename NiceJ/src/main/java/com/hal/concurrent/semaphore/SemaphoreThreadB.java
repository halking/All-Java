package com.hal.concurrent.semaphore;

import com.hal.thread.ThreadService;

public class SemaphoreThreadB extends Thread {
	private SemaphoreService service;
	
	public SemaphoreThreadB(ThreadService service) {
		super();
		this.service=(SemaphoreService) service;
	}
	
	@Override
	public void run() {
//		service.serialProcess();
		service.consumeItem();
	}
}
