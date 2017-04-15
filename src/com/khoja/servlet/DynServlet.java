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
import com.khoja.classes.DynUser;
import com.khoja.classes.Hashing;
import com.khoja.dao.AdminDao;
import com.khoja.dao.DNSDao;
import com.khoja.dao.DynDao;
import com.khoja.dao.LoginDao;
import com.mysql.jdbc.StringUtils;


public class DynServlet extends HttpServlet {
	
	private static final long serialVersionUID = 1L;
	
	public static boolean checkHostname(String hostname) {
    	
		String hostregex = "^(([a-zA-Z0-9]|[a-zA-Z0-9][a-zA-Z0-9\\-]*[a-zA-Z0-9])\\.)*([A-Za-z0-9]|[A-Za-z0-9][A-Za-z0-9\\-]*[A-Za-z0-9])$";
						
    	Pattern validchars = Pattern.compile(hostregex);
        Pattern length = Pattern.compile (".{5,32}");
               
        return validchars.matcher(hostname).matches()
        	      && length.matcher(hostname).find();
	}

public static boolean checkPassword(String password) {
    	
    	Pattern letter = Pattern.compile("[a-zA-Z]");
        Pattern digit = Pattern.compile("[0-9]");
        Pattern special = Pattern.compile ("[!#$%&*()_+=<>?{}~-]");
        Pattern eight = Pattern.compile (".{8,}");
        Pattern all = Pattern.compile ("^[\\s\\da-zA-Z0-9!#$%&*()_+=<>?{}~-]*$");
        
               
        return eight.matcher(password).matches()
        	      && special.matcher(password).find()
        	      && digit.matcher(password).find()
        	      && letter.matcher(password).find()
        	      && all.matcher(password).find();
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
    	
		HttpSession session = request.getSession(false);
		DynUser current_user = (DynUser) session.getAttribute("LoggedUser");
		
		ArrayList<DynHost> hosts = DynDao.getHosts(current_user.getID(), dbProperties);
		ArrayList<DynDomain> domains = DynDao.getDomains(dbProperties);
        request.setAttribute("domainList", domains);
        request.setAttribute("hostList", hosts);
                
        
        if (request.getParameter("deleteaccount") != null) {
    		
			String deleteaccount = request.getParameter("deleteaccount");

			if (deleteaccount.equals("yes")) {
				DynDao.deleteUser(current_user.getID(), current_user.getUsername(), dbProperties);
				DynDao.deleteZoneUser(current_user.getUsername(), dbProperties);
				DNSDao.deleteUserHostname(current_user.getID(), dbProperties);
				AdminDao.deleteUserHosts(current_user.getID(), dbProperties);
				session.invalidate();
			}
		}
        
		if (request.getParameter("logout") != null) {
		
			String logout = request.getParameter("logout");

			if (logout.equals("yes")) {
				
				session.invalidate();
				
			}
		}
		
		request.getRequestDispatcher(request.getContextPath() + "/app/dashboard.jsp").forward(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		DB dbProperties = new DB(getServletContext().getInitParameter("dbHost"), getServletContext().getInitParameter("dbPort"), getServletContext().getInitParameter("dbName"), getServletContext().getInitParameter("dbUser"), getServletContext().getInitParameter("dbPassword"));
    	
		HttpSession session = request.getSession(false);
		DynUser current_user = (DynUser) session.getAttribute("LoggedUser");
		
		String add=request.getParameter("add"); 
		String addDomain=request.getParameter("domain"); 
		String addHostname=request.getParameter("hostname"); 
		String deleteHost=request.getParameter("deletehost"); 
		String setHost=request.getParameter("sethost"); 
		String updatePassword=request.getParameter("updatepassword");
		String currentPassword=request.getParameter("currentpassword");
		String password1=request.getParameter("newpassword1");
		String password2=request.getParameter("newpassword2");
		
		if (updatePassword != null && updatePassword.equals("yes") && !current_user.getForgot()) {
			if (LoginDao.validate(current_user.getUsername(), currentPassword, request, dbProperties) != null ) {
				if ( password1.equals(password2) ) {
					if (checkPassword(password1)) {
						request.setAttribute("password_error", "<p style=\"color:green\">Password updated.</p>");
						DynDao.updatePassword(Hashing.returnMD5(password1), current_user, dbProperties);
						System.out.println("Password Updated for User: "+current_user.getUsername());
					} else {
						request.setAttribute("password_error", "<p style=\"color:red\">Password must be 8 or more characters long with at least 1 letter, 1 number, and 1 special character (!#$%&*()_+=<>?{}~-).</p>");
					}
				} else {
					request.setAttribute("password_error", "<p style=\"color:red\">Passwords do not match.</p>");
		        }
			} else {
				request.setAttribute("password_error", "<p style=\"color:red\">Current password is incorrect.</p>");
			}
			
			RequestDispatcher rd = getServletContext().getRequestDispatcher(request.getContextPath() + "/app/dashboard.jsp");
	        rd.forward(request, response);
		}
		
		
		if (updatePassword != null && updatePassword.equals("yes") && current_user.getForgot()) {
				if ( password1.equals(password2) ) {
					if (checkPassword(password1)) {
						request.setAttribute("password_error", "<p style=\"color:green\">Password updated.</p>");
						DynDao.updatePassword(Hashing.returnMD5(password1), current_user, dbProperties);
						System.out.println("Password Updated for User: "+current_user.getUsername());
					} else {
						request.setAttribute("password_error", "<p style=\"color:red\">Password must be 8 or more characters long with at least 1 letter, 1 number, and 1 special character (!#$%&*()_+=<>?{}~-).</p>");
					}
				} else {
					request.setAttribute("password_error", "<p style=\"color:red\">Passwords do not match.</p>");
		        }
			RequestDispatcher rd = getServletContext().getRequestDispatcher(request.getContextPath() + "/app/dashboard.jsp");
	        rd.forward(request, response);
		}
		
		
		if (setHost != null && Integer.parseInt(setHost) > 0) {
			String hostIP=request.getParameter("IP"+setHost); 
			if (isValidInetAddress(hostIP)) {
				if ( DynDao.updateHost(Integer.parseInt(setHost), hostIP, current_user.getID(), dbProperties) && DynDao.updateDNSHost(Integer.parseInt(setHost), hostIP, current_user.getID(), dbProperties)) {
					request.setAttribute("hosts_error", "<p style=\"color:green\"> Hostname IP updated ("+hostIP+").</p>");
		        	System.out.println("Host IP Address Updated - User: "+current_user.getUsername()+" - IP Address: "+hostIP);
				} else {
					request.setAttribute("hosts_error", "<p style=\"color:red\">Error updating IP.</p>");
				}
			} else {
				request.setAttribute("hosts_error", "<p style=\"color:red\">Invalid IP address.</p>");
			}
			RequestDispatcher rd = getServletContext().getRequestDispatcher(request.getContextPath() + "/app/dashboard.jsp");
        	rd.forward(request, response);
		}
		
		
		if (add != null && add.equals("yes")) {
			if (checkHostname(addHostname)) {
				if (!DynDao.doesHostExist(addHostname, Integer.parseInt(addDomain.trim()), dbProperties)) {
					DynDao.addHost(addHostname, Integer.parseInt(addDomain.trim()), current_user.getID(), dbProperties);
					DNSDao.addHostname(addHostname, Integer.parseInt(addDomain.trim()), current_user.getID(), dbProperties);
					request.setAttribute("host_error", "<p style=\"color:green\">New hostname added ("+addHostname+").</p>");
		        	System.out.println("New Host Added - Hostname: "+addHostname+" User: "+current_user.getUsername());
				} else {
					request.setAttribute("host_error", "<p style=\"color:red\">Hostname already exists.</p>");
				}
			} else {
				request.setAttribute("host_error", "<p style=\"color:red\">Hostnames must be between 5 and 32 characters long, containing only letters, numbers, and hyphens(-). Hostnames cannot start or end with a hyphen(-).</p>");
	      	}
        	RequestDispatcher rd = getServletContext().getRequestDispatcher(request.getContextPath() + "/app/dashboard.jsp");
        	rd.forward(request, response);
		}
		
		if (deleteHost != null) {
			if (DynDao.removeHost(Integer.parseInt(deleteHost.trim()), current_user.getID(), dbProperties)) {
				request.setAttribute("hosts_error", "<p style=\"color:green\">Hostname deleted.</p>");
				DNSDao.deleteHostname(Integer.parseInt(deleteHost.trim()), dbProperties);
				System.out.println("Delete Host - Host ID: "+deleteHost);
			} else {
				request.setAttribute("hosts_error", "<p style=\"color:red\">An error occured while removing host name.</p>");
				System.out.println("Host Removed - Host ID: "+deleteHost+" User: "+current_user.getUsername());
			}
			
        	RequestDispatcher rd = getServletContext().getRequestDispatcher(request.getContextPath() + "/app/dashboard.jsp");
        	rd.forward(request, response);
		}
		
		doGet(request, response);
		
	}
}