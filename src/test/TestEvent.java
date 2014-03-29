package test;

import static org.junit.Assert.*;

import org.junit.Test;

import java.util.Date;

import server.framework.Calendar;
import server.framework.Event;

public class TestEvent {
	public static final long MIN_IN_MSEC = 60000l;
	
	@Test
	public void testEventBuilder_defaults() {
		
		String name = "eventName";
		Date startDate = new Date(1395770374);
		
		Event.EventBuilder tester = new Event.EventBuilder(name, startDate);
		
		assertTrue(tester.build().getStartDate().equals(startDate) && tester.build().getName().equals(name));
		
	}
	
	@Test
	public void testEventBuilder_null() {
		
		Event.EventBuilder tester = new Event.EventBuilder(null, null);
		
		assertTrue(tester.build().getStartDate() == null && tester.build().getName() == null);
		
	}
	
	@Test
	public void testEventBuilder_withLocation() {
		
		String name = "eventName";
		String loc = "location";
		Date startDate = new Date(1395770374);
		
		Event.EventBuilder tester = new Event.EventBuilder(name, startDate);
		tester.withLocation(loc);
		
		assertTrue(tester.build().getStartDate().equals(startDate) && tester.build().getName().equals(name) && tester.build().getLocation().equals(loc));
		
	}
	
	@Test
	public void testEventBuilder_withOwner_null() {
		
		String eName = "eventName";
		Date startDate = new Date(1395770374);

		Calendar cal = null;
		
		Event.EventBuilder tester = new Event.EventBuilder(eName, startDate);
		tester.withOwner(cal);
		
		assertTrue(tester.build().getStartDate().equals(startDate) && tester.build().getName().equals(eName) && tester.build().getOwner() == null);
		
	}
	
	@Test
	public void testEventBuilder_withEndDate() {
		
		String eName = "eventName";
		Date startDate = new Date(0);
		
		Date endDate = new Date(1395770374);
		
		Event.EventBuilder tester = new Event.EventBuilder(eName, startDate);
		tester.withEnd(endDate);
		
		assertTrue(tester.build().getStartDate().equals(startDate) && tester.build().getName().equals(eName) && tester.build().getEndDate().equals(endDate));
		
	}
	
	@Test
	public void testEventEquals_equal_defaults() {
		
		String eName = "eventName";
		Date startDate = new Date(0);
		
		Event.EventBuilder a = new Event.EventBuilder(eName, startDate);
		
		Event b = a.build();
		Event c = a.build();
		
		assertTrue(b.equals(c) && c.equals(b));

	}
	
	@Test
	public void testEventEquals_equal_withEnd() {
		
		String eName = "eventName";
		Date startDate = new Date(0);
		Date endDate = new Date(1000);
		
		Event.EventBuilder a = new Event.EventBuilder(eName, startDate);
		a.withEnd(endDate);
		
		Event b = a.build();
		Event c = a.build();
		
		assertTrue(b.equals(c) && c.equals(b));
		
	}
	
	@Test
	public void testEventEquals_equal_null() {

		Event.EventBuilder a = new Event.EventBuilder(null, null);
		
		Event b = a.build();
		Event c = a.build();
		
		assertTrue(b.equals(c) && c.equals(b));
		
	}
	
	@Test
	public void testEventEquals_diffNames() {
		
		String name1 = "eventName1";
		String name2 = "eventName2";
		Date start1 = new Date(0);

		Event.EventBuilder a = new Event.EventBuilder(name1, start1);
		Event.EventBuilder b = new Event.EventBuilder(name2, start1);
		
		Event c = a.build();
		Event d = b.build();
		
		assertFalse(c.equals(d) && d.equals(c));
		
	}
	
	@Test
	public void testEventEquals_diffStart() {
		
		String name1 = "eventName1";
		Date start1 = new Date(0);
		Date start2 = new Date(MIN_IN_MSEC);
		
		Event.EventBuilder a = new Event.EventBuilder(name1, start1);
		Event.EventBuilder b = new Event.EventBuilder(name1, start2);
		
		Event c = a.build();
		Event d = b.build();
		
		assertFalse(c.equals(d) && d.equals(c));
		
	}
	
	@Test
	public void testEventEquals_diffEnd() {
		
		String name1 = "eventName1";
		Date start1 = new Date(0);
		Date end1 = new Date(1000);
		Date end2 = new Date(1000 + MIN_IN_MSEC);
		
		Event.EventBuilder a = new Event.EventBuilder(name1, start1);
		Event.EventBuilder b = new Event.EventBuilder(name1, start1);
		a.withEnd(end1);
		b.withEnd(end2);
		
		Event c = a.build();
		Event d = b.build();
		
		assertFalse(c.equals(d) && d.equals(c));
		
	}
	
	@Test
	public void testEventEquals_wEnd_woEnd() {
		
		String name1 = "eventName1";
		Date start1 = new Date(0);
		Date end1 = new Date(1000);
		
		Event.EventBuilder a = new Event.EventBuilder(name1, start1);
		Event.EventBuilder b = new Event.EventBuilder(name1, start1);
		a.withEnd(end1);
		
		Event c = a.build();
		Event d = b.build();
		
		assertFalse(c.equals(d) && d.equals(c));
		
	}
	
	@Test
	public void testEventEquals_nullObj_vs_nonNull() {
		
		String eName = "eventName";
		Date startDate = new Date(0);
		
		Event.EventBuilder a = new Event.EventBuilder(eName, startDate);
		
		Event c = a.build();
		Event d = null;
		
		assertFalse(c.equals(d));
		
	}
	
	@Test
	public void testEventEquals_nullFields_vs_nonNull() {
		
		String eName = "eventName";
		Date startDate = new Date(0);
		
		Event.EventBuilder a = new Event.EventBuilder(eName, startDate);
		Event.EventBuilder b = new Event.EventBuilder(null, null);
		
		Event c = a.build();
		Event d = b.build();
		
		assertFalse(c.equals(d) && d.equals(c));
		
	}
}
