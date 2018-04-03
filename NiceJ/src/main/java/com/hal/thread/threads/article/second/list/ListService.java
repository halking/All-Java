package com.hal.thread.threads.article.second.list;

import java.util.ArrayList;
import java.util.List;

public class ListService {
	private List list = new ArrayList<>();
	synchronized public void add(String name){
		System.out.println("Thread "+Thread.currentThread().getName()+" is executing "+"add method!");
		list.add(name);
		System.out.println("Thread "+Thread.currentThread().getName()+" is quiting "+"add method!");
	}
	synchronized public int getSize(){
		System.out.println("Thread "+Thread.currentThread().getName()+" is executing"+"getSize method!");
		int size = list.size();
		System.out.println("Thread "+Thread.currentThread().getName()+" is quiting "+"getSize method!");
		return size;
	}
	public ListDao addList(ListDao listDao,String data) {
		try {
			synchronized (listDao) {
				if (listDao.getSize()<1) {
					Thread.sleep(2000);
					listDao.add(data);
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		return listDao;
	}
}
