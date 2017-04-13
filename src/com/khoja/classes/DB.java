package com.khoja.classes;

public class DB {

	private static String url;
    private static String dbName;
    private static String driver = "com.mysql.jdbc.Driver";
    private static String userName;
    private static String password;
        
    public DB(String Host, String Port, String  dbname, String  user, String  pass) {
    	url = "jdbc:mysql://"+Host+":"+Port+"/";
    	dbName = dbname;
    	userName = user;
    	password = pass;
    }

    public String getDBURL() {
		return url;
	}

	public String getDBName() {
		return dbName;
	}

	public String getDriver() {
		return driver;
	}

	public String getUsername() {
		return userName;
	}

	public String getPassword() {
		return password;
	}	
}