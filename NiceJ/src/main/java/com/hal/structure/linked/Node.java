/**
 * 
 */
package com.hal.structure.linked;

import com.hal.structure.common.Position;

/**
 * @author hal
 *
 */
public class Node implements Position {
    private Object  element;
    private  Node next;
    /**
	 * 
	 */
	public Node() {
		// TODO Auto-generated constructor stub
		this(null,null);
	}
	public Node(Object e,Node n){
		this.element = e;
		this.next = n;
	}
	/**
	 * @return the element
	 */
	public Object getElement() {
		return element;
	}
	/**
	 * @param element the element to set
	 */
	public Object setElement(Object element) {
		Object oldElem = this.element;
		this.element = element;
		return oldElem;
	}
	/**
	 * @return the next
	 */
	public Node getNext() {
		return next;
	}
	/**
	 * @param next the next to set
	 */
	public void setNext(Node next) {
		this.next = next;
	}
	
}
