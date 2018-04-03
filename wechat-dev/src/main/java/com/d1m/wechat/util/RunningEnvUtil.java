package com.d1m.wechat.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class RunningEnvUtil {
	public final static int ENV_PRODUCT = 0;
	public final static int ENV_TESTING = 1;
	public final static int ENV_DEVELOPEMNT = 2;
	
	//set default to be development environment.
	private static int currentEnv = ENV_DEVELOPEMNT;
	
	//config through property httpPath
	public static String HTTP_PATH = "http://www.qq.com";

    @Value("${running.env}")
    public void setCurrentEnv(String env) {
    	currentEnv = Integer.parseInt(env);
    }
    
    @Value("${httpPath}")
    public void setHttpPath(String httpPath) {
    	HTTP_PATH = httpPath;
    }
    
	public static int getRunningEnv() {
		return currentEnv;
	}
	
	public static String getHttpPath() {
		return HTTP_PATH;
	}
}
