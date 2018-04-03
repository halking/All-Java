package com.hal.jws;

import javax.jws.WebParam;
import javax.jws.WebService;

import com.hal.jws.entity.User;

@WebService
public interface HelloWorld {
	
	public  String sayHello(@WebParam(name="params") String params);
	
	public  String sayToUser(@WebParam(name="user") User  user);

}
