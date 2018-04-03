package com.hal.dpattern.abstractFactory;

public class AFactory implements AbstractFactory {

	public Plant createPlant() {
		// TODO Auto-generated method stub
		return new PlantA();
	}

	public Fruit createFruit() {
		// TODO Auto-generated method stub
		return new FruitOne();
	}

}
