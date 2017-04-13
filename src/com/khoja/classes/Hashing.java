package com.khoja.classes;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Hashing {

	private static MessageDigest md;
	
	public static String returnMD5(String text) {
		
		try {
			md = MessageDigest.getInstance("MD5");
		
			md.update(text.getBytes());

			byte byteData[] = md.digest();

			StringBuffer sb = new StringBuffer();
			for (int i = 0; i < byteData.length; i++) {
				sb.append(Integer.toString((byteData[i] & 0xff) + 0x100, 16).substring(1));
			}

			return sb.toString();
			
		} catch (NoSuchAlgorithmException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
        
        return "";
    }
		
}