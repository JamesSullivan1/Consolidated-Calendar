package server.web;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.servlet.jsp.JspWriter;

import server.calAPI.APIManager;
import server.calAPI.GoogleCalAPI;
import server.exception.ServiceAccessException;
import server.framework.Calendar;

public class PushEventsHelper {

	@SuppressWarnings("unchecked")
	public static void push(HttpServletRequest request, HttpSession session,
			JspWriter out) {

		// Create Google API Manager
		APIManager gCalAPIManager = new APIManager(new GoogleCalAPI());
		// Get Consolidated session calendar if it exists
		Calendar consolidated = (Calendar) session
				.getAttribute("consolidatedCalendar");
		if (consolidated == null) {
			consolidated = new Calendar.CalendarBuilder("Consolidated", null)
					.withService("Consolidated-Cal").build();
		}

		try {
			gCalAPIManager.addEvents(consolidated.getEvents(), session);
		} catch (ServiceAccessException e) {
			// Set error for user.
			ArrayList<String> errors = (ArrayList<String>) session
					.getAttribute("parseErrors");
			synchronized (errors) {
				errors.add("An error occurred when attempting to add events to Google Calendar: "
						+ e.getMessage());
			}
			return;
		}

		try {
			out.println("You're winner");
		} catch (IOException e) {
			// Safe to duck - Indicates that the frontend is not responsive.
		}

	}

}
