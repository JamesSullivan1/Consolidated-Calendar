<%@ page language="java" contentType="text/html charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="server.oauth.GoogleAuthHelper"%>
<%@ page import="server.web.IndexHelper" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page isELIgnored="false" %>

<%! static String pageTarget; %>

<%
IndexHelper.init(session);
IndexHelper.processGoogleAuth(request, session);
IndexHelper.processPageTarget(request, session);
IndexHelper.processLogOut(request, session);
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<link rel="stylesheet" type="text/css" href="css/styles.css">
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Consolidated Calendar</title>
</head>
<body>

<center><h1>Consolidated Calendar</h1></center>

<c:if test="${not empty sessionScope.authCredential}">
	<form method="post">
		<input type="hidden" name="logOut" value="true">
		<input type="submit" value="Log out">
	</form>
</c:if>

<jsp:include page="${sessionScope.pageTarget}" />

</body>
</html>