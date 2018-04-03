package com.hal.thread.lock;

import java.util.concurrent.locks.ReentrantReadWriteLock;

public class ReadWriteService {
	
	private ReentrantReadWriteLock readLock = new ReentrantReadWriteLock();
	private ReentrantReadWriteLock writeLock = new ReentrantReadWriteLock();

	public void read() {
		try {
			try {
				readLock.readLock().lock();
				System.out.println(readLock.getReadHoldCount());
				System.out.println(Thread.currentThread().getName() + "--获得读锁--"+System.currentTimeMillis());
				Thread.sleep(1000);
			} finally {
					readLock.readLock().unlock();
			}
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public void write() {
		try {
			try {
				writeLock.writeLock().lock();
				System.out.println(Thread.currentThread().getName() + "--获得写锁--"+System.currentTimeMillis());
				Thread.sleep(1000);
			} finally {
					writeLock.writeLock().unlock();
			}
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
