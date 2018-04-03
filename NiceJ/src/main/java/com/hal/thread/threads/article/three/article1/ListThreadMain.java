package com.hal.thread.threads.article.three.article1;

public class ListThreadMain {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		try {
			
		MyList list = new MyList();
		ListThreadA listA 	= new ListThreadA(list);
		listA.setName("listA");
		listA.start();
		Thread.sleep(5000);
		ListThreadB listB = new ListThreadB(list);
		listB.start();
		} catch (InterruptedException e) {
			// TODO: handle exception
		}
	}

}
