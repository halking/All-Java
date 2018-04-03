/**
 * 
 */
package com.hal.thread.assume;

import java.util.TimerTask;

/**
 * @author hal
 *
 */
public class TimerTask01 extends TimerTask {

	/* 
	 *2016年4月15日
	 * @see java.util.TimerTask#run()
	 */
	@Override
	public void run() {
		// TODO Auto-generated method stub
     try {
		for (int i = 0; i < 5; i++) {
			Thread.sleep(1000);
			System.out.println("Run"+i);
		}
	} catch (Exception e) {
		// TODO: handle exception
	}
	}

	/**
	 *2016年4月15日
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
