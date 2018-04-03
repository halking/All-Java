package com.hal.thread.threads.article.first;

public class InterruptedThread  extends Thread{
			@Override
			public void run() {
				// TODO Auto-generated method stub
				super.run();
				try {
					for (int i = 0; i < 1000000; i++) {
						if (this.interrupted()) {
							System.out.println(this.getName()+" was interruped ");
							throw new InterruptedException();
						}
						System.out.println("i="+(i+1));
					}
					System.out.println("it is the expression after loop statement");
				} catch (InterruptedException e) {
					// TODO: handle exception
					System.out.println("it was interrupted");
					e.printStackTrace();
				}
			}
}
