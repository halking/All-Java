package com.hal.dpattern.single;

public enum EnumSingel {
	SINGLE;
	private Object object;

	private EnumSingel() {
		object = new Object();
	}
	public Object getInstance() {
		return object;
	}
	public  static void main(String[] args) {
		EnumSingel.SINGLE.getInstance();
	}
}
