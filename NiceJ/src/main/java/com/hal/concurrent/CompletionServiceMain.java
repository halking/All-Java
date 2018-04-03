package com.hal.concurrent;

import java.util.concurrent.Callable;
import java.util.concurrent.CompletionService;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CompletionServiceMain{

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		try {
			ExecutorService executorService = Executors.newCachedThreadPool();
			CompletionService<String> service = new  ExecutorCompletionService<String>(executorService);
			for (int i = 0; i < 10; i++) {
				service.submit(new Callable<String>() {
					@Override
					public String call() throws Exception {
						long time = (int)(Math.random()*1000);
						System.out.println("sleep="+time+" "+Thread.currentThread().getName());
						Thread.sleep(time);
						return "steven"+time+" "+Thread.currentThread().getName();
					}
				});
			}
			for (int i = 0; i < 10; i++) {
				System.out.println(service.take().get());
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

}
