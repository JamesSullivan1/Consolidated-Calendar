package server.web;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.servlet.jsp.JspWriter;

import server.calAPI.APIManager;
import server.calAPI.GoogleCalAPI;
import server.framework.*;

/**
 * Main class that provides all java functionality for feedResults.jsp.
 * 
 * @author James
 */
public class MergeResultsHelper {

	/**
	 * Parses the user's input .ics feeds, creating new session calendars for
	 * each feed and populating these calendars with events.
	 * 
	 * @param request
	 *            server request object
	 * @param session
	 *            server session object
	 */
	@SuppressWarnings("unchecked")
	public static void parseFeeds(HttpServletRequest request,
			HttpSession session) {
		// Get current feed list if it exists.
		ArrayList<URL> icsList = (ArrayList<URL>) session
				.getAttribute("icsList");
		if (icsList == null) {
			return; // TODO Add error handling
		}

		// Get calendarList. Create if nonexistent.
		ArrayList<Calendar> calendarList = (ArrayList<Calendar>) session
				.getAttribute("calendarList");
		if (calendarList == null) {
			calendarList = new ArrayList<Calendar>();
		}

		// Get Consolidated session calendar if it exists
		Calendar consolidated = (Calendar) session
				.getAttribute("consolidatedCalendar");
		if (consolidated == null) {
			consolidated = new Calendar("Consolidated", "Consolidated-Cal");
		}

		// Get session eventsToAdd if it exist
		ArrayList<Event> eventsToAdd = (ArrayList<Event>) session
				.getAttribute("eventsToAdd");
		if (eventsToAdd == null) {
			eventsToAdd = consolidated.getEvents();
		}

		// Iterate through the ICS list, creating a unique calendar for each ICS
		// link.
		// Finish by merging this calendar into 'consolidated'.
		for (URL link : icsList) {
			File inputFile = null;
			try {
				inputFile = ICSFeedParser.downloadICSFile(link);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			// Get new calendar from the ICS feed
			Calendar newCal = null;
			try {
				newCal = ICSFeedParser.getCalendarData(inputFile);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			calendarList.add(newCal);
			// Get event data
			Event[] events = null;
			try {
				events = ICSFeedParser.getEvents(inputFile);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			// Add all events to the newly created calendar
			for (Event e : events) {
				newCal.addEvent(e);
			}
			// Merge new calendar into 'consolidated'
			consolidated.merge(newCal);
			// Cleanup
			inputFile.delete();
		}

		session.setAttribute("calendarList", calendarList);
		session.setAttribute("consolidatedCalendar", consolidated);
		session.setAttribute("eventsToAdd", eventsToAdd);
		// session.setAttribute("icsList", null);
	}

	/**
	 * Pulls user's Events from their Google Calendar and merges into
	 * consolidated.
	 * 
	 * @param request
	 *            server request object
	 * @param session
	 *            server session object
	 */
	public static void pullGoogleEvents(HttpServletRequest request, HttpSession session) {

		//Create Google API Manager
		APIManager gCalAPIManager = new APIManager(new GoogleCalAPI());
		
		// Get Consolidated session calendar if it exists
		Calendar consolidated = (Calendar) session.getAttribute("consolidatedCalendar");
		if (consolidated == null) {
			consolidated = new Calendar("Consolidated", "Consolidated-Cal");
		}

		Calendar primaryGCal = gCalAPIManager.fetch(session);

		if (primaryGCal != null) {
			consolidated.eventDiff(primaryGCal);
		}

	}

	public static void showResults(HttpServletRequest request,
			HttpSession session, JspWriter out) {

		// Get Consolidated session calendar if it exists
		Calendar consolidated = (Calendar) session
				.getAttribute("consolidatedCalendar");
		if (consolidated == null) {
			consolidated = new Calendar("Consolidated", "Consolidated-Cal");
		}

		if (consolidated.getEvents().isEmpty()) {
			try {
				out.println("No events found that are not already in your Calendar.");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else {
			try {
				out.println("<h2>Found " + consolidated.getEvents().size()
						+ " new events.</h2>");
				for (Event e : consolidated.getEvents()) {
					if (e != null) {
						out.println("<li>" + e.toString() + "</li>");
					}
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
