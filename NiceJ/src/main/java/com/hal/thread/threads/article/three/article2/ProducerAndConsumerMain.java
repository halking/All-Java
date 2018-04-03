package com.hal.thread.threads.article.three.article2;

public class ProducerAndConsumerMain {

	public static void main(String[] args) throws InterruptedException {
		// TODO Auto-generated method stub
		String lock = new String("");
		Producer producer = new Producer(lock);
		Consumer consumer = new Consumer(lock);
		ThreadP[]  threadP = new ThreadP[2];
		ThreadC[] threadC = new ThreadC[2];
		for (int i = 0; i < 2; i++) {
			threadP[i] = new ThreadP(producer);
			threadP[i].setName("producer"+(i+1));
			threadC[i] = new ThreadC(consumer);
			threadC[i].setName("consumer"+(i+1));
			threadP[i].start();
			threadC[i].start();
		}
		Thread.sleep(5000);
		Thread[] arr = new  Thread[Thread.currentThread().getThreadGroup().activeCount()];
		System.out.println(arr.length);
		Thread.currentThread().getThreadGroup().enumerate(arr);
		for (int i = 0; i < arr.length; i++) {
			System.out.println(arr[i].getName()+"_"+arr[i].getState());
		}
	}
}
