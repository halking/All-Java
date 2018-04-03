package com.hal.proxy;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;

public class ProxyClient {
	
	  private static final Class<?>[] constructorParams =
	        { InvocationHandler.class };

	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		// create a target instance	
		invokeNewProxy();
		invokeByreflect();
	}
   public static void invokeNewProxy() throws Exception{
       //System.out.println(System.getProperties());
        System.getProperties().put("sun.misc.ProxyGenerator.saveGeneratedFiles", "true");
		BaseProxy targetBean = new TargetBean();
		//create a proxy instance
		BaseProxy proxy =(BaseProxy) Proxy.newProxyInstance(BaseProxy.class.getClassLoader(),
			     targetBean.getClass().getInterfaces(),
				new ProxyBean(targetBean));
		Constructor<?> cl = proxy.getClass().getConstructor(constructorParams);
		System.out.println(cl.getModifiers()+"--"+cl.getName().toString()+"--"+cl.getParameterTypes().toString());
		//invoke the target instance method through the proxy
		 proxy.sayHello();
   }
   public static void invokeByreflect() throws Exception{
	    Class proxy = Proxy.getProxyClass(BaseProxy.class.getClassLoader(), BaseProxy.class);
	    Constructor  constructor = proxy.getConstructor(InvocationHandler.class);
	    System.out.println(constructor.getName());
	    BaseProxy baseProxy = (BaseProxy)constructor.newInstance(new ProxyBean(new TargetBean()));
	    baseProxy.sayHello();
   }
}
