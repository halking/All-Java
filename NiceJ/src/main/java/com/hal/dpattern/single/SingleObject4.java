package com.hal.dpattern.single;

public class SingleObject4 {
	private SingleObject4() {}
	private static SingleObject4 object = null;
	
	static {
		object = new SingleObject4();
	}
	public static SingleObject4 getInstance() {
		return object;
	}
}
