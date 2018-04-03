package com.hal.thread.lock.write;

import com.hal.thread.lock.ReadWriteService;

public class WriteThreadA extends Thread {
	private ReadWriteService service;

	public WriteThreadA(ReadWriteService service) {
		super();
		this.service = service;
	}

	@Override
	public void run() {
		super.run();
			service.write();
	}
}
