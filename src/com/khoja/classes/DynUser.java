package com.khoja.classes;

import java.io.Serializable;

public class DynUser implements Serializable {

	private static final long serialVersionUID = -8966501142994173386L;

	private String Dyn_User_Email;
    private int Dyn_User_ID;
    private boolean Dyn_User_Admin;
    private String Dyn_User_Name;
    private String Dyn_User_Create_Date;
    private boolean Dyn_User_Enabled = false;
    private boolean Dyn_User_Forgot = false;

    public DynUser(String userEmail, int userID, boolean admin, String username) {
    	Dyn_User_Email = userEmail;
    	Dyn_User_ID = userID;
    	Dyn_User_Name = username;
    	Dyn_User_Admin = admin;
    }
    
    public DynUser(String userEmail, int userID, boolean admin, String username, boolean enabled, String date) {
    	Dyn_User_Email = userEmail;
    	Dyn_User_ID = userID;
    	Dyn_User_Name = username;
    	Dyn_User_Admin = admin;
    	Dyn_User_Create_Date = date;
    	Dyn_User_Enabled = enabled;
    }
    
    public DynUser(String userEmail, int userID, boolean admin, String username, boolean enabled, String date, boolean forgot) {
    	Dyn_User_Email = userEmail;
    	Dyn_User_ID = userID;
    	Dyn_User_Name = username;
    	Dyn_User_Admin = admin;
    	Dyn_User_Create_Date = date;
    	Dyn_User_Enabled = enabled;
    	Dyn_User_Forgot = forgot;
    }
    
    
    public String getEmail() {
        return Dyn_User_Email;
    }

    public String getUsername() {
        return Dyn_User_Name;
    }

    public int getID() {
        return Dyn_User_ID;
    }

    public boolean isAdmin() {
    	return Dyn_User_Admin;
    }

	public boolean getEnabled() {
		return Dyn_User_Enabled;
	}

	public String getCreateDate() {
		return Dyn_User_Create_Date;
	}

	public boolean getForgot() {
		return Dyn_User_Forgot;
	}
	
}