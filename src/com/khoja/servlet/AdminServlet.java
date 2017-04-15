package com.khoja.servlet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.regex.Pattern;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.validator.routines.InetAddressValidator;

import com.khoja.classes.DB;
import com.khoja.classes.DynDomain;
import com.khoja.classes.DynHost;
import com.khoja.classes.DynProperties;
import com.khoja.classes.DynUser;
import com.khoja.dao.AdminDao;
import com.khoja.dao.DNSDao;
import com.mysql.jdbc.StringUtils;

public class AdminServlet extends HttpServlet {
	
	private static final long serialVersionUID = 1L;
	
	public static boolean checkDomainname(String hostname) {
    	
		String hostregex = "^(([a-zA-Z0-9]|[a-zA-Z0-9][a-zA-Z0-9\\-]*[a-zA-Z0-9])\\.)*([A-Za-z0-9]|[A-Za-z0-9][A-Za-z0-9\\-]*[A-Za-z0-9])$";
						
    	Pattern validchars = Pattern.compile(hostregex);
        Pattern length = Pattern.compile (".{5,32}");
        Pattern onedot = Pattern.compile ("^[^.]*(?:(?:\\.[^\\.]*){0,1})$");
               
        return validchars.matcher(hostname).matches()
        	      && length.matcher(hostname).find()
        		  && onedot.matcher(hostname).find();
	}

	
    public boolean isValidRetry(String numtext) {
    	boolean results = false;
    	int num;
    	try{
    		num = Integer.parseInt(numtext);	
    	}catch(NumberFormatException e){
    		return false;
    	}
    	
    	if ( num > 119 && num < 7201) {
    		results = true;
    	}
    	return results;   			
    }

	public boolean isValidTTL(String numtext) {
    	boolean results = false;
    	int num;
    	try{
    		num = Integer.parseInt(numtext);	
    	}catch(NumberFormatException e){
    		return false;
    	}
    	
    	if ( num > 0 && num < 604801) {
    		results = true;
    	}
    	return results;   			
    }

	public boolean isValidRefresh(String numtext) {
    	boolean results = false;
    	int num;
    	try{
    		num = Integer.parseInt(numtext);	
    	}catch(NumberFormatException e){
    		return false;
    	}
    	
    	if ( num > 1199 && num < 43201) {
    		results = true;
    	}
    	return results;   			
    }

	public boolean isValidExpire(String expire) {
    	boolean results = false;
    	int num;
    	try{
    		num = Integer.parseInt(expire);	
    	}catch(NumberFormatException e){
    		return false;
    	}
    	
    	if ( num > 1209599 && num < 2419201) {
    		results = true;
    	}
    	return results;   			
    }

	public boolean isValidPerson(String person) {
    	
    	Pattern charac = Pattern.compile("[a-zA-Z0-9]");
        Pattern eight = Pattern.compile (".{5,}");
        Pattern all = Pattern.compile ("^[\\s\\da-zA-Z0-9]*$");
        
               
        return eight.matcher(person).matches()
        	      && charac.matcher(person).find()
        	      && all.matcher(person).find();
    }

	
	public boolean isValidSerial(String serial) {
    	boolean results = false;
    	Long num;
    	Long max = Long.parseLong("4294967296");
    	try{
    		num = Long.parseLong(serial);	
    	}catch(NumberFormatException e){
    		return false;
    	}
    	
    	if ( num > 999999999 && num < max ) {
    		results = true;
    	}
    	return results;   			
    }	
	
	public boolean isValidMinimum(String min) {
    	boolean results = false;
    	int num;
    	try{
    		num = Integer.parseInt(min);	
    	}catch(NumberFormatException e){
    		return false;
    	}
    	if ( num > 299 && num < 86401) {
    		results = true;
    	}
    	return results;   			
    }
	
	public static boolean isValidInetAddress(final String address) {
	    if (StringUtils.isEmptyOrWhitespaceOnly(address)) {
	        return false;
	    }
	    if (InetAddressValidator.getInstance().isValid(address)) {
	        return true;
	    }

	    return false;
	}
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		DB dbProperties = new DB(getServletContext().getInitParameter("dbHost"), getServletContext().getInitParameter("dbPort"), getServletContext().getInitParameter("dbName"), getServletContext().getInitParameter("dbUser"), getServletContext().getInitParameter("dbPassword"));
    			
		ArrayList<DynHost> hosts = AdminDao.getHosts(dbProperties);
		ArrayList<DynDomain> domains = AdminDao.getDomains(dbProperties);
		ArrayList<DynUser> users = AdminDao.getUsers(dbProperties);
		DynProperties prop = AdminDao.getProperties(dbProperties);
		
        request.setAttribute("domainList", domains);
        request.setAttribute("hostList", hosts);
        request.setAttribute("userList", users);        
		
        request.setAttribute("systemProperties", prop);
        
		request.getRequestDispatcher(request.getContextPath() + "/app/admin/admin.jsp").forward(request, response);

	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		
		DB dbProperties = new DB(getServletContext().getInitParameter("dbHost"), getServletContext().getInitParameter("dbPort"), getServletContext().getInitParameter("dbName"), getServletContext().getInitParameter("dbUser"), getServletContext().getInitParameter("dbPassword"));
    			
		String enableUser=request.getParameter("enableUser"); 
		String disableUser=request.getParameter("disableUser");
		String deleteUser=request.getParameter("deleteUser"); 
		String adminUser=request.getParameter("adminUser"); 
		String noadminUser=request.getParameter("noadminUser");
		
		String deleteHost=request.getParameter("deleteHost"); 
		String deleteDomain=request.getParameter("deleteDomain"); 
		
		String addDomain=request.getParameter("addDomain"); 
		String addDomainname=request.getParameter("domainname"); 
		
		String ns1Set=request.getParameter("ns1set"); 
		String ns1=request.getParameter("ns1"); 
		
		String ns2Set=request.getParameter("ns2set"); 
		String ns2=request.getParameter("ns2"); 
		
		String rootSet=request.getParameter("rootset"); 
		String root=request.getParameter("root_domain_address"); 
		
		String retrySet=request.getParameter("retryset"); 
		String retry=request.getParameter("retry"); 
		
		String ttlSet=request.getParameter("ttlset"); 
		String ttl=request.getParameter("ttl"); 
		
		String minSet=request.getParameter("minset"); 
		String minimum=request.getParameter("minimum"); 
		
		String expireSet=request.getParameter("expireset"); 
		String expire=request.getParameter("expire"); 
		
		String refreshSet=request.getParameter("refreshset"); 
		String refresh=request.getParameter("refresh"); 
		
		String serialSet=request.getParameter("serialset"); 
		String serial=request.getParameter("serial"); 
		
		String personSet=request.getParameter("personset"); 
		String person=request.getParameter("person"); 
		
		HttpSession session = request.getSession(false);
		DynUser current_user = (DynUser) session.getAttribute("LoggedUser");
		
		if (addDomain != null && addDomain.equals("yes")) {
			if (checkDomainname(addDomainname)) {
				// Valid Domain Name
				if (!AdminDao.doesDomainExist(addDomainname, dbProperties)) {
					// Domain Name Not In List
					AdminDao.addDomain(addDomainname, dbProperties);
					request.setAttribute("domain_error", "<p style=\"color:green\">New domain name added ("+addDomainname+").</p>");
		        	System.out.println("New Domain Added - Domain Name: "+addDomainname+" User: "+current_user.getUsername());
				} else {
					request.setAttribute("domain_error", "<p style=\"color:red\">Domain already exists.</p>");
				}
			} else {
				request.setAttribute("domain_error", "<p style=\"color:red\">Domain names must be between 5 and 32 characters long, containing only letters, numbers, and hyphens(-). Domain names cannot start or end with a hyphen(-) and must have only one period(.).</p>");
			}
			RequestDispatcher rd = getServletContext().getRequestDispatcher(request.getContextPath() + "/app/admin/admin.jsp");
        	rd.forward(request, response);
		}
		
		if (deleteUser != null) {
			if (AdminDao.deleteZoneUser(AdminDao.getUserName(Integer.parseInt(deleteUser.trim()), dbProperties), dbProperties) && AdminDao.deleteUser(Integer.parseInt(deleteUser.trim()), dbProperties)) {
				
				System.out.println("User Deleted - User ID: "+deleteUser);
				if (AdminDao.deleteUserHosts(Integer.parseInt(deleteUser.trim()), dbProperties)) {
					DNSDao.deleteUserHostname(Integer.parseInt(deleteUser.trim()), dbProperties);
					request.setAttribute("users_error", "<p style=\"color:green\">User account deleted.</p>");
					System.out.println("Users Hosts Deleted - User ID: "+deleteUser);
				} else {
					request.setAttribute("users_error", "<p style=\"color:red\">An error occured when deleting users hosts.</p>");
					System.out.println("Error Users Hosts Not Deleted - User ID: "+deleteUser);
				}
			} else {
				request.setAttribute("users_error", "<p style=\"color:red\">An error occured while deleting the user.</p>");
				System.out.println("Error User Not Deleted - User ID: "+deleteUser);
			}
        	RequestDispatcher rd = getServletContext().getRequestDispatcher(request.getContextPath() + "/app/admin/admin.jsp");
        	rd.forward(request, response);
		}
		
		if (adminUser != null) {
			if (AdminDao.enableAdmin(Integer.parseInt(adminUser.trim()), dbProperties)) {
				request.setAttribute("users_error", "<p style=\"color:green\">Admin privilages added to user.</p>");
				System.out.println("User is Admin - User ID: "+adminUser);
			} else {
				request.setAttribute("users_error", "<p style=\"color:red\">An error occured while making the user an admin.</p>");
				System.out.println("Error Make User Admin - User ID: "+adminUser);
			}
        	RequestDispatcher rd = getServletContext().getRequestDispatcher(request.getContextPath() + "/app/admin/admin.jsp");
        	rd.forward(request, response);
		}
		
		if (noadminUser != null) {
			if (AdminDao.disableAdmin(Integer.parseInt(noadminUser.trim()), dbProperties)) {
				request.setAttribute("users_error", "<p style=\"color:green\">Admin privilages removed from user.</p>");
				System.out.println("User is Admin - User ID: "+noadminUser);
			} else {
				request.setAttribute("users_error", "<p style=\"color:red\">An error occured while removing user admin privileges.</p>");
				System.out.println("Error Remove User Admin - User ID: "+noadminUser);
			}
        	RequestDispatcher rd = getServletContext().getRequestDispatcher(request.getContextPath() + "/app/admin/admin.jsp");
        	rd.forward(request, response);
		}
		
		if (enableUser != null) {
			if (AdminDao.enableUser(Integer.parseInt(enableUser.trim()), dbProperties)) {
				request.setAttribute("users_error", "<p style=\"color:green\">User accout status set to Enabled.</p>");
				System.out.println("User is Enabled - User ID: "+enableUser);
			} else {
				request.setAttribute("users_error", "<p style=\"color:red\">An error occured while eabling the user account.</p>");
				System.out.println("Error Enabling User - User ID: "+enableUser);
			}
        	RequestDispatcher rd = getServletContext().getRequestDispatcher(request.getContextPath() + "/app/admin/admin.jsp");
        	rd.forward(request, response);
		}
		
		if (disableUser != null) {
			if (AdminDao.disableUser(Integer.parseInt(disableUser.trim()), dbProperties)) {
				request.setAttribute("users_error", "<p style=\"color:green\">User accout status set to Disabled.</p>");
				System.out.println("User is Enabled - User ID: "+disableUser);
			} else {
				request.setAttribute("users_error", "<p style=\"color:red\">An error occured while disabling the user account.</p>");
				System.out.println("Error Disabling User - User ID: "+disableUser);
			}
        	RequestDispatcher rd = getServletContext().getRequestDispatcher(request.getContextPath() + "/app/admin/admin.jsp");
        	rd.forward(request, response);
		}

		if (deleteHost != null) {
			if (AdminDao.deleteUserHost(Integer.parseInt(deleteHost), dbProperties)) {
				request.setAttribute("hosts_error", "<p style=\"color:green\">Hostname deleted.</p>");
				System.out.println("Host Deleted - User ID: "+deleteHost);
				DNSDao.deleteHostname(Integer.parseInt(deleteHost), dbProperties);
			} else {
				request.setAttribute("hosts_error", "<p style=\"color:red\">An error occured while deleting host.</p>");
				System.out.println("Error Host Not Deleted - User ID: "+deleteHost);
			}
        	RequestDispatcher rd = getServletContext().getRequestDispatcher(request.getContextPath() + "/app/admin/admin.jsp");
        	rd.forward(request, response);
		}
		
		if (deleteDomain != null) {
			if (AdminDao.deleteDomain(Integer.parseInt(deleteDomain.trim()), dbProperties)) {
				System.out.println("Domain deleted - User ID: "+deleteDomain);
				if (AdminDao.deleteDomainHosts(Integer.parseInt(deleteDomain.trim()), dbProperties)) {
					DNSDao.deleteDomain(Integer.parseInt(deleteDomain.trim()), dbProperties);
					request.setAttribute("domains_error", "<p style=\"color:green\">Domain name deleted.</p>");
					System.out.println("Domains Hosts Deleted - User ID: "+deleteDomain);
				} else {
					request.setAttribute("domains_error", "<p style=\"color:red\">An error occured when deleting domains hosts.</p>");
					System.out.println("Error Domains Hosts Not Deleted - User ID: "+deleteDomain);
				}
				
			} else {
				request.setAttribute("domains_error", "<p style=\"color:red\">An error occured while deleting domain.</p>");
				System.out.println("Error Domain Not Deleted - User Name: "+current_user.getUsername());
			}
        	RequestDispatcher rd = getServletContext().getRequestDispatcher(request.getContextPath() + "/app/admin/admin.jsp");
        	rd.forward(request, response);
		}
		
		if (ns1Set != null && ns1Set.equals("yes")) {
			
			RequestDispatcher rd = getServletContext().getRequestDispatcher(request.getContextPath() + "/app/admin/admin.jsp");
			
			if (isValidInetAddress(ns1)) {
				if ( AdminDao.setNS1(ns1, dbProperties)) {
					request.setAttribute("property_error", "<p style=\"color:green\"> NS1 IP address updated ("+ns1+").</p>");
		        	System.out.println("NS1 IP Address Updated - IP Address: "+ns1);
				} else {
					request.setAttribute("property_error", "<p style=\"color:red\">Error updating IP.</p>");
		        }
			} else {
				request.setAttribute("property_error", "<p style=\"color:red\">Invalid IP address.</p>");	
			}
			rd.forward(request, response);
		}
		
		if (ns2Set != null && ns2Set.equals("yes")) {
			
			RequestDispatcher rd = getServletContext().getRequestDispatcher(request.getContextPath() + "/app/admin/admin.jsp");
			
			if (isValidInetAddress(ns2)) {
				if ( AdminDao.setNS2(ns2, dbProperties)) {
					request.setAttribute("property_error", "<p style=\"color:green\"> NS2 IP address updated ("+ns2+").</p>");
		        	System.out.println("NS2 IP Address Updated - IP Address: "+ns2);
				} else {
					request.setAttribute("property_error", "<p style=\"color:red\">Error updating IP.</p>");
		        }
			} else {
				request.setAttribute("property_error", "<p style=\"color:red\">Invalid IP address.</p>");	
			}
			rd.forward(request, response);
		}
		
		if (rootSet != null && rootSet.equals("yes")) {
			
			RequestDispatcher rd = getServletContext().getRequestDispatcher(request.getContextPath() + "/app/admin/admin.jsp");
			
			if (isValidInetAddress(root)) {
				if ( AdminDao.setRoot(root, dbProperties)) {
					request.setAttribute("property_error", "<p style=\"color:green\"> Host IP address updated ("+root+").</p>");
		        	System.out.println("Host IP Address Updated - IP Address: "+root);
				} else {
					request.setAttribute("property_error", "<p style=\"color:red\">Error updating IP.</p>");
		        }
			} else {
				request.setAttribute("property_error", "<p style=\"color:red\">Invalid IP address.</p>");	
			}
			rd.forward(request, response);
		}
		
		if (retrySet != null && retrySet.equals("yes")) {
			
			RequestDispatcher rd = getServletContext().getRequestDispatcher(request.getContextPath() + "/app/admin/admin.jsp");
			
			if (isValidRetry(retry)) {
				if ( AdminDao.setRetry(Integer.parseInt(retry), dbProperties)) {
					request.setAttribute("property_error", "<p style=\"color:green\"> Retry is updated ("+retry+").</p>");
		        	System.out.println("Retry Value Updated - Retry: "+retry);
				} else {
					request.setAttribute("property_error", "<p style=\"color:red\">Error updating retry value.</p>");
		        }
			} else {
				request.setAttribute("property_error", "<p style=\"color:red\">Invalid imput. Retry must be a number between 120 and 7200.</p>");	
			}
			rd.forward(request, response);
		}
		
		if (ttlSet != null && ttlSet.equals("yes")) {
			
			RequestDispatcher rd = getServletContext().getRequestDispatcher(request.getContextPath() + "/app/admin/admin.jsp");
			
			if (isValidTTL(ttl)) {
				if ( AdminDao.setRetry(Integer.parseInt(ttl), dbProperties)) {
					request.setAttribute("property_error", "<p style=\"color:green\"> TTL is updated ("+ttl+").</p>");
		        	System.out.println("TTL Value Updated - TTL: "+ttl);
				} else {
					request.setAttribute("property_error", "<p style=\"color:red\">Error updating ttl value.</p>");
		        }
			} else {
				request.setAttribute("property_error", "<p style=\"color:red\">Invalid imput. TTL must be a number between 0 and 604800 (eg. 180).</p>");	
			}
			rd.forward(request, response);
		}
		
		if (expireSet != null && expireSet.equals("yes")) {
			
			RequestDispatcher rd = getServletContext().getRequestDispatcher(request.getContextPath() + "/app/admin/admin.jsp");
			
			if (isValidExpire(expire)) {
				if ( AdminDao.setExpire(Integer.parseInt(expire), dbProperties)) {
					request.setAttribute("property_error", "<p style=\"color:green\"> Expire value is updated ("+expire+").</p>");
		        	System.out.println("Expire Value Updated - Expire: "+expire);
				} else {
					request.setAttribute("property_error", "<p style=\"color:red\">Error updating expire value.</p>");
		        }
			} else {
				request.setAttribute("property_error", "<p style=\"color:red\">Invalid imput. Expire must be a number between 1209600 and 2419200.</p>");	
			}
			rd.forward(request, response);
		}
		
		if (refreshSet != null && refreshSet.equals("yes")) {
			
			RequestDispatcher rd = getServletContext().getRequestDispatcher(request.getContextPath() + "/app/admin/admin.jsp");
			
			if (isValidRefresh(refresh)) {
				if ( AdminDao.setRefresh(Integer.parseInt(refresh), dbProperties)) {
					request.setAttribute("property_error", "<p style=\"color:green\"> Refresh value is updated ("+refresh+").</p>");
		        	System.out.println("Refresh Value Updated - Refresh: "+refresh);
				} else {
					request.setAttribute("property_error", "<p style=\"color:red\">Error updating refresh value.</p>");
		        }
			} else {
				request.setAttribute("property_error", "<p style=\"color:red\">Invalid imput. Refresh must be a number between 1200 and 43200.</p>");	
			}
			rd.forward(request, response);
		}
		
		if (minSet != null && minSet.equals("yes")) {
			
			RequestDispatcher rd = getServletContext().getRequestDispatcher(request.getContextPath() + "/app/admin/admin.jsp");
			
			if (isValidMinimum(minimum)) {
				if ( AdminDao.setMinimum(Integer.parseInt(refresh), dbProperties)) {
					request.setAttribute("property_error", "<p style=\"color:green\"> Minimum value is updated ("+minimum+").</p>");
		        	System.out.println("Minimum Value Updated - Minimum: "+minimum);
				} else {
					request.setAttribute("property_error", "<p style=\"color:red\">Error updating Minimum value.</p>");
		        }
			} else {
				request.setAttribute("property_error", "<p style=\"color:red\">Invalid imput. Minimum must be a number between 300 and 86400.</p>");	
			}
			rd.forward(request, response);
		}
		
		if (serialSet != null && serialSet.equals("yes")) {
			
			RequestDispatcher rd = getServletContext().getRequestDispatcher(request.getContextPath() + "/app/admin/admin.jsp");
			
			if (isValidSerial(serial)) {
				if ( AdminDao.setSerial(serial, dbProperties)) {
					request.setAttribute("property_error", "<p style=\"color:green\"> Serial value is updated ("+serial+").</p>");
		        	System.out.println("Serial Value Updated - Serial: "+serial);
				} else {
					request.setAttribute("property_error", "<p style=\"color:red\">Error updating Serial value.</p>");
		        }
			} else {
				request.setAttribute("property_error", "<p style=\"color:red\">Invalid imput. Serial Numbers must be 10 digits long, with a maximum value of 4294967295. The first 8 digits should be the date of the last update (eg. YYYYMMDD - 20170326).</p>");	
			}
			rd.forward(request, response);
		}
		
		if (personSet != null && personSet.equals("yes")) {
			
			RequestDispatcher rd = getServletContext().getRequestDispatcher(request.getContextPath() + "/app/admin/admin.jsp");
			
			if (isValidPerson(person)) {
				if ( AdminDao.setPerson(person, dbProperties)) {
					request.setAttribute("property_error", "<p style=\"color:green\"> Person value is updated ("+person+").</p>");
		        	System.out.println("Person Value Updated - Person: "+person);
				} else {
					request.setAttribute("property_error", "<p style=\"color:red\">Error updating Person value.</p>");
		        }
			} else {
				request.setAttribute("property_error", "<p style=\"color:red\">Invalid imput. Person must be 5 or more characters long, and must be only letters and numbers.</p>");	
			}
			rd.forward(request, response);
		}
		
		
		doGet(request, response);
	}
}