package server.web;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import server.framework.*;

public class FeedResultsHelper {

	/**
	 * Parses the user's input .ics feeds, creating new session calendars for
	 * each feed and populating these calendars with events.
	 * 
	 * @param request
	 * @param session
	 */
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

		// Iterate through the ICS list, creating a unique calendar for each ICS link.
		// Finish by merging this calendar into 'consolidated'.
		for (URL link : icsList) {
			File inputFile = null;
			try {
				inputFile = ICSFeedParser.downloadICSFile(link);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			String[] calendarInfo = null;
			try {
				calendarInfo = ICSFeedParser.getCalendarData(inputFile);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			// Get Calendar data
			String calServID = calendarInfo[0];
			String calendarName = calendarInfo[1];
			// Construct new calendar and add it to the session's calendarList
			Calendar newCal = new Calendar(calendarName, calServID);
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
		//session.setAttribute("icsList", null);
	}

}
