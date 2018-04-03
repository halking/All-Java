package com.hal.enums;

public class EnumTest {
	 public static void main(String[] args) {
			// TODO Auto-generated method stub
			for (Coin c : Coin.values()) {
				System.out.println(c.getVlaue());
			}
			System.out.println(Coin.valueOf("PENNY"));
			System.out.println("this is a enum variable  :"+Coin.DIME.getVlaue() );
	}
}
