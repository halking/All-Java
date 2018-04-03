package com.hal.thread;

public class Consume implements Runnable {
	public Storage storage;
	public int num;

	public Consume(Storage storage, int n) {
		// TODO Auto-generated constructor stub
		this.storage = storage;
		num = n;
	}

	public void run() {
		// TODO Auto-generated method stub
		storage.consume(num);
	}
}
