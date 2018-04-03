/**
 * 
 */
package com.hal.structure.linked;

import com.hal.structure.stack.ExceptionStackEmpty;
import com.hal.structure.stack.Stack;


/**
 * @author hal
 *
 */
public class StackLinked implements Stack {
	protected Node top;
	protected int size;

	/**
 * 
 */
	public StackLinked() {
		// TODO Auto-generated constructor stub
		top= null;size=0;
   	}

	/*
	 * 2016年4月22日
	 * 
	 * @see com.hal.stack.Stack#getsize()
	 * 
	 * @return
	 */
	@Override
	public int getsize() {
		// TODO Auto-generated method stub
		return size;
	}

	/*
	 * 2016年4月22日
	 * 
	 * @see com.hal.stack.Stack#isEmpty()
	 * 
	 * @return
	 */
	@Override
	public boolean isEmpty() {
		// TODO Auto-generated method stub
		return (top==null)?true:false;
	}

	/*
	 * 2016年4月22日
	 * 
	 * @see com.hal.stack.Stack#top()
	 * 
	 * @return
	 * 
	 * @throws ExceptionStackEmpty
	 */
	@Override
	public Object top() throws ExceptionStackEmpty {
		// TODO Auto-generated method stub
		if (isEmpty()) {
			throw new ExceptionStackEmpty("栈空!");
		}
		return top.getElement();
	}

	/*
	 * 2016年4月22日
	 * 
	 * @see com.hal.stack.Stack#push(java.lang.Object)
	 * 
	 * @param object
	 */
	@Override
	public void push(Object object) {
		// TODO Auto-generated method stub
     Node v = new Node(object,top);
     top = v;
     size++;
	}

	/*
	 * 2016年4月22日
	 * 
	 * @see com.hal.stack.Stack#pop()
	 * 
	 * @return
	 * 
	 * @throws ExceptionStackEmpty
	 */
	@Override
	public Object pop() throws ExceptionStackEmpty {
		// TODO Auto-generated method stub
		if (isEmpty()) {
			throw new ExceptionStackEmpty("栈空!");
		}
		Object temp = top.getElement();
		top = top.getNext();
		size--;
		return temp;
	}
	public static void main(String[] args) {
		StackLinked stackLinked = new StackLinked();
//		stackLinked.pop();
		stackLinked.push(5);
		  System.out.println(stackLinked.size);
	}
}
