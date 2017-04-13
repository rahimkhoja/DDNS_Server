package com.khoja.servlet;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.khoja.classes.DB;
import com.khoja.dao.LoginDao;

public class LoginServlet extends HttpServlet{

    private static final long serialVersionUID = 1L;
    
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
	}

    public void doPost(HttpServletRequest request, HttpServletResponse response)  
            throws ServletException, IOException {  
        
        String n=request.getParameter("username");  
        String p=request.getParameter("userpass"); 
        HttpSession session = request.getSession(false);
        
        DB dbProperties = new DB(getServletContext().getInitParameter("dbHost"), getServletContext().getInitParameter("dbPort"), getServletContext().getInitParameter("dbName"), getServletContext().getInitParameter("dbUser"), getServletContext().getInitParameter("dbPassword"));
        
        if(LoginDao.validate(n, p, request, dbProperties) != null){ 
            session.setAttribute("LoggedUser", LoginDao.validate(n, p, request, dbProperties));
            RequestDispatcher dispatcher = getServletContext().getRequestDispatcher(request.getContextPath() + "/app/dashboard.jsp");
            dispatcher.forward(request, response);
            
        } else{  
        	request.setAttribute("login_error", "<p style=\"color:red\">Sorry user name or password error</p>");
        	RequestDispatcher rd = getServletContext().getRequestDispatcher(request.getContextPath() + "/login.jsp");
        	rd.forward(request, response);
        }  
        
        doGet(request, response);
    }  
} 