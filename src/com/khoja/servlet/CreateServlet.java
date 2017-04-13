package com.khoja.servlet;

import java.io.IOException;
import java.util.Properties;
import java.util.regex.Pattern;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.PasswordAuthentication;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.khoja.classes.DB;
import com.khoja.classes.Hashing;
import com.khoja.classes.KeyGenerator;
import com.khoja.dao.CreateDao;


public class CreateServlet extends HttpServlet{

    private static final long serialVersionUID = 1L;

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
    
    public void sendMail(String uEmail, String URLlink, String Uname, String mailuser, String mailpass, String mailserver, String mailport, String mailsendadd) {
    	
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
                InternetAddress.parse(uEmail));
            message.setSubject("Activate your account!!!");
            message.setContent("Dear "+Uname+", <BR><BR> Please click the following link to activate your account. <BR><BR> <a href=\""+URLlink+"\">Activate Account</a> <BR><BR> Thank You,", "text/html; charset=utf-8");
           
            Transport.send(message);

            System.out.println("Create Account E-mail Sent");

        } catch (MessagingException e) {
            
            System.out.println("Error - Create Account E-mail Send FAILED");
            throw new RuntimeException(e);
        }
 
    	
    }
    
    public static String getValidatorURL(HttpServletRequest request, String email, String key1, String key2) {
        String scheme = request.getScheme() + "://";
        String serverName = request.getServerName();
        String serverPort = (request.getServerPort() == 80) ? "" : ":" + request.getServerPort();
        String contextPath = request.getContextPath();
        
        String URItype = "&action=activate";
        String URI1 = "&key1="+key1;
        String URI2 = "&key2="+key2;
        String URIemail = "&email="+email;
        return scheme + serverName + serverPort + contextPath + "/validator.html?" + URItype + URIemail + URI1 + URI2;
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
    
    public void doPost(HttpServletRequest request, HttpServletResponse response)  
            throws ServletException, IOException {  

    	DB dbProperties = new DB(getServletContext().getInitParameter("dbHost"), getServletContext().getInitParameter("dbPort"), getServletContext().getInitParameter("dbName"), getServletContext().getInitParameter("dbUser"), getServletContext().getInitParameter("dbPassword"));
    	
    	String newUserName=request.getParameter("username");  
        String newUserEmail=request.getParameter("useremail");  
        String newUserPass1=request.getParameter("userpass1"); 
        String newUserPass2=request.getParameter("userpass2"); 
        String user_accept_terms=request.getParameter("terms"); 
        boolean term_accept = Boolean.parseBoolean(user_accept_terms);
       
        
        if (isValidEmailAddress(newUserEmail)) {
        	if (!CreateDao.doesEmailExist(newUserEmail, dbProperties)) {
        		if (!CreateDao.doesUserExist(newUserName, dbProperties)) {
        			if(checkUser(newUserName)) {
        				if(checkPassword(newUserPass1)) {
        					if (newUserPass1.equals(newUserPass2)) {
        						if (term_accept) {
        							String keygen1 = KeyGenerator.GenerateKey(); 
        							String keygen2 = KeyGenerator.GenerateKey(); 
        						
        							if (CreateDao.addUser(newUserName, newUserEmail,Hashing.returnMD5(newUserPass1), keygen1, keygen2, dbProperties)) {
        						
	        							String validatorURL =  getValidatorURL(request, newUserEmail, keygen1, keygen2);
	        							System.out.println(validatorURL);
	        							sendMail(newUserEmail, validatorURL, newUserName,getServletContext().getInitParameter("mailUser"),getServletContext().getInitParameter("mailPass"),getServletContext().getInitParameter("mailServer"),getServletContext().getInitParameter("mailPort"),getServletContext().getInitParameter("mailAddress"));
	        							request.removeAttribute("newUserEmail");
	        							request.removeAttribute("username");
	        							
        							}
        							request.setAttribute("create_error", "<p style=\"color:green\">New account created. Check email for activation link.</p>");
        			        		RequestDispatcher rd = getServletContext().getRequestDispatcher(request.getContextPath() + "/create.jsp");
        			        		rd.forward(request, response);
        						} else {
            						request.setAttribute("create_error", "<p style=\"color:red\">Terms & Conditions not accepted.</p>");
            			        	RequestDispatcher rd = getServletContext().getRequestDispatcher(request.getContextPath() + "/create.jsp");
            			        	rd.forward(request, response);
            					}
        						
        			        	
        					} else {
        						request.setAttribute("create_error", "<p style=\"color:red\">Passwords do not match</p>");
        			        	RequestDispatcher rd = getServletContext().getRequestDispatcher(request.getContextPath() + "/create.jsp");
        			        	rd.forward(request, response);
        					}
        				} else {
        					request.setAttribute("create_error", "<p style=\"color:red\">Password must be 8 or more characters long with at least 1 letter, 1 number, and 1 special character (!#$%&*()_+=<>?{}~-).</p>");
    			        	RequestDispatcher rd = getServletContext().getRequestDispatcher(request.getContextPath() + "/create.jsp");
    			        	rd.forward(request, response);
        				}
        			} else {
        				request.setAttribute("create_error", "<p style=\"color:red\">User Name must be 5 or more characters long and only alphanumeric characters.</p>");
			        	RequestDispatcher rd = getServletContext().getRequestDispatcher(request.getContextPath() + "/create.jsp");
			        	rd.forward(request, response);
        			}
        		} else {
        			request.setAttribute("create_error", "<p style=\"color:red\">User Name already in use.</p>");
		        	RequestDispatcher rd = getServletContext().getRequestDispatcher(request.getContextPath() + "/create.jsp");
		        	rd.forward(request, response);
        		}
        	} else {
    			request.setAttribute("create_error", "<p style=\"color:red\">Email already in use.</p>");
	        	RequestDispatcher rd = getServletContext().getRequestDispatcher(request.getContextPath() + "/create.jsp");
	        	rd.forward(request, response);
    		}
        } else {
        	request.setAttribute("create_error", "<p style=\"color:red\">Invalid email address.</p>");
        	RequestDispatcher rd = getServletContext().getRequestDispatcher(request.getContextPath() + "/create.jsp");
        	rd.forward(request, response);
        }
  
        
        doGet(request, response);
         
    }  
} 