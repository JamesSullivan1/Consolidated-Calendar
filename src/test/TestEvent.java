package test;

import static org.junit.Assert.*;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Date;

import server.framework.Calendar;
import server.framework.Event;

public class TestEvent {
	
	@Test
	public void testEventBuilder_goodInput() {
		
		String name = "eventName";
		Date startDate = new Date(1395770374);
		
		Event.EventBuilder tester = new Event.EventBuilder(name, startDate);
		
		assertTrue(tester.build().getStartDate().equals(startDate));
		assertTrue(tester.build().getName().equals(name));
		
	}
	
	@Test
	public void testEventBuilder_badInput() {
		
		Event.EventBuilder tester = new Event.EventBuilder(null, null);
		
		assertTrue(tester.build().getStartDate().equals(null));
		assertTrue(tester.build().getName().equals(null));
		
	}
	
	@Test
	public void testEventBuilder_withLocation_goodInput() {
		
		String name = "eventName";
		String loc = "location";
		Date startDate = new Date(1395770374);
		
		Event.EventBuilder tester = new Event.EventBuilder(name, startDate);
		tester.withLocation(loc);
		
		assertTrue(tester.build().getStartDate().equals(startDate));
		assertTrue(tester.build().getName().equals(name));
		assertTrue(tester.build().getLocation().equals(loc));
		
	}
	
	@Test
	public void testEventBuilder_withLocation_badInput() {
		
		String name = "eventName";
		Date startDate = new Date(1395770374);
		
		Event.EventBuilder tester = new Event.EventBuilder(name, startDate);
		tester.withLocation(null);
		
		assertTrue(tester.build().getStartDate().equals(startDate));
		assertTrue(tester.build().getName().equals(name));
		assertTrue(tester.build().getLocation().equals(null));
		
	}
	
	@Test
	public void testEventBuilder_withOwner_goodInput() {
		
		String eName = "eventName";
		Date startDate = new Date(1395770374);
		
		String cName = "calName";
		ArrayList<Event> arr = new ArrayList<Event>();
		Calendar cal = new Calendar.CalendarBuilder(cName, arr).build();
		
		Event.EventBuilder tester = new Event.EventBuilder(eName, startDate);
		tester.withOwner(cal);
		
		assertTrue(tester.build().getStartDate().equals(startDate));
		assertTrue(tester.build().getName().equals(eName));
		assertTrue(tester.build().getOwner().equals(cal));
		
	}
	
	@Test
	public void testEventBuilder_withOwner_badInput() {
		
		String eName = "eventName";
		Date startDate = new Date(1395770374);

		Calendar cal = null;
		
		Event.EventBuilder tester = new Event.EventBuilder(eName, startDate);
		tester.withOwner(cal);
		
		assertTrue(tester.build().getStartDate().equals(startDate));
		assertTrue(tester.build().getName().equals(eName));
		assertTrue(tester.build().getOwner().equals(cal));
		
	}
	
	@Test
	public void testEventBuilder_withEndDate_goodInput() {
		
		String eName = "eventName";
		Date startDate = new Date(0);
		
		Date endDate = new Date(1395770374);
		
		Event.EventBuilder tester = new Event.EventBuilder(eName, startDate);
		tester.withEnd(endDate);
		
		assertTrue(tester.build().getStartDate().equals(startDate));
		assertTrue(tester.build().getName().equals(eName));
		assertTrue(tester.build().getEndDate().equals(endDate));
		
	}
	
	@Test
	public void testEventBuilder_withEndDate_badInput() {
		
		String eName = "eventName";
		Date startDate = new Date(0);
		
		Date endDate = null;
		
		Event.EventBuilder tester = new Event.EventBuilder(eName, startDate);
		tester.withEnd(endDate);
		
		assertTrue(tester.build().getStartDate().equals(startDate));
		assertTrue(tester.build().getName().equals(eName));
		assertTrue(tester.build().getEndDate().equals(endDate));
		
	}
	
	@Test
	public void testEventEquals_equal() {
		
		String eName = "eventName";
		Date startDate = new Date(0);
		Date endDate = new Date(1000);
		
		Event.EventBuilder a = new Event.EventBuilder(eName, startDate);
		
		Event.EventBuilder b = new Event.EventBuilder(eName, startDate);
		b.withEnd(endDate);
		
		Event c = a.build();
		Event d = a.build();
		
		assertTrue(c.equals(d));
		assertTrue(d.equals(c));
		
		c = b.build();
		d = b.build();
		
		assertTrue(c.equals(d));
		assertTrue(d.equals(c));
		
	}
	
	@Test
	public void testEventEquals_non_equal() {
		
		String name1 = "eventName1";
		Date start1 = new Date(0);
		Date end1 = new Date(1000);
		
		String name1 = "eventName2";
		Date start1 = new Date(0);
		Date end1 = new Date(1000);
		
		Event.EventBuilder a = new Event.EventBuilder(eName, startDate);
		
		Event.EventBuilder b = new Event.EventBuilder(eName, startDate);
		b.withEnd(endDate);
		
		Event c = a.build();
		Event d = a.build();
		
		assertTrue(c.equals(d));
		assertTrue(d.equals(c));
		
		c = b.build();
		d = b.build();
		
		assertTrue(c.equals(d));
		assertTrue(d.equals(c));
		
	}
	
	@Test
	public void testEventEquals_badInput() {
		
	}
	
	public void test() {
		//not yet implemented
	}
}
