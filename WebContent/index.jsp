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
.auth a {
	display: block;
	border-style: solid;
	border-color: #bbb #888 #666 #aaa;
	border-width: 1px 2px 2px 1px;
	background: #ccc;
	color: #333;
	line-height: 2;
	text-align: center;
	text-decoration: none;
	font-weight: 900;
	width: 13em;
}

.auth a:active {
	border-color: #666 #aaa #bbb #888;
	border-width: 2px 1px 1px 2px;
	color: #000;
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

			/*
			 * Once the user is authenticated, print out some basic user
			 * information in JSON format to verify that the authentication 
			 * worked.
			 */
			out.println("<pre>");
			out.println(helper.getUserInfoJson(request.getParameter("code")));
			out.println("</pre>");
		}
	%>
	</div>
</body>
</html>