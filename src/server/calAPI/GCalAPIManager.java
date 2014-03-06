package server.calAPI;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.google.api.client.util.DateTime;
import com.google.api.services.calendar.model.CalendarListEntry;
import com.google.api.services.calendar.model.Event;
import com.google.api.services.calendar.model.EventDateTime;

import server.framework.*;

public class GCalAPIManager {
	
	//Asks for client and retrieves/parses GCal to our Calendar format
	public static Calendar fetchGCal(com.google.api.services.calendar.Calendar client) throws IOException{
		
		//find primary calendar
		com.google.api.services.calendar.model.CalendarList test = client.calendarList().list().setPageToken(null).execute();
		List<CalendarListEntry> items = test.getItems();
		CalendarListEntry primeListEntry = null;
		for (CalendarListEntry calEntry : items){
			if (calEntry.isPrimary()){
				primeListEntry = calEntry;
			}
		}
		
		//retrieve primary calendar
		String primeId = primeListEntry.getId();
		com.google.api.services.calendar.model.Calendar primeCal = client.calendars().get(primeId).execute();
		String calname = primeCal.getSummary();
		String servId = primeCal.getId();
		
		//create google event list
		com.google.api.services.calendar.model.Events feed = client.events().list(primeId).execute();
		List<Event> eventList = feed.getItems();
		
		//create our event list
		ArrayList<server.framework.Event> e = new ArrayList<server.framework.Event>();
		
		//Convert  events
		for (Event event : eventList){
			Date startDate = new Date(event.getStart().getDate().getValue());
			Date endDate = new Date(event.getEnd().getDate().getValue());
			String summary = event.getSummary();
			String location = event.getLocation();
			
			server.framework.Event temp = new server.framework.Event(summary, location, startDate, endDate);
			e.add(temp);
		}
		
		// Construct calendar from google data
		Calendar c = new Calendar(e, calname, servId);
		return c;
	}
	
	/**
	 * Adds events into the primary google calendar.
	 * 
	 * @param e
	 *            An ArrayList of events predetermined to not be redundant
	 * @param client
	 *            The current google client using the app
	 */
	public static void addEvents(ArrayList<server.framework.Event> e, com.google.api.services.calendar.Calendar client) throws IOException {
	    //Get list of google events from our events
		ArrayList<Event> newEvents = convertEvents(e, client);
	    
	    //find primary calendar
	    com.google.api.services.calendar.model.CalendarList test = client.calendarList().list().setPageToken(null).execute();
	    List<CalendarListEntry> items = test.getItems();
	    CalendarListEntry primeListEntry = null;
	    for (CalendarListEntry calEntry : items){
	    	if (calEntry.isPrimary()){
	    		primeListEntry = calEntry;
	    	}
	    }
	  	
	    //retrieve primary calendar
	    String primeId = primeListEntry.getId();
	  	
	    //Add new events
		for(Event event : newEvents){
			client.events().insert(primeId, event);
		}
	}
	
	/**
	 * Converts an ArrayList of our Events into an ArrayList Google Events
	 * 
	 * @param e
	 * 			An ArrayList of our Events
	 * @param client
	 * 			The current google client using the app
	 */
	private static ArrayList<Event> convertEvents(ArrayList<server.framework.Event> e, com.google.api.services.calendar.Calendar client){
		ArrayList<Event> newEvents = new ArrayList<Event>();
		for(server.framework.Event event : e){
			Event temp = new Event();
			temp.setSummary(event.getName());
			temp.setLocation(event.getLocation());
			temp.setStart(new EventDateTime().setDate(new DateTime(event.getStartDate())));
			temp.setEnd(new EventDateTime().setDate(new DateTime(event.getEndDate())));
			
			newEvents.add(temp);
		}
		return newEvents;
	}
}
