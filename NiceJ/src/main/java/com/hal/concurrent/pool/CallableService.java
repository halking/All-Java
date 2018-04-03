package com.hal.concurrent.pool;

import java.util.concurrent.Callable;

public class CallableService<T> implements Callable<T> {
	private int age;
	public CallableService(int age) {
		super();
		this.age=age;
	}
	@Override
	public T call() throws Exception {
		if (age ==1) {
//			throw new  Exception(Thread.currentThread().getName()+"线程中断");
		}
//		Thread.sleep(1000);
		System.out.println(Thread.currentThread().getName()+" is running>>>>"+age);
		return  (T) ("返回值是："+age);
	}
}
