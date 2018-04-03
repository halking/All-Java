/**
 * 
 */
package com.hal.structure.stack;


/**
 * @author hal
 *
 */
public interface Stack {
	public static final int  CAPACITY = 1024;
    public int  getsize() ;
    public boolean isEmpty();
    public Object top() throws ExceptionStackEmpty;
    public void push(Object object);
    public Object pop() throws ExceptionStackEmpty; 
}
