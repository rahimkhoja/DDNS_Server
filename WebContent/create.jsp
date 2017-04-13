<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<c:import url="/CreateServlet" />
<c:if test="${LoggedUser.enabled}"><c:redirect url="/app/dashboard.jsp"/></c:if>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Hiive.biz - Free Dynamic DNS Service - Create Account</title>
<script type="text/javascript" src="http://www.hiive.biz/ddns/include.js"></script>
<script type="text/javascript">
<!--
    function popterms(url){
      newwindow = window.open(
          url, '', "status=yes, height=500; width=500; resizeable=0");
  }
// -->
</script>
</head>
<body>
<table style="width: 100%;">
  <tr>
  <td style="max-width: 99%"></td>
         <td style="width:40px; white-space:nowrap;"><a href="${pageContext.request.contextPath}/index.jsp" >[home]</a></td>
     <td style="width:40px; white-space:nowrap;"><a href="${pageContext.request.contextPath}/login.jsp" >[login]</a></td>
    <td style="width:40px; white-space:nowrap;"><a href="${pageContext.request.contextPath}/forgot.jsp" >[forgot]</a></td>
  </tr>
</table>
<h1>Create Account</h1>
<BR>
<c:out escapeXml="false" value="${create_error}"/>
    <form action="${pageContext.request.contextPath}/create.jsp" method="post">
        <fieldset style="width: 300px">
            <legend> Create New User </legend>
            <table>
            	<tr>
                    <td>User Name</td>
                    <td><input type="text" name="username" value="<c:out value="${param.username}" />" required="required" /></td>
                </tr>
                <tr>
                    <td>Email</td>
                    <td><input type="text" name="useremail" required="required" value="<c:out value="${param.useremail}" />" /></td>
                </tr>
                <tr>
                    <td>Password 1</td>
                    <td><input type="password" name="userpass1" required="required" /></td>
                </tr>
                <tr>
                    <td>Password 2</td>
                    <td><input type="password" name="userpass2" required="required" /></td>
                </tr>
                <tr>
                    <td colspan="2"><input type="checkbox" name="terms" value="true">  I agree to the <a href="javascript:popterms('${pageContext.request.scheme}://${pageContext.request.serverName}:${pageContext.request.serverPort}${pageContext.request.contextPath}/terms.html');">Terms and Conditions</a>.</td>
                </tr>
                <tr>
                    <td colspan="2"><input type="submit" value="Create Account" /></td>
                </tr>
            </table>
        </fieldset>
    </form>
    
</body>
</html>
    