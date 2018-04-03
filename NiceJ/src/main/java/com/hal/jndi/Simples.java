package com.hal.jndi;

import java.util.Hashtable;

import javax.naming.Context;
import javax.naming.NamingException;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;

public class Simples {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		// Set up environment for creating initial context
		Hashtable<String, Object> env = new Hashtable<String, Object>(11);

		env.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
		env.put(Context.PROVIDER_URL, "ldap://localhost:389/o=JNDITutorial");

		// Authenticate as S. User and password "mysecret"
		env.put(Context.SECURITY_AUTHENTICATION, "simple");
		env.put(Context.SECURITY_PRINCIPAL, "cn=S. User, ou=NewHires, o=JNDITutorial");
		env.put(Context.SECURITY_CREDENTIALS, "mysecret");

		try {
			// Create initial context
			DirContext ctx = new InitialDirContext(env);

			System.out.println(ctx.lookup("ou=NewHires"));

			// do something useful with ctx

			// Close the context when we're done
//			ctx.close();
		} catch (NamingException e) {
			e.printStackTrace();
		}
	}

}
