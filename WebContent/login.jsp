<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<c:import url="/LoginServlet" />
<c:if test="${LoggedUser.enabled}"><c:redirect url="/app/dashboard.jsp"/></c:if>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Hiive.biz - Free Dynamic DNS Service - Login</title>
<script type="text/javascript" src="http://www.hiive.biz/ddns/include.js"></script>
</head>
<body>
<table style="width: 100%;">
  <tr>
    <td style="max-width: 99%"></td>
    <td style="width:40px; white-space:nowrap;"><a href="${pageContext.request.contextPath}/index.jsp" >[home]</a></td>
    <td style="width:40px; white-space:nowrap;"><a href="${pageContext.request.contextPath}/create.jsp" >[create user]</a></td>
    <td style="width:40px; white-space:nowrap;"><a href="${pageContext.request.contextPath}/forgot.jsp" >[forgot]</a></td>
  </tr>
</table>
<h1>Login</h1>
<BR>
<c:out escapeXml="false" value="${login_error}"/>
	<form action="${pageContext.request.contextPath}/login.jsp" method="post">
        <fieldset style="width: 300px">
            <legend> Login to App </legend>
            <table>
                <tr>
                    <td>User Name</td>
                    <td><input type="text" name="username" required="required" /></td>
                </tr>
                <tr>
                    <td>Password</td>
                    <td><input type="password" name="userpass" required="required" /></td>
                </tr>
                <tr>
                    <td><input type="submit" value="Login" /></td>
                </tr>
            </table>
        </fieldset>
    </form>
</body>
</html>