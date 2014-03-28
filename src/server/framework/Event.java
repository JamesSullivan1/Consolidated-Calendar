package server.framework;

import java.io.Serializable;
import java.util.Date;

/**
 * An internal representation of an Event in a Calendar.
 * 
 */
public class Event implements Serializable {
	private static final long serialVersionUID = 1L;
	private String name; // Required
	private String location;
	private Calendar owner;
	private Date startDate; // Required
	private Date endDate;
	private boolean hasEndDate; // Required

	/*
	 * Private contructor for the Event object.
	 */
	private Event(EventBuilder builder) {
		this.name = builder.name;
		this.location = builder.location;
		this.owner = builder.owner;
		this.startDate = builder.start;
		this.endDate = builder.end;
		this.hasEndDate = builder.hasEndDate;
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
	 * @return True if the Event has a specified End Date
	 */
	public boolean hasEndDate() {
		return this.hasEndDate;
	}

	/**
	 * Returns true if the other event is identical (checks name, start and end
	 * time).
	 * 
	 * @param Event
	 *            event the event to compare.
	 * @return True if the events are identical.
	 */
	@Override
	public boolean equals(Object o) {
		if (!(o instanceof Event)){
			return false;
		}
		// Safe to cast to Event now
		Event event = (Event)o;
		
		boolean nameEquals = (event.getName() == null) == (this.name == null);
		boolean startDateEquals = (event.getStartDate() == null) == (this.startDate == null);
		
		//test equality of each attribute
		if (event.getName() != null && this.name != null){
			nameEquals = (event.getName().equalsIgnoreCase(this.getName()));
		}
		
		if (event.getStartDate() != null && this.startDate != null){
			startDateEquals = event.getStartDate().equals(this.getStartDate());
		}

		boolean endDateEquals = false;
		if (event.hasEndDate() && this.hasEndDate()) {
			endDateEquals = event.getEndDate().equals(this.getEndDate());
		} else {
			endDateEquals = (!event.hasEndDate() && !this.hasEndDate());
		}

		return nameEquals && startDateEquals && endDateEquals;
	}

	/**
	 * Returns a string representation of this Event.
	 * 
	 * @return String representation. Format- "name: startDate [to endDate]"
	 */
	public String toString() {
		if (endDate == null) {
			return name + ":    " + startDate.toString();
		}
		return name + ":    " + startDate.toString() + " to "
				+ endDate.toString();
	}

	/**
	 * Static Event Builder class to enforce the parameters for the creation of
	 * Events.
	 * 
	 * @author james
	 * 
	 */
	public static class EventBuilder {

		String name; // Required
		String location;
		Calendar owner;
		Date start; // Required
		Date end;
		boolean hasEndDate; // Required

		/**
		 * Constructor for the EventBuilder providing required parameters.
		 * 
		 * @param name
		 *            Event name
		 * @param start
		 *            End time for the Event
		 */
		public EventBuilder(String name, Date start) {
			this.name = name;
			this.start = start;
			this.hasEndDate = false;
		}

		/**
		 * @param end
		 *            Optional end date
		 */
		public EventBuilder withEnd(Date end) {
			this.end = end;
			this.hasEndDate = true;
			return this;
		}

		/**
		 * @param loc
		 *            Optional Event location
		 */
		public EventBuilder withLocation(String loc) {
			this.location = loc;
			return this;
		}

		/**
		 * @param owner
		 *            Optional Calendar that owns the Event
		 */
		public EventBuilder withOwner(Calendar owner) {
			this.owner = owner;
			return this;
		}

		/**
		 * @return A new Event object built by this.
		 */
		public Event build() {
			return new Event(this);
		}
	}

}
