<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
    <%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
    <c:if test="${LoggedUser.enabled}"><c:redirect url="/app/dashboard.jsp"/></c:if>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Hiive.biz - Free Dynamic DNS Service</title>
<script type="text/javascript" src="http://www.hiive.biz/ddns/include.js"></script>
</head>
<body>
<table style="width: 100%;">
  <tr>
  <td style="max-width: 99%"></td>
    <td style="width:40px; white-space:nowrap;"><a href="${pageContext.request.contextPath}/login.jsp" >[login]</a></td>
    <td style="width:80px; white-space:nowrap;"><a href="${pageContext.request.contextPath}/create.jsp" >[create user]</a></td>    
    <td style="width:40px; white-space:nowrap;"><a href="${pageContext.request.contextPath}/forgot.jsp" >[forgot]</a></td>
  </tr>
</table>
</body>
</html>