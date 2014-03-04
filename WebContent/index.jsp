<%@ page language="java" contentType="text/html charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="server.oauth.GoogleAuthHelper"%>
<%@ page import="server.web.IndexHelper" %>
<%@ page isELIgnored="false" %>

<%! static String pageTarget; %>

<%
IndexHelper.processGoogleAuth(request, session, out);
IndexHelper.processPageTarget(request, session);
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<link rel="stylesheet" type="text/css" href="css/styles.css">
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Consolidated Calendar</title>
</head>
<body>

<jsp:include page="${sessionScope.pageTarget}" />

</body>
</html>