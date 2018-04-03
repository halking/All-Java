/**
 * 
 */
package com.hal.thread.assume;


/**
 * @author hal
 *
 */
public class DeadLockDemo implements Runnable {
    private   boolean flag;
    static  Object obj1 = new Object();
    static Object obj2= new Object();
	/* 
	 *2016年4月15日
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run() {
		// TODO Auto-generated method stub
      String name = Thread.currentThread().getName();
      System.out.println(name +" -- --   flag: "+flag);
      if (flag) {
		synchronized (obj1) {
			try {
				System.out.println(name+"---- 休眠500毫秒，等待obj2 ");
				Thread.sleep(500);
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}
			synchronized (obj2) {
				   System.out.println("获取obj2");
			}
		}
	}else{
		synchronized (obj2) {
			try {
				System.out.println(name+"----休眠500毫秒，等待obj1 ");
				Thread.sleep(500);
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}
			synchronized (obj1) {
				   System.out.println("获取obj1");
			}
		}
	}
	}

	/**
	 *2016年4月15日
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
    DeadLockDemo d1 = new DeadLockDemo();
    DeadLockDemo  d2 = new DeadLockDemo();
    /**
     * 构造竞争资源模型
     */
    d1.flag = true;
    d2.flag = false;
    Thread t1 = new Thread(d1);
    Thread t2 = new Thread(d2);
    t1.start();
    t2.start();
	}

}
