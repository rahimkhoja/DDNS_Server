package com.khoja.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import com.khoja.classes.DynHost;
import com.khoja.classes.DynUser;
import com.khoja.classes.DB;
import com.khoja.classes.DynDomain;

public class DynDao {

	public static ArrayList<DynHost> getHosts(int userID, DB dbinfo) {
		ArrayList<DynHost> hostlist = new ArrayList<DynHost>(); 
        Connection conn = null;
        PreparedStatement pst = null;
        ResultSet rs = null;

        try {
            Class.forName(dbinfo.getDriver()).newInstance();
            conn = DriverManager
                    .getConnection(dbinfo.getDBURL() + dbinfo.getDBName(), dbinfo.getUsername(), dbinfo.getPassword());

            pst = conn.prepareStatement("SELECT dyn_hosts.id, dyn_hosts.hostname, dyn_hosts.last_update, dyn_hosts.domain_id, dyn_domains.domain_name, dyn_hosts.ip_address FROM dyn_server_db.dyn_hosts, dyn_server_db.dyn_domains WHERE dyn_domains.id=dyn_hosts.domain_id AND dyn_hosts.dyn_user_id=?;");
            
            pst.setInt(1, userID);
            rs = pst.executeQuery();
            
            while (rs.next()) {
            	int recid  = rs.getInt("id");
	            String hname = rs.getString("hostname");
	            String domname = rs.getString("domain_name");
	            String lupdate = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss").format(rs.getTimestamp("last_update"));
	            String ip = rs.getString("ip_address");
	            hostlist.add(new DynHost(hname, domname, recid, lupdate, ip));
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

        return hostlist;
	}
	
	public static ArrayList<DynDomain> getDomains(DB dbinfo) {
		
		
		ArrayList<DynDomain> domainlist = new ArrayList<DynDomain>(); 
        Connection conn = null;
        PreparedStatement pst = null;
        ResultSet rs = null;

        try {
            Class.forName(dbinfo.getDriver()).newInstance();
            conn = DriverManager
                    .getConnection(dbinfo.getDBURL() + dbinfo.getDBName(), dbinfo.getUsername(), dbinfo.getPassword());

            pst = conn
                    .prepareStatement("SELECT id, domain_name FROM dyn_server_db.dyn_domains;");
            
            rs = pst.executeQuery();
            
            while (rs.next()) {
            	int recid  = rs.getInt("id");
	            String domname = rs.getString("domain_name");
	            domainlist.add(new DynDomain(recid, domname));
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

        return domainlist;
	}
	
	
	public static boolean addHost(String hostname, int domid, int userid, DB dbinfo) {
		boolean result = false;
		Connection conn = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
            
		try {
			Class.forName(dbinfo.getDriver()).newInstance();
			conn = DriverManager
                .getConnection(dbinfo.getDBURL() + dbinfo.getDBName(), dbinfo.getUsername(), dbinfo.getPassword());

			pst = conn
                .prepareStatement("insert into dyn_hosts (hostname, domain_id, dyn_user_id, ip_address) values (?, ?, ?, (SELECT root_domain_ip FROM dyn_server_db.dyn_properties LIMIT 1));");
			pst.setString (1, hostname);
			pst.setInt (2, domid);
			pst.setInt (3, userid);

			int success = pst.executeUpdate();
        
			if (success > 0) {   
				System.out.println("New Hostname Added - Hostname: "+hostname+" - Domain ID: "+domid+" - User ID: "+userid);
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

	public static boolean doesHostExist(String host, int domainid, DB dbinfo) {
        boolean result = true;
        Connection conn = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
                
        try {
			Class.forName(dbinfo.getDriver()).newInstance();
			conn = DriverManager
                .getConnection(dbinfo.getDBURL() + dbinfo.getDBName(), dbinfo.getUsername(), dbinfo.getPassword());

            pst = conn
                    .prepareStatement("SELECT hostname, domain_id FROM dyn_server_db.dyn_hosts WHERE hostname=? AND domain_id=?"
                    		+ ";");
            pst.setString(1, host);
            pst.setInt(2, domainid);
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
	
	public static boolean removeHost(int hostid, int userid, DB dbinfo) {
		boolean result = false;
		Connection conn = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
            
		try {
			Class.forName(dbinfo.getDriver()).newInstance();
			conn = DriverManager
                .getConnection(dbinfo.getDBURL() + dbinfo.getDBName(), dbinfo.getUsername(), dbinfo.getPassword());

			pst = conn
                .prepareStatement("DELETE FROM dyn_hosts WHERE id = ? AND dyn_user_id=?;");
			pst.setInt (1, hostid);
			pst.setInt (2, userid);

			int success = pst.executeUpdate();
        
			if (success > 0) {   
				System.out.println("Hostname Deleted - Hostname ID: "+hostid+" - User ID: "+userid);
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
	
	public static boolean deleteUser(int userid, String username, DB dbinfo) {
		boolean result = false;
		Connection conn = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
            
		try {
			Class.forName(dbinfo.getDriver()).newInstance();
			conn = DriverManager
                .getConnection(dbinfo.getDBURL() + dbinfo.getDBName(), dbinfo.getUsername(), dbinfo.getPassword());

			pst = conn
                .prepareStatement("DELETE FROM dyn_users WHERE id = ? AND user=?;");
			pst.setInt (1, userid);
			pst.setString (2, username);

			int success = pst.executeUpdate();
        
			if (success > 0) {   
				System.out.println("User Deleted - User ID: "+userid+" - User Name: "+username);
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


	public static boolean deleteZoneUser(String username, DB dbinfo) {
		boolean result = false;
		Connection conn = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
            
		try {
			Class.forName(dbinfo.getDriver()).newInstance();
			conn = DriverManager
                .getConnection(dbinfo.getDBURL() + dbinfo.getDBName(), dbinfo.getUsername(), dbinfo.getPassword());

			pst = conn
                .prepareStatement("DELETE FROM dyn_user_roles WHERE user=?;");
			pst.setString (1, username);

			int success = pst.executeUpdate();
        
			if (success > 0) {   
				System.out.println("User Deleted User Role - User Name: "+username);
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
	
	public static boolean updateDNSHost(int hostID, String ip, int userID, DB dbinfo) {
		boolean result = false;
    Connection conn = null;
    PreparedStatement pst = null;
    int recordUpdated = 0;
            
    try {
		Class.forName(dbinfo.getDriver()).newInstance();
		conn = DriverManager
            .getConnection(dbinfo.getDBURL() + dbinfo.getDBName(), dbinfo.getUsername(), dbinfo.getPassword());

        pst = conn
                .prepareStatement("UPDATE dyn_server_db.dyn_dns_records SET data=? WHERE dyn_dns_records.userID=? AND dyn_dns_records.hostID=?;");
	        pst.setString(1, ip);
	        pst.setInt(2, userID);
	        pst.setInt(3, hostID);
        
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

	
	public static boolean updateHost(int hostID, String ip, int userID, DB dbinfo) {
			boolean result = false;
	    Connection conn = null;
	    PreparedStatement pst = null;
	    int recordUpdated = 0;
	            
	    try {
			Class.forName(dbinfo.getDriver()).newInstance();
			conn = DriverManager
	            .getConnection(dbinfo.getDBURL() + dbinfo.getDBName(), dbinfo.getUsername(), dbinfo.getPassword());

	        pst = conn
	                .prepareStatement("UPDATE dyn_server_db.dyn_hosts SET ip_address=?, last_update=? WHERE dyn_hosts.dyn_user_id=? AND dyn_hosts.id=?;");
		        pst.setString(1, ip);
		        pst.setTimestamp(2,new Timestamp(System.currentTimeMillis()));
		        pst.setInt(3, userID);
		        pst.setInt(4, hostID);
		        
	        
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
	
	public static boolean updatePassword(String password, DynUser current, DB dbinfo) {
		boolean result = false;
    Connection conn = null;
    PreparedStatement pst = null;
    int recordUpdated = 0;
            
    try {
		Class.forName(dbinfo.getDriver()).newInstance();
		conn = DriverManager
            .getConnection(dbinfo.getDBURL() + dbinfo.getDBName(), dbinfo.getUsername(), dbinfo.getPassword());

        pst = conn
                .prepareStatement("UPDATE dyn_server_db.dyn_users SET password=? WHERE dyn_users.id=?;");
	        pst.setString(1, password);
	        pst.setInt(2, current.getID());
        
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
}
