package server.web;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.servlet.jsp.JspWriter;
import server.framework.Calendar;
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
	 * @param session
	 *            server session object
	 * @param out
	 *            writer to print to the calling jsp page
	 * @throws IOException
	 */
	public static void showGoogleAuthLink(HttpSession session, JspWriter out)
			throws IOException {
		final GoogleAuthHelper helper = new GoogleAuthHelper();

		// On the first visit, give the user a link to the authentication page
		out.println("<a href='" + helper.buildLoginUrl()
				+ "'>Sign in with Google</a>");
		session.setAttribute("stateString", helper.getStateToken());
	}

	/**
	 * Processes adds and removals to the saved ics strings.
	 * 
	 * @param request
	 *            server request object
	 * @param session
	 *            server session object
	 */
	public static void editICSFeeds(HttpServletRequest request,
			HttpSession session) {
		// Get current feed list if it exists.
		@SuppressWarnings("unchecked")
		ArrayList<URL> icsList = (ArrayList<URL>) session
				.getAttribute("icsList");

		// Pull add request if it exists, and only process if it is not already
		// saved. Do a basic url check.
		String newFeed = request.getParameter("addICS");
		if (newFeed != null) {
			try {
				URL newURL = new URL(newFeed);
				if (!icsList.contains(newURL)) {
					if (newFeed.split("\\?")[0].matches("^https?://.+\\.ics$")) {
						//New ICS feed is good. Create parsing thread.
						icsList.add(newURL);
						AddICSThread addICS = new AddICSThread(session, newURL);
						addICS.start();
					} else
						session.setAttribute("badFeed", true);
				}
			} catch (MalformedURLException e) {
				session.setAttribute("badFeed", true);
			}
		}

		// Pull remove request and remove feed.
		try {
			String removeFeedString = (String)request.getParameter("removeICS");
			if (removeFeedString != null) {
				URL toRemove = new URL(removeFeedString);
				
				//Remove url from ics list.
				icsList.remove(toRemove);
				
				//Get stored ics calendar.
				Calendar oldCal = (Calendar)session.getAttribute(toRemove.toString());
				if (oldCal != null) {
					//Remove stored ics calendar from session.
					session.removeAttribute(toRemove.toString());
					
					// Get Consolidated session calendar
					Calendar consolidated = (Calendar)session.getAttribute("consolidatedCalendar");
					
					synchronized (consolidated) {
						//Remove events in old calendar from consolidated calendar
						consolidated.eventDiff(oldCal);
						
						//Set new events flag.
						session.setAttribute("eventsToAdd", !consolidated.getEvents().isEmpty());
					}
				}
			}
		}
		catch (MalformedURLException e) {
			// TODO Remove feed string was bad. This should not happen!
			// It would not have been stored in the first place.
			e.printStackTrace();
		}
	}
}
