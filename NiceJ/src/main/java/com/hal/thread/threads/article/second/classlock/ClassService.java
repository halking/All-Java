package com.hal.thread.threads.article.second.classlock;

public class ClassService {
		
	synchronized public static void printA() {
		System.out.println("Thread "+Thread.currentThread().getName()+" enter into "+"printA method");
		try {
			Thread.sleep(3000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("Thread "+Thread.currentThread().getName()+" leave"+" printA method!");
	}
	synchronized public static void printB() {
		System.out.println("Thread "+Thread.currentThread().getName()+" enter into "+"printB method");
		System.out.println("Thread "+Thread.currentThread().getName()+" leave"+" printB method!");
	}
	 public static void printC() {
		System.out.println("Thread "+Thread.currentThread().getName()+" enter into "+"printC method");
		System.out.println("Thread "+Thread.currentThread().getName()+" leave"+" printC method!");
	}
}
