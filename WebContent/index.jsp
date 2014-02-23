<%@ page contentType="text/html charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="server.oauth.GoogleAuthHelper"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Test Page</title>
<style>
body {
	font-family: Sans-Serif;
	margin: 1em;
}
</style>
</head>
<body>
	<div class="auth">
		<%
			final GoogleAuthHelper helper = new GoogleAuthHelper();
			// On the first visit, give the user a link to the authentication page
			if (request.getParameter("code") == null
					|| request.getParameter("state") == null) {

				out.println("<a href='" + helper.buildLoginUrl()
						+ "'>Sign in with Google</a>");

				session.setAttribute("state", helper.getStateToken());

			}
			// Subsequent visits with valid, matching state tokens indicate
			// successful authentication
			else if (request.getParameter("code") != null
					&& request.getParameter("state") != null
					&& request.getParameter("state").equals(
							session.getAttribute("state"))) {

				session.removeAttribute("state");

				String calendarList = helper.getCalendarListJson(request
						.getParameter("code"));

				out.println("Successful login to Google. Below are your calendars in JSON format.");
				/*
				 * Once the user is authenticated, print out some basic user
				 * information in JSON format to verify that the authentication 
				 * worked.
				 */
				out.println("<pre>");
				out.println(calendarList);
				out.println("</pre>");
			}
		%>
	</div>
</body>
</html>