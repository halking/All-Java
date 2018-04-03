package com.hal.thread;

public class MoneyCount {
     
	public synchronized  void  addMoney(int m){
		  System.out.println(Thread.currentThread().getName()+">存入： "+m);
	}
	
	public  synchronized void getMoney(int m){
		System.out.println(Thread.currentThread().getName()+">取钱： "+m);
	}
} 
