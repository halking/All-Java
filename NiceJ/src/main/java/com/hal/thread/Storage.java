package com.hal.thread;

import java.util.ArrayList;
import java.util.List;


public class Storage {
       final static  int  MAX_SIZE = 100;
       private List<Object> date  = new ArrayList<Object>();
       
       public  synchronized void  produce(int num){
    	   if (date.size()+num>MAX_SIZE) {
    		   System.out.println(Thread.currentThread().getName()+"--生产操作-->数量： "+num+"，超出库存大小，生产阻塞！----库存： "+date.size());
			     try {
					wait();
				} catch (Exception e) {
					// TODO: handle exception
					e.printStackTrace();
				}  
			     
		}
    	   for (int i = 0; i < num; i++) {
				date.add(new Object());
			}
		     System.out.println(Thread.currentThread().getName()+"--生产操作-->数量： "+num+"，成功入库，生产阻塞！----库存： "+date.size());
		     notify();
       }
       public synchronized void  consume(int num){
    	   if (date.size()-num<0) {
    		   System.out.println(Thread.currentThread().getName()+"--消费操作-->数量： "+num+"，库存为空，生产阻塞！----库存： "+date.size());
			     try {
					wait();
				} catch (Exception e) {
					// TODO: handle exception
					e.printStackTrace();
				}  
			    
		}
    	   //到这里，表示可以正常消费  
	        for(int i = 0; i < num; i++){//消费num个产品  
	            date.remove(0);  
	        }  
	        System.out.println(Thread.currentThread().getName()+"--消费操作-->数量：" + num + "，消费成功~------库存：" + date.size());  
	        //消费完产品后，唤醒其他等待生产的线程  
	     notify();
       }
}
