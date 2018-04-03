/**
 * 
 */
package com.hal.structure.tree;

/**
 * @author hal
 *
 */
public interface Tree {
    public Object  getElement() ;
    public Object  setElemrnt(Object object);
    public TreeLinkedList  getParent();
    public TreeLinkedList gerFirstChild();
    public TreeLinkedList getNextSilbling();
    public  int getSize();
    public int getHeight();
    public int getDepth();
}
