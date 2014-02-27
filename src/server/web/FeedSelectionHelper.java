package server.web;

import java.io.IOException;
import javax.servlet.http.HttpSession;
import javax.servlet.jsp.JspWriter;
import com.google.api.client.auth.oauth2.Credential;
import server.oauth.GoogleAuthHelper;


public class FeedSelectionHelper
	{
	
	/**
	 * Displays google sign in link.
	 * 
	 * @param session
	 * @param out
	 * @throws IOException
	 */
	public static void showGoogleAuthLink(HttpSession session, JspWriter out) throws IOException {
	final GoogleAuthHelper helper = new GoogleAuthHelper();
	
	// On the first visit, give the user a link to the authentication page
	out.println("<a href='" + helper.buildLoginUrl() + "'>Sign in with Google</a>");
	session.setAttribute("stateString", helper.getStateToken());
	}
	
	/**
	 * @param session
	 * @param out
	 * @throws IOException
	 */
	public static void showCalendarChoice(HttpSession session, JspWriter out) throws IOException {
		final GoogleAuthHelper helper = new GoogleAuthHelper();
		final Credential credential = (Credential)session.getAttribute("authCredential");
			
		/*
		 * Once the user is authenticated, print out some basic user
		 * information in JSON format to verify that the authentication 
		 * worked.
		 */
		final String calendarList = helper.getCalendarListJson(credential);
		out.println("Successful login to Google. Below are your calendars in JSON format.");
		out.println("<pre>");
		out.println(calendarList);
		out.println("</pre>");
	}
}
