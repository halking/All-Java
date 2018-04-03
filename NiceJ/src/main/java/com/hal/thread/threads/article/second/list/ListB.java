package com.hal.thread.threads.article.second.list;

public class ListB extends Thread {
//	private ListService service;
	private ListDao listDao;

	public ListB(ListDao listDao) {
		// TODO Auto-generated constructor stub
//		this.service = service;
		this.listDao = listDao;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
//		for (int i = 0; i < 10000; i++) {
//			service.add("ListA"+i);
//		}
//		System.out.println("list size is "+service.getSize());
		
		ListService service = new ListService();
		service.addList(listDao, "ListB");
	}
}
