package com.hal.thread.threads.article.second.list;

import java.util.ArrayList;
import java.util.List;

public class ListMain {

	public static void main(String[] args) throws InterruptedException {
		// TODO Auto-generated method stub
//		ListService service = new ListService();
		ListDao listDao = new ListDao();
		ListA listA = new ListA(listDao);
		listA.setName("ListA");
		listA.start();
		ListB listB = new ListB(listDao);
		listB.setName("ListB");
		listB.start();
		Thread.sleep(6000);
		System.out.println("list size is "+listDao.getSize());
	}

}
