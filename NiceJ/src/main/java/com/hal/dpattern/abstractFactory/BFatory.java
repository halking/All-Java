package com.hal.dpattern.abstractFactory;

public class BFatory implements AbstractFactory {

	public Plant createPlant() {
		// TODO Auto-generated method stub
		return new PlantB();
	}

	public Fruit createFruit() {
		// TODO Auto-generated method stub
		return new FruitTwo();
	}

}
