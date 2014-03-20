package server.framework;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;
import java.util.regex.Pattern;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public final class ICSFeedParser {

	private static final String DATE_FORMAT_STRING = "yyyyMMdd'T'HHmmss'Z'";

	/**
	 * Downloads a .ics file from a source URL and returns a local link to the
	 * file.
	 * 
	 * @param link
	 *            The URL pointing to a .ics file download.
	 * @return A local file reference link to the .ics file.
	 * @throws IOException
	 */
	public static File downloadICSFile(URL link) throws IOException {
		Random r = new Random();
		int rand = r.nextInt(65536);
		File f = new File(rand + ".ics");
		URLConnection con;
		DataInputStream dis;
		FileOutputStream fos;
		ArrayList<Byte> fileData;
		con = link.openConnection();
		dis = new DataInputStream(con.getInputStream());
		if (con.getContentLength() < 0) {
			fileData = new ArrayList<Byte>(65536);
		} else {
			fileData = new ArrayList<Byte>(con.getContentLength());
		}
		int nextVal;
		while ((nextVal = dis.read()) > -1) {
			fileData.add((byte) nextVal);
		}
		dis.close();
		byte[] byteArray = new byte[fileData.size()];
		for (int i = 0; i < fileData.size(); i++) {
			byte b = fileData.get(i).byteValue();
			byteArray[i] = b;
		}

		fos = new FileOutputStream(f);
		fos.write(byteArray);
		fos.close();

		if (!f.exists()) {
			throw new IOException();
		}

		return f;
	}

	/**
	 * Parses a .ics file for Calendar Data and returns a new Calendar object
	 * accordingly.
	 * 
	 * @param f
	 *            The file to be parsed.
	 * @return A new Calendar corresponding to the parsed information.
	 * @throws IOException
	 */
	public static Calendar getCalendarData(File f)
			throws FileNotFoundException, IOException {
		Scanner parser = new Scanner(f);
		parser.useDelimiter(Pattern.compile("\\n"));
		String current = null;
		String servid = null;
		String calname = null;
		String[] splitter = null;
		String[] calendarData = new String[2];
		boolean flag = true;

		while (parser.hasNext() && flag) {
			current = parser.next();
			splitter = current.split(":");

			// Service ID field
			if (splitter[0].equals("PRODID") && servid == null) {
				servid = current.split(":")[1];
				calendarData[0] = servid;
			}
			// Calendar Name field
			else if (splitter[0].equals("X-WR-CALNAME") && calname == null) {
				calname = current.split(":")[1];
				calendarData[1] = calname;
				flag = false;
			}
		}
		parser.close();
		return new Calendar.CalendarBuilder(calendarData[0], null).withService(
				calendarData[1]).build();
	}

	/**
	 * Parses a .ics file for Event Data, returning a list of events.
	 * 
	 * @param f
	 *            The file to be parsed.
	 * @return An Event[] representing the events in the .ics feed.
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public static Event[] getEvents(File f) throws FileNotFoundException,
			IOException {
		DateFormat df = new SimpleDateFormat(DATE_FORMAT_STRING);
		Scanner parser = new Scanner(f);
		parser.useDelimiter(Pattern.compile("\\n"));
		String current = null;
		String[] splitter = null;
		String[] eventData = new String[4];
		ArrayList<Event> eventList = new ArrayList<Event>();
		boolean flag = true;

		while (parser.hasNext() && flag) {
			current = parser.next();
			boolean inEvent = false;

			// Events starts here
			if (current.equals("BEGIN:VEVENT\r")) {
				inEvent = true;
			}

			Date start = null;
			Date end = null;
			while (inEvent) {

				current = parser.next();
				splitter = current.split(":");

				// Event Name field
				if (splitter[0].equals("SUMMARY")) {
					eventData[0] = splitter[1];
				}
				// Event Location field
				else if (splitter[0].equals("LOCATION")) {
					eventData[1] = splitter[1];
				}
				// Event Start Date field
				else if (splitter[0].equals("DTSTART")) {
					try {
						start = df.parse(splitter[1]);
					} catch (ParseException e) {
						e.printStackTrace();
					}
				}
				// Event End Date field (OPTIONAL)
				else if (splitter[0].equals("DTEND")) {
					try {
						end = df.parse(splitter[1]);
					} catch (ParseException e) {
						e.printStackTrace();
					}
				}
				// If input was valid, create a new corresponding event
				else if (current.equals("END:VEVENT\r")) {
					inEvent = false;
					// Eliminate events with null input for the first three
					// fields
					boolean validEventParsed = eventData[0] != null
							&& eventData[1] != null && start != null;

					// Store valid parsed event data in a new Event
					if (validEventParsed) {
						Event e = new Event.EventBuilder(eventData[0], start)
								.withLocation(eventData[1]).withEnd(end)
								.build();
						eventList.add(e);
					}

				}

			}

		}
		parser.close();

		// Convert to static Event[]
		Event[] events = new Event[eventList.size()];
		events = eventList.toArray(events);

		return events;
	}
}
