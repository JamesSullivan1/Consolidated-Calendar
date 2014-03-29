package test;

import static org.junit.Assert.*;

import java.io.*;

import org.junit.Test;

import server.framework.Calendar;
import server.framework.Calendar.CalendarBuilder;
import server.framework.ICSFeedParser;

public class TestParser {

	@Test
	public void OpenMissingInputCalendar(){
		//Open nonexistent file
		File f = new File("null");
		try{
			ICSFeedParser.getCalendarData(f);
		}
		catch(FileNotFoundException enf){
			boolean pass = true;
			assertTrue(pass);
		}
		catch(IOException eio){
			fail("Wrong exception type!\nExpected: FileNotFoundException\nGot: IOException");
		}
	}

	@Test
	public void OpenMissingInputEvent(){
		//Open nonexistent file
		File f = new File("null");
		try{
			ICSFeedParser.getEvents(f);
		}
		catch(FileNotFoundException enf){
			boolean pass = true;
			assertTrue(pass);
		}
		catch(IOException eio){
			fail("Wrong exception type!\nExpected: FileNotFoundException\nGot: IOException");
		}
	}

	@Test
	public void ParseInvalidFileCalendar(){
		File f = new File("resources/icsTestFiles/INVALID");
		try{
			ICSFeedParser.getCalendarData(f);
		}
		catch(FileNotFoundException enf){
			fail("Exception caught!\nGot: FileNotFoundException\nExpected: IOException");
		}
		catch(IOException eio){
			boolean pass = true;
			assertTrue(pass);
		}
	}

	@Test
	public void ParseInvalidFileEvents(){
		File f = new File("resources/icsTestFiles/INVALID");
		try{
			ICSFeedParser.getEvents(f);
		}
		catch(FileNotFoundException enf){
			fail("Exception caught!\nGot: FileNotFoundException\nExpected: IOException");
		}
		catch(IOException eio){
			boolean pass = true;
			assertTrue(pass);
		}
	}

	@Test
	public void ParseCalendarNoService(){
		Calendar expect = new server.framework.Calendar.CalendarBuilder("test", null).withService("\r").build();
		File f = new File("resources/icsTestFiles/nocalservice.ics");
		try{
			assertTrue(expect.getService().equals(ICSFeedParser.getCalendarData(f).getService()));
		}
		catch(FileNotFoundException enf){
			fail("Exception caught!\nGot: FileNotFoundException\nExpected: Valid File parse (return valid calendar)");
		}
		catch(IOException eio){
			fail("Exception caught!\nGot: IOException\nExpected: Valid File parse (return valid calendar)");
		}
	}

	@Test
	public void ParseCalendarNoName(){
		Calendar expect = new server.framework.Calendar.CalendarBuilder("\r", null).withService("test").build();
		File f = new File("resources/icsTestFiles/nocalname.ics");
		try{
			assertEquals(expect.getName(),ICSFeedParser.getCalendarData(f).getName());
		}
		catch(FileNotFoundException enf){
			fail("Exception caught!\nGot: FileNotFoundException\nExpected: Valid File parse (return valid calendar)");
		}
		catch(IOException eio){
			fail("Exception caught!\nGot: IOException\nExpected: Valid File parse (return valid calendar)");
		}
	}
	
	@Test
	public void ParseWrongFormatCalendar(){
		//The formating for calendar name and service ID is broken in this file.
		File f = new File("resources/icsTestFiles/wrongformat_c.ics");
		try{
			ICSFeedParser.getCalendarData(f);
		}
		catch(FileNotFoundException enf){
			fail("Exception caught!\nGot: FileNotFoundException\nExpected: IOException (fail parse)");
		}
		catch(IOException eio){
			boolean pass = true;
			assertTrue(pass);
		}
	}

	@Test
	public void ParseWrongFormatEvents(){
		//Event formatting (Begin and End) is broken
		File f = new File("resources/icsTestFiles/wrongformat_e.ics");
		try{
			ICSFeedParser.getEvents(f);
		}
		catch(FileNotFoundException enf){
			fail("Exception caught!\nGot: FileNotFoundException\nExpected: IOException (fail parse)");
		}
		catch(IOException eio){
			boolean pass = true;
			assertTrue(pass);
		}
	}

	@Test
	public void ParseValidFileCalendar(){
		File f = new File("resources/icsTestFiles/valid.ics");
		try{
			ICSFeedParser.getCalendarData(f);
		}
		catch(FileNotFoundException enf){
			fail("Exception caught!\nGot: FileNotFoundException\nExpected: Valid File parse (return valid calendar)");
		}
		catch(IOException eio){
			fail("Exception caught!\nGot: IOException\nExpected: Valid File parse (return valid calendar)");
		}
	}

	@Test
	public void ParseValidFileEvents(){
		File f = new File("resources/icsTestFiles/valid.ics");
		try{
			ICSFeedParser.getEvents(f);
		}
		catch(FileNotFoundException enf){
			fail("Exception caught!\nGot: FileNotFoundException\nExpected: Valid File parse (return valid event list)");
		}
		catch(IOException eio){
			fail("Exception caught!\nGot: IOException\nExpected: Valid File parse (return valid event list)");
		}
	}
}
