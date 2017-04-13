package com.khoja.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.khoja.classes.DB;

public class CreateDao {
	
	public static boolean doesUserExist(String user, DB dbinfo) {
        boolean result = true;
        Connection conn = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
                
        try {
            Class.forName(dbinfo.getDriver()).newInstance();
            conn = DriverManager
                    .getConnection(dbinfo.getDBURL() + dbinfo.getDBName(), dbinfo.getUsername(), dbinfo.getPassword());

            pst = conn
                    .prepareStatement("select * from dyn_users where user=?");
            pst.setString(1, user);

            rs = pst.executeQuery();
            
            if (!rs.next()) {            
            	result = false;
            } 
            
        } catch (Exception e) {
            System.out.println(e);
        } finally {
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (pst != null) {
                try {
                    pst.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }

        return result;
    }
	
	public static boolean doesEmailExist(String email, DB dbinfo) {
        boolean result = true;
        Connection conn = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
                
        try {
            Class.forName(dbinfo.getDriver()).newInstance();
            conn = DriverManager
                    .getConnection(dbinfo.getDBURL() + dbinfo.getDBName(), dbinfo.getUsername(), dbinfo.getPassword());
            pst = conn
                    .prepareStatement("select * from dyn_users where email=?");
            pst.setString(1, email);

            rs = pst.executeQuery();
            
            if (!rs.next()) {            
            	result = false;
            } 
            
        } catch (Exception e) {
            System.out.println(e);
        } finally {
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (pst != null) {
                try {
                    pst.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }

        return result;
    }
	
	public static boolean addUser(String user, String email, String pass, String key1, String key2, DB dbinfo) {
        boolean result = false;
        Connection conn = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
                
        try {
            Class.forName(dbinfo.getDriver()).newInstance();
            conn = DriverManager
                    .getConnection(dbinfo.getDBURL() + dbinfo.getDBName(), dbinfo.getUsername(), dbinfo.getPassword());

            pst = conn
                    .prepareStatement("insert into dyn_users (email, user, password, key1, key2) values (?, ?, ?, ?, ?)");
            pst.setString (1, email);
            pst.setString (2, user);
            pst.setString (3, pass);
            pst.setString (4, key1);
            pst.setString (5, key2);

            int success = pst.executeUpdate();
            
            if (success > 0) {   
            	System.out.println("New User Added - Email: "+email+" - User: "+user+" - Password: "+pass+" - Key 1: "+key1+" - Key 2: "+key2);
            	result = true;
            } 
            
        } catch (Exception e) {
            System.out.println(e);
        } finally {
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (pst != null) {
                try {
                    pst.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }

        return result;
    }
	
	public static boolean valitadeUser(String email, String key1, String key2, DB dbinfo) {
		boolean result = false;
        Connection conn = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
                
        try {
            Class.forName(dbinfo.getDriver()).newInstance();
            conn = DriverManager
                    .getConnection(dbinfo.getDBURL() + dbinfo.getDBName(), dbinfo.getUsername(), dbinfo.getPassword());
            
            pst = conn
                    .prepareStatement("select * from dyn_users where user=? and key1=? and key2=? and enabled=0");
            pst.setString(1, email);
            pst.setString(1, key1);
            pst.setString(1, key2);

            rs = pst.executeQuery();
            
            if (rs.next()) {            
            	result = true;
            } 
            
        } catch (Exception e) {
            System.out.println(e);
        } finally {
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (pst != null) {
                try {
                    pst.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }

        return result;
    }
	
}