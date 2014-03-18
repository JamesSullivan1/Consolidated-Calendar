package server.framework;

import java.util.ArrayList;
import java.util.Date;

import server.framework.Event.EventBuilder;

/**
 * Calendar - An internal representation of a Calendar.
 * 
 */
public class Calendar {
	private ArrayList<Event> events;
	private String name;
	private String service;

	/**
	 * Constructor for a new Calendar object.
	 * 
	 * @param events
	 *            A (possibly empty) ArrayList containing Event objects
	 * @param name
	 *            A descriptor name for the Calendar
	 * @param service
	 *            A descriptor for the backing service of the Calendar
	 */
	public Calendar(CalendarBuilder cb) {
		this.events = cb.events;
		this.name = cb.name;
		this.service = cb.service;
	}

	/**
	 * Constructor for a new Calendar object with no existing events.
	 * 
	 * @param events
	 *            A (possibly empty) ArrayList containing Event objects
	 * @param name
	 *            A descriptor name for the Calendar
	 * @param service
	 *            A descriptor for the backing service of the Calendar
	 */
	public Calendar(String name, String service) {
		this.events = new ArrayList<Event>();
		this.name = name;
		this.service = service;
	}

	/**
	 * @return The service descriptor of the Calendar.
	 */
	public String getService() {
		return service;
	}

	/**
	 * @return The name descriptor of the Calendar.
	 */
	public String getName() {
		return name;
	}

	/**
	 * @return The ArrayList<Event> containing the Calendar's events.
	 */
	public ArrayList<Event> getEvents() {
		return events;
	}

	/**
	 * Add event to the Calendar, ignoring a duplicate event. Sets the Event's owner to be This.
	 * <p>
	 * <ul>
	 * <li>Precondition: An event object is provided as input
	 * <li>Postcondition: The event object is inserted into the Calendar, if it
	 * is not a duplicate.
	 * </ul>
	 * 
	 * @param event
	 *            The event to be added.
	 */
	public void addEvent(Event event) {
		// Redundancy checking
		for (Event e : events) {
			if (e.equals(event))
				return;
		}
		events.add(event);
	}

	/**
	 * Remove event from the Calendar, if it is list (including duplicate
	 * events)
	 * <p>
	 * <ul>
	 * <li>Precondition: An event object to be removed is provided as input
	 * <li>Postcondition: The event object is removed from the Calendar, or any
	 * duplicate events.
	 * </ul>
	 * 
	 * @param event
	 *            The event to be removed.
	 */
	public void removeEvent(Event event) {
		// Remove actual event and return
		if (events.contains(event)) {
			events.remove(event);
			return;
		}

		// Remove duplicate events
		for (Event e : events) {
			if (e.equals(event)) {
				events.remove(e);
				return;
			}
		}
	}
	
	/**
	 * Merges a second calendar into this Calendar, handling redundancy as needed.
	 * <p>
	 * <ul>
	 * <li>Precondition: A calendar object is given as input.
	 * <li>Postcondition: Any events not already in this Calendar are added into it.
	 * </ul>
	 * 
	 * @param c The Calendar to be merged in
	 */
	public void merge(Calendar c) {
		for (Event e : c.getEvents()) {
			addEvent(e);
		}
	}
	
	/**
	 * Removes all events from this common to the input Calendar
	 * @param other Calendar to check
	 */
	public void eventDiff(Calendar other){
		
		//other's events
		if (other.getEvents() == null) {
			return;
		}
		ArrayList<Event> otherEvents= other.getEvents();
		
		for(Event e : otherEvents){
			removeEvent(e);
		}
	}
	
	
	/**
	 * Static Calendar Builder class to enforce the parameters for the creation of Calendars
	 * 
	 * @author james
	 * 
	 */
	public static class CalendarBuilder {

		String name; // Required
		ArrayList<Event> events; // Required
		String service;

		/**
		 * Constructor for the CalendarBuilder providing required parameters.
		 * 
		 * @param name
		 *            Calendar name
		 * @param events
		 *            ArrayList (possibly empty) of calendar events
		 */
		public CalendarBuilder(String name, ArrayList<Event> events) {
			this.name = name;
			this.events = events;
			this.service = null;
		}

		/**
		 * @param service
		 *            Optional service name
		 */
		public CalendarBuilder withService(String service) {
			this.service = service;
			return this;
		}
		/**
		 * @return A new Calendar object built by this.
		 */
		public Calendar build() {
			return new Calendar(this);
		}
	}
}
