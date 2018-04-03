/**
 * 
 */
package com.hal.structure.tree;

/**
 * @author hal
 *
 */
public class TreeLinkedList implements Tree {
     private Object element;
     private  TreeLinkedList parent,firstChild,nextSibling;
     /**
	 * 
	 */
	public TreeLinkedList() {
		// TODO Auto-generated constructor stub
		this(null, null, null, null);
	}
	public TreeLinkedList(Object e,TreeLinkedList p,TreeLinkedList  f,TreeLinkedList n){
		element = e;
		parent =p;
		firstChild =f;
		nextSibling = n;
	}
	/* 
	 *2016年4月24日
	 * @see com.hal.tree.Tree#getElement()
	 *@return
	 */
	@Override
	public Object getElement() {
		// TODO Auto-generated method stub
		return element;
	}

	/* 
	 *2016年4月24日
	 * @see com.hal.tree.Tree#setElemrnt(java.lang.Object)
	 *@param object
	 *@return
	 */
	@Override
	public Object setElemrnt(Object object) {
		// TODO Auto-generated method stub
		Object old = element;
		element = object;
		return old;
	}

	/* 
	 *2016年4月24日
	 * @see com.hal.tree.Tree#getParent()
	 *@return
	 */
	@Override
	public TreeLinkedList getParent() {
		// TODO Auto-generated method stub
		return parent;
	}

	/* 
	 *2016年4月24日
	 * @see com.hal.tree.Tree#gerFirstChild()
	 *@return
	 */
	@Override
	public TreeLinkedList gerFirstChild() {
		// TODO Auto-generated method stub
		return firstChild;
	}

	/* 
	 *2016年4月24日
	 * @see com.hal.tree.Tree#getNextSilbling()
	 *@return
	 */
	@Override
	public TreeLinkedList getNextSilbling() {
		// TODO Auto-generated method stub
		return nextSibling;
	}

	/* 
	 *2016年4月24日
	 * @see com.hal.tree.Tree#getSize()
	 *@return
	 */
	@Override
	public int getSize() {
		// TODO Auto-generated method stub
		int size =1;
		TreeLinkedList subtree = firstChild;
	    while (subtree !=null) {
			size += subtree.getSize();
			subtree = subtree.getNextSilbling();
		}
		return size;
	}

	/* 
	 *2016年4月24日
	 * @see com.hal.tree.Tree#getHeight()
	 *@return
	 */
	@Override
	public int getHeight() {
		// TODO Auto-generated method stub
		int height = -1;
		TreeLinkedList subtree  = firstChild;
		while (firstChild!=null) {
			height = Math.max(height, subtree.getHeight());
			subtree = subtree.getNextSilbling();
		}
		return height+1;
	}

	/* 
	 *2016年4月24日
	 * @see com.hal.tree.Tree#getDepth()
	 *@return
	 */
	@Override
	public int getDepth() {
		// TODO Auto-generated method stub
		int depht= 0;
		TreeLinkedList p = parent;
		while (parent!=null) {
			depht++;
			p=p.getParent();
		}
		return depht;
	}
}
