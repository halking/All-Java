package com.hal.dpattern.combination;

public class Leaf extends Component {

	public Leaf(String name) {
		super(name);
	}

	@Override
	public void add(Component c) {
		System.out.println("can not add a leaf!");
	}

	@Override
	public void remove(Component c) {
		// TODO Auto-generated method stub
		System.out.println("hava't the leaf  removed");
	}

	@Override
	public void display(int depth) {
		// TODO Auto-generated method stub
		System.out.println("depth of leaf is"+depth);
	}

}
