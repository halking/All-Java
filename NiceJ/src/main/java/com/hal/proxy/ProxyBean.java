package com.hal.proxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

public class ProxyBean implements InvocationHandler {

	private Object targetObject;
	public ProxyBean(){
		
	}
	public ProxyBean(Object targetObject){
		this.targetObject = targetObject;
	}

	public Object invoke(Object proxy, Method method, Object[] params) throws Throwable {
		// TODO Auto-generated method stub
		long st = System.nanoTime();
		System.out.println("==============start execute method:"+method.getName());
		Object result  = method.invoke(targetObject, params);
		long end = System.nanoTime();
		System.out.println("============execute method "+method.getName()+" in "+(end-st)+" naoseconds");
		return result;
	}
   @Override
public String toString() {
	// TODO Auto-generated method stub
	return  ProxyBean.class.getName()+"---"+ProxyBean.class.getConstructors().toString();
}
}
