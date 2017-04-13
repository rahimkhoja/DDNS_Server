package com.khoja.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;

import com.khoja.classes.DB;
import com.khoja.classes.DynUser;
import com.khoja.classes.KeyGenerator;

public class ValidateDao {
	
	public static boolean setEnabled(int userID, DB dbinfo) {
		boolean result = false;
	    Connection conn = null;
	    PreparedStatement pst = null;
	    int recordUpdated = 0;
	            
	    try {
			Class.forName(dbinfo.getDriver()).newInstance();
			conn = DriverManager
	            .getConnection(dbinfo.getDBURL() + dbinfo.getDBName(), dbinfo.getUsername(), dbinfo.getPassword());

	        pst = conn
	                .prepareStatement("UPDATE dyn_server_db.dyn_users SET dyn_users.enabled=1, dyn_users.key1=?, dyn_users.key2=?, dyn_users.enable_date=? WHERE dyn_users.id=? AND dyn_users.forgot=0");
	        pst.setString(1, KeyGenerator.GenerateKey());
	        pst.setString(2, KeyGenerator.GenerateKey());
	        pst.setTimestamp(3, new Timestamp(System.currentTimeMillis()));
	        pst.setInt(4, userID);
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
	
	public static boolean setNotForgot(int userID, DB dbinfo) {
		boolean result = false;
	    Connection conn = null;
	    PreparedStatement pst = null;
	    int recordUpdated = 0;
	            
	    try {
			Class.forName(dbinfo.getDriver()).newInstance();
			conn = DriverManager
	            .getConnection(dbinfo.getDBURL() + dbinfo.getDBName(), dbinfo.getUsername(), dbinfo.getPassword());

	        pst = conn
	                .prepareStatement("UPDATE dyn_server_db.dyn_users SET dyn_users.forgot=0, dyn_users.key1=?, dyn_users.key2=? WHERE dyn_users.id=? AND dyn_users.enabled=1");
	        pst.setString(1, KeyGenerator.GenerateKey());
	        pst.setString(2, KeyGenerator.GenerateKey());
	        pst.setInt(3, userID);
	        
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
	
	public static boolean checkForgot(String email, String key1, String key2, DB dbinfo) {
		boolean result = false;
        Connection conn = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
                
        try {
			Class.forName(dbinfo.getDriver()).newInstance();
			conn = DriverManager
                .getConnection(dbinfo.getDBURL() + dbinfo.getDBName(), dbinfo.getUsername(), dbinfo.getPassword());

            pst = conn.prepareStatement("SELECT dyn_users.id, dyn_users.email, dyn_users.user, dyn_users.enabled, dyn_users.create_date, dyn_users.admin, dyn_users.forgot FROM dyn_server_db.dyn_users WHERE dyn_users.email=? AND dyn_users.key1=? AND dyn_users.key2=? AND dyn_users.enabled=1 AND dyn_users.forgot=1 LIMIT 1;");
            pst.setString(1, email);
            pst.setString(2, key1);
            pst.setString(3, key2);
            rs = pst.executeQuery();
            
            if (!rs.next()) {            
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
	
	public static boolean enableUserZone( String username , DB dbinfo) {
		boolean result = false;
        Connection conn = null;
        PreparedStatement pst = null;
        int updated = 0;
                
        try {
			Class.forName(dbinfo.getDriver()).newInstance();
			conn = DriverManager
                .getConnection(dbinfo.getDBURL() + dbinfo.getDBName(), dbinfo.getUsername(), dbinfo.getPassword());

            pst = conn.prepareStatement("insert into dyn_user_roles (user, role_name) values (?, ?);");
            pst.setString(1, username);
            pst.setString(2, "dynusers");
            updated = pst.executeUpdate();
            
            if (updated > 0) {            
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
	
	
	public static boolean checkNotEnabled(String email, String key1, String key2, DB dbinfo) {
		boolean result = false;
        Connection conn = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
                
        try {
			Class.forName(dbinfo.getDriver()).newInstance();
			conn = DriverManager
                .getConnection(dbinfo.getDBURL() + dbinfo.getDBName(), dbinfo.getUsername(), dbinfo.getPassword());

            pst = conn.prepareStatement("SELECT dyn_users.id, dyn_users.email, dyn_users.user, dyn_users.enabled, dyn_users.create_date, dyn_users.admin, dyn_users.forgot FROM dyn_server_db.dyn_users WHERE dyn_users.email=? AND dyn_users.key1=? AND dyn_users.key2=? AND dyn_users.enabled=0 AND dyn_users.forgot=0 LIMIT 1;");
            pst.setString(1, email);
            pst.setString(2, key1);
            pst.setString(3, key2);
            rs = pst.executeQuery();
            
            if (!rs.next()) {            
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
	
	public static DynUser loadUser(String email, String key1, String key2, DB dbinfo) {       
        Connection conn = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        DynUser output = null;

        
        try {
            Class.forName(dbinfo.getDriver()).newInstance();
            conn = DriverManager
                    .getConnection(dbinfo.getDBURL() + dbinfo.getDBName(), dbinfo.getUsername(), dbinfo.getPassword());

            pst = conn.prepareStatement("SELECT dyn_users.id, dyn_users.email, dyn_users.user, dyn_users.enabled, dyn_users.create_date, dyn_users.admin, dyn_users.forgot FROM dyn_server_db.dyn_users WHERE dyn_users.email=? AND dyn_users.key1=? AND dyn_users.key2=?;");
            pst.setString(1, email);
            pst.setString(2, key1);
            pst.setString(3, key2);
            
            rs = pst.executeQuery();
            
            while (rs.next()) {
            	System.out.println("User Account Validated - User Email: "+email);
            	int userid  = rs.getInt("id");
	            String uname = rs.getString("user");
	            boolean admin = rs.getBoolean("admin");
	            boolean enabled = rs.getBoolean("enabled");
	            boolean forgot = rs.getBoolean("forgot");
	            String uemail = rs.getString("email");
	        	String ucreatedate = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss").format(rs.getTimestamp("create_date"));
            	output =  new DynUser(uemail, userid, admin, uname, enabled, ucreatedate, forgot);
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
        return output;
    }

	public static boolean validateUser(String email, String key1, String key2, DB dbinfo) {        
        Connection conn = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        boolean output = false;

        
        try {
        	Class.forName(dbinfo.getDriver()).newInstance();
            conn = DriverManager
                    .getConnection(dbinfo.getDBURL() + dbinfo.getDBName(), dbinfo.getUsername(), dbinfo.getPassword());

            pst = conn.prepareStatement("SELECT dyn_users.id, dyn_users.email, dyn_users.user, dyn_users.enabled, dyn_users.create_date, dyn_users.admin FROM dyn_server_db.dyn_users WHERE dyn_users.email=? AND dyn_users.key1=? AND dyn_users.key2=?;");
            pst.setString(1, email);
            pst.setString(2, key1);
            pst.setString(3, key2);
            
            
            
            rs = pst.executeQuery();
            
            while (rs.next()) {
            	System.out.println("User Account Validated - User Email: "+email);
            	output = true;
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

        return output;
    }
}
	
