package com.hal.thread;

import java.util.Timer;
import java.util.TimerTask;

public class TimerMain {
		private static Timer timer = new Timer();
		
		public static class Mytask extends TimerTask{
			@Override
			public void run() {
				System.out.println(Thread.currentThread().getName()+" is running!");
			}
		}
		public static void main(String[] args) {
			//第一种启动方法
			Mytask mytask = new Mytask();
			timer.schedule(mytask, 1000L);
			//第二种启动方法
			Thread thread = new Thread(mytask,"mytask");
			thread.start();
		}
	
}
