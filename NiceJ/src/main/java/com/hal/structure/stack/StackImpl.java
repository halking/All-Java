/**
 * 
 */
package com.hal.structure.stack;


/**
 * @author hal
 *
 */
public class StackImpl implements Stack {
	protected int capacity;
	protected Object[] S;
	protected int top = -1;


	public StackImpl() {
		// TODO Auto-generated constructor stub
		this(CAPACITY);
	}
   public StackImpl(int c){
	   capacity = c;
	   S = new Object[capacity];
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
		return top + 1;
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
		return (top<0);
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
			throw new ExceptionStackEmpty("栈空异常！");
		}
		return S[top];
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
     if (getsize() == CAPACITY) {
		throw new ExceptionStackFull("栈满异常！");
	}
     S[++top] = object;
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
		Object ele;
		if (isEmpty()) {
			throw new ExceptionStackFull("栈空,不能出栈操作！");
		}
		ele = S[top];
		S[top--] =null;
		return ele;
	}

}
