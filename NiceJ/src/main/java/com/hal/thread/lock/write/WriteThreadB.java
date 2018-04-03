package com.hal.thread.lock.write;

import com.hal.thread.lock.ReadWriteService;

public class WriteThreadB extends Thread {
	private ReadWriteService service;

	public WriteThreadB(ReadWriteService service) {
		super();
		this.service = service;
	}

	@Override
	public void run() {
		service.write();
	}
}
