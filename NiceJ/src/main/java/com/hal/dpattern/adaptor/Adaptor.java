package com.hal.dpattern.adaptor;

public class Adaptor implements Interface110 {

	Clazz220 object220 = new Clazz220();
	@Override
	public void getVoltage110() {
		object220.getVoltage220();
		System.out.println("start convert voltage......");
		System.out.println("=====this is 110 voltage");
	}
	
}
