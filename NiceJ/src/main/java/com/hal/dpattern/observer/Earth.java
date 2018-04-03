package com.hal.dpattern.observer;

public class Earth extends Observable {
	
		private String weather = "sunny";
		
		public String getWeather() {
			return weather;
		}

		public void setWeather(String weather) {
			this.weather = weather;
			setChange();
			notifyObserver(this.weather);
		}

}
