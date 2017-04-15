package com.khoja.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.validator.routines.InetAddressValidator;
import org.apache.tomcat.util.codec.binary.Base64;

import com.khoja.classes.DB;
import com.khoja.classes.DynUser;
import com.khoja.dao.LoginDao;
import com.khoja.dao.UpdateDao;
import com.mysql.jdbc.StringUtils;

public class UpdateServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
	public static boolean isValidInetAddress(final String address) {
	    if (StringUtils.isEmptyOrWhitespaceOnly(address)) {
	        return false;
	    }
	    if (InetAddressValidator.getInstance().isValid(address)) {
	        return true;
	    }
	        return false;
	}
	
	
    public UpdateServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		DB dbProperties = new DB(getServletContext().getInitParameter("dbHost"), getServletContext().getInitParameter("dbPort"), getServletContext().getInitParameter("dbName"), getServletContext().getInitParameter("dbUser"), getServletContext().getInitParameter("dbPassword"));
    			
		PrintWriter out = response.getWriter();
		String authHeader = request.getHeader("authorization");
		String encodedValue = authHeader.split(" ")[1];
		byte[] decoded = Base64.decodeBase64(encodedValue);
		String decodedValue =  new String(decoded, StandardCharsets.UTF_8);
		DynUser current = LoginDao.validate( decodedValue.split(":")[0], decodedValue.split(":")[1], request, dbProperties);
		
		if ( current != null ) {
			String myip = request.getParameter("myip");
			if (myip == null) {
				myip = request.getHeader("X-FORWARDED-FOR");   
				if(myip==null)	{
				    myip = request.getRemoteAddr();
				}
			}
			if (isValidInetAddress(myip)) {
				String hostname = request.getParameter("hostname");
				List<String> hostnames;
				hostnames = Arrays.asList(hostname.split("\\s*,\\s*"));
				ArrayList<String> hostlist = new ArrayList<String>(hostnames); 
				if (hostlist.size() < 11) {  // 10 or less hosts as 10 is max for users
					
					ArrayList<Integer> hostID = UpdateDao.checkHosts(hostlist, current, dbProperties);
					if (hostID != null) {
						if (!UpdateDao.isTheSame(myip, hostlist, current, dbProperties)) {
							//update records
							UpdateDao.updateAddress(myip, hostID, dbProperties);
							UpdateDao.updateDNSAddress(myip, hostID, dbProperties);
							out.println("good");
							out.println(myip);
						} else {
							// no change needed.
							out.println("nochg");
						}
						
					} else {
						out.println("nohost");
					}
					
				} else {
					out.println("numhost");
				}
				
			} else {
				out.println("badaddress");
			}
		} else {
			out.println("badauth");
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
