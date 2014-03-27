package test;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Date;

import org.junit.Test;

import server.framework.*;

public class TestCalendar {

	/*
	 * Contains no events.
	 */
	Calendar setup_empty(String service) {
		if (service == null) {
			return new Calendar.CalendarBuilder("Empty", null).build();
		}
		return new Calendar.CalendarBuilder("Empty", null).withService(service)
				.build();
	}

	/*
	 * Contains three events with the current system time. A - Start date only B
	 * - Start and End Date C - Start, End and Location
	 */
	Calendar setup_nonempty_A(String service) {
		ArrayList<Event> events = new ArrayList<Event>();
		Event a = new Event.EventBuilder("a", new Date()).build();
		events.add(a);
		Event b = new Event.EventBuilder("b", new Date()).withEnd(new Date())
				.build();
		events.add(b);
		Event c = new Event.EventBuilder("c", new Date()).withEnd(new Date())
				.withLocation("loc").build();
		events.add(c);
		if (service == null) {
			return new Calendar.CalendarBuilder("Empty", events).build();
		}
		return new Calendar.CalendarBuilder("Empty", events).withService(
				service).build();
	}

	/*
	 * Contains events with 100 static dates, all separated by 5ms from the
	 * epoch
	 */
	Calendar setup_nonempty_B(String service) {
		ArrayList<Event> events = new ArrayList<Event>();
		long tStart = 0l;
		long tEnd = 5l;
		Date dtStart = new Date(tStart);
		Date dtEnd = new Date(tEnd);
		for (int i = 0; i < 100; i++) {
			Event a = new Event.EventBuilder(new Integer(i).toString(), dtStart)
					.withEnd(dtEnd).build();
			events.add(a);
			tStart += 5;
			tEnd += 5;
			dtStart = new Date(tStart);
			dtEnd = new Date(tEnd);
		}
		if (service == null) {
			return new Calendar.CalendarBuilder("B", events).build();
		}
		return new Calendar.CalendarBuilder("B", events).withService(service)
				.build();
	}

	/*
	 * Contains events with 100 static dates, all separated by 5ms from the
	 * epoch (plus 50)
	 */
	Calendar setup_nonempty_C(String service) {
		ArrayList<Event> events = new ArrayList<Event>();
		long tStart = 50l;
		long tEnd = 55l;
		Date dtStart = new Date(tStart);
		Date dtEnd = new Date(tEnd);
		for (int i = 0; i < 100; i++) {
			Event a = new Event.EventBuilder(new Integer(i + 10).toString(),
					dtStart).withEnd(dtEnd).build();
			events.add(a);
			tStart += 5;
			tEnd += 5;
			dtStart = new Date(tStart);
			dtEnd = new Date(tEnd);
		}
		if (service == null) {
			return new Calendar.CalendarBuilder("C", events).build();
		}
		return new Calendar.CalendarBuilder("C", events).withService(service)
				.build();
	}

	// Test calendar builder with all null input
	@Test
	public void testCalendarBuilder_null() {
		Calendar c = new Calendar.CalendarBuilder(null, null).build();
		assertTrue(c != null);
	}

	// Test calendar builder with a name given
	@Test
	public void testCalendarBuilder_withName() {
		Calendar c = setup_empty(null);
		assertTrue(c.getName() == "Empty");
	}

	// Test calendar builder with a service given
	@Test
	public void testCalendarBuilder_empty_withService() {
		Calendar c = setup_empty("servid");
		assertTrue(c.getService().equals("servid"));
	}

	// Test calendar builder with a nonempty event array given
	@Test
	public void testCalendarBuilder_withEvents() {
		Calendar c = setup_nonempty_B(null);
		assertTrue(c.getEvents().isEmpty() == false);
		assertTrue(c.getEvents().get(0) != null);
	}

	// Tests calendar event addition with an event that has current date
	@Test
	public void testAddEvent() {
		Calendar c = setup_empty(null);
		Event e = new Event.EventBuilder("test", new Date()).build();
		c.addEvent(e);
		assertTrue(c.getEvents().get(0).equals(e));
	}

	// Tests calendar event addition of a redundant event
	@Test
	public void testAddRedundantEvent() {
		Calendar c = setup_empty(null);
		Event e = new Event.EventBuilder("test", new Date()).build();
		c.addEvent(e);
		c.addEvent(e);
		assertTrue(c.getEvents().size() < 2);
	}

	// Tests calendar event removal of an existing event
	@Test
	public void testRemoveEvent_inEvents() {
		Calendar c = setup_empty(null);
		Event e = new Event.EventBuilder("test", new Date()).build();
		c.addEvent(e);
		c.removeEvent(e);
		assertTrue(c.getEvents().isEmpty());
	}

	// Tests calendar event removal of a nonexistent event
	@Test
	public void testRemoveEvent_notInEvents() {
		Calendar c = setup_empty(null);
		Event e = new Event.EventBuilder("test", new Date()).build();
		Event f = new Event.EventBuilder("test2", new Date(0l)).build();
		c.addEvent(e);
		c.removeEvent(f);
		assertTrue(c.getEvents().size() == 1);
		assertTrue(c.getEvents().get(0).equals(e));
	}

	// Tests merging two empty calendars
	@Test
	public void testMerge_empty_empty() {
		Calendar a = setup_empty(null);
		Calendar b = setup_empty(null);
		a.merge(b);
		assertTrue(a.getEvents().isEmpty());
	}

	// Tests merging a nonempty calendar into an empty one
	@Test
	public void testMerge_empty_nonempty() {
		Calendar a = setup_empty(null);
		Calendar b = setup_nonempty_B(null);
		ArrayList<Event> bEvents = b.getEvents();
		a.merge(b);
		assertTrue(a.getEvents().equals(bEvents));
	}

	// Tests merging an empty calendar into a nonempty one
	@Test
	public void testMerge_nonempty_empty() {
		Calendar a = setup_nonempty_B(null);
		ArrayList<Event> aEvents = a.getEvents();
		Calendar b = setup_empty(null);
		a.merge(b);
		assertTrue(a.getEvents().equals(aEvents));
	}

	// Tests merging a nonempty calendar into a nonempty one with no redundant
	// events
	@Test
	public void testMerge_nonempty_nonempty() {
		Calendar a = setup_nonempty_A(null);
		ArrayList<Event> aEvents = a.getEvents();
		Calendar b = setup_nonempty_B(null);
		aEvents.addAll(b.getEvents()); // Combined array
		a.merge(b);
		assertTrue(a.getEvents().equals(aEvents));
	}

	// Tests merging a nonempty calendar into a nonempty one with redundant
	// events
	@Test
	public void testMerge_withRedundancy() {
		Calendar a = setup_nonempty_B(null); // 100 sequential events starting
												// from T = 0, T += 5
		ArrayList<Event> aEvents = new ArrayList<Event>(a.getEvents());
		Calendar b = setup_nonempty_C(null); // 100 sequential events starting
												// from T = 50, T += 5
		ArrayList<Event> bEvents = new ArrayList<Event>(b.getEvents());
		Date cutoff = aEvents.get(99).getStartDate();
		for (Event e : bEvents) {
			if (e.getStartDate().after(cutoff)) {
				aEvents.add(e);
			}
		}
		a.merge(b);
		boolean sameList = true;
		for (int i = 0; i < a.getEvents().size(); i++) {
			if (!a.getEvents().get(i).equals(aEvents.get(i))) {
				sameList = false;
			}
		}
		assertTrue(sameList);
	}

	// Tests event-diff on two empty calendars
	@Test
	public void testDiff_empty_empty() {
		Calendar a = setup_empty(null);
		Calendar b = setup_empty(null);
		a.eventDiff(b);
		assertTrue(a.getEvents().isEmpty());
	}

	// Test event-diff from an nonempty calendar into an empty one
	@Test
	public void testDiff_empty_nonempty() {
		Calendar a = setup_empty(null);
		Calendar b = setup_nonempty_B(null);
		a.eventDiff(b);
		assertTrue(a.getEvents().isEmpty());
	}

	// Test event-diff from an empty calendar into a nonempty one
	@Test
	public void testDiff_nonempty_empty() {
		Calendar a = setup_nonempty_B(null);
		ArrayList<Event> aEvents = a.getEvents();
		Calendar b = setup_empty(null);
		a.eventDiff(b);
		assertTrue(a.getEvents().equals(aEvents));
	}

	// Tests event-diff from two calendars with no common events

	@Test
	public void testDiff_nonempty_nonempty() {
		Calendar a = setup_nonempty_A(null);
		ArrayList<Event> aEvents = a.getEvents();
		Calendar b = setup_nonempty_B(null);
		a.eventDiff(b);
		assertTrue(a.getEvents().equals(aEvents)); // Assert nothing changed
	}

	// Tests event-diff from two calendars with some common events

	@Test
	public void testDiff_withRedundancy() {
		Calendar a = setup_nonempty_B(null); // 100 sequential events starting
												// from T = 0, T += 5
		ArrayList<Event> aEvents = new ArrayList<Event>(a.getEvents());
		Calendar b = setup_nonempty_C(null); // 100 sequential events starting
												// from T = 50, T += 5
		Date cutoff = b.getEvents().get(0).getStartDate();
		for (Event e : a.getEvents()) {
			if (e.getStartDate().after(cutoff)
					|| (e.getStartDate().equals(cutoff))) {
				aEvents.remove(e);
			}
		}
		a.eventDiff(b);
		boolean sameList = true;
		for (int i = 0; i < a.getEvents().size(); i++) {
			if (!a.getEvents().get(i).equals(aEvents.get(i))) {
				sameList = false;
			}
		}
		assertTrue(sameList);
	}

	@Test
	public void test() {

	}

}
