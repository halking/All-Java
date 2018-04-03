package com.hal.socket;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * @author hal
 */
public class InetAddressDemo {

	/**
	 *2016年4月26日
	 * @param args
	 */
	public static void main(String[] args)  throws  UnknownHostException{
         InetAddress     ipo = InetAddress.getLocalHost();
         System.out.println("标准主机名："+ipo.getCanonicalHostName());
         System.out.println("主机ip地址："+ipo.getHostAddress());
         System.out.println("主机名："+ipo.getHostName());
         System.out.println("主机字符串："+ipo.toString());
         System.out.println("============================");
         InetAddress  ipn = InetAddress.getByName("www.baidu.com");
         System.out.println("标准主机名："+ipn.getCanonicalHostName());
         System.out.println("主机ip地址："+ipn.getHostAddress());
         System.out.println("主机名："+ipn.getHostName());
         System.out.println("主机字符串："+ipn.toString());
         InetAddress  ipaAddress = InetAddress.getByAddress(ipn.getAddress());
         System.out.println("标准主机名："+ipaAddress.getCanonicalHostName());
         System.out.println("主机ip地址："+ipaAddress.getHostAddress());
         System.out.println("主机名："+ipaAddress.getHostName());
         System.out.println("主机字符串："+ipaAddress.toString());
         System.out.println("============================");
         if (ipaAddress.equals(ipn)) {
			System.out.println("ipn  == ipaddress");
		} else {
			System.out.println("ipn != ipaddress");
		}
	}

}
