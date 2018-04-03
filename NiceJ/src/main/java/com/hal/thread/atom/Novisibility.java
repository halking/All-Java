package com.hal.thread.atom;


public class Novisibility {
   
	private  static   boolean  flag;
	private static   int  num;
	
	private  static class  ReaderThread extends  Thread{
		@Override
		public void  run() {
			// TODO Auto-generated method stub
	        this.synrun();
		}
		public  synchronized  void  synrun(){
			while (!flag) {
				Thread.yield();
				System.out.println(num);
                flag = true;
			}
		}
	}
    public  synchronized static void setter(){
    	 num = 40;
//    	 flag =true;
    }
    public  synchronized  static int getnum(){
    	return num;
    }
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		setter();
		System.out.println(getnum());
		new ReaderThread().start();
	}

}
