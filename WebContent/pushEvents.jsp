<%@ page import="server.web.PushEventsHelper"%>
<%@ page import="server.framework.Calendar"%>
<%@ page import="server.framework.Event"%>
<%@ page import="java.util.ArrayList"%>
<%@ page import="java.util.Date"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page isELIgnored="false"%>

<form method="post">
	<input type="hidden" name="pageTarget" value="feedSelection.jsp">
	<input type="submit" value="Return to Feed Selection">
</form>



<%
	PushEventsHelper.push(request, session, out);
%>
