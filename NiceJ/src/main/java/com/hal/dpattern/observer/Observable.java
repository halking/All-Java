package com.hal.dpattern.observer;

public abstract class Observable {
			private Observer[] observer;
			
			public void setChange() {
				System.out.println("weather changes");
			}
			public void notifyObserver(String weather) {
				for (int i = 0; i < observer.length; i++) {
					observer[i].update(this, weather);
				}
			}
			
			public void addObserver(Observer... observer) {
				this.observer = observer;
			}
			
			public void delete() {
				
			}
}
