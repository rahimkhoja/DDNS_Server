<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
 <%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<c:import url="/app/DynServlet" />
<c:if test="${empty LoggedUser}"><c:redirect url="/index.jsp"/></c:if>
<c:set var="domains" value="${requestScope.domainList}" />
<c:set var="hosts" value="${requestScope.hostList}" />
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Hiive.biz - Free Dynamic DNS Service - Dashboard - Welcome <c:out value="${sessionScope.LoggedUser.username}"/> (<c:out value="${sessionScope.LoggedUser.email}"/>)</title>
<script type="text/javascript" src="http://www.hiive.biz/ddns/include.js"></script>
<script type="text/javascript">
<!--
function isProcess()
{
  var answer = confirm ("Are you sure you want to continue?");
  if (answer){
              document.myForm.answer.value = "true";  
              document.myForm.submit();
              return true;
    }else return false
 
}
// -->
</script></head>
<body>
<table style="width: 100%;">
  <tr>
    <td style="max-width: 99%"></td>
    <td style="width:70px; white-space:nowrap;">[<c:out value="${sessionScope.LoggedUser.username}"/> (<c:out value="${sessionScope.LoggedUser.email}"/>)] - </td>
   <c:if test="${sessionScope.LoggedUser.admin}"><td style="width:40px; white-space:nowrap;"><a href="${pageContext.request.contextPath}/app/admin/admin.jsp">[admin]</a></td></c:if>
   <td style="width:60px; white-space:nowrap;"><a href="${pageContext.request.contextPath}/app/dashboard.jsp?deleteaccount=yes" onclick="return isProcess();">[delete account]</a></td>
    <td style="width:40px; white-space:nowrap;"><a href="${pageContext.request.contextPath}/app/dashboard.jsp?logout=yes" onclick="return isProcess();">[logout]</a></td>
  </tr>
</table>
<h1>Dashboard</h1>
<BR>
<c:out escapeXml="false" value="${hosts_error}"/>
<form action="${pageContext.request.contextPath}/app/dashboard.jsp" method="post">
	<fieldset>
		<legend> Your Dynamic Hosts </legend>
  		<table>
  		<tr>
    		<th>Host Name</th>
    		<th>Domain Name</th> 
    		<th>IP Address</th>
    		<th>Last Update</th>
    		<th></th>
  		</tr>
     	<c:forEach items= "${hosts}" var="host" >
  		<tr>
    		<td>${host.hostname}</td>
    		<td>${host.domain}</td> 
    		<td><input type="text" name="IP${host.ID}" required="required" value="<c:out value="${host.IPAddress}"/>"/> <button name="sethost" type="submit" value="${host.ID}" onclick="return isProcess();">Set IP</button></td>
    		<td>${host.lastUpdate}</td>
    		<td><button name="deletehost" type="submit" value="${host.ID}" onclick="return isProcess();">Delete</button></td>
  		</tr>
     	</c:forEach> 
		</table>
	</fieldset>
</form>
<BR><c:if test="${(fn:length(hosts) le 9)||(LoggedUser.admin)}">
<c:out escapeXml="false" value="${host_error}"/>
<form action="${pageContext.request.contextPath}/app/dashboard.jsp" method="post">
	<fieldset>
	<legend> Add Dynamic Host  (eg host.domain.com) </legend>
    <table>
 	<tr>
 		<td><input type="text" name="hostname" required="required" /></td>
  		<td><select name='domain'>
		<c:forEach items="${domains}" var="domain">
  			<option value="${domain.ID}">${domain.domain}</option>
  		</c:forEach>
		</select></td>
 		<td><button name="add" type="submit" value="yes">Add</button></td>
 	</tr>
  	</table>
    </fieldset>
</form>
<BR></c:if>
<c:out escapeXml="false" value="${password_error}"/>
<form action="${pageContext.request.contextPath}/app/dashboard.jsp" method="post">
	<fieldset>
	<legend> Update Password </legend>
    <table>
 	<c:if test="${not LoggedUser.forgot}"><tr>
 		<td>Current Password</td>
  		<td><input type="password" name="currentpassword" required="required" /></td>
 	</tr></c:if>
 	<tr>
 		<td>New Password 1</td>
  		<td><input type="password" name="newpassword1" required="required" /></td>
 	</tr>
 	<tr>
 		<td>New Password 2</td>
  		<td><input type="password" name="newpassword2" required="required" /></td>
 	</tr>
 	<tr>
 		<td></td>
  		<td><button name="updatepassword" type="submit" value="yes">Update Password</button></td>
 	</tr>
  	</table>
    </fieldset>
</form>
</body>
</html>