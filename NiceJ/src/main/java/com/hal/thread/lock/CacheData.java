package com.hal.thread.lock;

import java.util.concurrent.locks.ReentrantReadWriteLock;

public class CacheData {
	Object data;
	volatile boolean cacheValid;
	ReentrantReadWriteLock rwLock = new ReentrantReadWriteLock();
	public CacheData() {
		// TODO Auto-generated constructor stub
	}
	public CacheData(boolean flag) {
		this.cacheValid = flag;
	}
	public void processData(Object data) {
		rwLock.readLock().lock();
		if (!cacheValid) {
			rwLock.readLock().unlock();
			rwLock.writeLock().lock();
			try {
				if (!cacheValid) {
					this.data = data;
					cacheValid = true;
				}
				rwLock.readLock().lock();
			} finally {
				rwLock.writeLock().unlock();
			}
		}
		try {
			System.out.println(Thread.currentThread().getName() + "----" + this.data+"---"+System.currentTimeMillis());
			cacheValid=false;
		} finally {
			rwLock.readLock().unlock();
		}
	}
}
