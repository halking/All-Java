/**
 * 
 */
package com.hal.thread.assume;

/**
 * @author hal
 *
 */
public class MultiThreadDemo  extends  Thread{
	char c;

	/**
 * 
 */
	public MultiThreadDemo(String name,char c) {
		// TODO Auto-generated constructor stub
		super(name);
		this.c = c;
	}
  /* 
 *2016年4月15日
 * @see java.lang.Thread#run()
 */
@Override
public void run() {
	// TODO Auto-generated method stub
	 char ch = c;
	 System.out.println();
	 System.out.println(getName()+":");
	 for (int i = 0; i <=30; i++) {
		  ch = (char )  (c+i);
		  System.out.print(ch+"  ");
	}
	 System.out.println(getName()+"end!");
}
	/**
	 * 2016年4月15日
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
    MultiThreadDemo mDemo1 = new MultiThreadDemo("th1", 'A');
    MultiThreadDemo mDemo2 = new MultiThreadDemo("th2", 'B');
    MultiThreadDemo mDemo3 = new MultiThreadDemo("th3", 'C');
    mDemo1.start();
    mDemo2.start();
    mDemo3.start();
    System.out.println("activecount="+Thread.activeCount());
	}

}
