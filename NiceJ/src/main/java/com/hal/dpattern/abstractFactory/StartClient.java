package com.hal.dpattern.abstractFactory;

public class StartClient {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		AbstractFactory factory = new AFactory();
		factory.createFruit();
		factory.createPlant();
	}

}
