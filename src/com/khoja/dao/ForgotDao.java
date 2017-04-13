package com.khoja.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

import com.khoja.classes.DB;

public class ForgotDao {

	
	public static boolean setForgot(int userid, String key1, String key2, DB dbinfo) {
		boolean result = false;
    Connection conn = null;
    PreparedStatement pst = null;
    int recordUpdated = 0;
            
    try {
		Class.forName(dbinfo.getDriver()).newInstance();
		conn = DriverManager
            .getConnection(dbinfo.getDBURL() + dbinfo.getDBName(), dbinfo.getUsername(), dbinfo.getPassword());

        pst = conn
                .prepareStatement("UPDATE dyn_users SET dyn_users.forgot=1, dyn_users.key1=?, dyn_users.key2=?, dyn_users.forgot_date=? WHERE dyn_users.id=?;");
        pst.setString(1, key1);
        pst.setString(2, key2);    
        pst.setTimestamp(3, new Timestamp(System.currentTimeMillis()));
        pst.setInt(4, userid);
        
        recordUpdated = pst.executeUpdate();
        
        if (recordUpdated > 0) {            
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
       
    }

    return result;
}
	
	public static int isEmailorUser(String user, DB dbinfo) {
		Connection conn = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        int userid =-1;

        try {
            Class.forName(dbinfo.getDriver()).newInstance();
            conn = DriverManager
                    .getConnection(dbinfo.getDBURL() + dbinfo.getDBName(), dbinfo.getUsername(), dbinfo.getPassword());

            pst = conn.prepareStatement("SELECT dyn_users.id FROM dyn_server_db.dyn_users WHERE dyn_users.forgot=0 AND (dyn_users.email=? OR dyn_users.user=?);");
            
            pst.setString(1, user);
            pst.setString(2, user);
                        
            rs = pst.executeQuery();
            
            while (rs.next()) {
            	userid  = rs.getInt("id");
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

        return userid;
	}

	public static String getName(int userID, DB dbinfo) {

		String name = null; 
        Connection conn = null;
        PreparedStatement pst = null;
        ResultSet rs = null;

        try {
            Class.forName(dbinfo.getDriver()).newInstance();
            conn = DriverManager
                    .getConnection(dbinfo.getDBURL() + dbinfo.getDBName(), dbinfo.getUsername(), dbinfo.getPassword());

            pst = conn
                    .prepareStatement("SELECT dyn_users.id, dyn_users.user FROM dyn_server_db.dyn_users WHERE dyn_users.enabled=1 AND dyn_users.id=? LIMIT 1;");
            
            
            pst.setInt(1, userID);
            
            rs = pst.executeQuery();
            
            while (rs.next()) {
            	name = rs.getString("user");
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

        return name;
	}

	public static String getEmail(int userID, DB dbinfo) {
		
			String email = null; 
	        Connection conn = null;
	        PreparedStatement pst = null;
	        ResultSet rs = null;

	        try {
	            Class.forName(dbinfo.getDriver()).newInstance();
	            conn = DriverManager
	                    .getConnection(dbinfo.getDBURL() + dbinfo.getDBName(), dbinfo.getUsername(), dbinfo.getPassword());

	            pst = conn
	                    .prepareStatement("SELECT dyn_users.email FROM dyn_server_db.dyn_users WHERE dyn_users.enabled=1 AND dyn_users.id=? LIMIT 1;");
	            pst.setInt(1, userID);
	            rs = pst.executeQuery();
	            
	            while (rs.next()) {
	            	email = rs.getString("email");
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

	        return email;
		}
}
