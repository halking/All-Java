package com.hal.jws.entity;

import java.io.Serializable;

public class User   implements Serializable{
	
	private static final long serialVersionUID = -608671218473243880L;

	private String name;
	
	private String address;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}
	
	

}
