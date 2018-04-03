package com.hal.thread.atom;

import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;

public class AtomicLongTest {
	private  final  AtomicLong  count  = new AtomicLong();
	public  long  getCount(){
		return  count.get();
	}
	public  void  Countor(){
		count.incrementAndGet();
	}
	public static void main(String[] args) {
		// TODO Auto-generated method stub
       AtomicLongTest  aTest  = new AtomicLongTest();
       System.out.println(aTest.getCount());
       aTest.Countor();
       System.out.println(aTest.getCount());
	}

}
