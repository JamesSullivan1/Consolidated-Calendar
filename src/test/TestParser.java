package test;

import static org.junit.Assert.*;

import java.io.*;

import org.junit.Ignore;
import org.junit.Test;

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

	
	//Starting with this test I need a way to access pre-created ICS files
	@Test
	public void ParseInvalidFileCalendar(){
		File f = new File("INVALID");
		try{
			ICSFeedParser.getCalendarData(f);
		}
		catch(FileNotFoundException enf){
			fail("Exception caught!\nGot: FileNotFoundException\nExpected: IOException");
		}
		catch(IOException eio){
			boolean pass = true;
			assert(pass);
		}
	}

	@Test
	public void ParseInvalidFileEvents(){
		File f = new File("INVALID");
		try{
			ICSFeedParser.getEvents(f);
		}
		catch(FileNotFoundException enf){
			fail("Exception caught!\nGot: FileNotFoundException\nExpected: IOException");
		}
		catch(IOException eio){
			boolean pass = true;
			assert(pass);
		}
	}

	@Test
	public void ParseWrongFormatCalendar(){					//I need a way to read actual pre-created .ics files in a way that is environment independent
		File f = new File("wrongformat_c.ics");
		try{
			ICSFeedParser.getCalendarData(f);
		}
		catch(FileNotFoundException enf){
			fail("Exception caught!\nGot: FileNotFoundException\nExpected: IOException (fail parse)");
		}
		catch(IOException eio){
			boolean pass = true;
			assert(pass);
		}
	}

	@Test
	public void ParseWrongFormatEvents(){
		File f = new File("wrongformat_e.ics");
		try{
			ICSFeedParser.getEvents(f);
		}
		catch(FileNotFoundException enf){
			fail("Exception caught!\nGot: FileNotFoundException\nExpected: IOException (fail parse)");
		}
		catch(IOException eio){
			boolean pass = true;
			assert(pass);
		}
	}

	@Test
	public void ParseValidFileCalendar(){
		File f = new File("valid.ics");
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
		File f = new File("valid.ics");
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

	//Same with the two above - need to read files without hardcoding a path
	//Make sure you test specific cases unique to Calendar or Event parsing as well.

}
