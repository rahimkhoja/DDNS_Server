package com.khoja.filters;

import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.khoja.classes.DynUser;

@WebFilter("/app/admin/*")
public class AdminFilter implements Filter {

	public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;
        HttpSession session = request.getSession(false);

        if (session == null || session.getAttribute("LoggedUser") == null) {
            response.sendRedirect(request.getContextPath() + "/index.jsp"); // No logged-in user found, so redirect to login page.
        } else {
        	DynUser currentUser = (DynUser) session.getAttribute("LoggedUser");
        	if (!currentUser.isAdmin()) {
        		response.sendRedirect(request.getContextPath() + "/app/dashboard.jsp"); // No admin user found, so redirect to dashboard page.
        	} else {        	
        		chain.doFilter(req, res); // Logged-in user found, so just continue request.
        	}
        }
    }

}
