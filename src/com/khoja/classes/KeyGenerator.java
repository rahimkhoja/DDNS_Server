package com.khoja.classes;

import java.security.SecureRandom;
import java.math.BigInteger;

public class KeyGenerator {

	private static SecureRandom random = new SecureRandom();

	public static String GenerateKey() {
	    return new BigInteger(384, random).toString(32);
	}
}
