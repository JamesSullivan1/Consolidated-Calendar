<%@ page import="server.oauth.GoogleAuthHelper"%>

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