package server.framework;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class Test {

	public static void main(String[] args) {
		File f = null;
		String[] input = null;
		Event[] events = null;
		URL url = null;
		try {
			url = new URL("https://d2l.ucalgary.ca/d2l/le/calendar/feed/user/feed.ics?token=a49a4svg4wl7jqq954fa");
		} catch (MalformedURLException e1) {
			e1.printStackTrace();
		}
		try {
			f = ICSFeedParser.downloadICSFile(url);
			System.out.println(f.getAbsolutePath());
			input = ICSFeedParser.getCalendarData(f);
			events = ICSFeedParser.getEvents(f);
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("FILE NOT FOUND!!!!!!");
		}
		System.out.println(input[0] + " " + input[1]);
		for (Event e : events) {
			System.out.println(e.toString());
		}
		
		f.delete();
	}
}
