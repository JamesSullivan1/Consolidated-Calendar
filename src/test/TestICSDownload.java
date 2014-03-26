package test;

import static org.junit.Assert.*;
import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import server.exception.ICSParseException;
import server.framework.ICSFeedParser;

/**
 * Series of tests for {@link ICSFeedParser#downloadICSFile(URL)}
 * 
 * @author Daniel
 */
public class TestICSDownload
	{
	private File file = null;

	/**
	 * Ensure tests start with an empty file.
	 */
	@Before
	public void setUp()
		{
		file = null;
		}

	/**
	 * If a test downloaded a file, delete it.
	 */
	@After
	public void tearDown()
		{
		if (file != null && file.exists()) file.delete();
		file = null;
		}

	/**
	 * Ensure an exception is thrown for files that do not exist.
	 */
	@Test
	public void testInvalidLink1()
		{
		try
			{
			URL test = new URL("http://fake.cpsc301calendar.com/alsofake.ics");
			file = ICSFeedParser.downloadICSFile(test);
			fail("bad url was accepted");
			}
		catch (MalformedURLException e)
			{
			fail("test url must pass URL check");
			}
		catch (ICSParseException e)
			{
			// We want this exception.
			}
		}

	/**
	 * Ensure exception is thrown for files that exist, but are not ics files.
	 */
	@Test
	public void testInvalidLink2()
		{
		try
			{
			URL test = new URL("https://www.dropbox.com/s/ta07rtmg2i8iqkp/notics.txt");
			file = ICSFeedParser.downloadICSFile(test);
			fail("bad url was accepted");
			}
		catch (MalformedURLException e)
			{
			fail("test url must pass URL check");
			}
		catch (ICSParseException e)
			{
			// We want this exception.
			}
		}

	@Test
	public void testEmptyLink()
		{
		try
			{
			URL test = new URL("https://www.dropbox.com/s/tod0n570a9ag5hc/emptyfeed.ics");
			file = ICSFeedParser.downloadICSFile(test);
			fail("file with no content should be rejected");

			// Confirm file was downloaded.
			if (file == null) fail("Null file returned");

			if (!file.exists()) fail("File doesn't exist");

			}
		catch (MalformedURLException e)
			{
			fail("test url must pass URL check");
			}
		catch (ICSParseException e)
			{
			//We want this exception.
			}
		}

	/**
	 * Ensure parser can download from a D2L ICS link.
	 */
	@Test
	public void testD2LLink()
		{
		try
			{
			URL test = new URL(
					"https://d2l.ucalgary.ca/d2l/le/calendar/feed/user/feed.ics?token=act28krc91pc7yfo59a1");
			file = ICSFeedParser.downloadICSFile(test);

			// Confirm file was downloaded.
			if (file == null) fail("Null file returned");

			if (!file.exists()) fail("File doesn't exist");

			}
		catch (MalformedURLException e)
			{
			fail("test url must pass URL check");
			}
		catch (ICSParseException e)
			{
			fail("No exception should be thrown");
			}
		}

	@Test
	public void testGoogleLink()
		{
		try
			{
			URL test = new URL(
					"https://www.google.com/calendar/ical/ej9g9brg1e9ki2vrfddsu6nsvs%40"+
					"group.calendar.google.com/private-d1872874aa94cf4a7c253ba0949b9058/basic.ics");
			file = ICSFeedParser.downloadICSFile(test);

			// Confirm file was downloaded.
			if (file == null) fail("Null file returned");

			if (!file.exists()) fail("File doesn't exist");

			}
		catch (MalformedURLException e)
			{
			fail("test url must pass URL check");
			}
		catch (ICSParseException e)
			{
			fail("No exception should be thrown");
			}
		}
	}
