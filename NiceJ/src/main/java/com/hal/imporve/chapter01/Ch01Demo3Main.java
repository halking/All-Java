package com.hal.imporve.chapter01;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class Ch01Demo3Main {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		int  i =80;
		String s1 = String.valueOf(i<100 ? 90 :100);
		String s2 = String.valueOf(i<100 ? 90 :100.0);
		System.out.println(s1+"\n"+s2);
		
		Base base = new Sub();
		base.fun(100, 50);
		Sub sub = new Sub();
//		sub.fun(100, 50);
		
		BigDecimal deposit = new BigDecimal(888888);
		BigDecimal rate = new BigDecimal(0.001857*3);
		BigDecimal interest = deposit.multiply(rate).setScale(2,RoundingMode.HALF_EVEN);
		System.out.println("利息是 : "+interest);
	}

}
