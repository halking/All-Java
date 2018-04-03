/**
 * 
 */
package com.hal.io;

import java.io.IOException;

/**
 * @author hal
 *
 */
public class SystemTest {
    public static void main(String[] args)  throws IOException{
		System.out.println("system input String");
		byte[]  b  = new byte[1024];
		int  count = System.in.read(b);
		System.out.println("you inut string is: ");
		for (int i = 0; i < count; i++) 
			 System.out.print((char)b[i]);
	}
}
