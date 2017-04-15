package com.khoja.classes;

public class DynProperties {

	private String NS1_IP_Address;
	private String NS2_IP_Address;
	private String ROOT_IP_Address;
	private String Responsible_Person;
	private String Serial_Number;
	private int TTL;
	private int Refresh;
	private int Expire;
	private int Minimum;
	private int Retry;

	public DynProperties(String ns1, String ns2, String root, String person, String serial, int ttl, int refresh, int expire, int min, int retry) {
		NS1_IP_Address = ns1;
		NS2_IP_Address = ns2;
		ROOT_IP_Address = root;
		Responsible_Person = person;
		Serial_Number = serial;
		TTL = ttl;
		Refresh = refresh;
		Expire = expire;
		Minimum = min;
		Retry = retry;
	}
	
	public String getNs1() {
		return NS1_IP_Address;
	}

	public String getNs2() {
		return NS2_IP_Address;
	}

	public String getRoot() {
		return ROOT_IP_Address;
	}

	public String getPerson() {
		return Responsible_Person;
	}
	
	public String getSerial() {
		return Serial_Number;
	}
	
	public int getTtl() {
		return TTL;
	}
	
	public int getRefresh() {
		return Refresh;
	}
	
	public int getExpire() {
		return Expire;
	}
	
	public int getMinimum() {
		return Minimum;
	}

	public int getRetry() {
		return Retry;
	}
	
}
