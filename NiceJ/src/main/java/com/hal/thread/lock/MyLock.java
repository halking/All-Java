package com.hal.thread.lock;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.slf4j.Logger;

import com.hal.utils.LogUtil;

public class MyLock {
	public static final Logger logger = LogUtil.initLog(MyLock.class);
	private final Lock lock = new ReentrantLock();
	private final Condition full = lock.newCondition();
	private final Condition empty = lock.newCondition();
	final Object[] items = new Object[100];
	int  take, put, count;
	public MyLock() {
		// TODO Auto-generated constructor stub
	}

	public Object take() {
		Object object = null;
		lock.lock();
		try {
			while (count == 0)
				empty.await();
			object = items[take];
			if (logger.isDebugEnabled()) 
				logger.info("take a item----"+object);
			if (++take == items.length)
				take = 0;
			--count;
			full.signal();
			return object;
		} catch (InterruptedException e) {
			// TODO: handle exception
		} finally {
			lock.unlock();
		}
		return object;
	}

	public void put(Object object) {
		lock.lock();
		try {
			while (count == items.length)
				full.await();
			items[put] = object;
			if (logger.isDebugEnabled()) 
				logger.info("put a item-----"+items[put]);
			if (++put == items.length)
				put = 0;
			++count;
			empty.signal();
		} catch (InterruptedException e) {
			// TODO: handle exception
		} finally {
			lock.unlock();
		}
	}

	public void TestLock() {
		try {
			lock.lock();
			for (int i = 0; i < 5; i++) {
				System.out.println(Thread.currentThread().getName() + "----\t" + (i + 1));
				Thread.sleep(1000);
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		} finally {
			lock.unlock();
		}
	}

	public void NormalMethod() {
		for (int i = 0; i < 5; i++) {
			System.out.println(Thread.currentThread().getName() + "----\t" + (i + 1));
		}
	}
}
