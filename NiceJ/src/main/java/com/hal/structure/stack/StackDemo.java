/**
 * 
 */
package com.hal.structure.stack;

/**
 * @author hal
 *
 */
public class StackDemo {

	/**
	 *2016年4月22日
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
	 
    StackImpl stackImpl   = new StackImpl(10);
    stackImpl.push(2);
    System.out.println(stackImpl.top());
    System.out.println(stackImpl.pop());
    System.out.println(stackImpl.isEmpty());
    stackImpl.pop();
	}

}
