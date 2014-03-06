package server.web;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.servlet.jsp.JspWriter;

import server.calAPI.GCalAPIManager;
import server.framework.Calendar;

public class PushEventsHelper {

	public static void push(HttpServletRequest request, HttpSession session, JspWriter out) {
		// Get Consolidated session calendar if it exists
		Calendar consolidated = (Calendar) session
				.getAttribute("consolidatedCalendar");
		if (consolidated == null) {
			consolidated = new Calendar("Consolidated", "Consolidated-Cal");
		}
		
		
		// Get Consolidated session calendar if it exists
		com.google.api.services.calendar.Calendar client = (com.google.api.services.calendar.Calendar) session
				.getAttribute("googleClient");
		if (client == null) {
			// TODO Handle errors
		}
		
		try {
			GCalAPIManager.addEvents(consolidated.getEvents(), client);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		try {
			out.println("You're winner");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
}
