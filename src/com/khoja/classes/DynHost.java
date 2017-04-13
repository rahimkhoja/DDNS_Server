package com.khoja.classes;

public class DynHost {

	private int Dyn_Record_ID;
	private String Dyn_Hostname;
	private String Dyn_Domain;
	private String Dyn_LastUpdate;
	private String Dyn_IP_Address;
	private String Dyn_Username;
	private String Dyn_Email;
	
	public DynHost(String hostn, String domainn, int recID, String last, String ipadd) {
		Dyn_Hostname = hostn;
		Dyn_Domain = domainn;
		Dyn_Record_ID = recID;
		Dyn_LastUpdate = last;
		Dyn_IP_Address = ipadd;
    }
	
	public DynHost(String hostn, String domainn, int recID, String last, String ipadd, String email, String usern) {
		Dyn_Hostname = hostn;
		Dyn_Domain = domainn;
		Dyn_Record_ID = recID;
		Dyn_LastUpdate = last;
		Dyn_IP_Address = ipadd;
		Dyn_Username = usern;
		Dyn_Email = email;
    }
	
	public int getID() {
		return Dyn_Record_ID;
	}

	public String getHostname() {
		return Dyn_Hostname;
	}

	public String getDomain() {
		return Dyn_Domain;
	}

	public String getLastUpdate() {
		return Dyn_LastUpdate;
	}

	public String getIPAddress() {
		return Dyn_IP_Address;
	}

	public String getUsername() {
		return Dyn_Username;
	}
	
	public String getEmail() {
		return Dyn_Email;
	}
}