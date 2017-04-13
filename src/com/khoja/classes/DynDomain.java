package com.khoja.classes;

public class DynDomain {

	private int Dyn_Record_ID;
	private String Dyn_Domain;
	private String Dyn_Domain_Create_Date;
	
	public DynDomain(int recID, String domainname) {
		Dyn_Record_ID = recID;
		Dyn_Domain = domainname;
	}
	
	public DynDomain(int recID, String domainname, String createdate) {
		Dyn_Record_ID = recID;
		Dyn_Domain = domainname;
		Dyn_Domain_Create_Date = createdate;
	}
	
	public String getDomain() {
		return Dyn_Domain;
	}

	public int getID() {
		return Dyn_Record_ID;
	}

	public String getCreateDate() {
		return Dyn_Domain_Create_Date;
	}

}
