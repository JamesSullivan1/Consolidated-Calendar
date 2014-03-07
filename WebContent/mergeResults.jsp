<%@ page import="server.web.MergeResultsHelper"%>
<%@ page import="server.framework.Calendar"%>
<%@ page import="server.framework.Event"%>
<%@ page import="java.util.ArrayList"%>
<%@ page import="java.util.Date"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page isELIgnored="false"%>

<%
	MergeResultsHelper.parseFeeds(request, session);
	MergeResultsHelper.pullGoogleEvents(request, session);
%>

<form method="post">
	<input type="hidden" name="pageTarget" value="feedSelection.jsp">
	<input type="submit" value="Return to Feed Selection">
</form>
	

<form method="post">
	<input type="hidden" name="pageTarget" value="pushEvents.jsp">
	<c:choose>
		<c:when test="${not empty sessionScope.eventsToAdd}">
			<input type="submit" value="Push events to Google Calendar">
		</c:when>
		<c:otherwise>
			<input type="submit" value="Push events to Google Calendar" disabled>
		</c:otherwise>
	</c:choose>
</form>

<%
	MergeResultsHelper.showResults(request, session, out);
%>
