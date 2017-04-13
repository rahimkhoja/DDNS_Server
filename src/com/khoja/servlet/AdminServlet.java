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

import com.khoja.classes.DB;
import com.khoja.classes.DynDomain;
import com.khoja.classes.DynHost;
import com.khoja.classes.DynUser;
import com.khoja.dao.AdminDao;
import com.khoja.dao.DNSDao;

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

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		DB dbProperties = new DB(getServletContext().getInitParameter("dbHost"), getServletContext().getInitParameter("dbPort"), getServletContext().getInitParameter("dbName"), getServletContext().getInitParameter("dbUser"), getServletContext().getInitParameter("dbPassword"));
    			
		ArrayList<DynHost> hosts = AdminDao.getHosts(dbProperties);
		ArrayList<DynDomain> domains = AdminDao.getDomains(dbProperties);
		ArrayList<DynUser> users = AdminDao.getUsers(dbProperties);
        request.setAttribute("domainList", domains);
        request.setAttribute("hostList", hosts);
        request.setAttribute("userList", users);        
		
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
		
		HttpSession session = request.getSession(false);
		DynUser current_user = (DynUser) session.getAttribute("LoggedUser");
		
		if (addDomain != null && addDomain.equals("yes")) {
			if (checkDomainname(addDomainname)) {
				// Valid Domain Name
				if (!AdminDao.doesDomainExist(addDomainname, dbProperties)) {
					// Domain Name Not In List
					AdminDao.addDomain(addDomainname, dbProperties);
					
					request.setAttribute("domain_error", "<p style=\"color:green\">New domain name added ("+addDomainname+").</p>");
		        	RequestDispatcher rd = getServletContext().getRequestDispatcher(request.getContextPath() + "/login.jsp");
		        	rd.forward(request, response);
		        	System.out.println("New Domain Added - Domain Name: "+addDomainname+" User: "+current_user.getUsername());
				} else {
					request.setAttribute("domain_error", "<p style=\"color:red\">Domain already exists.</p>");
		        	RequestDispatcher rd = getServletContext().getRequestDispatcher(request.getContextPath() + "/login.jsp");
		        	rd.forward(request, response);
				}
			} else {
				request.setAttribute("domain_error", "<p style=\"color:red\">Domain names must be between 5 and 32 characters long, containing only letters, numbers, and hyphens(-). Domain names cannot start or end with a hyphen(-) and must have only one period(.).</p>");
	        	RequestDispatcher rd = getServletContext().getRequestDispatcher(request.getContextPath() + "/login.jsp");
	        	rd.forward(request, response);
			}
		}
		
		if (deleteUser != null) {
			if (AdminDao.deleteUser(Integer.parseInt(deleteUser.trim()), dbProperties)) {
				
				System.out.println("User Deleted - User ID: "+deleteUser);
				if (AdminDao.deleteUserHosts(Integer.parseInt(deleteUser.trim()), dbProperties)) {
					request.setAttribute("users_error", "<p style=\"color:green\">User account deleted.</p>");
					DNSDao.deleteUserHostname(Integer.parseInt(deleteUser.trim()), dbProperties);
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
		
		doGet(request, response);

	}
}