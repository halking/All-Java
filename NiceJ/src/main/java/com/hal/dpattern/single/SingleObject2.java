package com.hal.dpattern.single;

public class SingleObject2 {

	private static SingleObject2 object;

	private SingleObject2() {
	}

	public static SingleObject2 getInstance() {
		if (object != null) {
		} else {
			synchronized (SingleObject2.class) {
				if (object == null) {
					object = new SingleObject2();
				}
			}
		}
		return object;
	}
}
