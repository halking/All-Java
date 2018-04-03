package com.hal.concurrent.semaphore;

import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import com.hal.thread.ThreadService;

public class SemaphoreService implements ThreadService {
	private static final int LEN = 5;
	private Semaphore semaphore = new Semaphore(5);
	private Semaphore producer = new Semaphore(10);
	private Semaphore consumer = new Semaphore(10);
	private Lock lock = new ReentrantLock();
	private Condition proCondition = lock.newCondition();
	private Condition conCondition = lock.newCondition();
	private Object[] items = new Object[10];
	
	public void parallelProcess() {
		try {
			semaphore.acquire();
			System.out.println(Thread.currentThread().getName() + "-readying-"+System.currentTimeMillis());
			for (int i = 0; i < 5; i++) {
				System.out.println(Thread.currentThread().getName() + "-打印-" +i);
			}
//			Thread.sleep(1000);
			semaphore.release();
			System.out.println(Thread.currentThread().getName() + "-end-"+System.currentTimeMillis());
		} catch (InterruptedException e) {
			// TODO: handle exception
		}
	}
	public void serialProcess() {
		try {
			semaphore.acquire();
			System.out.println(Thread.currentThread().getName() + "-readying-"+System.currentTimeMillis());
			try {
				lock.lock();
				System.out.println(Thread.currentThread().getName() + "-开始打印-"+System.currentTimeMillis());
				for (int i = 0; i < 1000; i++) {
					System.out.println(Thread.currentThread().getName() + "-打印-" + i);
				}
				System.out.println(Thread.currentThread().getName() + "-结束打印-"+System.currentTimeMillis());
				semaphore.release();
				System.out.println(Thread.currentThread().getName() + "-end-"+System.currentTimeMillis());
			} finally {
				lock.unlock();
			}
		} catch (InterruptedException e) {
			// TODO: handle exception
		}
	}
	
	private boolean isFull(){
		boolean isFull =true;
		for (int i = 0; i < items.length; i++) {
			if (items[i]==null) {
				isFull=false;
				break;
			}
		}
		if (isFull) {
			return true;
		}else {
			return false;
		}
	}
	private boolean isEmpty(){
		boolean isEmpty = true;
		for (int i = 0; i < items.length; i++) {
			if (items[i]!=null) {
				isEmpty=false;
				break;
			}
		}
		if (isEmpty) {
			return true;
		}else {
			return false;
		}
	}
	
	public  void produceItem() {
		try {
			producer.acquire();
			lock.lock();
			while (isFull()) {
				proCondition.await();
			}
			for (int i = 0; i < items.length; i++) {
				if (items[i] == null) {
					items[i] = "produce-"+i;
					System.out.println(Thread.currentThread().getName()+"-produce-"+items[i]);
					break;
				}
			}
			conCondition.signalAll();
			lock.unlock();
		} catch (InterruptedException e) {
			// TODO: handle exception
		}finally {
			producer.release();
		}
	}
	public void consumeItem() {
		try {
			consumer.acquire();
			lock.lock();
			while (isEmpty()) {
				conCondition.await();
			}
			for (int i = 0; i < items.length; i++) {
				if (items[i]!=null) {
					System.out.println(Thread.currentThread().getName()+"-consume-"+items[i]);
					items[i] = null;
					break;
				}
			}
			proCondition.signalAll();
			lock.unlock();
		} catch (InterruptedException e) {
			// TODO: handle exception
		}
		finally {
			consumer.release();
		}
	}
}
