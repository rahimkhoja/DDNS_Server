<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<c:import url="/ForgotServlet" />
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Hiive.biz - Free Dynamic DNS Service - Forgot Password</title>
<script type="text/javascript" src="http://www.hiive.biz/ddns/include.js"></script>
</head>
<body>
<table style="width: 100%;">
  <tr>
    <td style="max-width: 99%"></td>
    <td style="width:40px; white-space:nowrap;"><a href="${pageContext.request.contextPath}/index.jsp" >[home]</a></td>
    <td style="width:40px; white-space:nowrap;"><a href="${pageContext.request.contextPath}/login.jsp" >[login]</a></td>
    <td style="width:40px; white-space:nowrap;"><a href="${pageContext.request.contextPath}/create.jsp" >[create user]</a></td>
  </tr>
</table>
<h1>Forgot Password</h1>
<BR>
<c:out escapeXml="false" value="${forgot_error}"/>
	<form action="${pageContext.request.contextPath}/forgot.jsp" method="post">
        <fieldset style="width: 300px">
            <legend> Forgot Password </legend>
            <table>
                <tr>
                    <td>Please enter User Name or Email Address.</td>
                </tr>
                <tr>
                    <td><input type="text" name="useremail" required="required" /></td>
                </tr>
                <tr>
                    <td><button name="forgotsubmit" type="submit" value="yes" onclick="return isProcess();">Submit</button></td>
                </tr>
            </table>
        </fieldset>
    </form>
</body>
</html>