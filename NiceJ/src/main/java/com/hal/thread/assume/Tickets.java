/**
 * 
 */
package com.hal.thread.assume;

/**
 * @author hal
 *
 */
public class Tickets {
      public int ticket;
      public  Tickets(){
    	  ticket = 10;
      }
      public synchronized void  action(String name) {
    	  System.out.println(name+"抢到了第"+ticket+"号票");
    	  ticket--;
	}
}
