package com.khoja.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.khoja.classes.DB;

public class DNSDao {

	public static boolean addHostname(String newhostname, int domainID, int userID, DB dbinfo) {
		boolean output = false;
        Connection conn = null;
        PreparedStatement pst = null;
        ResultSet rs = null;

        try {
            Class.forName(dbinfo.getDriver()).newInstance();
            conn = DriverManager
                    .getConnection(dbinfo.getDBURL() + dbinfo.getDBName(), dbinfo.getUsername(), dbinfo.getPassword());
            
            String SQLquery = "INSERT INTO dyn_server_db.dyn_dns_records (dyn_dns_records.zone, dyn_dns_records.host, dyn_dns_records.type, dyn_dns_records.data, dyn_dns_records.ttl, dyn_dns_records.refresh, dyn_dns_records.retry, dyn_dns_records.expire, dyn_dns_records.minimum, dyn_dns_records.serial, dyn_dns_records.resp_person, dyn_dns_records.primary_ns, dyn_dns_records.domainID, dyn_dns_records.userID, dyn_dns_records.hostID) VALUES "
            		+ "((SELECT domain_name from dyn_server_db.dyn_domains WHERE id="+domainID+" LIMIT 1), '"+newhostname+"', 'A', (SELECT root_domain_ip FROM dyn_server_db.dyn_properties LIMIT 1), (SELECT ttl FROM dyn_server_db.dyn_properties LIMIT 1), NULL, NULL, NULL, NULL, NULL, NULL, NULL, "+domainID+", "+userID+", (SELECT id from dyn_server_db.dyn_hosts WHERE hostname='"+newhostname+"' AND domain_id="+domainID+" LIMIT 1));";
            
            
            System.out.println(SQLquery);
            System.out.println();

            pst = conn.prepareStatement(SQLquery);
                 
            boolean queryComplete= pst.execute();
            
            if (queryComplete) {
            	System.out.println("Host Added - Host Name: "+newhostname);
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

	public static boolean setHostname(String newip, int userID, int hostID, DB dbinfo) {
		boolean output = false;
        Connection conn = null;
        PreparedStatement pst = null;
        ResultSet rs = null;

        try {
            Class.forName(dbinfo.getDriver()).newInstance();
            conn = DriverManager
                    .getConnection(dbinfo.getDBURL() + dbinfo.getDBName(), dbinfo.getUsername(), dbinfo.getPassword());

            pst = conn.prepareStatement("UPDATE dyn_server_db.dyn_dns_records SET dyn_dns_records.data='?' WHERE dyn_dns_records.userID=? AND dyn_dns_records.hostID=?;");
	        
            pst.setString(1, newip);        // domain id
            pst.setInt(2, userID);          // user id
            pst.setInt(3, hostID);          // host id  
            
            int queryComplete= pst.executeUpdate();
            
            if (queryComplete > 0) {
            	System.out.println("Hostname DNS Entry Modified - Hostname ID: "+hostID);
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
	
	public static boolean deleteHostname(int hostid, DB dbinfo) {
		boolean result = false;
		Connection conn = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
            
		try {
			Class.forName(dbinfo.getDriver()).newInstance();
			conn = DriverManager
                .getConnection(dbinfo.getDBURL() + dbinfo.getDBName(), dbinfo.getUsername(), dbinfo.getPassword());

			pst = conn
                .prepareStatement("DELETE FROM dyn_dns_records WHERE dyn_dns_records.hostID=?;");
			pst.setInt (1, hostid);

			int success = pst.executeUpdate();
        
			if (success > -1) {   
				System.out.println("Hostname DNS Entry Deleted - Hostname ID: "+hostid);
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
	
	public static boolean deleteUserHostname(int userid, DB dbinfo) {
		boolean result = false;
		Connection conn = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
            
		try {
			Class.forName(dbinfo.getDriver()).newInstance();
			conn = DriverManager
                .getConnection(dbinfo.getDBURL() + dbinfo.getDBName(), dbinfo.getUsername(), dbinfo.getPassword());

			pst = conn
                .prepareStatement("DELETE FROM dyn_dns_records WHERE dyn_dns_records.userID=?;");
			pst.setInt (1, userid);

			int success = pst.executeUpdate();
        
			if (success > -1) {   
				System.out.println("User's Hosts Deteled from DNS - User ID: "+userid);
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
	
	public static boolean deleteDomain(int domainID, DB dbinfo) {
		boolean result = false;
		Connection conn = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
            
		try {
			Class.forName(dbinfo.getDriver()).newInstance();
			conn = DriverManager
                .getConnection(dbinfo.getDBURL() + dbinfo.getDBName(), dbinfo.getUsername(), dbinfo.getPassword());

			pst = conn
                .prepareStatement("DELETE FROM dyn_dns_records WHERE dyn_dns_records.domainID=?;");
			pst.setInt (1, domainID);

			int success = pst.executeUpdate();
        
			if (success > -1) {   
				System.out.println("Domain and associated hosts deteled from DNS - Domain ID: "+domainID);
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
	

	public static boolean newDomain(String domainname, DB dbinfo) {
		boolean result = false;
	    Connection conn = null;
	    PreparedStatement pst = null;
	    ResultSet rs = null;
	
	   String SQLQuery = "INSERT INTO dyn_server_db.dyn_dns_records (dyn_dns_records.zone, dyn_dns_records.host, dyn_dns_records.type, dyn_dns_records.data, dyn_dns_records.ttl, dyn_dns_records.refresh, dyn_dns_records.retry, dyn_dns_records.expire, dyn_dns_records.minimum, dyn_dns_records.serial, dyn_dns_records.resp_person, dyn_dns_records.primary_ns, dyn_dns_records.domainID, dyn_dns_records.userID , dyn_dns_records.hostID) VALUES "
	    		+ "('"+domainname.trim()+"', '@', 'SOA', NULL, (SELECT ttl FROM dyn_server_db.dyn_properties LIMIT 1), (SELECT refresh FROM dyn_server_db.dyn_properties LIMIT 1), (SELECT retry FROM dyn_server_db.dyn_properties LIMIT 1), (SELECT expire FROM dyn_server_db.dyn_properties LIMIT 1), (SELECT minimum FROM dyn_server_db.dyn_properties LIMIT 1), (SELECT serial FROM dyn_server_db.dyn_properties LIMIT 1), (SELECT resp_person FROM dyn_server_db.dyn_properties LIMIT 1), 'ns1', (SELECT dyn_domains.id from dyn_server_db.dyn_domains WHERE dyn_domains.domain_name = '"+domainname.trim()+"'), NULL, NULL), "
	    		+ "('"+domainname.trim()+"', '@', 'NS', 'ns1', (SELECT ttl FROM dyn_server_db.dyn_properties LIMIT 1), NULL, NULL, NULL, NULL, NULL, NULL, NULL, (SELECT dyn_domains.id from dyn_server_db.dyn_domains WHERE dyn_domains.domain_name='"+domainname.trim()+"' LIMIT 1), NULL, NULL), "
	    		+ "('"+domainname.trim()+"', '@', 'NS', 'ns2', (SELECT ttl FROM dyn_server_db.dyn_properties LIMIT 1), NULL, NULL, NULL, NULL, NULL, NULL, NULL, (SELECT dyn_domains.id from dyn_server_db.dyn_domains WHERE dyn_domains.domain_name='"+domainname.trim()+"' LIMIT 1), NULL, NULL), "
	    		+ "('"+domainname.trim()+"', '@', 'A', (SELECT root_domain_ip FROM dyn_server_db.dyn_properties LIMIT 1), (SELECT ttl FROM dyn_server_db.dyn_properties LIMIT 1), NULL, NULL, NULL, NULL, NULL, NULL, NULL, (SELECT dyn_domains.id from dyn_server_db.dyn_domains WHERE dyn_domains.domain_name='"+domainname.trim()+"' LIMIT 1), NULL, NULL), " 		
	    		+ "('"+domainname.trim()+"', 'ns1', 'A', (SELECT primary_ns FROM dyn_server_db.dyn_properties LIMIT 1), (SELECT ttl FROM dyn_server_db.dyn_properties LIMIT 1), NULL, NULL, NULL, NULL, NULL, NULL, NULL, (SELECT dyn_domains.id from dyn_server_db.dyn_domains WHERE dyn_domains.domain_name='"+domainname.trim()+"' LIMIT 1), NULL, NULL), " 
	    		+ "('"+domainname.trim()+"', 'ns2', 'A', (SELECT secondary_ns FROM dyn_server_db.dyn_properties LIMIT 1), (SELECT ttl FROM dyn_server_db.dyn_properties LIMIT 1), NULL, NULL, NULL, NULL, NULL, NULL, NULL, (SELECT dyn_domains.id from dyn_server_db.dyn_domains WHERE dyn_domains.domain_name='"+domainname.trim()+"' LIMIT 1), NULL, NULL), " 
	    		+ "('"+domainname.trim()+"', 'www', 'A', (SELECT root_domain_ip FROM dyn_server_db.dyn_properties LIMIT 1), (SELECT ttl FROM dyn_server_db.dyn_properties LIMIT 1), NULL, NULL, NULL, NULL, NULL, NULL, NULL, (SELECT dyn_domains.id from dyn_server_db.dyn_domains WHERE dyn_domains.domain_name='"+domainname.trim()+"' LIMIT 1), NULL, NULL);";
	    
	    
	    System.out.println(SQLQuery);
	    try {
	        Class.forName(dbinfo.getDriver()).newInstance();
	        conn = DriverManager
	                .getConnection(dbinfo.getDBURL() + dbinfo.getDBName(), dbinfo.getUsername(), dbinfo.getPassword());
	
	        pst = conn.prepareStatement(SQLQuery);
	        
	        int success = pst.executeUpdate();
	        
			if (success > -1) {   
				System.out.println("Domain Added to DNS - Domain Name: "+domainname);
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