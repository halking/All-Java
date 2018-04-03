package com.hal.frame.test;

import java.util.HashSet;
import java.util.Set;

public class SetTest {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String[]  references = {"ARD23749"};
        Set<String> referenceSet = new HashSet<String>();
        for (int i = 0; i < references.length; i++) {
            referenceSet.add(references[i]);
        }
        for (String string : referenceSet) {
			System.out.println(string);
		}
	}

}
