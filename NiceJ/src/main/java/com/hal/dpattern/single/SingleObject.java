package com.hal.dpattern.single;

public class SingleObject {
	
	private static SingleObject object = new SingleObject();
	
	private SingleObject(){ }
	
	public static SingleObject getInstance() {
		return object;
	}
}
