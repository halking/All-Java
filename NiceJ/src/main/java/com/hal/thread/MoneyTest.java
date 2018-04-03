package com.hal.thread;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class MoneyTest {
   public static void main(String[] args) {
	  final MoneyCount money = new MoneyCount();
	
	ExecutorService  executor  = Executors.newFixedThreadPool(10);
	
	executor.execute(new Runnable() {
		
		public void run() {
			// TODO Auto-generated method stub
			int i = 5;
			while(i-->0){
				money.addMoney(500);
				try {
					Thread.sleep(500);
				} catch (InterruptedException e) {
					// TODO: handle exception
					e.printStackTrace();
				}
			}
		}
	});
	Future<?>  future = executor.submit(new Runnable() {
		
		public void run() {
			// TODO Auto-generated method stub
			int i = 5;
			while (i-->0) {
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					// TODO: handle exception
					System.err.println("取钱失败: "+e);
				}
				money.getMoney(200);
			}
		}
	});
	try {
		Object object = future.get();
		System.out.println(object);
	} catch (Exception e) {
		// TODO: handle exception
		e.printStackTrace();
	}finally{
		executor.shutdown();
	}
}
}
