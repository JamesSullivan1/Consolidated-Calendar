<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ page isELIgnored="false"%>
<%@ page import="test.TestEventPull"%>
<%@ page import="java.util.ArrayList"%>
<%@ page import="server.framework.Event"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Functional Test Page</title>
</head>
<body>

<c:if test="${sessionScope.threadCount > 0}">
	<meta http-equiv="refresh" content="1" />
</c:if>

<%
TestEventPull.testSuccessfulPull(session, out, new ArrayList<Event>());
%>

</body>
</html>