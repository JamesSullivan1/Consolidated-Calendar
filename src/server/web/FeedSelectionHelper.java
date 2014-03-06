package server.web;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.servlet.jsp.JspWriter;
import server.oauth.GoogleAuthHelper;


/**
 * Main class that provides all java functionality for feedResults.jsp.
 * 
 * @author Daniel Wehr
 */
public class FeedSelectionHelper {
	
	/**
	 * Displays google sign in link.
	 * 
	 * @param session server session object
	 * @param out writer to print to the calling jsp page
	 * @throws IOException
	 */
	public static void showGoogleAuthLink(HttpSession session, JspWriter out) throws IOException {
	final GoogleAuthHelper helper = new GoogleAuthHelper();
	
	// On the first visit, give the user a link to the authentication page
	out.println("<a href='" + helper.buildLoginUrl() + "'>Sign in with Google</a>");
	session.setAttribute("stateString", helper.getStateToken());
	}
	
	/**
	 * Processes adds and removals to the saved ics strings.
	 * 
	 * @param request server request object
	 * @param session server session object
	 */
	public static void editICSFeeds(HttpServletRequest request, HttpSession session) {
		//Get current feed list if it exists.
		@SuppressWarnings("unchecked")
		ArrayList<URL> icsList = (ArrayList<URL>)session.getAttribute("icsList");
		if (icsList == null)
			icsList = new ArrayList<URL>();
		
		//Pull add request if it exists, and only process if it is not already saved
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
		
		//Pull remove request and remove feed.
		String removeFeed = (String)request.getParameter("removeICS");
		if (removeFeed != null)
			icsList.remove(removeFeed);
		
		session.setAttribute("icsList", icsList);
	}
}
