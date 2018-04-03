/**
 * 
 */
package com.hal.structure.tree;

import java.util.List;

import com.hal.structure.common.Iterator;
import com.hal.structure.common.Position;

/**
 * @author hal
 *
 */
public class IteratorTree implements Iterator {
   private List  list;
   private  Position nextPosition;
   /**
 * 
 */
public IteratorTree() {
	// TODO Auto-generated constructor stub
	list =null;
}
/**
 * 前序遍历
 *2016年4月24日
 * @param T
 */
  public void elementPreorderIterator(TreeLinkedList T){
	  if (null==T) return;
	  list.lastIndexOf(T);
  }
   /* 
	 *2016年4月24日
	 * @see com.hal.common.Iterator#hasNext()
	 *@return
	 */
	@Override
	public boolean hasNext() {
		// TODO Auto-generated method stub
		return false;
	}

	/* 
	 *2016年4月24日
	 * @see com.hal.common.Iterator#getNext()
	 *@return
	 */
	@Override
	public Object getNext() {
		// TODO Auto-generated method stub
		return null;
	}

}
