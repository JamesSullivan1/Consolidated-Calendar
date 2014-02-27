<%@ page import="server.oauth.GoogleAuthHelper"%>
<%@ page import="server.web.FeedSelectionHelper" %>

<h1>Select Output Calendar</h1>

<%
if (session.getAttribute("authCredential") == null)
	FeedSelectionHelper.showGoogleAuthLink(session, out);
else
	FeedSelectionHelper.showCalendarChoice(session, out);
%>

<h1>Select Input Calendar(s)</h1>