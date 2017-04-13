package com.khoja.dao;

import com.khoja.classes.Hashing;
import com.khoja.classes.DB;
import com.khoja.classes.DynUser;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;

import javax.servlet.http.HttpServletRequest;

public class LoginDao {
    public static DynUser validate(String name, String pass, HttpServletRequest request, DB dbinfo) {     
        Connection conn = null;
        PreparedStatement pst = null;
        ResultSet rs = null;

        
        try {
            Class.forName(dbinfo.getDriver()).newInstance();
            conn = DriverManager
                    .getConnection(dbinfo.getDBURL() + dbinfo.getDBName(), dbinfo.getUsername(), dbinfo.getPassword());

            pst = conn.prepareStatement("SELECT dyn_users.id, dyn_users.email, dyn_users.user, dyn_users.enabled, dyn_users.create_date, dyn_users.admin FROM dyn_server_db.dyn_users WHERE dyn_users.user=? and dyn_users.password=? and enabled=1 ;");
            pst.setString(1, name);
            pst.setString(2, Hashing.returnMD5(pass));
            
            rs = pst.executeQuery();
            
            while (rs.next()) {
            	int userid  = rs.getInt("id");
	            String uname = rs.getString("user");
	            boolean admin = rs.getBoolean("admin");
	            boolean enabled = rs.getBoolean("enabled");
	            String email = rs.getString("email");
            	String ucreatedate = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss").format(rs.getTimestamp("create_date"));
	            return new DynUser(email, userid, admin, uname, enabled, ucreatedate);
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

        return null;
    }
}