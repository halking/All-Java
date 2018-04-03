package com.hal.structure.tree;

import com.hal.structure.common.Iterator;
import com.hal.structure.common.Position;

/**
 * @author hal
 */
public interface BinTreePosition extends Position {
    public boolean  hasParent();
    public  BinTreePosition getParent();
    public void setParent(BinTreePosition p);
    public boolean isLeaf();
    public boolean isLChild();
    public boolean hasLChild();
    public BinTreePosition getLChild();
    public void setLChild();
    public boolean isRChild();
    public boolean hasRChild();
    public BinTreePosition getRChild();
    public void setRChild();
    public int getSize();
    public  void updateSize();
    public  int getHeight();
    public void updateHeight();
    public int getDepth();
    public void updatedepth();
    public BinTreePosition  getPre();
    public BinTreePosition  getSucc();
    public BinTreePosition secede();
    public BinTreePosition attachL(BinTreePosition c);
    public  BinTreePosition  attachR(BinTreePosition c);
    public  Iterator  elementPreorder();
    public  Iterator  elementIntorder();
    public  Iterator  elementPostorder();
    public  Iterator  elementLevelorder();
}
