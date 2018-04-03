package com.hal.thread.threads.article.second;

/**
 * 
 * @author D1M
 *  A线程先持有object对象的lock锁，B线程可以异步方式的调用object对象中的非synchronized类型的方法。
 *  A线程先持有object对象的lock锁，B线程如果同事调用object对象中的synchronized类型的方法，
 *  则需要等待A线程执行完并释放lock锁（同步）。
 */
public class SynMain {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		SynObject object = new SynObject();
		SynThreadA threadA = new SynThreadA(object);
		threadA.setName("ThreadA");
		SynThreadB threadB = new SynThreadB(object);
		threadB.setName("ThreadB");
		threadA.start();
		threadB.start();
		
	}

}
