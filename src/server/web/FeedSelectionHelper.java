package server.web;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.servlet.jsp.JspWriter;

import com.google.api.client.auth.oauth2.Credential;

import server.oauth.GoogleAuthHelper;


public class FeedSelectionHelper {
	
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
//	public static void showCalendarChoice(HttpSession session, JspWriter out) throws IOException {
//		final GoogleAuthHelper helper = new GoogleAuthHelper();
//		final Credential credential = (Credential)session.getAttribute("authCredential");
//			
//		/*
//		 * Once the user is authenticated, print out some basic user
//		 * information in JSON format to verify that the authentication 
//		 * worked.
//		 */
//		final String calendarList = helper.getCalendarListJson(credential);
//		out.println("Successful login to Google. Below are your calendars in JSON format.");
//		out.println("<pre>");
//		out.println(calendarList);
//		out.println("</pre>");
//	}
	
	/**
	 * @param request
	 * @param session
	 */
	public static void editICSFeeds(HttpServletRequest request, HttpSession session)
		{
		//Get current feed list if it exists.
		ArrayList<URL> icsList = (ArrayList<URL>)session.getAttribute("icsList");
		if (icsList == null)
			icsList = new ArrayList<URL>();
		
		String newFeed = request.getParameter("addICS");
		if (newFeed != null)
			if (!icsList.contains(newFeed))
				if (newFeed.split("\\?")[0].matches("^https?://.+\\.ics$")){ //Some initial weak ics url validation.
					URL link = null;
					try {
						link = new URL(newFeed);
					} catch (MalformedURLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					icsList.add(link);
				}
				else
					session.setAttribute("badFeed", true);
		
		String removeFeed = (String)request.getParameter("removeICS");
		if (removeFeed != null)
			icsList.remove(removeFeed);
		
		session.setAttribute("icsList", icsList);
		}
}
