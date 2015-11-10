package com.mtm.ksu.mobile.common;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;

public class AndroidUtility {

	public static String getMacAddress(){
		InetAddress ip;
		String result = null;
		try {
			 
			ip = InetAddress.getLocalHost();
			//System.out.println("Current IP address : " + ip.getHostAddress());
			NetworkInterface network = NetworkInterface.getByInetAddress(ip);
	 
			byte[] mac = network.getHardwareAddress(); 
			//System.out.print("Current MAC address : ");
			if (mac==null) return "";
			 
			StringBuilder sb = new StringBuilder();
			for (int i = 0; i < mac.length; i++) {
				sb.append(String.format("%02X%s", mac[i], (i < mac.length - 1) ? "-" : ""));		
			}
			result = sb.toString();
	 
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (SocketException e){
			e.printStackTrace();
		}
		
		return result;
		
	}
}
