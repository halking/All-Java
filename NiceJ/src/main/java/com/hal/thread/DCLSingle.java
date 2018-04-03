package com.hal.thread;

public class DCLSingle {

	private static volatile DCLSingle dclSingle;

	private DCLSingle() {
	}
	
	public static DCLSingle getInstance() {
		if (dclSingle != null) {
		} else {
			synchronized (DCLSingle.class) {
				if (dclSingle == null) {
					dclSingle = new DCLSingle();
				}
			}
		}
		return dclSingle;
	}
}
