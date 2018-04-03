/**
 * 
 */
package com.hal.thread.assume;

import java.util.Timer;

/**
 * @author hal
 *
 */
public class TimerTest {
   public static void main(String[] args) {
	Timer timer = new Timer();
	TimerTask01 task01 = new TimerTask01();
	timer.schedule(task01, 1000);
	System.out.println();
	try {
		for (int i = 0; i < 5; i++) {
			Thread.sleep(1000);
			System.out.println("Main"+i);
		}
	} catch (Exception e) {
		// TODO: handle exception
	}
}
}
