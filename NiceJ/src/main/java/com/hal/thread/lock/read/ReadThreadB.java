package com.hal.thread.lock.read;

import com.hal.thread.lock.ReadWriteService;

public class ReadThreadB extends Thread {
	private ReadWriteService service;

	public ReadThreadB(ReadWriteService service) {
		super();
		this.service = service;
	}

	@Override
	public void run() {
		service.read();
	}
}
