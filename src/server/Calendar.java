package server;

import java.util.ArrayList;

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
	public Calendar(ArrayList<Event> events, String name, String service) {
		this.events = events;
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
	 * Add event to the Calendar, ignoring a duplicate event.
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
		// Some code
		event.setOwner(this);
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
	}
}
