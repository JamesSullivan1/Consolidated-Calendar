<%@ page import="server.web.FeedResultsHelper"%>
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
	FeedResultsHelper.parseFeeds(request, session);
%>

<c:if test="${not empty sessionScope.consolidatedCalendar}">
	<div class="consolidatedEvents">
		<ul>
			<%
				Calendar consolidated = (Calendar) (session
							.getAttribute("consolidatedCalendar"));
					ArrayList<Event> events = consolidated.getEvents();
					for (Event e : events) {
						if (e != null) {
							out.println("<li>" + e.toString() + "</li>");
						}
					}
			%>
		</ul>
	</div>
</c:if>