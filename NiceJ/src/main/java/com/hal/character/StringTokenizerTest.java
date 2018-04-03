package com.hal.character;

import java.util.StringTokenizer;

public class StringTokenizerTest {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
			String ch = "Job;Catalog;Stock;NDSK";
			StringTokenizer tokenizer = new StringTokenizer(ch, ";");
			String n1 = tokenizer.nextToken();
			String n2 = tokenizer.nextToken();
			System.out.println(n1+";"+n2);
	}

}
