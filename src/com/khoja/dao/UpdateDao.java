package com.khoja.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;

import org.apache.commons.lang3.StringUtils;

import com.khoja.classes.DB;
import com.khoja.classes.DynUser;

public class UpdateDao {

	
	public static boolean isTheSame(String address, ArrayList<String> hosts, DynUser current, DB dbinfo) {
		
	    Connection conn = null;
	    PreparedStatement pst = null;
        String hostsCSV = StringUtils.join(hosts, "','");
        hostsCSV = "'" + hostsCSV + "'";

	    ResultSet rs = null;
	    
        ArrayList<Integer> number = new ArrayList<Integer>();
	            
	    try {
			Class.forName(dbinfo.getDriver()).newInstance();
			conn = DriverManager
	            .getConnection(dbinfo.getDBURL() + dbinfo.getDBName(), dbinfo.getUsername(), dbinfo.getPassword());

	        pst = conn
	                .prepareStatement("SELECT dyn_hosts.id FROM dyn_server_db.dyn_hosts, dyn_server_db.dyn_domains WHERE dyn_hosts.domain_id=dyn_domains.id AND dyn_hosts.dyn_user_id=? AND dyn_hosts.ip_address=? AND concat(dyn_hosts.hostname, '.', dyn_domains.domain_name) IN ("+hostsCSV+") AND last_update >= NOW() - INTERVAL 10 MINUTE;");
	                		
	        pst.setInt(1, current.getID());
	        pst.setString(2, address);
	        
	        System.out.println(pst);
	        
	        rs = pst.executeQuery();
	        
	        while (rs.next()) {
	        	number.add(rs.getInt("id"));
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
	    if ( number.size() == hosts.size()) {
	    	return true;
	    } else {
	    	return false;
	    }
	}
	
	public static boolean updateAddress(String address, ArrayList<Integer> hosts, DB dbinfo) {
		boolean result = false;
	    Connection conn = null;
	    PreparedStatement pst = null;
	    int recordUpdated = 0;
	            
	    try {
			Class.forName(dbinfo.getDriver()).newInstance();
			conn = DriverManager
	            .getConnection(dbinfo.getDBURL() + dbinfo.getDBName(), dbinfo.getUsername(), dbinfo.getPassword());

	        pst = conn
	                .prepareStatement("UPDATE dyn_server_db.dyn_hosts SET dyn_hosts.ip_address=?, dyn_hosts.last_update=? WHERE dyn_hosts.id IN ( "+Arrays.toString(hosts.toArray()).replace("[", "").replace("]", "")+" );");
	        pst.setString(1, address);
	        pst.setTimestamp(2, new Timestamp(System.currentTimeMillis()));
	        
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
	
	public static boolean updateDNSAddress(String address, ArrayList<Integer> hosts, DB dbinfo) {
		boolean result = false;
	    Connection conn = null;
	    PreparedStatement pst = null;
	    int recordUpdated = 0;
	            
	    try {
			Class.forName(dbinfo.getDriver()).newInstance();
			conn = DriverManager
	            .getConnection(dbinfo.getDBURL() + dbinfo.getDBName(), dbinfo.getUsername(), dbinfo.getPassword());

	        pst = conn
	                .prepareStatement("UPDATE dyn_server_db.dyn_dns_records SET dyn_dns_records.data=? WHERE dyn_dns_records.hostID IN ( "+Arrays.toString(hosts.toArray()).replace("[", "").replace("]", "")+" );");
	        pst.setString(1, address);
	        
	        System.out.println(pst);
	        
	        recordUpdated = pst.executeUpdate();
	        
	        System.out.println("Hosts Updated "+recordUpdated+" of "+hosts.size());
	        
	        if (recordUpdated == hosts.size()) {            
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
	
	public static ArrayList<Integer> checkHosts(ArrayList<String> hosts, DynUser current, DB dbinfo) {
		
		ArrayList<Integer> hostIDs = new ArrayList<Integer>();
        String hostsCSV = StringUtils.join(hosts, "','");
        hostsCSV = "'" + hostsCSV + "'";
        
		
		Connection conn = null;
	    PreparedStatement pst = null;
	    ResultSet rs = null;
	            
	    try {
			Class.forName(dbinfo.getDriver()).newInstance();
			conn = DriverManager
	            .getConnection(dbinfo.getDBURL() + dbinfo.getDBName(), dbinfo.getUsername(), dbinfo.getPassword());

	        pst = conn
	                .prepareStatement("SELECT dyn_hosts.id, dyn_hosts.hostname, dyn_domains.domain_name, concat(dyn_hosts.hostname, '.', dyn_domains.domain_name) as FQDN FROM dyn_server_db.dyn_hosts, dyn_server_db.dyn_domains WHERE dyn_hosts.domain_id=dyn_domains.id AND dyn_hosts.dyn_user_id=? AND concat(dyn_hosts.hostname, '.', dyn_domains.domain_name) IN ("+hostsCSV+");");
	        pst.setInt(1, current.getID() );
	        System.out.println(pst);
	        rs = pst.executeQuery();
	        
	        while (rs.next()) {
	        	hostIDs.add(rs.getInt("id"));
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
	    if (hosts.size() == hostIDs.size()) {
        	return hostIDs;
        } else {
	        return null;
        }
	}
	
}
