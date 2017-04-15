package com.khoja.servlet;

import java.io.IOException;
import java.util.Properties;
import java.util.regex.Pattern;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.khoja.classes.DB;
import com.khoja.classes.KeyGenerator;
import com.khoja.dao.ForgotDao;


public class ForgotServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    public ForgotServlet() {
        super();
        // TODO Auto-generated constructor stub
    }
    
   public void sendMail(int uID, String URLlink, String Uname, String mailuser, String mailpass, String mailserver, String mailport, String mailsendadd, DB dbProperties) {
    	
    	Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", mailserver);
        props.put("mail.smtp.port", mailport);

        Session session = Session.getInstance(props,
          new javax.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(mailuser, mailpass);
            }
          });

        try {

            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(mailsendadd));
            message.setRecipients(Message.RecipientType.TO,
                InternetAddress.parse(ForgotDao.getEmail(uID, dbProperties)));
            message.setSubject("Forgot your password!");
            message.setContent("Dear "+Uname+", <BR><BR> Please click the following link to gain access to your account. <BR><BR> <a href=\""+URLlink+"\">Activate Account</a> <BR><BR> Thank You,", "text/html; charset=utf-8");
           
            Transport.send(message);

            System.out.println("Forgot Password E-mail Sent - "+ForgotDao.getEmail(uID, dbProperties));

        } catch (MessagingException e) {
            
            System.out.println("Error - Forgot Password E-mail Send FAILED - "+ForgotDao.getEmail(uID, dbProperties));
            throw new RuntimeException(e);
        }	
    }
    
   
    public static boolean isValidEmailAddress(String email) {
        boolean result = true;
        try {
           InternetAddress emailAddr = new InternetAddress(email);
           emailAddr.validate();
        } catch (AddressException ex) {
           result = false;
        }
        return result;
    }
    

	public static String getValidatorURL(HttpServletRequest request, int userID, String key1, String key2, DB dbProperties) {
        String scheme = request.getScheme() + "://";
        String serverName = request.getServerName();
        String serverPort = (request.getServerPort() == 80) ? "" : ":" + request.getServerPort();
        String contextPath = request.getContextPath();
        
        String URItype = "&action=forgot";
        String URI1 = "&key1="+key1;
        String URI2 = "&key2="+key2;
        String URIemail = "&email="+ForgotDao.getEmail(userID, dbProperties);
        return scheme + serverName + serverPort + contextPath + "/validator.html?" + URItype + URIemail + URI1 + URI2;
    }
   
	
    public static boolean checkUser(String username) {
    	
    	Pattern letter = Pattern.compile("[a-zA-Z0-9]");
        Pattern five = Pattern.compile (".{5,}");
        Pattern all = Pattern.compile ("^[\\s\\da-zA-Z0-9]*$");
        
                
        return five.matcher(username).matches()
        	      && letter.matcher(username).find()
        	      && all.matcher(username).find();
    }
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
	}


	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		DB dbProperties = new DB(getServletContext().getInitParameter("dbHost"), getServletContext().getInitParameter("dbPort"), getServletContext().getInitParameter("dbName"), getServletContext().getInitParameter("dbUser"), getServletContext().getInitParameter("dbPassword"));
    	
		String forgotpassword=request.getParameter("forgotsubmit"); 
		String forgotemailorpassword=request.getParameter("useremail"); 
		
		if (forgotpassword != null && forgotpassword.equals("yes")) {
			if (checkUser(forgotemailorpassword) || isValidEmailAddress(forgotemailorpassword)) {
				if (ForgotDao.isEmailorUser(forgotemailorpassword, dbProperties) > 0) {
					String key1 = KeyGenerator.GenerateKey();
					String key2 = KeyGenerator.GenerateKey();
					int userID = ForgotDao.isEmailorUser(forgotemailorpassword, dbProperties);
					if (ForgotDao.setForgot(userID, key1, key2, dbProperties)) {
						String url_string = getValidatorURL(request, userID, key1, key2, dbProperties);
						sendMail(userID, url_string, ForgotDao.getName(userID, dbProperties),getServletContext().getInitParameter("mailUser"),getServletContext().getInitParameter("mailPass"),getServletContext().getInitParameter("mailServer"),getServletContext().getInitParameter("mailPort"),getServletContext().getInitParameter("mailAddress"), dbProperties);
					};
				}
			}
			request.setAttribute("forgot_error", "<p style=\"color:green\">Check email for activation link.</p>");
    		RequestDispatcher rd = getServletContext().getRequestDispatcher(request.getContextPath() + "/forgot.jsp");
    		rd.forward(request, response);
		}
		doGet(request, response);
	}
}
