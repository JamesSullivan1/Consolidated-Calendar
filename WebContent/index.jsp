<%@ page contentType="text/html charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="OAuth2.GoogleAuthHelper"%>

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
		if (request.getParameter("code") == null
				|| request.getParameter("state") == null) {
			/*
			 * On the first visit, give the user a link to the authentication
			 * page
			 */
			out.println("<a href='" + helper.buildLoginUrl()
					+ "'>log in with google</a>");

			/*
			 * set the secure state token
			 */
			session.setAttribute("state", helper.getStateToken());

		} else if (request.getParameter("code") != null
				&& request.getParameter("state") != null
				&& request.getParameter("state").equals(
						session.getAttribute("state"))) {

			session.removeAttribute("state");

			String userInfo = helper.getUserInfoJson(request.getParameter("code"));
			
			out.println("Successful login to Google. Here is some basic user info.");
			/*
			 * Once the user is authenticated, print out some basic user
			 * information in JSON format to verify that the authentication 
			 * worked.
			 */
			out.println("<pre>");
			out.println(userInfo);
			out.println("</pre>");
		}
	%>
	</div>
</body>
</html>