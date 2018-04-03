package com.hal.dpattern.observer;

public class WeatherService {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Earth earth = new Earth();
		Satellite satellite = new Satellite();
		Satellite satellite2 = new Satellite();
		earth.addObserver(satellite,satellite2);
		System.out.println("weather forecast start....");
		earth.setWeather("heavy rain");
		earth.setWeather("sunny");
		earth.setWeather("high wind");
		System.out.println("weather forecast end....");
	}

}
