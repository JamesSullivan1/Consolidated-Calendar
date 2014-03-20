package server.web;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.servlet.jsp.JspWriter;

import server.calAPI.APIManager;
import server.calAPI.GoogleCalAPI;
import server.framework.Calendar;

public class PushEventsHelper {

	public static void push(HttpServletRequest request, HttpSession session, JspWriter out) {
		
		//Create Google API Manager
		APIManager gCalAPIManager = new APIManager(new GoogleCalAPI());
		
		// Get Consolidated session calendar if it exists
		Calendar consolidated = (Calendar) session.getAttribute("consolidatedCalendar");
		if (consolidated == null) {
			consolidated = new Calendar.CalendarBuilder("Consolidated", null).withService("Consolidated-Cal").build();
		}
		
		gCalAPIManager.addEvents(consolidated.getEvents(), session);

		try {
			out.println("You're winner");
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
}
