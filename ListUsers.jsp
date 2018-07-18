<%@page import="com.visa.training.User"%>
<%@page import="java.util.List"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Users</title>
   
<table> 
<c:forEach items="${users}" var="user">
<tr><td>${user.name}</td><td>${user.age}</td></tr> 
</c:forEach>
</table>
<br/><a href="UserForm.jsp">Add User</a>
</body>
</html>