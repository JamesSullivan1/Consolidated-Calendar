package server.web;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import javax.servlet.http.HttpSession;
import server.framework.Calendar;
import server.framework.Event;
import server.framework.ICSFeedParser;

/**
 * Provides threaded support for parsing an ics file located at a given URL and
 * integrating its events into the consolidated calendar.
 * 
 * @author Daniel
 */
public class AddICSThread extends Thread {
	private HttpSession session;
	private URL url;

	public AddICSThread(HttpSession session, URL url) {
		this.session = session;
		this.url = url;
	}

	@SuppressWarnings("unchecked")
	public void run() {
		ThreadHelper.incrementThreadCount(session);

		System.out.println("Parsing");
		// Create a unique calendar for the ICS feed located at given url.
		File inputFile = null;
		try {
			inputFile = ICSFeedParser.downloadICSFile(url);
			System.out.println("Success");
		} catch (IOException e) {
			ArrayList<String> errors = (ArrayList<String>) session
					.getAttribute("parseErrors");
			synchronized (errors) {
				errors.add("Error downloading ICS file: " + e.getMessage());
			}
			ThreadHelper.decrementThreadCount(session);
			return;
		}

		// Parse new calendar from the ICS file
		Calendar newCal = null;
		try {
			newCal = ICSFeedParser.getCalendarData(inputFile);
		} catch (IOException e) {
			ArrayList<String> errors = (ArrayList<String>) session
					.getAttribute("parseErrors");
			synchronized (errors) {
				errors.add("Error parsing ICS calendar data from file: "
						+ e.getMessage());
			}
			ThreadHelper.decrementThreadCount(session);
			return;
		}

		// Parse event data from the ICS file
		Event[] events = null;
		try {
			events = ICSFeedParser.getEvents(inputFile);
		} catch (IOException e) {
			ArrayList<String> errors = (ArrayList<String>) session
					.getAttribute("parseErrors");
			synchronized (errors) {
				errors.add("Error parsing ICS event data from file: "
						+ e.getMessage());
			}
			ThreadHelper.decrementThreadCount(session);
			return;
		}

		// Add all events to the newly created calendar
		for (Event e : events) {
			newCal.addEvent(e);
		}

		// Get Consolidated session calendar
		Calendar consolidated = (Calendar) session
				.getAttribute("consolidatedCalendar");

		synchronized (consolidated) {
			// Store the new calendar in session under the calendar url.
			session.setAttribute(url.toString(), newCal);

			// Get the stored google calendar, if it exists.
			// Remove duplicates if necessary.
			Calendar gCal = (Calendar) session.getAttribute("primaryGCal");
			if (gCal != null)
				newCal.eventDiff(gCal);

			// Merge new calendar into consolidated calendar
			consolidated.merge(newCal);

			// Set new events flag.
			session.setAttribute("eventsToAdd", !consolidated.getEvents()
					.isEmpty());
		}

		// Cleanup
		inputFile.delete();
		ThreadHelper.decrementThreadCount(session);
	}

}
