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
	 * @param owner
	 *            The Calendar that owns the Event
	 * @param startDate
	 *            The starting date of the Event
	 * @param endDate
	 *            The ending date of the Event
	 */
	public Event(String name, String location, Date startDate, Date endDate, Calendar owner) {
		this.name = name;
		this.location = location;
		this.owner = owner;
		this.startDate = startDate;
		this.endDate = endDate;
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
	public Date getStart() {
		return startDate;
	}

	/**
	 * @return The End date of the Event
	 */
	public Date getEnd() {
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

	public String toString() {
		return name;
	}
}
