package com.khoja.servlet;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.khoja.classes.DB;
import com.khoja.classes.DynUser;
import com.khoja.dao.ValidateDao;

public class ValidateServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    public ValidateServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		HttpSession session = request.getSession(true);
		
		DB dbProperties = new DB(getServletContext().getInitParameter("dbHost"), getServletContext().getInitParameter("dbPort"), getServletContext().getInitParameter("dbName"), getServletContext().getInitParameter("dbUser"), getServletContext().getInitParameter("dbPassword"));
    	
		DynUser current = null;
		String scheme = request.getScheme() + "://";
        String serverName = request.getServerName();
        String serverPort = (request.getServerPort() == 80) ? "" : ":" + request.getServerPort();
        String contextPath = request.getContextPath();
		
		String key1 = request.getParameter("key1");
		String key2 = request.getParameter("key2");
		String email = request.getParameter("email");
		String action = request.getParameter("action");
		
		
		if (key1 != null && key2 != null && email != null && action != null) {
			if (action.equals("forgot"))
			{
				if (ValidateDao.checkForgot(key1, key2, email, dbProperties)) {
					if (ValidateDao.validateUser(email, key1, key2, dbProperties)) {
						current = ValidateDao.loadUser(email, key1, key2, dbProperties);
						session.setAttribute("LoggedUser", current);
						System.out.println("User Validated and Logged In - "+email);
						ValidateDao.setNotForgot(current.getID(), dbProperties);
					} else {
						System.out.println("User Not Validated - "+email);
					}
				}
			}
			
			if (action.equals("activate"))
			{
				if (ValidateDao.checkNotEnabled(key1, key2, email, dbProperties)) {
					if (ValidateDao.validateUser(email, key1, key2, dbProperties)) {
						
						current = ValidateDao.loadUser(email, key1, key2, dbProperties);
						session.setAttribute("LoggedUser", current);
						System.out.println("User Enabled and Logged In - "+email);
						ValidateDao.setEnabled(current.getID(), dbProperties);
						ValidateDao.enableUserZone(current.getUsername(), dbProperties);
					} else {
						System.out.println("User Not Validated - "+email);
					}
				}
			}
			
		}
		response.sendRedirect(scheme + serverName + serverPort + contextPath +"/index.jsp");
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}
}