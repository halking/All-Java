package com.hal.dpattern.single;

public class SingleObject3 {
	private SingleObject3(){ }
	
	
	private static class SingleFactory{
		private static SingleObject3 object = new SingleObject3();
	} 
	
	public static SingleObject3 getInstance(){
		return SingleFactory.object;
	}
}
