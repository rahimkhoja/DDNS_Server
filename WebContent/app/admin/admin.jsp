<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<c:import url="/app/admin/AdminServlet" />
<c:if test="${empty LoggedUser}"><c:redirect url="/index.jsp"/></c:if>
<c:set var="domains" value="${requestScope.domainList}" />
<c:set var="hosts" value="${requestScope.hostList}" />
<c:set var="users" value="${requestScope.userList}" />
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Hiive.biz - Free Dynamic DNS Service - Administration Panel - Welcome <c:out value="${sessionScope.LoggedUser.username}"/> (<c:out value="${sessionScope.LoggedUser.email}"/>)</title>
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
    <c:if test="${sessionScope.LoggedUser.admin}"><td style="width:40px;"><a href="${pageContext.request.contextPath}/app/dashboard.jsp">[dashboard]</a></td></c:if>
    <td style="width:60px; white-space:nowrap;"><a href="${pageContext.request.contextPath}/app/dashboard.jsp?deleteaccount=yes" onclick="return isProcess();">[delete account]</a></td>
    <td style="width:40px;"><a href="${pageContext.request.contextPath}/app/dashboard.jsp?logout=yes" onclick="return isProcess();">[logout]</a></td>
  </tr>
</table>
<h1>Administration Console</h1>
<BR>
<c:out escapeXml="false" value="${users_error}"/>
<form action="${pageContext.request.contextPath}/app/admin/admin.jsp" method="post">
	<fieldset>
		<legend> Users </legend>
  		<table>
  		<tr>
    		<th>User Name</th>
    		<th>User Email</th> 
    		<th>User Created</th>
    		<th>User Enabled/Disabled</th>
    		<th>User Admin</th>
    		<th></th>
  		</tr>
     	<c:forEach items= "${users}" var="user" >
  		<tr>
    		<td>${user.username}</td>
    		<td>${user.email}</td> 
    		<td>${user.createDate}</td>
    		<td><c:choose><c:when test="${user.enabled}"><button name="disableUser" type="submit" value="${user.ID}" onclick="return isProcess();">Disable</button></c:when>    
    <c:otherwise><button name="enableUser" type="submit" value="${user.ID}" onclick="return isProcess();">Enable</button></c:otherwise></c:choose></td>
    		<td><c:choose><c:when test="${user.admin}"><button name="noadminUser" type="submit" value="${user.ID}" onclick="return isProcess();">Remove Admin</button></c:when>    
    <c:otherwise><button name="adminUser" type="submit" value="${user.ID}" onclick="return isProcess();">Add Admin</button></c:otherwise></c:choose></td>
    		<td><button name="deleteUser" type="submit" value="${user.ID}" onclick="return isProcess();">Delete</button></td>
  		</tr>
     	</c:forEach> 
		</table>
	</fieldset>
</form>
<BR>
<c:out escapeXml="false" value="${hosts_error}"/>
<form action="${pageContext.request.contextPath}/app/admin/admin.jsp" method="post">
	<fieldset>
		<legend> Dynamic Hosts </legend>
  		<table>
  		<tr>
    		<th>Host Name</th>
    		<th>Host User</th>
    		<th>Domain Name</th> 
    		<th>IP Address</th>
    		<th>Last Update</th>
    		<th></th>
  		</tr>
     	<c:forEach items= "${hosts}" var="host" >
  		<tr>
    		<td>${host.hostname}</td>
    		<td>${host.username}</td>
    		<td>${host.domain}</td> 
    		<td>${host.IPAddress}</td>
    		<td>${host.lastUpdate}</td>
    		<td><button name="deleteHost" type="submit" value="${host.ID}" onclick="return isProcess();">Delete</button></td>
  		</tr>
     	</c:forEach> 
		</table>
	</fieldset>
</form>
<BR>
<c:out escapeXml="false" value="${domains_error}"/>
<form action="${pageContext.request.contextPath}/app/admin/admin.jsp" method="post">
	<fieldset>
		<legend> Domains </legend>
  		<table>
  		<tr>
    		<th>Domain Name</th>
    		<th>Create Date</th>
    		<th></th>
  		</tr>
     	<c:forEach items= "${domains}" var="domain" >
  		<tr>
    		<td>${domain.domain}</td>
    		<td>${domain.createDate}</td>
    		<td><button name="deleteDomain" type="submit" value="${domain.ID}" onclick="return isProcess();">Delete</button></td>
  		</tr>
     	</c:forEach> 
		</table>
	</fieldset>
</form>
<BR>
<c:out escapeXml="false" value="${domain_error}"/>
<form action="${pageContext.request.contextPath}/app/admin/admin.jsp" method="post">
	<fieldset>
	<legend> Add New Domain (eg domain.com) </legend>
    <table>
 	<tr>
 		<td><input type="text" name="domainname" required="required" /></td>
  		
 		<td><button name="addDomain" type="submit" value="yes">Add</button></td>
 	</tr>
  	</table>
    </fieldset>
</form><BR>
<c:out escapeXml="false" value="${property_error}"/>
<form action="${pageContext.request.contextPath}/app/admin/admin.jsp" method="post">
	<fieldset>
	<legend> System Properties </legend>
    <table>
 	<tr>
 		<td>NS1 IP Address</td>
 		<td><input type="text" name="ns1" required="required" value="<c:out value="${systemProperties.ns1}" />" /></td>
 		<td><button name="ns1set" type="submit" value="yes" onclick="return isProcess();">Set Name Server 1</button></td>
 	</tr>
 	<tr>
 		<td>NS2 IP Address</td>
 		<td><input type="text" name="ns2" required="required" value="<c:out value="${systemProperties.ns2}" />" /></td>
 		<td><button name="ns2set" type="submit" value="yes" onclick="return isProcess();">Set Name Server 2</button></td>
 	</tr>
 	<tr>
 		<td>New Host IP Address</td>
 		<td><input type="text" name="root_domain_address" required="required" value="<c:out value="${systemProperties.root}" />" /></td>
 		<td><button name="rootset" type="submit" value="yes" onclick="return isProcess();">Set New Host Address</button></td>
 	</tr>
 	<tr>
 		<td>Responsible Person (eg person@domain.com )</td>
 		<td><input type="text" name="person" required="required" value="<c:out value="${systemProperties.person}" />" /></td>
 		<td><button name="personset" type="submit" value="yes" onclick="return isProcess();">Set Person</button></td>
 	</tr>
 	<tr>
 		<td>Serial Number</td>
 		<td><input type="text" name="serial" required="required" value="<c:out value="${systemProperties.serial}" />" /></td>
 		<td><button name="serialset" type="submit" value="yes" onclick="return isProcess();">Set Serial Number</button></td>
 	</tr>
 	<tr>
 		<td>TTL</td>
 		<td><input type="text" name="ttl" required="required" value="<c:out value="${systemProperties.ttl}" />" /></td>
 		<td><button name="ttlset" type="submit" value="yes" onclick="return isProcess();">Set TTL</button></td>
 	</tr>
 		<td>Refresh</td>
 		<td><input type="text" name="refresh" required="required" value="<c:out value="${systemProperties.refresh}" />" /></td>
 		<td><button name="refreshset" type="submit" value="yes" onclick="return isProcess();">Set Refresh</button></td>
 	</tr>
 		<td>Expire</td>
 		<td><input type="text" name="expire" required="required" value="<c:out value="${systemProperties.expire}" />" /></td>
 		<td><button name="expireset" type="submit" value="yes" onclick="return isProcess();">Set Expire</button></td>
 	</tr>
 		<td>Minimum</td>
 		<td><input type="text" name="minimum" required="required" value="<c:out value="${systemProperties.minimum}" />" /></td>
 		<td><button name="minset" type="submit" value="yes" onclick="return isProcess();">Set Minimum</button></td>
 	</tr>
 		<td>Retry</td>
 		<td><input type="text" name="retry" required="required" value="<c:out value="${systemProperties.retry}" />" /></td>
 		<td><button name="retryset" type="submit" value="yes" onclick="return isProcess();">Set Retry</button></td>
 	</tr>
  	</table>
    </fieldset>
</form>
<BR></body>
</html>