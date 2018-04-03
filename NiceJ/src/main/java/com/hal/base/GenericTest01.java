/**
 * 
 */
package com.hal.base;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

/**
 * @author hal
 *
 */
public class GenericTest01<T> {

	/**
	 * @param <T>
	 * @param args
	 */
	public static  void main(String[] args) {
		// TODO Auto-generated method stub
        Vector<String>  vector = new  Vector<String>();
        vector.add("kkk");
        print(vector);
        
        Vector<?>  v = new  Vector<String>(); 
        List<A>  lAs = new ArrayList<A>();
        List  aList = new ArrayList();
        lAs =  aList;
        aList = lAs;
	}
	public static   void print(Vector<?>  c) {
		 for (Object object : c) {
			     System.out.println(object);
		}
	}
	public  static  class hal< T extends Person>{
		
	}

}
