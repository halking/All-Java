package com.hal.thread;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class StorageTest {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
       Storage storage = new Storage();
       ExecutorService  taskSubmit  = Executors.newFixedThreadPool(10);
       
       taskSubmit.submit(new  Consume(storage, 40));
       taskSubmit.submit(new Consume(storage, 30));  
       taskSubmit.submit(new Consume(storage, 10));  
       taskSubmit.submit(new Consume(storage, 20));  
 
       //给定6个生产者  
       taskSubmit.submit(new Produce(storage, 70));  
       taskSubmit.submit(new Produce(storage, 10));  
       taskSubmit.submit(new Produce(storage, 20));  
       taskSubmit.submit(new Produce(storage, 10));  
       taskSubmit.submit(new Produce(storage, 10));  
       taskSubmit.submit(new Produce(storage, 10));  
       
       taskSubmit.shutdown();
	}

}
