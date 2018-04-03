package com.hal.thread.threads.article.second.list;

import java.util.ArrayList;
import java.util.List;

public class ListDao {
	private List list = new ArrayList<>();
	synchronized public void add(String name){
		list.add(name);
	}
	synchronized public int getSize(){
		int size = list.size();
		return size;
	}
	
}
