package com.hal.jndi;

import java.awt.Button;
import java.util.Hashtable;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

public class SerchObj {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Hashtable<String, Object> env = new Hashtable<String, Object>(11);
		env.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
		env.put(Context.PROVIDER_URL, "ldap://localhost:389/o=JNDITutorial");

		try {
			Context ctx = new InitialContext(env);
			Button button = new Button("push me");

			ctx.bind("cn=button", button);

			Button button2 = (Button) ctx.lookup("cn=button");
			System.out.println(button2);
			ctx.close();
		} catch (NamingException e) {
			// TODO: handle exception
		}
	}

}
