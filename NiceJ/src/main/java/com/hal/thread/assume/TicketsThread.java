/**
 * 
 */
package com.hal.thread.assume;

import javax.xml.stream.events.StartDocument;

/**
 * @author hal
 *
 */
public class TicketsThread  extends Thread{

	/**
	 *2016年4月15日
	 * @param args
	 */
	Tickets tickets;
	String name;
	/**
	 * 
	 */
	public TicketsThread(Tickets tickets,String name) {
		// TODO Auto-generated constructor stub
		this.tickets =tickets;
		this.name = name;
		start();
	}
	/* 
	 *2016年4月15日
	 * @see java.lang.Thread#run()
	 */
	@Override
	public void run() {
		// TODO Auto-generated method stub
		try {
			for (int i = 0; i < 5; i++) {
				tickets.action(name);
				Thread.sleep(1000);
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
	public static void main(String[] args) {
		// TODO Auto-generated method stub
      Tickets  tickets =new Tickets();
      TicketsThread thread1 = new TicketsThread(tickets, "小王");
      TicketsThread thread2= new TicketsThread(tickets, "黄哥哥");
	}

}
