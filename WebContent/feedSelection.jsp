<%@ page import="server.oauth.GoogleAuthHelper"%>
<%@ page import="server.web.FeedSelectionHelper" %>
<%@ page import="java.util.ArrayList" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page isELIgnored="false" %>

<% FeedSelectionHelper.editICSFeeds(request, session); %>

<h1>Select Output Calendar</h1>

<%
if (session.getAttribute("authCredential") == null)
	FeedSelectionHelper.showGoogleAuthLink(session, out);
else
	FeedSelectionHelper.showCalendarChoice(session, out);
%>

<h1>Add Input Calendar(s)</h1>

<c:if test="${not empty sessionScope.icsList}">
	<c:forEach var="feed" items="${sessionScope.icsList}">
		<form method="post">
			${feed} <input type="hidden" name="removeICS" value="${feed}"><input type="submit" value="Remove">
		</form>
	</c:forEach>
</c:if>

<form method="post">
New ICS Feed: <input type="text" name="addICS" size="60"> <input type="submit" value="Add">
</form>

<c:if test="${not empty sessionScope.badFeed && sessionScope.badFeed}">
	<div class="error">Invalid ICS Feed.</div>
</c:if>
<% session.removeAttribute("badFeed"); %>

<br />
<form method="post">
	<input type="hidden" name="pageTarget" value="feedResults.jsp">
	<c:choose>
		<c:when test="${not empty sessionScope.icsList}">
			<input type="submit" value="Process Feeds">
		</c:when>
		<c:otherwise>
			<input type="submit" value="Process Feeds" disabled>
		</c:otherwise>
	</c:choose>
</form>
