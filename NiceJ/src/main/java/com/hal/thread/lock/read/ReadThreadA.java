package com.hal.thread.lock.read;

import com.hal.thread.lock.ReadWriteService;

public class ReadThreadA extends Thread {
	private ReadWriteService service;

	public ReadThreadA(ReadWriteService service) {
		super();
		this.service = service;
	}

	@Override
	public void run() {
		super.run();
		service.read();
	}
}
