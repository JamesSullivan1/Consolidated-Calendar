<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Functional Test Page</title>
</head>
<body>

<%
//Confirm logged in to google.
Object authCheck = session.getAttribute("authCredential");
if (authCheck == null)
	{
	out.println("Please return to index.jsp and log in to the test google account before visiting this page.");
	}
else
	{
	//Run the different test methods here, giving them session object to pull session and jspwriter out object
	//for the test output to print back into this page.
	//
	//The methods can still be in TestEventPull / TestEventPush and can be called from here.
	}
%>

</body>
</html>