package server.framework;

import java.util.Date;

/**
 * An internal representation of an Event in a Calendar.
 * 
 */
public class Event {
	private String name;
	private String location;
	private Calendar owner;
	private Date startDate;
	private Date endDate;

	/**
	 * Constructor for an Event object.
	 * 
	 * @param name
	 *            The name descriptor for the Event
	 * @param location
	 *            The location of the Event
	 * @param startDate
	 *            The starting date of the Event
	 * @param endDate
	 *            The ending date of the Event
	 */
	public Event(String name, String location, Date startDate, Date endDate) {
		this.name = name;
		this.location = location;
		this.startDate = startDate;
		this.endDate = endDate;
	}
	
	/**
	 * Constructor for an Event object with only one date specified.
	 * 
	 * @param name
	 *            The name descriptor for the Event
	 * @param location
	 *            The location of the Event
	 * @param startDate
	 *            The starting date of the Event
	 */
	public Event(String name, String location, Date startDate) {
		this.name = name;
		this.location = location;
		this.startDate = startDate;
	}

	/**
	 * @return The name descriptor of the Event
	 */
	public String getName() {
		return name;
	}

	/**
	 * @return The location of the Event
	 */
	public String getLocation() {
		return location;
	}

	/**
	 * @return The Calendar that owns the Event
	 */
	public Calendar getOwner() {
		return owner;
	}

	/**
	 * @return The Start date of the Event
	 */
	public Date getStartDate() {
		return startDate;
	}

	/**
	 * @return The End date of the Event
	 */
	public Date getEndDate() {
		return endDate;
	}

	/**
	 * Sets the Calendar that owns the Event.
	 * 
	 * @param owner
	 *            The Calendar to own the Event.
	 */
	public void setOwner(Calendar owner) {
		this.owner = owner;
	}

	/**
	 * Returns true if the other event is identical (checks name, start and end
	 * time).
	 * 
	 * @param Event
	 *            event the event to compare.
	 * @return True if the events are identical.
	 */
	public boolean equals(Event event) {
		boolean nameEquals = false;
		boolean startDateEquals = false;
		boolean endDateEquals = false;
		if (event.name.equalsIgnoreCase(this.name)) {
			nameEquals = true;
		} 
		if (event.startDate.equals(this.startDate)) {
			startDateEquals = true;
		} 
		if (event.endDate != null && this.endDate != null) {
			if (event.endDate.equals(this.endDate)) {
				endDateEquals = true;
			}
		} 
		if (event.endDate == null || this.endDate == null){
			endDateEquals = true;
		}
		return nameEquals && startDateEquals && endDateEquals;
	}

	/**
	 * Returns a string representation of this Event.
	 * @return String representation. Format- "name: startDate [to endDate]"
	 */
	public String toString() {
		if (endDate == null) {
			return name + ":    " + startDate.toString();
		}
		return name + ":    " + startDate.toString() + " to " + endDate.toString();
	}
}
