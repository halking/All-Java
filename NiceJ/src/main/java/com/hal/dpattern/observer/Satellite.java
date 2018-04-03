package com.hal.dpattern.observer;

public class Satellite implements Observer {
	
	private String weather;
	@Override
	public void update(Observable observable, Object object) {
		// TODO Auto-generated method stub
		this.weather = (String) object;
		System.out.println("weather changed recently :"+weather);
	}


}
