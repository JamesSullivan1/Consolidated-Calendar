package server.web;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.http.HttpSession;

import server.calAPI.APIManager;
import server.calAPI.GoogleCalAPI;
import server.framework.*;

/**
 * Provides threaded support for retrieving calendar data from google
 * which is then synced with the consolidated calendar.
 * 
 * @author Daniel
 */
public class AuthThread extends Thread {
	private HttpSession session;
	
	/**
	 * Construct new authentication thread with set session.
	 * 
	 * @param session server session object
	 */
	public AuthThread(HttpSession session) {
		this.session = session;
	}

	/*
	 * (non-Javadoc)
	 * @see java.lang.Thread#run()
	 */
	@SuppressWarnings("unchecked")
	public void run() {
		ThreadHelper.incrementThreadCount(session);
		
		//Create Google API Manager
		APIManager gCalAPIManager = new APIManager(new GoogleCalAPI());
		
		//Get Consolidated calendar.
		Calendar consolidated = (Calendar)session.getAttribute("consolidatedCalendar");
		if (consolidated == null) {
			synchronized(session) {
				consolidated = new Calendar.CalendarBuilder("Consolidated",null).withService("Consolidated-Cal").build();
				session.setAttribute("consolidatedCalendar", consolidated);
			}
		}
		
		//Pull calendar from google.
		Calendar primaryGCal = null;
		try {
			//Fetch users primary google calendar
			primaryGCal = gCalAPIManager.fetch(session);
		} catch (IOException e) {
			//Set error for user.
			ArrayList<String> errors = (ArrayList<String>)session.getAttribute("parseErrors");
			synchronized (errors) {
				errors.add("Failed to get calendar from google: "+e.getMessage());
			}
			ThreadHelper.decrementThreadCount(session);
			return;
		}
		
		//Remove gcal events from consolidated calendar.
		synchronized (consolidated) {
			session.setAttribute("primaryGCal", primaryGCal);
			consolidated.eventDiff(primaryGCal);
			
			//Set new events flag.
			session.setAttribute("eventsToAdd", !consolidated.getEvents().isEmpty());
		}
		
		ThreadHelper.decrementThreadCount(session);
	}
}